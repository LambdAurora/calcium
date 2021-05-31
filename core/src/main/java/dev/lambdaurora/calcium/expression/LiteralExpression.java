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
 * Represents a literal expression, or constant expression.
 * <p>
 * The evaluation method returns the given constant number.
 */
public class LiteralExpression implements Expression {
    private final ComplexNumber constant;

    public LiteralExpression(ComplexNumber constant) {
        this.constant = constant;
    }

    @Override
    public Value evaluate(SymbolTable symbolTable) {
        return this.constant;
    }
}
