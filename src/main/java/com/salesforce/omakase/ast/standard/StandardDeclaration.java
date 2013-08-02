/**
 * ADD LICENSE
 */
package com.salesforce.omakase.ast.standard;

import com.google.common.base.Objects;
import com.salesforce.omakase.ast.declaration.RefinedDeclaration;

/**
 * TODO Description
 * 
 * @author nmcwilliams
 */
final class StandardDeclaration extends AbstractSyntax implements RefinedDeclaration {
    private final String property;
    private final String value;

    /**
     * TODO
     * 
     * @param line
     *            TODO
     * @param column
     *            TODO
     * @param property
     *            TODO
     * @param value
     *            TODO
     */
    public StandardDeclaration(int line, int column, String property, String value) {
        super(line, column);
        this.property = property;
        this.value = value;
    }

    @Override
    public String property() {
        return property;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public RefinedDeclaration refine() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("line", line())
            .add("column", column())
            .add("property", property)
            .add("value", value)
            .toString();
    }
}
