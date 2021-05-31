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

package dev.lambdaurora.calcium.parser.parselet;

import dev.lambdaurora.calcium.expression.Expression;
import dev.lambdaurora.calcium.expression.LiteralExpression;
import dev.lambdaurora.calcium.math.ComplexNumber;
import dev.lambdaurora.calcium.parser.Parser;
import dev.lambdaurora.calcium.parser.token.Token;

import java.text.ParseException;

/**
 * Parses literals like {@code 2}, {@code 0b1100_1101}, {@code 2.36e-5}, etc.
 */
public class LiteralParselet implements PrefixParselet {
    /**
     * Parses the integer literal with the given base.
     *
     * @param token the token of the literal
     * @param text the string representation of the literal
     * @param base the base which the literal is in
     * @return the corresponding {@linkplain LiteralExpression} if parsed successfully
     * @throws ParseException if the literal could not be parsed
     */
    public LiteralExpression parseBase(Token token, String text, int base) throws ParseException {
        if (text.isEmpty())
            throw new ParseException("Could not parse literal \"" + token.text() + "\", invalid number.", token.offset());

        try {
            var res = Long.parseLong(text, base);
            return new LiteralExpression(new ComplexNumber(res));
        } catch (NumberFormatException e) {
            throw new ParseException("Could not parse literal \"" + token.text()
                    + "\", invalid number. (NumberFormatException: " + e.getMessage() + ")",
                    token.offset());
        }
    }

    @Override
    public Expression parse(Parser parser, Token token) throws ParseException {
        var text = token.text().replaceAll("_", ""); // Remove all visual _

        if (text.startsWith("0b")) {
            return this.parseBase(token, text.substring(2), 2);
        } else if (text.startsWith("0o")) {
            return this.parseBase(token, text.substring(2), 8);
        } else if (text.startsWith("0x")) {
            return this.parseBase(token, text.substring(2), 16);
        }

        boolean imaginary = text.endsWith("i");
        if (imaginary) text = text.substring(0, text.length() - 1);

        try {
            var parsed = Double.parseDouble(text);

            ComplexNumber number;
            if (imaginary) number = new ComplexNumber(0, parsed);
            else number = new ComplexNumber(parsed);

            return new LiteralExpression(number);
        } catch (NumberFormatException e) {
            throw new ParseException("Could not parse literal \"" + token.text()
                    + "\", invalid number. (NumberFormatException: " + e.getMessage() + ")",
                    token.offset());
        }
    }
}
