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

import dev.lambdaurora.calcium.expression.AssignExpression;
import dev.lambdaurora.calcium.expression.Expression;
import dev.lambdaurora.calcium.expression.IdentifierExpression;
import dev.lambdaurora.calcium.parser.Parser;
import dev.lambdaurora.calcium.parser.Precedence;
import dev.lambdaurora.calcium.parser.token.Token;

import java.text.ParseException;

/**
 * Parses assignment expressions like "a = b".
 * <p>
 * The left side of an assignment expression must be an identifier, and expressions are right-associative.
 */
public class AssignParselet implements InfixParselet {
    @Override
    public Expression parse(Parser parser, Expression left, Token token) throws ParseException {
        if (left instanceof IdentifierExpression idExpr) {
            var right = parser.parseExpression(this.getPrecedence() - 1);
            return new AssignExpression(idExpr.id(), right);
        }

        throw new ParseException("The left-hand side of an assignment must be a name.", token.offset());
    }

    @Override
    public int getPrecedence() {
        return Precedence.ASSIGNMENT;
    }
}
