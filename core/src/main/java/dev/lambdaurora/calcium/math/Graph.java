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

package dev.lambdaurora.calcium.math;

import dev.lambdaurora.calcium.expression.Expression;
import dev.lambdaurora.calcium.symbol.SymbolTable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a graph.
 * <p>
 * Evaluates the given expression from {@code xMin} to {@code xMax} incrementing of {@code step}.
 */
public class Graph {
    private double xMin;
    private double xMax;
    private double step;
    private double min;
    private double max;
    private Expression expression;
    private final Map<Double, Double> points = new LinkedHashMap<>();

    /**
     * Returns a new {@linkplain Graph} instance.
     *
     * @param xMin the minimum X-coordinate to evaluate
     * @param xMax the maximum X-coordinate to evaluate
     * @param step the step to increment from
     * @param expression the expression to evaluate to build the graph
     */
    public Graph(double xMin, double xMax, double step, Expression expression) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.step = step;
        this.expression = expression;

        this.compute();
    }

    public double getXMin() {
        return this.xMin;
    }

    public void setXMin(double xMin) {
        this.xMin = xMin;
        this.compute();
    }

    public double getXMax() {
        return this.xMax;
    }

    public void setXMax(double xMax) {
        this.xMax = xMax;
        this.compute();
    }

    public double getXAxisLength() {
        return this.xMax - this.xMin;
    }

    public double getStep() {
        return this.step;
    }

    public void setStep(double step) {
        this.step = step;
        this.compute();
    }

    public double getMinValue() {
        return this.min;
    }

    public double getMaxValue() {
        return this.max;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
        this.compute();
    }

    protected void compute() {
        this.points.clear();
        this.min = this.max = 0;

        var symbolTable = new SymbolTable();

        for (double x = this.xMin; x <= this.xMax; x += this.step) {
            symbolTable.setVariable("x", new ComplexNumber(x));

            try {
                var result = this.expression.evaluate(symbolTable);

                if (result instanceof ComplexNumber number && number.isReal()) {
                    var val = number.realValue();
                    if (val > this.max && !Double.isInfinite(val)) this.max = val;
                    if (val < this.min && !Double.isInfinite(val)) this.min = val;
                    this.points.put(x, val);
                }
            } catch (ArithmeticException e) {
                this.points.put(x, Double.NaN); // Uh oh
            }
        }
    }

    /**
     * Returns the first X-coordinate with value in this graph.
     *
     * @return the first X-coordinate with value
     */
    public double getFirstX() {
        return this.points.keySet().stream().findFirst().orElse(this.getXMin());
    }

    /**
     * Returns the first value of this graph.
     *
     * @return the first value
     */
    public double getFirstValue() {
        return this.points.values().stream().findFirst().orElse(0.0);
    }

    public void forEach(PointConsumer pointConsumer) {
        this.points.forEach(pointConsumer::consume);
    }

    @FunctionalInterface
    public interface PointConsumer {
        void consume(double x, Double y);
    }
}
