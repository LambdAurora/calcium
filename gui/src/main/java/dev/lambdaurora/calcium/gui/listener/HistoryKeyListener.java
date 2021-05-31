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

package dev.lambdaurora.calcium.gui.listener;

import dev.lambdaurora.calcium.gui.ExpressionHistory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Represents a key listener which can be used for browsing history.
 */
public class HistoryKeyListener implements KeyListener {
    private final ExpressionHistory history;
    protected final JTextField textField;

    public HistoryKeyListener(ExpressionHistory history, JTextField textField) {
        this.history = history;
        this.textField = textField;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isActionKey()) {
            var text = switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> this.history.browseUp(this.textField.getText());
                case KeyEvent.VK_DOWN -> this.history.browseDown(this.textField.getText());
                default -> null;
            };

            if (text != null) {
                this.textField.setText(text);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
