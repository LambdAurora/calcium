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

package dev.lambdaurora.calcium.symbol;

import dev.lambdaurora.calcium.NoneValue;
import dev.lambdaurora.calcium.Value;
import dev.lambdaurora.calcium.expression.Expression;
import dev.lambdaurora.calcium.math.ComplexNumber;
import dev.lambdaurora.calcium.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents a symbol table.
 * <p>
 * A symbol table stores variables and functions.
 */
public class SymbolTable {
    private final Map<String, Variable> variables = new HashMap<>();
    private final Map<String, FunctionSymbol> functions = new HashMap<>();
    private final Random random = new Random();

    public SymbolTable() {
        this.registerConstants();
    }

    protected void registerConstants() {
        /* Constants */
        this.setConstant("i", ComplexNumber.I);
        this.setConstant("pi", new ComplexNumber(Math.PI));
        this.setConstant("e", new ComplexNumber(Math.E));
        this.setConstant("None", NoneValue.NONE);

        /* Basic Functions */
        this.setFunction("abs",
                new OneArgumentFunctionSymbol((number, symbolTable) -> new ComplexNumber(Expression.expectComplex(number).abs()), true));
        this.setFunction("sqr",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.sqr(Expression.expectComplex(number)), true));
        this.setFunction("sqrt",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.sqrt(Expression.expectComplex(number)), true));

        this.setFunction("exp",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.exp(Expression.expectComplex(number)), true));

        this.setFunction("ln",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.ln(Expression.expectComplex(number)), true));
        this.setFunction("log",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.log(Expression.expectComplex(number)), true));

        /* Random functions */
        this.setFunction("random", new RandomNumberFunctionSymbol());
        this.setFunction("rand_int", new RandomIntegerFunctionSymbol());

        /* Complex-related functions */
        this.setFunction("arg",
                new OneArgumentFunctionSymbol((number, symbolTable) -> new ComplexNumber(Expression.expectComplex(number).arg()),
                        true));
        this.setFunction("Re",
                new OneArgumentFunctionSymbol((number, symbolTable) -> new ComplexNumber(Expression.expectComplex(number).real()),
                        true));
        this.setFunction("Im",
                new OneArgumentFunctionSymbol((number, symbolTable) -> new ComplexNumber(0.0, Expression.expectComplex(number).imaginary()),
                        true));
        this.setFunction("conj",
                new OneArgumentFunctionSymbol((number, symbolTable) -> Expression.expectComplex(number).conjugate(), true));

        /* Trigonometry functions */
        this.setFunction("cos",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.cos(Expression.expectComplex(number)), true));
        this.setFunction("sin",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.sin(Expression.expectComplex(number)), true));
        this.setFunction("tan",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.tan(Expression.expectComplex(number)), true));

        this.setFunction("acos",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.acos(Expression.expectComplex(number)), true));
        this.setFunction("asin",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.asin(Expression.expectComplex(number)), true));
        this.setFunction("atan",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.atan(Expression.expectComplex(number)), true));

        this.setFunction("cosh",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.cosh(Expression.expectComplex(number)), true));
        this.setFunction("sinh",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.sinh(Expression.expectComplex(number)), true));
        this.setFunction("tanh",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.tanh(Expression.expectComplex(number)), true));

        this.setFunction("acosh",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.acosh(Expression.expectComplex(number)), true));
        this.setFunction("asinh",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.asinh(Expression.expectComplex(number)), true));
        this.setFunction("atanh",
                new OneArgumentFunctionSymbol((number, symbolTable) -> MathHelper.atanh(Expression.expectComplex(number)), true));

        /* Misc functions */
        this.setFunction("sum", new SumFunction());
    }

    /**
     * Returns the random number generator used by this symbol table.
     *
     * @return the random number generator
     */
    public Random getRandom() {
        return this.random;
    }

    /**
     * Gets a variable by its identifier.
     *
     * @param id the variable identifier
     * @return the variable if it exists, otherwise {@code null}
     */
    public Variable getVariable(String id) {
        return this.variables.get(id);
    }

    /**
     * Sets a value to a variable.
     *
     * @param id the identifier of the variable
     * @param value the new value
     */
    public void setVariable(String id, Value value) {
        var variable = this.variables.get(id);
        if (variable != null)
            variable.setValue(value);
        else
            this.setVariable(new Variable(id, value, false));
    }

    /**
     * Sets a variable.
     *
     * @param variable the variable
     */
    public void setVariable(Variable variable) {
        var oldVariable = this.variables.get(variable.getName());
        if (oldVariable != null && oldVariable.isConstant())
            throw new IllegalStateException("Cannot replace variable \"" + variable.getName() + "\" as it is a constant.");
        this.variables.put(variable.getName(), variable);
    }

    private void setConstant(String id, Value value) {
        this.setVariable(new Variable(id, value, true));
    }

    /**
     * Gets a function by its identifier.
     *
     * @param id the identifier of the function
     * @return the function if it exists, otherwise {@code null}
     */
    public FunctionSymbol getFunction(String id) {
        return this.functions.get(id);
    }

    /**
     * Sets a function.
     *
     * @param id the identifier of the function
     * @param function the function
     */
    public void setFunction(String id, FunctionSymbol function) {
        var oldFunction = this.functions.get(id);
        if (oldFunction != null && oldFunction.isBuiltin())
            throw new IllegalStateException("Cannot replace function \"" + id + "\" as it is a built-in function.");
        this.functions.put(id, function);
    }

    /**
     * Evaluates an expression and puts the result as the {@code Ans} variable.
     *
     * @param expression the expression to evaluate
     * @return the result of the evaluation
     */
    public Value evaluateExpression(Expression expression) {
        var result = expression.evaluate(this);
        this.setVariable("Ans", result);
        return result;
    }

    /**
     * Returns a copy of this symbol table.
     *
     * @return the copy
     */
    public SymbolTable copy() {
        var copy = new SymbolTable();
        copy.variables.putAll(this.variables);
        copy.functions.putAll(this.functions);

        return copy;
    }

    /**
     * Clears the symbols table.
     */
    public void clear() {
        this.variables.clear();
        this.functions.clear();
        this.registerConstants();
    }
}
