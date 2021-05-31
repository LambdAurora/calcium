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
 * Represents an expression which can be evaluated to a number.
 */
public interface Expression {
    /**
     * Evaluates the expression.
     *
     * @return the result of the expression
     */
    Value evaluate(SymbolTable symbolTable);

    /**
     * Returns the given value if it's an integer, else throws an {@link IllegalArgumentException}.
     *
     * @param value the value to check
     * @return the given number if it's an integer
     * @throws IllegalArgumentException if the value is not an integer
     */
    static long expectIntStrict(Value value) {
        if (!(value instanceof ComplexNumber) || !((ComplexNumber) value).isInteger())
            throw new IllegalArgumentException("The argument is not an integer (" + value + "), while an integer was expected.");

        return ((ComplexNumber) value).intValue();
    }

    /**
     * Returns the given value if it's a complex number, else throws an {@link IllegalArgumentException}.
     *
     * @param value the value to check
     * @return the given number if it's a complex number
     * @throws IllegalArgumentException if the value is not a complex number
     */
    static ComplexNumber expectComplex(Value value) {
        if (!(value instanceof ComplexNumber))
            throw new IllegalArgumentException("The argument is not a complex number (" + value + "), while a complex number was expected.");

        return (ComplexNumber) value;
    }
}
