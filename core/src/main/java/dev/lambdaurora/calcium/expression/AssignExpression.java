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
import dev.lambdaurora.calcium.symbol.SymbolTable;

/**
 * Represents an assignation to a variable.
 * <p>
 * Evaluating this expression will evaluate the given {@code value} expression which will be the value of the variable.
 */
public class AssignExpression implements Expression {
    private final String variable;
    private final Expression value;

    public AssignExpression(String variable, Expression value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public Value evaluate(SymbolTable symbolTable) {
        var val = this.value.evaluate(symbolTable);
        symbolTable.setVariable(this.variable, val);
        return val;
    }
}
