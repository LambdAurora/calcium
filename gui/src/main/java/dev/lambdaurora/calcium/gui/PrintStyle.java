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

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Represents a print style.
 * <p>
 * This is used to determine the style to print with when printing to the graphical output console.
 */
public enum PrintStyle {
    REGULAR(style -> StyleConstants.setFontSize(style, 14)),
    BOLD(style -> StyleConstants.setBold(style, true)),
    ITALIC(style -> StyleConstants.setItalic(style, true)),
    INPUT(style -> StyleConstants.setForeground(style, new Color(0xff2d982d))),
    ERROR(style -> StyleConstants.setForeground(style, Color.RED));

    private final String name;
    private final Consumer<Style> styleSetup;

    PrintStyle(Consumer<Style> styleSetup) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.styleSetup = styleSetup;
    }

    /**
     * Returns the name of the print style.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setups the given style.
     *
     * @param style the style to setup
     */
    public void setupStyle(Style style) {
        this.styleSetup.accept(style);
    }
}
