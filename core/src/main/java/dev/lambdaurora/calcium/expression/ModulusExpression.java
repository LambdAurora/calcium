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

import static dev.lambdaurora.calcium.expression.Expression.expectIntStrict;

/**
 * Represents a modulus expression.
 * <p>
 * To successfully evaluate the left and right expressions must evaluate to integers.
 */
public class ModulusExpression extends BinaryExpression {
    public ModulusExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value evaluate(SymbolTable symbolTable) {
        var b = expectIntStrict(this.left.evaluate(symbolTable));
        var n = expectIntStrict(this.right.evaluate(symbolTable));
        return new ComplexNumber(Math.floorMod(b, n));
    }
}
