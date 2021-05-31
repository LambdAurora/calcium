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

package dev.lambdaurora.calcium.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.DoubleFunction;

/**
 * Utility class containing some static definitions of number formatters.
 */
public final class NumberFormatters {
    public static final DoubleFunction<String> SCIENTIFIC_FORMATTER = a -> {
        if (a == (long) a && a < 1E7)
            return String.format("%d", (long) a);
        else {
            if (a >= 1E7 || a < 1E-3) return String.format("(%s)", a);
            else return String.format("%s", a);
        }
    };

    private static final DecimalFormat ALL_DIGITS_FORMAT = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    public static final DoubleFunction<String> ALL_DIGITS_FORMATTER = ALL_DIGITS_FORMAT::format;

    public static final DoubleFunction<String> BINARY_FORMATTER = a -> {
        if (a != (long) a)
            throw new IllegalArgumentException("Cannot format the given number into (" + a + ") binary.");

        return Long.toBinaryString((long) a);
    };

    public static final DoubleFunction<String> IEEE_745_FORMATTER = a -> Long.toBinaryString(Double.doubleToRawLongBits(a));

    public static final DoubleFunction<String> OCTAL_FORMATTER = a -> {
        if (a != (long) a)
            throw new IllegalArgumentException("Cannot format the given number (" + a + ") into octal.");

        return Long.toOctalString((long) a);
    };

    public static final DoubleFunction<String> HEX_FORMATTER = a -> {
        if (a != (long) a)
            throw new IllegalArgumentException("Cannot format the given number (" + a + ") into hexadecimal.");

        return Long.toHexString((long) a);
    };

    static {
        ALL_DIGITS_FORMAT.setMaximumFractionDigits(340);
    }
}
