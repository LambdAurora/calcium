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

package dev.lambdaurora.calcium.gui.component;


import dev.lambdaurora.calcium.math.Graph;
import dev.lambdaurora.calcium.math.MathHelper;

import javax.swing.*;
import java.awt.*;

/**
 * A viewer component for graphs.
 */
public class GraphViewerPanel extends JPanel {
    private final static Color LINE_COLOR = new Color(44, 102, 230, 180);
    private static final Color GRID_COLOR = new Color(175, 175, 175, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private static final double ZERO_EPSILON = 1E-8;

    private final Graph graph;
    private int padding = 25;
    private double yMin;
    private boolean useUserDefinedYMin = false;
    private double yMax;
    private boolean useUserDefinedYMax = false;

    public GraphViewerPanel(Graph graph) {
        this.graph = graph;
        this.yMin = graph.getMinValue();
        this.yMax = graph.getMaxValue();
    }

    /**
     * Returns the minimum Y-coordinate which can be either an user-defined value or the minimum value in the graph.
     *
     * @return the minimum Y-coordinate
     */
    public double getYMin() {
        if (this.useUserDefinedYMin)
            return this.yMin;
        else
            return this.graph.getMinValue();
    }

    /**
     * Sets the user-defined minimum Y-coordinate.
     *
     * @param yMin the minimum Y-coordinate
     */
    public void setYMin(double yMin) {
        this.yMin = yMin;
        this.setUserDefinedYMin(true);
    }

    /**
     * Sets whether the minimum Y-coordinate is user-defined or not.
     *
     * @param value {@code true} if the minimum Y-coordinate is user-defined, else {@code false}
     */
    public void setUserDefinedYMin(boolean value) {
        this.useUserDefinedYMin = value;
        this.repaint();
    }

    /**
     * Returns the maximum Y-coordinate which can be either an user-defined value or the maximum value in the graph.
     *
     * @return the maximum Y-coordinate
     */
    public double getYMax() {
        if (this.useUserDefinedYMax)
            return this.yMax;
        else
            return this.graph.getMaxValue();
    }

    /**
     * Sets the user-defined maximum Y-coordinate.
     *
     * @param yMax the maximum Y-coordinate
     */
    public void setYMax(double yMax) {
        this.yMax = yMax;
        this.setUserDefinedYMax(true);
    }

    /**
     * Sets whether the maximum Y-coordinate is user-defined or not.
     *
     * @param value {@code true} if the maximum Y-coordinate is user-defined, else {@code false}
     */
    public void setUserDefinedYMax(boolean value) {
        this.useUserDefinedYMax = value;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        var graphWidth = this.getWidth() - 2 * this.padding;
        var graphHeight = this.getHeight() - 2 * this.padding;

        double xScale = graphWidth / this.graph.getXAxisLength();
        double yScale = graphHeight / (this.getYMax() - this.getYMin());

        // Background
        g2.setColor(Color.WHITE);
        g2.fillRect(this.padding, this.padding, graphWidth, graphHeight);

        this.paintGrid(g2, graphWidth, graphHeight, xScale, yScale);

        final var oldStroke = g2.getStroke();
        g2.setColor(LINE_COLOR);
        g2.setStroke(GRAPH_STROKE);

        var xOffset = -this.graph.getXMin();
        var yOffset = -this.getYMin();

        var firstX = this.graph.getFirstX();
        var lastPoint = new double[]{firstX, this.graph.getFirstValue()};
        this.graph.forEach((x2, y2) -> {
            if (x2 == firstX)
                return;

            var x1 = lastPoint[0];
            var y1 = MathHelper.clamp(lastPoint[1], this.getYMin(), this.getYMax());

            lastPoint[0] = x2;
            var clampedY2 = MathHelper.clamp(y2, this.getYMin(), this.getYMax());

            if ((y1 != lastPoint[1] || Double.isInfinite(y2) && y1 == clampedY2)
                    && (clampedY2 != y2 || Double.isInfinite(lastPoint[1]) && y1 == clampedY2)) {
                lastPoint[1] = y2;
                return;
            }

            lastPoint[1] = y2;

            y2 = clampedY2;

            if (y2 == Double.POSITIVE_INFINITY) {
                y2 = this.getYMin();
                if (y1 == y2)
                    return;
            } else if (y2 == Double.NEGATIVE_INFINITY) {
                y2 = this.getYMax();
                if (y1 == y2)
                    return;
            }
            if (y1 == Double.POSITIVE_INFINITY) {
                y1 = this.getYMax();
                if (y1 == y2)
                    return;
            }

            if (Double.isNaN(y1) || Double.isNaN(y2))
                return;

            g2.drawLine((int) (this.padding + (xOffset + x1) * xScale),
                    (int) (this.padding + graphHeight - (yOffset + y1) * yScale),
                    (int) (this.padding + (xOffset + x2) * xScale),
                    (int) (this.padding + graphHeight - (yOffset + y2) * yScale));
        });

        g2.setStroke(oldStroke);
    }

    protected double calculateInterval(double range) {
        double x = Math.pow(10.0, Math.floor(Math.log10(range)));
        if (range / x >= 5)
            return x;
        else if (range / (x / 2.0) >= 5)
            return x / 2.0;
        else
            return x / 5.0;
    }

    /**
     * Paints the grid.
     *
     * @param g the graphics instance
     * @param graphWidth the width of the graph
     * @param graphHeight the height of the graph
     * @param xScale the X scale
     * @param yScale the Y scale
     */
    protected void paintGrid(Graphics2D g, int graphWidth, int graphHeight, double xScale, double yScale) {
        var xOffset = -this.graph.getXMin();
        var yOffset = -this.getYMin();

        var xAxis = this.padding + graphHeight;
        if (this.getYMin() < 0.0) {
            if (this.getYMax() < 0.0)
                xAxis = this.padding;
            else
                xAxis = (int) (this.padding + graphHeight - yOffset * yScale);
        }

        var yAxis = this.padding;
        if (this.graph.getXMin() < 0.0) {
            if (this.graph.getXMax() < 0.0)
                yAxis = this.padding + graphWidth;
            else
                yAxis = (int) (this.padding + xOffset * xScale);
        }

        // Draw grid
        g.setColor(GRID_COLOR);

        // X
        var xInterval = this.calculateInterval(this.graph.getXMax() - this.graph.getXMin());
        for (double x = this.graph.getXMin(); x <= this.graph.getXMax(); x += xInterval) {
            this.paintXTick(g, graphHeight, xScale, xOffset, x, xAxis);
        }

        // Y
        var yMin = this.getYMin();
        var yMax = this.getYMax();
        var yInterval = this.calculateInterval(yMax - yMin);
        for (double y = yMin; y <= yMax; y += yInterval) {
            this.paintYTick(g, graphWidth, graphHeight, yScale, yOffset, y, yAxis);
        }

        // Draw axis.
        g.setColor(Color.BLACK);
        // X
        g.drawLine(this.padding, xAxis, this.padding + graphWidth, xAxis);
        // Y
        g.drawLine(yAxis, this.padding, yAxis, this.padding + graphHeight);
    }

    private String prettifyLabel(String label) {
        var split = label.split("\\.");
        if (split.length == 1)
            return label;
        else if (split[1].length() > 8) { // Likely to have a float precision error.
            char base = split[1].charAt(0);
            if (split[1].charAt(1) == '0') {
                label = split[0] + '.' + base;
            } else if (split[1].charAt(1) == '9') {
                if (base == '9') {
                    try {
                        long integerPart = Long.parseLong(split[0]);
                        if (label.charAt(0) == '-') label = (integerPart - 1) + ".0";
                        else label = (integerPart + 1) + ".0";
                    } catch (NumberFormatException e) {
                        return label;
                    }
                } else {
                    base = (char) (base + 1);
                    label = split[0] + '.' + base;
                }
            }
        }

        return label;
    }

    private void paintXTick(Graphics2D g, int graphHeight, double xScale, double xOffset, double x, int xAxis) {
        if (x < ZERO_EPSILON && x > -ZERO_EPSILON) x = 0.;
        int lineX = (int) (this.padding + (xOffset + x) * xScale);

        g.setColor(GRID_COLOR);
        g.drawLine(lineX, this.padding, lineX, this.padding + graphHeight);

        var labelName = this.prettifyLabel(String.valueOf(x));
        var metrics = g.getFontMetrics();
        int labelWidth = metrics.stringWidth(labelName);

        var labelX = lineX - labelWidth / 2;
        if (x == 0.0) labelX = lineX - labelWidth;

        g.setColor(Color.WHITE);
        g.fillRect(labelX, xAxis + 4, labelWidth, metrics.getHeight());
        g.setColor(Color.BLACK);
        g.drawString(labelName, labelX, xAxis + metrics.getHeight());
    }

    private void paintYTick(Graphics2D g, int graphWidth, int graphHeight, double yScale, double yOffset, double y, int yAxis) {
        if (y < ZERO_EPSILON && y > -ZERO_EPSILON) y = 0.0;
        var labelName = this.prettifyLabel(String.valueOf(y));

        if (y == Double.POSITIVE_INFINITY)
            y = this.graph.getMaxValue();
        else if (y == Double.NEGATIVE_INFINITY)
            y = this.graph.getMinValue();

        int lineY = (int) (this.padding + graphHeight - (yOffset + y) * yScale);

        g.setColor(GRID_COLOR);
        g.drawLine(this.padding, lineY, this.padding + graphWidth, lineY);

        g.setColor(Color.BLACK);
        var metrics = g.getFontMetrics();
        int labelWidth = metrics.stringWidth(labelName);

        if (labelName.equals("0.0"))
            return;
        var labelX = yAxis == this.padding + graphWidth ? this.padding + graphWidth + 2 : yAxis - labelWidth - 2;
        g.drawString(labelName, labelX, lineY + metrics.getHeight() - 2);
    }
}
