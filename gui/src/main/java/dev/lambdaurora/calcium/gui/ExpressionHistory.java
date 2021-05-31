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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an history of expressions.
 */
public class ExpressionHistory {
    private final List<String> history = new ArrayList<>();
    private int currentlyBrowsing = 0;
    private String currentlyTyping = "";

    public void insertExpression(String expression) {
        // Add the expression string to the history and take care to not duplicate it if the latest expression in history
        // is the same as currently.
        if (this.history.isEmpty() || !this.history.get(this.history.size() - 1).equals(expression))
            this.history.add(expression);
        // Size limit for the history to avoid eating up the RAM.
        if (this.history.size() > 2048)
            this.history.remove(0);

        this.currentlyBrowsing = this.history.size();
        this.currentlyTyping = "";
    }

    /**
     * Returns whether the expression history is currently being browsed or not.
     *
     * @return {@code true} if the expression history is currently being browsed, otherwise {@code false}
     */
    public boolean isCurrentlyBeingBrowsed() {
        return this.currentlyBrowsing != this.history.size();
    }

    /**
     * Browses up the history.
     * <p>
     * Also keep in memory {@code currentText} if the history was not already being browsed
     * so {@link #browseDown(String)} can set it back.
     *
     * @param currentText the current input text
     * @return one older text in the history than the current one, or {@code currentText} if the history is empty
     */
    public String browseUp(String currentText) {
        if (!this.isCurrentlyBeingBrowsed())
            this.currentlyTyping = currentText;

        if (this.history.isEmpty())
            return currentText;

        this.currentlyBrowsing = Math.max(0, this.currentlyBrowsing - 1);
        return this.history.get(this.currentlyBrowsing);
    }

    /**
     * Browses down the history.
     *
     * @param currentText the current input text
     * @return {@code currentText} if the history is empty,
     * or the original text the user was typing if browsing is finished,
     * or a newer text in the history.
     */
    public String browseDown(String currentText) {
        if (!this.isCurrentlyBeingBrowsed()) // Unlikely
            this.currentlyTyping = currentText;

        this.currentlyBrowsing = Math.min(this.currentlyBrowsing + 1, this.history.size());

        if (this.currentlyBrowsing == this.history.size())
            return this.currentlyTyping;

        return this.history.get(this.currentlyBrowsing);
    }
}
