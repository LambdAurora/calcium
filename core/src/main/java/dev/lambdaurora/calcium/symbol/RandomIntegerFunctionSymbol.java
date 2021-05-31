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
 * Represents a function which returns a random integer between a minimum and a maximum bound.
 * <p>
 * If a minimum bound is not specified, it's supposed to be 0 if the specified "maximum" bound is positive.
 * If the specified bound is negative, it's supposed that it's the minimum bound and the maximum bound is 0.
 */
public final class RandomIntegerFunctionSymbol extends FunctionSymbol {
    public RandomIntegerFunctionSymbol() {
        super(true);
    }

    @Override
    public Value evaluate(List<Expression> arguments, SymbolTable symbolTable) {
        if (arguments.size() > 2) {
            throw new IllegalArgumentException("Too many arguments (" + arguments.size() + "), expected maximum 2.");
        }

        var first = getInteger(arguments, symbolTable, 0);
        if (arguments.size() == 2) {
            var max = getInteger(arguments, symbolTable, 1);

            if (first >= max)
                throw new IllegalArgumentException("The minimum bound (" + first + ") is greater than the maximum bound (" + max + ").");

            return new ComplexNumber(first + Math.floorMod(symbolTable.getRandom().nextLong(), max - first));
        } else {
            return new ComplexNumber(Math.floorMod(symbolTable.getRandom().nextLong(), first));
        }
    }
}
