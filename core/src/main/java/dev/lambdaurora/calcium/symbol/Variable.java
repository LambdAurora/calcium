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

import dev.lambdaurora.calcium.Value;

/**
 * Represents a variable with a value and a boolean to determine whether it's a constant or not.
 */
public final class Variable {
    private final String name;
    private final boolean constant;
    private Value value;

    public Variable(String name, Value value, boolean constant) {
        this.name = name;
        this.value = value;
        this.constant = constant;
    }

    /**
     * Returns the name of this variable.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the value of this variable.
     *
     * @return the value
     */
    public Value getValue() {
        return this.value;
    }

    /**
     * Sets the value of this variable if it's not a constant.
     *
     * @param value the new value
     * @throws IllegalStateException if the variable is a constant
     */
    public void setValue(Value value) {
        if (this.isConstant()) throw new IllegalStateException("Cannot replace variable \"" + this.name + "\" as it is a constant.");
        this.value = value;
    }

    /**
     * Returns whether this variable is a constant or not.
     *
     * @return {@code true} if this variable is a constant, else {@code false}
     */
    public boolean isConstant() {
        return this.constant;
    }
}
