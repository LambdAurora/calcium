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
import dev.lambdaurora.calcium.math.MathHelper;
import dev.lambdaurora.calcium.symbol.SymbolTable;

import static dev.lambdaurora.calcium.expression.Expression.expectComplex;

/**
 * Represents an exponent expression.
 */
public class ExponentExpression extends BinaryExpression {
    public ExponentExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Value evaluate(SymbolTable symbolTable) {
        return pow(expectComplex(this.left.evaluate(symbolTable)), expectComplex(this.right.evaluate(symbolTable)));
    }

    public static ComplexNumber pow(ComplexNumber z, double n) {
        if (z.equals(ComplexNumber.ZERO))
            return ComplexNumber.ZERO;
        if (z.isReal() && z.real() > 0.0)
            return new ComplexNumber(Math.pow(z.real(), n));

        var t = MathHelper.ln(z);
        return ComplexNumber.polar(Math.exp(n * t.real()), n * t.imaginary());
    }

    public static ComplexNumber pow(ComplexNumber z, ComplexNumber n) {
        if (z.equals(ComplexNumber.ZERO))
            return ComplexNumber.ZERO;

        // Try to be more accurate (so (2+2i)**2 returns 8i and not (4.898587196589414E-16)+8.000000000000002i.
        if (n.isReal() && n.real() > 1) {
            var res = new ComplexNumber(1);
            var e = n.intValue();
            if (n.isInteger()) {
                for (int i = 0; i < e; ++i)
                    res = res.multiply(z);
                return res;
            }
        }

        if (n.isReal())
            return pow(z, n.real());

        return MathHelper.exp(n.multiply(MathHelper.ln(z)));
    }
}
