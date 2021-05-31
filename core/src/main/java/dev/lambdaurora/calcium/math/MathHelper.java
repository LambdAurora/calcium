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

package dev.lambdaurora.calcium.math;

/**
 * A Math helper, mainly focus on complex numbers.
 */
public final class MathHelper {
    public static final double PI_2 = Math.PI / 2.0;

    private MathHelper() {
        throw new UnsupportedOperationException("MathHelper contains only static-definitions");
    }

    /**
     * Clamps a value between {@code min} and {@code max}.
     *
     * @param value the value to clamp
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return the clamped value
     */
    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    /**
     * Returns the square of the given complex number.
     *
     * @param z the complex number to square
     * @return the squared complex number
     */
    public static ComplexNumber sqr(ComplexNumber z) {
        return z.multiply(z);
    }

    /**
     * Returns the square root of the given complex number.
     *
     * @param z the complex number to square root
     * @return the squared root complex number
     */
    public static ComplexNumber sqrt(ComplexNumber z) {
        var x = z.real();
        var y = z.imaginary();

        if (x == 0.0) {
            var t = Math.sqrt(Math.abs(y) / 2.0);
            return new ComplexNumber(t, y < 0.0 ? -t : t);
        } else {
            var t = Math.sqrt(2 * (z.abs() + Math.abs(x)));
            var u = t / 2;
            return x > 0.0
                    ? new ComplexNumber(u, y / t)
                    : new ComplexNumber(Math.abs(y) / t, y < 0.0 ? -u : u);
        }
    }

    /**
     * Returns Euler's number <i>e</i> raised to the power of a {@link ComplexNumber} value.
     *
     * @param z the exponent to raise <i>e</i> to
     * @return the value <i>e</i><sup>{@code z}</sup>, where <i>e</i> is the base of the natural logarithms
     */
    public static ComplexNumber exp(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.exp(z.real()));
        return ComplexNumber.polar(Math.exp(z.real()), z.imaginary());
    }

    /**
     * Returns the natural logarithm (base <i>e</i>) of a {@link ComplexNumber} value.
     *
     * @param z a value
     * @return the value ln({@code z}), the natural logarithm of {@code z}
     */
    public static ComplexNumber ln(ComplexNumber z) {
        return new ComplexNumber(Math.log(z.abs()), z.arg());
    }

    /**
     * Returns the base 10 logarithm of a {@link ComplexNumber} value.
     *
     * @param z a value
     * @return the base 10 logarithm of {@code z}
     */
    public static ComplexNumber log(ComplexNumber z) {
        return ln(z).divide(Math.log(10));
    }

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param z an angle, in radians
     * @return the cosine of the argument
     */
    public static ComplexNumber cos(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.cos(z.real()));

        return new ComplexNumber(
                Math.cos(z.real()) * Math.cosh(z.imaginary()),
                -Math.sin(z.real()) * Math.sinh(z.imaginary())
        );
    }

    public static ComplexNumber cosh(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.cosh(z.real()));

        return new ComplexNumber(
                Math.cosh(z.real()) * Math.cos(z.imaginary()),
                Math.sinh(z.real()) * Math.sin(z.imaginary())
        );
    }

    public static ComplexNumber acos(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.acos(z.real()));

        var t = asin(z);
        return new ComplexNumber(PI_2 - t.real(), -t.imaginary());
    }

    public static ComplexNumber acosh(ComplexNumber z) {
        // Kahan's formula.
        return ln(
                sqrt(z.add(1.0).multiply(0.5))
                        .add(sqrt(z.subtract(1.0).multiply(0.5)))
        ).multiply(2.0);
    }

    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param z an angle, in radians
     * @return the sine of the argument
     */
    public static ComplexNumber sin(ComplexNumber z) {
        if (z.isReal()) {
            var val = z.real();
            var piTest = val / Math.PI;
            if (piTest == (long) piTest) // The goal is to make the result of sin a little bit more exact.
                return ComplexNumber.ZERO; // The number is a = pi * x where x is an integer. So the result of sin is 0.
            return new ComplexNumber(Math.sin(val));
        }

        return new ComplexNumber(
                Math.sin(z.real()) * Math.cosh(z.imaginary()),
                -Math.cos(z.real()) * Math.sinh(z.imaginary())
        );
    }

    public static ComplexNumber sinh(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.sinh(z.real()));

        return new ComplexNumber(
                Math.sinh(z.real()) * Math.cos(z.imaginary()),
                Math.cosh(z.real()) * Math.sin(z.imaginary())
        );
    }

    public static ComplexNumber asin(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.asin(z.real()));

        var t = new ComplexNumber(-z.imaginary(), z.real());
        t = asinh(t);
        return new ComplexNumber(t.imaginary(), -t.real());
    }

    public static ComplexNumber asinh(ComplexNumber z) {
        var t = new ComplexNumber(
                (z.real() - z.imaginary()) * (z.real() + z.imaginary()) + 1.0,
                2.0 * z.real() * z.imaginary()
        );
        t = sqrt(t);
        return ln(z.add(t));
    }

    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param z an angle, in radians
     * @return the tangent of the argument
     */
    public static ComplexNumber tan(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.tan(z.real()));

        return sin(z).divide(cos(z));
    }

    public static ComplexNumber tanh(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.tanh(z.real()));

        return sinh(z).divide(cosh(z));
    }

    public static ComplexNumber atan(ComplexNumber z) {
        if (z.isReal()) return new ComplexNumber(Math.atan(z.real()));

        var realSqr = z.real() * z.real();
        var x = 1.0 - realSqr - z.imaginary() * z.imaginary();

        var numerator = z.imaginary() + 1.0;
        var denominator = z.imaginary() - 1.0;

        numerator = realSqr + numerator * numerator;
        denominator = realSqr + denominator * denominator;

        return new ComplexNumber(
                0.5 * Math.atan2(2.0 * z.real(), x),
                0.25 * Math.log(numerator / denominator)
        );
    }

    public static ComplexNumber atanh(ComplexNumber z) {
        var iSqr = z.imaginary() * z.imaginary();
        var x = 1.0 - iSqr - z.real() * z.real();

        var numerator = 1.0 + z.real();
        var denominator = 1.0 - z.real();

        numerator = iSqr + numerator * numerator;
        denominator = iSqr + denominator * denominator;

        return new ComplexNumber(
                0.25 * (Math.log(numerator) - Math.log(denominator)),
                0.5 * Math.atan2(2.0 * z.imaginary(), x)
        );
    }
}
