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

import dev.lambdaurora.calcium.expression.IdentifierExpression;
import dev.lambdaurora.calcium.gui.component.GraphPanel;
import dev.lambdaurora.calcium.gui.listener.HistoryKeyListener;

import javax.swing.*;
import javax.swing.text.StyleContext;
import java.awt.*;

public class CalculatorFrame {
    private final Calculator calculator;
    private final JFrame frame;
    private JTextPane outputPane;
    private JTextField inputTextField;

    public CalculatorFrame(Calculator calculator) {
        this.calculator = calculator;

        this.frame = new JFrame("Calculator");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setMinimumSize(new Dimension(400, 600));

        this.buildMenuBar();
        this.buildTabs();
    }

    private void buildMenuBar() {
        var menuBar = new JMenuBar();

        // Build the File menu
        var menu = new JMenu("File");

        var menuItem = new JMenuItem("Clear output");
        menuItem.addActionListener(e -> this.outputPane.setText(""));
        menu.add(menuItem);

        menuItem = new JMenuItem("Clear symbol table");
        menuItem.addActionListener(e -> this.calculator.getSymbolTable().clear());
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Quit");
        menuItem.addActionListener(e -> System.exit(0));
        menu.add(menuItem);

        menuBar.add(menu);

        // Build the Graph menu
        /*menu = new JMenu("Graph");
        menuBar.add(menu);*/

        this.frame.setJMenuBar(menuBar);
    }

    private void buildTabs() {
        var tabs = new JTabbedPane();

        tabs.addTab("Calculator", this.buildIOPane());
        tabs.addTab("Graph", new GraphPanel(-10.0, 10.0, 1.0, new IdentifierExpression("x"), "x"));

        this.frame.setContentPane(tabs);
    }

    private JPanel buildIOPane() {
        this.buildOutputPane();

        var panel = new JPanel(new GridBagLayout());
        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.85;
        c.gridheight = 1;

        var scrollOutputPane = new JScrollPane(this.outputPane);
        scrollOutputPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollOutputPane, c);

        this.inputTextField = new JTextField();
        this.inputTextField.setFont(Calculator.IO_FONT);
        this.inputTextField.addActionListener(e -> {
            var text = this.inputTextField.getText();
            this.inputTextField.setText("");
            this.calculator.evaluateExpression(text);
        });
        this.calculator.setInputField(this.inputTextField);
        this.inputTextField.addKeyListener(new HistoryKeyListener(this.calculator.getHistory(), this.inputTextField));

        c.gridy = 1;
        c.weighty = 0.15;
        c.gridheight = 1;

        var inputPanel = new JPanel(new GridBagLayout());

        var inputC = (GridBagConstraints) c.clone();
        inputC.gridy = 0;
        inputC.weighty = 0.5;
        inputPanel.add(this.inputTextField, inputC);

        var outputModeComboBox = new JComboBox<>(OutputMode.values());
        outputModeComboBox.setToolTipText("The output mode of the calculator.");
        outputModeComboBox.setSelectedIndex(0);
        outputModeComboBox.addActionListener(e -> this.calculator.setOutputMode((OutputMode) outputModeComboBox.getSelectedItem()));
        inputC.gridy = 1;
        inputPanel.add(outputModeComboBox, inputC);

        panel.add(inputPanel, c);

        //this.frame.setContentPane(panel);
        return panel;
    }

    private void buildOutputPane() {
        this.outputPane = new JTextPane();
        this.outputPane.setFont(Calculator.IO_FONT);
        var doc = this.outputPane.getStyledDocument();

        // Initialize some styles.
        var def = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);

        var regular = doc.addStyle("regular", def);
        PrintStyle.REGULAR.setupStyle(regular);

        for (var printStyle : PrintStyle.values()) {
            if (printStyle == PrintStyle.REGULAR) continue;
            printStyle.setupStyle(doc.addStyle(printStyle.getName(), regular));
        }

        this.calculator.setOutputPane(this.outputPane);
        this.calculator.printOut("Welcome.", PrintStyle.BOLD);

        this.outputPane.setEditable(false);
    }

    public void start() {
        this.frame.pack();
        this.frame.setVisible(true);
    }
}
