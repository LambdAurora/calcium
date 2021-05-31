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
 * A PrefixParselet is associated with a token that appears at the beginning of an expression.
 * <p>
 * This means that its {@link #parse(Parser, Token)} method will be called with the consumed leading token,
 * and is responsible for parsing anything that comes after that token if needed.
 * <p>
 * A PrefixParselet can also be used for single-token expressions like literals or variables.
 *
 * @see InfixParselet
 */
public interface PrefixParselet {
    /**
     * Parses a single-token expression or a prefixed expression.
     * <p>
     * The consumed leading token is given, this method is responsible for parsing anything that comes after that token if needed.
     *
     * @param parser the parser
     * @param token the consumed leading token
     * @return the parsed expression
     * @throws ParseException if the parsing failed
     */
    Expression parse(Parser parser, Token token) throws ParseException;
}
