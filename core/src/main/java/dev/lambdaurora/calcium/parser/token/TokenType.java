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

import dev.lambdaurora.calcium.parser.parselet.*;
import dev.lambdaurora.calcium.parser.parselet.binary.*;

/**
 * Represents a token type.
 * <p>
 * Each token type has a {@link TokenMatcher} to match the token in a string.
 */
public enum TokenType {
    LEFT_PAREN(TokenMatcher.of('(', false), new GroupParselet(), new CallParselet()),
    RIGHT_PAREN(TokenMatcher.of(')', false), null, null),
    COMMA(TokenMatcher.of(',', false), null, null),
    ASSIGN(TokenMatcher.of('='), null, new AssignParselet()),
    PLUS(TokenMatcher.of('+'), null, new SumParselet()),
    MINUS(TokenMatcher.of('-'), new InvertParselet(), new SubtractionParselet()),
    ASTERISK(TokenMatcher.of('*'), null, new ProductParselet()),
    SLASH(TokenMatcher.of('/'), null, new DivisionParselet()),
    EXPONENT(TokenMatcher.of("**"), null, new ExponentParselet()),
    EXCLAMATION(TokenMatcher.of('!'), null, new FactorialParselet()),
    PIPE(TokenMatcher.of('|'), new AbsoluteParselet(), null),
    MOD(TokenMatcher.of("mod"), null, new ModulusParselet()),
    LITERAL(new LiteralTokenMatcher(), new LiteralParselet(), null),
    IDENTIFIER(input -> {
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            if (c == '_' && i == 0) {
                if (i == input.length() - 1) return 0;
                else if (!isIdentifierCharacter(input.charAt(i + 1))) return 0;
            } else if (!isIdentifierCharacter(c)) {
                return i;
            }

            i++;
        }
        return i;
    }, new IdentifierParselet(), null),
    EOF(TokenMatcher.of('\0'), null, null);

    private final TokenMatcher matcher;
    private final PrefixParselet prefixParselet;
    private final InfixParselet infixParselet;

    TokenType(TokenMatcher matcher, PrefixParselet prefixParselet, InfixParselet infixParselet) {
        this.matcher = matcher;
        this.prefixParselet = prefixParselet;
        this.infixParselet = infixParselet;
    }

    /**
     * Returns the token matcher of this token type.
     *
     * @return the token matcher
     */
    public TokenMatcher getMatcher() {
        return this.matcher;
    }

    /**
     * Returns the associated prefix parselet to this token type.
     * May returns {@code null} if this token type doesn't have an associated prefix parselet.
     *
     * @return the associated prefix parselet if one is associated to this token type, otherwise {@code null}
     */
    public PrefixParselet getPrefixParselet() {
        return this.prefixParselet;
    }

    /**
     * Returns the associated infix parselet to this token type.
     * May returns {@code null} if this token type doesn't have an associated infix parselet.
     *
     * @return the associated infix parselet if one is associated to this token type, otherwise {@code null}
     */
    public InfixParselet getInfixParselet() {
        return this.infixParselet;
    }

    private static boolean isIdentifierCharacter(char c) {
        return Character.isLetter(c) || c == '_';
    }
}
