/**
 * ADD LICENSE
 */
package com.salesforce.omakase.ast.selector;

import static com.salesforce.omakase.emitter.EmittableRequirement.REFINED_SELECTOR;

import com.salesforce.omakase.As;
import com.salesforce.omakase.ast.collection.AbstractGroupable;
import com.salesforce.omakase.emitter.Description;
import com.salesforce.omakase.emitter.Emittable;

/**
 * Represents a CSS attribute selector.
 * 
 * @author nmcwilliams
 */
@Emittable
@Description(value = "attribute selector segment", broadcasted = REFINED_SELECTOR)
public class AttributeSelector extends AbstractGroupable<SelectorPart> implements SimpleSelector {
    /**
     * Creates a new instance with the given line and column numbers.
     * 
     * @param line
     *            The line number.
     * @param column
     *            The column number.
     */
    public AttributeSelector(int line, int column) {
        super(line, column);
    }

    @Override
    public boolean isSelector() {
        return true;
    }

    @Override
    public boolean isCombinator() {
        return false;
    }

    @Override
    public SelectorPartType type() {
        return SelectorPartType.ATTRIBUTE_SELECTOR;
    }

    @Override
    protected SelectorPart self() {
        return this;
    }

    @Override
    public String toString() {
        return As.string(this)
            .indent()
            .add("position", super.toString())
            .toString();
    }
}
