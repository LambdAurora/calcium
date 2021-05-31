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

package dev.lambdaurora.calcium.gui;

import dev.lambdaurora.calcium.parser.Lexer;
import dev.lambdaurora.calcium.parser.Parser;
import dev.lambdaurora.calcium.parser.token.UnknownTokenException;
import dev.lambdaurora.calcium.symbol.SymbolTable;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.text.ParseException;

public class Calculator {
    public static final Font IO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 14);

    private final SymbolTable symbolTable = new SymbolTable();
    private final ExpressionHistory history = new ExpressionHistory();
    private OutputMode outputMode = OutputMode.DECIMAL;
    private JTextPane outputPane;
    private JTextField inputField;

    /**
     * Returns the symbol table.
     *
     * @return the symbol table
     */
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    /**
     * Returns the expression history of this calculator.
     *
     * @return the expression history
     */
    public ExpressionHistory getHistory() {
        return this.history;
    }

    /**
     * Returns the current output mode used by this calculator to print values.
     *
     * @return the used output mode
     */
    public OutputMode getOutputMode() {
        return this.outputMode;
    }

    /**
     * Sets the output mode to use to print values.
     *
     * @param outputMode the output mode
     */
    public void setOutputMode(OutputMode outputMode) {
        this.outputMode = outputMode;
    }

    /**
     * Returns the output text pane.
     * <p>
     * Used to print messages out.
     *
     * @return the output text pane
     */
    public JTextPane getOutputPane() {
        return this.outputPane;
    }

    /**
     * Sets the output text pane.
     * <p>
     * Used to print messages out.
     *
     * @param outputPane the output text pane
     */
    public void setOutputPane(JTextPane outputPane) {
        this.outputPane = outputPane;
    }

    /**
     * Returns the main input field.
     *
     * @return the main input field
     */
    public JTextField getInputField() {
        return this.inputField;
    }

    /**
     * Sets the main input field.
     *
     * @param inputField the main input field
     */
    public void setInputField(JTextField inputField) {
        this.inputField = inputField;
    }

    public void printOut(String msg) {
        this.printOut(msg, PrintStyle.REGULAR);
    }

    /**
     * Prints out to the graphical console output.
     *
     * @param msg the message to print
     * @param style the style to print in
     */
    public void printOut(String msg, PrintStyle style) {
        if (this.outputPane == null) { // Should not happen.
            System.out.println(msg);
            return;
        }

        var doc = this.outputPane.getStyledDocument();
        var docStyle = doc.getStyle(style.getName());

        try {
            doc.insertString(doc.getLength(), msg + "\n", docStyle);
        } catch (BadLocationException e) {
            System.err.println("Could not print \"" + msg + "\" to graphical output console with style " + style + ".");
            e.printStackTrace();
        }

        this.outputPane.setCaretPosition(doc.getLength());
    }

    /**
     * Evaluates an expression and prints out the result, and add the expression to the history.
     *
     * @param expressionString the expression string
     */
    public void evaluateExpression(String expressionString) {
        this.printOut("> " + expressionString, PrintStyle.INPUT);

        this.history.insertExpression(expressionString);

        try {
            var parser = new Parser(new Lexer(expressionString));

            var expression = parser.parseExpression();

            var result = this.symbolTable.evaluateExpression(expression);

            this.outputMode.print(this, result);
        } catch (UnknownTokenException e) {
            var ident = "  " + " ".repeat(e.getErrorOffset());
            this.printOut(ident + "^\n" + ident + e.getMessage(), PrintStyle.ERROR);
        } catch (ParseException e) {
            var ident = "  " + " ".repeat(e.getErrorOffset());
            this.printOut(ident + "^\n" + ident + e.getMessage(), PrintStyle.ERROR);
        } catch (RuntimeException e) {
            this.printOut(e.getClass().getSimpleName() + ": " + e.getMessage(), PrintStyle.ERROR);
        }
    }
}
