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

import dev.lambdaurora.calcium.expression.Expression;
import dev.lambdaurora.calcium.parser.token.Token;
import dev.lambdaurora.calcium.parser.token.TokenType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the parser.
 * <p>
 * To start the parser with the given lexer, call {@link #parseExpression()}.
 */
public class Parser {
    private final Iterator<Token> lexer;
    private final List<Token> read = new ArrayList<>();

    public Parser(Iterator<Token> lexer) {
        this.lexer = lexer;
    }

    /**
     * Parses an expression.
     * <p>
     * Takes the minimal allowed precedence, if the parser encounters an expression whose precedence is lower than we allow,
     * it just stops parsing and returns what it has so far.
     *
     * @param precedence the precedence value
     * @return the parse expression
     * @throws ParseException if the parsing failed
     * @see #parseExpression()
     */
    public Expression parseExpression(int precedence) throws ParseException {
        var token = this.consume();

        if (token.type().getPrefixParselet() == null)
            throw new ParseException("Could not parse \"" + token.text() + "\".", token.offset());

        var left = token.type().getPrefixParselet().parse(this, token);

        while (precedence < this.getCurrentPrecedence()) {
            token = this.consume();

            var infix = token.type().getInfixParselet();
            left = infix.parse(this, left, token);
        }

        return left;
    }

    /**
     * Parses an expression.
     *
     * @return the parsed expression
     * @throws ParseException if the parsing failed
     * @see #parseExpression(int)
     */
    public Expression parseExpression() throws ParseException {
        return this.parseExpression(0);
    }

    /**
     * Returns whether the current token matches the given token type.
     *
     * @param expected the expected token type
     * @return {@code true} if the token matches the expected token type, else {@code false}
     */
    public boolean match(TokenType expected) {
        var token = this.lookAhead(0);
        if (token.type() != expected) {
            return false;
        }

        this.consume();
        return true;
    }

    /**
     * Consumes a token.
     *
     * @return the consumed token
     */
    public Token consume() {
        // Make sure we've read the token.
        this.lookAhead(0);

        return this.read.remove(0);
    }

    /**
     * Consumes a token, and expects it to match the given token type.
     *
     * @param expected the expected token type
     * @return the token
     * @throws ParseException if the token's type is not the expected token type
     */
    public Token expect(TokenType expected) throws ParseException {
        var token = this.consume();
        if (token.type() != expected) {
            throw new ParseException("Expected " + expected + " but got " + token.type() + " at " + token.offset(), token.offset());
        }
        return token;
    }

    /**
     * Looks ahead and returns the token at {@code distance} from the current one.
     *
     * @param distance the distance from the token
     * @return the token
     */
    private Token lookAhead(int distance) {
        // Read as many as needed.
        while (distance >= this.read.size()) {
            this.read.add(this.lexer.next());
        }

        // Get the queued token.
        return this.read.get(distance);
    }

    private int getCurrentPrecedence() {
        var parser = this.lookAhead(0).type().getInfixParselet();
        if (parser != null) return parser.getPrecedence();

        return 0;
    }
}
