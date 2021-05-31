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
import dev.lambdaurora.calcium.parser.Parser;
import dev.lambdaurora.calcium.parser.token.Token;

import java.text.ParseException;

/**
 * An InfixParselet is associated with a token that appears in the middle of an expression.
 *
 * @see PrefixParselet
 */
public interface InfixParselet {
    /**
     * Parses a binary expression or a post-fixed expression.
     * <p>
     * This method is called after the left-hand side expression has been parsed,
     * and it in turn is responsible for parsing everything that comes after the token.
     * <p>
     * May also be used for post-fixed expression, in which case it doesn't consume any more tokens.
     *
     * @param parser the parser
     * @param left the parsed left-hand side expression
     * @param token the consumed associated token
     * @return the parsed expression
     * @throws ParseException if the parsing failed
     */
    Expression parse(Parser parser, Expression left, Token token) throws ParseException;

    /**
     * Returns the precedence value, used for priority. Look at {@link Parser#parseExpression(int)} for more details.
     *
     * @return the precedence value
     */
    int getPrecedence();
}
