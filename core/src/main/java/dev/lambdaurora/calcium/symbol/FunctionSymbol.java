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
 * Represents a function.
 */
public abstract class FunctionSymbol {
    private final boolean builtin;

    protected FunctionSymbol(boolean builtin) {
        this.builtin = builtin;
    }

    /**
     * Evaluates the expression given the list of arguments and the symbol table.
     *
     * @param arguments the arguments of the function
     * @param symbolTable the symbol table
     * @return the result of the function
     */
    public abstract Value evaluate(List<Expression> arguments, SymbolTable symbolTable);

    public static ComplexNumber getReal(List<Expression> args, SymbolTable symbolTable, int index) {
        if (args.size() <= index)
            throw new IllegalArgumentException("Too few arguments were passed, expected at least " + (index + 1) + ".");

        var value = args.get(index).evaluate(symbolTable);

        if (!(value instanceof ComplexNumber number))
            throw new IllegalArgumentException("Argument " + index + " is not a real number (" + value
                    + "), while a real number was expected.");

        if (!number.isReal())
            throw new IllegalArgumentException("Argument " + index + " is not a real number (" + number + "), while a real number was expected.");

        return number;
    }

    public static long getInteger(List<Expression> args, SymbolTable symbolTable, int index) {
        if (args.size() <= index)
            throw new IllegalArgumentException("Too few arguments were passed, expected at least " + (index + 1) + ".");

        var value = args.get(index).evaluate(symbolTable);

        if (!(value instanceof ComplexNumber number))
            throw new IllegalArgumentException("Argument " + index + " is not an integer number (" + value
                    + "), while an integer number was expected.");

        if (!number.isReal())
            throw new IllegalArgumentException("Argument " + index + " is not an integer number (" + number
                    + "), while an integer number was expected.");

        if (!number.isInteger())
            throw new IllegalArgumentException("Argument " + index + " is not an integer number (" + number
                    + "), while an integer number was expected.");

        return number.intValue();
    }

    public boolean isBuiltin() {
        return this.builtin;
    }
}
