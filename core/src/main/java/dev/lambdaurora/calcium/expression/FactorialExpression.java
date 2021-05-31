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

package dev.lambdaurora.calcium.expression;

import dev.lambdaurora.calcium.Value;
import dev.lambdaurora.calcium.math.ComplexNumber;
import dev.lambdaurora.calcium.symbol.SymbolTable;

/**
 * Represents a factorial expression.
 * <p>
 * To successfully evaluate {@code n} must evaluate to an integer greater than or equal to 0.
 */
public record FactorialExpression(Expression n) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable) {
        var max = Expression.expectIntStrict(n.evaluate(symbolTable));

        if (max < 0)
            throw new UnsupportedOperationException("Factorial is only defined for natural numbers (and zero).");

        var res = 1;
        for (int i = 1; i <= max; i++) {
            res *= i;
        }

        return new ComplexNumber(res);
    }
}
