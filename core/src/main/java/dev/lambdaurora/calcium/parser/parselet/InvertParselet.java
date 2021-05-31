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
import dev.lambdaurora.calcium.expression.InvertExpression;
import dev.lambdaurora.calcium.parser.Parser;
import dev.lambdaurora.calcium.parser.Precedence;
import dev.lambdaurora.calcium.parser.token.Token;

import java.text.ParseException;

/**
 * Parses sign inversion like {@code - <expression>}.
 */
public class InvertParselet implements PrefixParselet {
    @Override
    public Expression parse(Parser parser, Token token) throws ParseException {
        return new InvertExpression(parser.parseExpression(Precedence.PREFIX));
    }
}
