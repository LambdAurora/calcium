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

package dev.lambdaurora.calcium.parser.token;

/**
 * Matches literal tokens.
 * <p>
 * Inspired by the regular expressions of
 * <a href="https://doc.rust-lang.org/stable/reference/tokens.html#integer-literals">the Rust reference</a>.
 * <p>
 * Note: the literal token matcher can match invalid literals which the parser needs to error on,
 * this avoids weird obscure errors like {@code 0b} being matched as the literal 0 and as the identifier b.
 */
public final class LiteralTokenMatcher implements TokenMatcher {
    private boolean isValidBinaryCharacter(char c) {
        return c == '0' || c == '1' || c == '_';
    }

    private boolean isValidOctalCharacter(char c) {
        return (c >= '0' && c < '8') || c == '_';
    }

    private boolean isValidHexCharacter(char c) {
        return this.isValidDecimalCharacter(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private boolean isValidDecimalCharacter(char c) {
        return (c >= '0' && c <= '9') || c == '_';
    }

    /**
     * Attempts to match a binary number from the given string. Assumes that the string starts with {@code 0b}.
     *
     * @param input the string to match
     * @return the length of the matched binary number, {@code 0} if unmatched
     */
    public int matchBinary(String input) {
        int i = 2;

        while (i < input.length()) {
            if (!this.isValidBinaryCharacter(input.charAt(i))) break;
            i++;
        }

        return i;
    }

    /**
     * Attempts to match an octal number from the given string. Assumes that the string starts with {@code 0o}.
     *
     * @param input the string to match
     * @return the length of the matched octal number, {@code 0} if unmatched
     */
    public int matchOctal(String input) {
        int i = 2;

        while (i < input.length()) {
            if (!this.isValidOctalCharacter(input.charAt(i))) break;
            i++;
        }

        return i;
    }

    /**
     * Attempts to match an hexadecimal number from the given string. Assumes that the string starts with {@code 0x}.
     *
     * @param input the string to match
     * @return the length of the matched hexadecimal number, {@code 0} if unmatched
     */
    public int matchHex(String input) {
        int i = 2;

        while (i < input.length()) {
            if (!this.isValidHexCharacter(input.charAt(i))) break;
            i++;
        }

        return i;
    }

    private int matchDecimal(String input, int offset) {
        while (offset < input.length()) {
            if (!this.isValidDecimalCharacter(input.charAt(offset))) break;
            offset++;
        }
        return offset;
    }

    private int matchDecimalLiteral(String input, int offset) {
        if (offset >= input.length())
            return offset;

        char c = input.charAt(offset);
        if (!this.isValidDecimalCharacter(c) || c == '_')
            return offset;

        return this.matchDecimal(input, offset + 1);
    }

    @Override
    public int match(String input) {
        if (input.startsWith("0b")) return this.matchBinary(input);
        else if (input.startsWith("0o")) return this.matchOctal(input);
        else if (input.startsWith("0x")) return this.matchHex(input);

        // Attempts to match a decimal literal.
        int i = this.matchDecimalLiteral(input, 0);

        if (i == 0) return 0;
        else if (i >= input.length()) return i;
        else if (input.charAt(i) == '.')
            // Attempts to match a decimal literal after a dot.
            i = this.matchDecimalLiteral(input, i + 1);

        if (i >= input.length()) return i;

        char c = input.charAt(i);
        if (c == 'e' || c == 'E') {
            i++;

            if (i >= input.length()) return i;

            c = input.charAt(i);
            if (c == '+' || c == '-') i++;

            i = this.matchDecimal(input, i);
        }

        if (i >= input.length()) return i;

        c = input.charAt(i);
        if (c == 'i') i++;

        return i;
    }
}
