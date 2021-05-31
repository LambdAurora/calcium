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

import dev.lambdaurora.calcium.expression.Expression;
import dev.lambdaurora.calcium.gui.Calculator;
import dev.lambdaurora.calcium.math.Graph;
import dev.lambdaurora.calcium.parser.Lexer;
import dev.lambdaurora.calcium.parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.function.Consumer;

/**
 * An interactive graph panel, includes a {@link GraphViewerPanel} and controls.
 */
public class GraphPanel extends JPanel {
    private final InteractiveGraph graph;
    private final JTextField expressionField;
    private final GraphViewerPanel viewer;

    /**
     * Returns a new {@linkplain GraphPanel} instance.
     *
     * @param xMin the minimum X-coordinate to evaluate
     * @param xMax the maximum X-coordinate to evaluate
     * @param step the step to increment from
     * @param expression the expression to evaluate to build the graph
     * @param expressionString the corresponding string of the given expression
     */
    public GraphPanel(double xMin, double xMax, double step, Expression expression, String expressionString) {
        super(new GridBagLayout());
        this.graph = new InteractiveGraph(xMin, xMax, step, expression);
        this.expressionField = new JTextField(expressionString);
        this.viewer = new GraphViewerPanel(this.graph);

        this.init();
    }

    private void init() {
        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.90;
        c.gridheight = 1;

        this.add(this.viewer, c);

        var controlsPanel = new JPanel(new GridBagLayout());
        var controlsConstraints = (GridBagConstraints) c.clone();
        controlsConstraints.fill = GridBagConstraints.HORIZONTAL;
        controlsConstraints.weighty = 0.65;

        controlsPanel.add(this.buildNumbersInputPanel(), controlsConstraints);

        controlsConstraints.gridy = 1;
        controlsConstraints.weighty = 0.35;
        controlsPanel.add(this.buildExpressionPanel(), controlsConstraints);

        c.gridy = 1;
        c.weighty = 0.10;
        this.add(controlsPanel, c);
    }

    private JPanel buildNumbersInputPanel() {
        var numbersInputPanel = new JPanel(new SpringLayout());

        numbersInputPanel.add(new JLabel("X min: ", JLabel.TRAILING));
        numbersInputPanel.add(this.makeSpinnerFor(this.graph.getXMin(), null, 1.0, this.graph::setXMin));

        numbersInputPanel.add(new JLabel("X max: ", JLabel.TRAILING));
        numbersInputPanel.add(this.makeSpinnerFor(this.graph.getXMax(), null, 1.0, this.graph::setXMax));

        numbersInputPanel.add(new JLabel("Step: ", JLabel.TRAILING));
        numbersInputPanel.add(this.makeSpinnerFor(this.graph.getStep(), 1E-16, 0.05, this.graph::setStep));

        numbersInputPanel.add(new JLabel("Y min: ", JLabel.TRAILING));
        var yMinSpinner = this.makeSpinnerFor(this.viewer.getYMin(), null, 1.0, this.viewer::setYMin);
        yMinSpinner.setEnabled(false);
        numbersInputPanel.add(yMinSpinner);
        var yMinCheckbox = new JCheckBox();
        yMinCheckbox.addChangeListener(e -> {
            this.viewer.setUserDefinedYMin(yMinCheckbox.isSelected());
            yMinSpinner.setEnabled(yMinCheckbox.isSelected());
        });
        numbersInputPanel.add(yMinCheckbox);

        numbersInputPanel.add(new JLabel("Y max: ", JLabel.TRAILING));
        var yMaxSpinner = this.makeSpinnerFor(this.viewer.getYMax(), null, 1.0, this.viewer::setYMax);
        yMaxSpinner.setEnabled(false);
        numbersInputPanel.add(yMaxSpinner);
        var yMaxCheckbox = new JCheckBox();
        yMaxCheckbox.addChangeListener(e -> {
            this.viewer.setUserDefinedYMax(yMaxCheckbox.isSelected());
            yMaxSpinner.setEnabled(yMaxCheckbox.isSelected());
        });
        numbersInputPanel.add(yMaxCheckbox);

        makeCompactGrid(numbersInputPanel, 2, 6, 6, 6, 6, 6);
        return numbersInputPanel;
    }

    private JPanel buildExpressionPanel() {
        var feedbackLabel = new JLabel("Took " + graph.getComputeTime() + "ms.");
        feedbackLabel.setFont(Calculator.IO_FONT);
        feedbackLabel.setForeground(Color.BLACK);

        this.expressionField.setFont(Calculator.IO_FONT);
        this.expressionField.setColumns(64);
        ActionListener submitAction = evt -> {
            try {
                var parser = new Parser(new Lexer(this.expressionField.getText()));

                var expression = parser.parseExpression();

                this.graph.setExpression(expression);
                feedbackLabel.setForeground(Color.BLACK);
                feedbackLabel.setText("Took " + graph.getComputeTime() + "ms.");
            } catch (ParseException | RuntimeException e) {
                feedbackLabel.setForeground(Color.RED);
                feedbackLabel.setText(e.getMessage());
            }
        };
        this.expressionField.addActionListener(submitAction);

        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.50;
        c.gridheight = 1;
        c.gridwidth = 2;

        var expressionPanel = new JPanel(new GridBagLayout());
        expressionPanel.add(this.expressionField, c);

        c.gridx = 2;
        c.gridwidth = 1;
        var submitButton = new JButton("Submit");
        submitButton.addActionListener(submitAction);
        expressionPanel.add(submitButton, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        expressionPanel.add(feedbackLabel, c);

        return expressionPanel;
    }

    private JSpinner makeSpinnerFor(double initialValue, Double minimum, double step, Consumer<Double> setter) {
        var model = new SpinnerNumberModel(initialValue, minimum, null, step);
        var spinner = new JSpinner(model);
        var editor = new JSpinner.NumberEditor(spinner);
        editor.getTextField().setColumns(8);
        spinner.setEditor(editor);
        spinner.addChangeListener(e -> setter.accept((Double) model.getValue()));
        return spinner;
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
            int row, int col,
            Container parent,
            int cols) {
        var layout = (SpringLayout) parent.getLayout();
        var c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * Aligns the first {@code rows} * {@code cols} components of {@code parent} in a grid.
     * <p>
     * Each component in a column is as wide as the maximum
     * preferred width of the components in that column;
     * height is similarly determined for each row.
     * The parent is made just big enough to fit them all.
     *
     * @param parent the parent container
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        var x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            var width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).
                                getWidth());
            }
            for (int r = 0; r < rows; r++) {
                var constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        var y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            var height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).
                                getHeight());
            }
            for (int c = 0; c < cols; c++) {
                var constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        var pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }

    class InteractiveGraph extends Graph {
        private long computeTime = 0L;

        public InteractiveGraph(double xMin, double xMax, double step, Expression expression) {
            super(xMin, xMax, step, expression);
        }

        /**
         * Returns the time it took to compute the graph in milliseconds.
         *
         * @return the compute time
         */
        public long getComputeTime() {
            return this.computeTime;
        }

        @Override
        protected void compute() {
            var start = System.currentTimeMillis();
            super.compute();
            this.computeTime = System.currentTimeMillis() - start;
            if (GraphPanel.this.viewer != null)
                GraphPanel.this.viewer.repaint();
        }
    }
}
