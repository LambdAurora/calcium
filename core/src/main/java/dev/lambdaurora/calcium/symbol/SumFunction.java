/*
 * Copyright (c) 2021 LambdAurora <aurora42lambda@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.lambdaurora.calcium.symbol;

import dev.lambdaurora.calcium.expression.Expression;
import dev.lambdaurora.calcium.expression.IdentifierExpression;
import dev.lambdaurora.calcium.math.ComplexNumber;

import java.util.List;

/**
 * Represents a sum function, taking a minimum argument, a maximum argument, an increment variable name, and an expression to evaluate.
 * <p>
 * The function will return the sum of the evaluated expressions from min to max.
 * <p>
 * Example: {@code sum(0, 5, a, a)} will return {@code 10} because it did {@code 0 + 1 + 2 + 3 + 4}.
 */
public class SumFunction extends FunctionSymbol {
    protected SumFunction() {
        super(true);
    }

    @Override
    public ComplexNumber evaluate(List<Expression> arguments, SymbolTable symbolTable) {
        if (arguments.size() != 4) {
            if (arguments.size() < 4) throw new IllegalArgumentException("Too few arguments (" + arguments.size() + "), expected 4.");
            else throw new IllegalArgumentException("Too many arguments (" + arguments.size() + "), expected 4.");
        }

        var min = getInteger(arguments, symbolTable, 0);
        var max = getInteger(arguments, symbolTable, 1);

        if (min > max) {
            throw new IllegalArgumentException("The max bound (" + max + ") is smaller than the min bound (" + min + ").");
        }

        var incrementVariable = arguments.get(2);
        if (!(incrementVariable instanceof IdentifierExpression)) {
            throw new IllegalArgumentException("Expected a variable name for argument 3.");
        }
        var variable = ((IdentifierExpression) incrementVariable).id();

        var newScope = symbolTable.copy();
        double real = 0.0;
        double imaginary = 0.0;

        var expr = arguments.get(3);

        for (long i = min; i < max; i++) {
            newScope.setVariable(variable, new ComplexNumber(i));

            var res = Expression.expectComplex(expr.evaluate(newScope));

            real += res.real();
            imaginary += res.imaginary();
        }

        return new ComplexNumber(real, imaginary);
    }
}
