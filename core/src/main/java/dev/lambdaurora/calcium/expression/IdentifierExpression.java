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
 * Represents an expression which refers an identifier.
 * <p>
 * If the identifier is a variable in the symbol table then it evaluates to that variable.
 */
public record IdentifierExpression(String id) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable) {
        var variable = symbolTable.getVariable(this.id());
        if (variable != null)
            return variable.getValue();

        throw new IllegalStateException("No variable with the name \"" + this.id() + "\" could have been found.");
    }
}
