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

import dev.lambdaurora.calcium.Value;
import dev.lambdaurora.calcium.util.NumberFormatters;

import java.util.function.DoubleFunction;

/**
 * Represents a complex number with a real part and an imaginary part.
 */
public record ComplexNumber(double real, double imaginary) implements Value {
    /**
     * Represents the complex number zero.
     */
    public static final ComplexNumber ZERO = new ComplexNumber(0.0, 0.0);
    /**
     * Represents the constant i.
     */
    public static final ComplexNumber I = new ComplexNumber(0.0, 1.0);

    public ComplexNumber(double real) {
        this(real, 0.0);
    }

    /**
     * Returns a complex number with magnitude {@code r} and phase angle {@code theta}.
     *
     * @param r magnitude
     * @param theta phase angle
     * @return a complex number determined by {@code r} and {@code theta}
     */
    public static ComplexNumber polar(double r, double theta) {
        return new ComplexNumber(r * Math.cos(theta), r * Math.sin(theta));
    }

    /**
     * Returns whether this number is a real number or not.
     *
     * @return {@code true} if the number is a real number, otherwise {@code false}
     */
    public boolean isReal() {
        return this.imaginary() == 0.0;
    }

    /**
     * Returns whether this number is an integer or not.
     *
     * @return {@code true} if the number is an integer, otherwise {@code false}
     */
    public boolean isInteger() {
        return this.isReal() && this.real() == (long) this.real();
    }

    /**
     * Returns the conjugate of this complex number.
     *
     * @return the conjugate
     */
    public ComplexNumber conjugate() {
        return new ComplexNumber(this.real(), -this.imaginary());
    }

    /**
     * Returns the absolute value of this complex number.
     *
     * @return the absolute value
     */
    public double abs() {
        var x = this.real();
        var y = this.imaginary();
        var s = Math.max(Math.abs(x), Math.abs(y));
        if (s == 0.0)  // Well...
            return s;
        x /= s;
        y /= s;
        return s * Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the argument of this complex number.
     *
     * @return the argument
     */
    public double arg() {
        return Math.atan2(this.imaginary(), this.real());
    }

    /**
     * Adds a real number to this complex number.
     *
     * @param real the real number to add
     * @return the result of the addition
     */
    public ComplexNumber add(double real) {
        return new ComplexNumber(this.real() + real, this.imaginary());
    }

    /**
     * Adds a complex number to this complex number.
     *
     * @param other the other complex number
     * @return the result of the addition
     */
    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(this.real() + other.real(), this.imaginary() + other.imaginary());
    }

    /**
     * Subtracts a real number to this complex number.
     *
     * @param real the real number to subtract
     * @return the result of the subtraction
     */
    public ComplexNumber subtract(double real) {
        return new ComplexNumber(this.real() - real, this.imaginary());
    }

    /**
     * Subtracts a complex number to this complex number.
     *
     * @param other the other complex number
     * @return the result of the subtraction
     */
    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(this.real() - other.real(), this.imaginary() - other.imaginary());
    }

    /**
     * Multiplies this complex number by a real coefficient.
     *
     * @param coefficient the real coefficient
     * @return the result of the multiplication
     */
    public ComplexNumber multiply(double coefficient) {
        return new ComplexNumber(this.real() * coefficient, this.imaginary() * coefficient);
    }

    /**
     * Multiplies a complex number with this complex number.
     *
     * @param other the other complex number
     * @return the result of the multiplication
     */
    public ComplexNumber multiply(ComplexNumber other) {
        double a = this.real();
        double b = this.imaginary();
        double c = other.real();
        double d = other.imaginary();

        // (a+bi) * (c+di) = ac + adi + bci + bdi² = (ac - bd) + (ad + bc)i
        return new ComplexNumber(a * c - b * d, a * d + b * c);
    }

    /**
     * Divides this complex number by a real coefficient.
     *
     * @param coefficient the real coefficient
     * @return the result of the division
     * @throws ArithmeticException if the coefficient equals 0
     */
    public ComplexNumber divide(double coefficient) {
        if (coefficient == 0.0)
            throw new ArithmeticException("Division by 0");
        return new ComplexNumber(this.real() / coefficient, this.imaginary() / coefficient);
    }

    /**
     * Divides this complex number by another complex number.
     *
     * @param other the other complex number
     * @return the result of the division
     * @throws ArithmeticException if the other complex number equals 0
     */
    public ComplexNumber divide(ComplexNumber other) {
        var denominatorConjugate = other.conjugate();
        var nominator = this.multiply(denominatorConjugate);
        return nominator.divide(other.real() * other.real() + other.imaginary() * other.imaginary());
    }

    /**
     * Returns the string representation of this complex number using the given number formatter.
     *
     * @param formatter the number formatter
     * @return the complex number as string
     */
    public String toStringWithFormat(DoubleFunction<String> formatter) {
        if (this.real() == 0.0 && this.imaginary() == 0.0)
            return formatter.apply(0.0);

        String imaginary;
        if (this.imaginary() == -1.0) imaginary = "-i";
        else if (this.imaginary() == 1.0) imaginary = "i";
        else if (this.imaginary() == 0.0) imaginary = "";
        else imaginary = formatter.apply(this.imaginary()) + "i";

        StringBuilder builder = new StringBuilder();
        if (this.real() != 0.0) {
            builder.append(formatter.apply(this.real()));

            if (this.imaginary() > 0.0 || (this.imaginary() < 0.0 && imaginary.charAt(0) == '(')) {
                builder.append('+');
            }
        }
        builder.append(imaginary);
        return builder.toString();
    }

    @Override
    public String toString() {
        return this.toStringWithFormat(NumberFormatters.SCIENTIFIC_FORMATTER);
    }

    /**
     * Returns this number as an integer.
     *
     * @return the integer value
     * @throws IllegalStateException if this number is not an integer value
     */
    public long intValue() {
        if (!this.isReal())
            throw new IllegalStateException(this + " is not an integer.");
        return (long) this.real();
    }

    /**
     * Returns this number as a real number.
     *
     * @return the real number
     * @throws IllegalStateException if this number is not a real number
     */
    public double realValue() {
        if (!this.isReal())
            throw new IllegalStateException(this + " is not a real number.");
        return this.real();
    }
}
