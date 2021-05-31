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

package dev.lambdaurora.calcium.parser;

import dev.lambdaurora.calcium.parser.token.Token;
import dev.lambdaurora.calcium.parser.token.TokenType;
import dev.lambdaurora.calcium.parser.token.UnknownTokenException;

import java.util.Iterator;

/**
 * Represents the lexer of the calculator.
 * <p>
 * Takes a string and splits it into a series of {@linkplain Token}.
 */
public class Lexer implements Iterator<Token> {
    // Cache the token types array to not rebuild it for each call.
    private static final TokenType[] TOKEN_TYPES = TokenType.values();

    private final String text;
    private int index;

    private Token current;

    public Lexer(String text) {
        this.text = text;
        this.index = 0;

        this.pickNext();
    }

    @Override
    public boolean hasNext() {
        return this.current.type() != TokenType.EOF;
    }

    @Override
    public Token next() {
        var current = this.current;
        this.pickNext();
        return current;
    }

    private void pickNext() {
        while (this.index < this.text.length()) {
            var part = this.text.substring(this.index);

            for (var type : TOKEN_TYPES) {
                var i = type.getMatcher().match(part);

                if (i > 0) {
                    this.current = new Token(type, part.substring(0, i), this.index);
                    this.index += i;
                    return;
                }
            }

            if (!Character.isWhitespace(part.charAt(0))) {
                throw new UnknownTokenException("Unknown token start character \"" + part.charAt(0) + "\".", this.index);
            }

            this.index++;
        }

        this.current = new Token(TokenType.EOF, "", this.text.length());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
