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

import java.util.List;

public class OneArgumentFunctionSymbol extends FunctionSymbol {
    private final OneArgumentFunction function;

    public OneArgumentFunctionSymbol(OneArgumentFunction function, boolean builtin) {
        super(builtin);
        this.function = function;
    }

    @Override
    public Value evaluate(List<Expression> arguments, SymbolTable symbolTable) {
        if (arguments.size() != 1) {
            if (arguments.isEmpty()) throw new IllegalArgumentException("Too few arguments were passed, expected: 1");
            else throw new IllegalArgumentException("Too many arguments were passed, expected: 1");
        }

        return this.function.evaluate(arguments.get(0).evaluate(symbolTable), symbolTable);
    }

    @FunctionalInterface
    public interface OneArgumentFunction {
        /**
         * Evaluates the one argument function with the given argument and the symbol table.
         *
         * @param number the argument
         * @param symbolTable the symbol table
         * @return the result of the evaluation
         */
        Value evaluate(Value number, SymbolTable symbolTable);
    }
}
