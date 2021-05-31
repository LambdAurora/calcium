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

import dev.lambdaurora.calcium.parser.Lexer;
import dev.lambdaurora.calcium.parser.token.TokenType;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LexerTests {
    @Test
    public void testLexer() {
        var expectedTokens = Arrays.stream(TokenType.values()).iterator();
        var lexer = new Lexer("() , = + - * /\t**! |mod   536.25i hello_world");

        while (expectedTokens.hasNext()) {
            var expectedToken = expectedTokens.next();
            var gotToken = lexer.next();

            assertEquals(expectedToken, gotToken.type());
        }
    }

    @Test
    public void testLiteralLexer() {
        var toTest = "1 2 3 4 56.3 25E3 25E-3 56i 56.4i 0.3 26E-3i";
        var lexer = new Lexer(toTest);

        int i = 0;
        while (lexer.hasNext()) {
            var token = lexer.next();
            if (token.type() != TokenType.LITERAL) {
                fail("Expected LITERAL got " + token + " (i=" + i + ")");
            }
            i++;
        }

        var expected = toTest.split(" ").length;
        if (expected != i)
            fail("Expected " + expected + " literals, got " + i);
    }
}
