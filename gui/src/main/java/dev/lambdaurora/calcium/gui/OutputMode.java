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

package dev.lambdaurora.calcium.gui;

import dev.lambdaurora.calcium.Value;
import dev.lambdaurora.calcium.math.ComplexNumber;
import dev.lambdaurora.calcium.util.NumberFormatters;

import java.util.function.BiConsumer;
import java.util.function.DoubleFunction;

/**
 * Represents the different output modes of values of the calculator.
 */
public enum OutputMode {
    DECIMAL("Decimal", (calculator, value) -> calculator.printOut(value.toString())),
    BINARY("Binary", (calculator, value) -> {
        if (value instanceof ComplexNumber complex) {
            if (complex.isReal() && !complex.isInteger())
                throw new IllegalStateException("Cannot print a real number in binary, please select IEEE-754.");
            var real = complex.real();
            if (real != (long) real)
                throw new IllegalStateException("Cannot print a complex number with non-integer real part in binary, please select IEEE-754.");
            var imaginary = complex.imaginary();
            if (imaginary != (long) imaginary)
                throw new IllegalStateException("Cannot print a complex number with non-integer imaginary part in binary, please select IEEE-754.");

            calculator.printOut(complex.toStringWithFormat(NumberFormatters.BINARY_FORMATTER));
        } else calculator.printOut(value.toString());
    }),
    IEEE_754("IEE-754", (calculator, value) -> {
        if (value instanceof ComplexNumber complex) calculator.printOut(complex.toStringWithFormat(NumberFormatters.IEEE_745_FORMATTER));
        else calculator.printOut(value.toString());
    }),
    OCTAL("Octal", getPrinterWithIntegerNumberFormatter("octal", NumberFormatters.OCTAL_FORMATTER)),
    HEX("Hexadecimal", getPrinterWithIntegerNumberFormatter("hex", NumberFormatters.HEX_FORMATTER));

    private final String name;
    private final BiConsumer<Calculator, Value> printer;

    OutputMode(String name, BiConsumer<Calculator, Value> printer) {
        this.name = name;
        this.printer = printer;
    }

    /**
     * Returns the name of this output mode.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Prints the given value to the calculator output.
     *
     * @param calculator the calculator
     * @param value the value to print
     */
    public void print(Calculator calculator, Value value) {
        this.printer.accept(calculator, value);
    }

    @Override
    public String toString() {
        return this.name;
    }

    private static BiConsumer<Calculator, Value> getPrinterWithIntegerNumberFormatter(String name, DoubleFunction<String> formatter) {
        return (calculator, value) -> {
            if (value instanceof ComplexNumber complex) {
                if (complex.isReal() && !complex.isInteger())
                    throw new IllegalStateException("Cannot print a real number in " + name + ".");
                var real = complex.real();
                if (real != (long) real)
                    throw new IllegalStateException("Cannot print a complex number with non-integer real part in " + name + ".");
                var imaginary = complex.imaginary();
                if (imaginary != (long) imaginary)
                    throw new IllegalStateException("Cannot print a complex number with non-integer imaginary part in " + name + ".");

                calculator.printOut(complex.toStringWithFormat(formatter));
            } else calculator.printOut(value.toString());
        };
    }
}
