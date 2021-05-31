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

import dev.lambdaurora.calcium.Value;
import dev.lambdaurora.calcium.expression.Expression;
import dev.lambdaurora.calcium.math.ComplexNumber;

import java.util.List;

/**
 * Represents a function which returns a random number between 0 and 1.
 */
public final class RandomNumberFunctionSymbol extends FunctionSymbol {
    public RandomNumberFunctionSymbol() {
        super(true);
    }

    @Override
    public Value evaluate(List<Expression> arguments, SymbolTable symbolTable) {
        if (!arguments.isEmpty()) {
            throw new IllegalArgumentException("Too many arguments were passed, expected: 0");
        }

        return new ComplexNumber(symbolTable.getRandom().nextDouble());
    }
}
