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

import static dev.lambdaurora.calcium.expression.Expression.expectComplex;

/**
 * Represents an addition expression.
 */
public class AddExpression extends BinaryExpression {
    public AddExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value evaluate(SymbolTable symbolTable) {
        return expectComplex(this.left.evaluate(symbolTable))
                .add(expectComplex(this.right.evaluate(symbolTable)));
    }
}
