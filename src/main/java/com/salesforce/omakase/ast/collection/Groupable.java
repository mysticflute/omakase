/**
 * ADD LICENSE
 */
package com.salesforce.omakase.ast.collection;

import com.salesforce.omakase.ast.Rule;
import com.salesforce.omakase.ast.Syntax;
import com.salesforce.omakase.ast.declaration.Declaration;
import com.salesforce.omakase.ast.selector.Selector;
import com.salesforce.omakase.emitter.PreProcess;
import com.salesforce.omakase.emitter.Rework;
import com.salesforce.omakase.emitter.Validate;
import com.salesforce.omakase.plugin.DependentPlugin;
import com.salesforce.omakase.plugin.basic.SyntaxTree;

/**
 * Represents an item that appears in a group or chain of other related units, for usage with {@link SyntaxCollection}.
 * 
 * <p>
 * If you are using any of these methods in a plugin you will need to register the {@link SyntaxTree} as a dependency.
 * See {@link DependentPlugin} for more details.
 * 
 * <p>
 * In many cases you may need to check if this item is <em>detached</em> first (true if explicitly detached or if it's a
 * new instance not yet added to the tree). Detached items usually should be ignored, except to reattach.
 * 
 * @see SyntaxCollection
 * 
 * @param <T>
 *            The type of units to be grouped with.
 * 
 * @author nmcwilliams
 */
public interface Groupable<T extends Syntax & Groupable<T>> {

    /**
     * Gets whether this unit is the first within its group.
     * 
     * <p>
     * Some units will not be linked if the {@link SyntaxTree} plugin is not enabled. For example, {@link Rule},
     * {@link Selector}, {@link Declaration} (and if unlinked this will always return false).
     * 
     * <p>
     * Please note, if you are making decisions based on this value there are a few things to keep in mind. First, if
     * you are doing something in a {@link PreProcess} method, there is a good chance there are still more units to be
     * added, so while this unit may be first or last now that could shortly change. Secondly, any rework plugins may
     * add or remove new units before or after this one. As such, don't use this in a {@link PreProcess} method, be
     * thoughtful about usage in a {@link Rework} method, and prefer if possible to use in a {@link Validate} method,
     * when all preprocessing and rework should be completed.
     * 
     * @return True if the unit is first within its group. Always returns false if this unit is detached.
     */
    boolean isFirst();

    /**
     * Gets whether this unit is the last within its group.
     * 
     * <p>
     * Some units will not be linked if the {@link SyntaxTree} plugin is not enabled. For example, {@link Rule},
     * {@link Selector}, {@link Declaration} (and if unlinked this will always return false).
     * 
     * <p>
     * Please note, if you are making decisions based on this value there are a few things to keep in mind. First, if
     * you are doing something in a {@link PreProcess} method, there is a good chance there are still more units to be
     * added, so while this unit may be first or last now that could shortly change. Secondly, any rework plugins may
     * add or remove new units before or after this one. As such, don't use this in a {@link PreProcess} method, be
     * thoughtful about usage in a {@link Rework} method, and prefer if possible to use in a {@link Validate} method,
     * when all preprocessing and rework should be completed.
     * 
     * @return True if the unit is last within its group. Always returns false if this unit is detached.
     */
    boolean isLast();

    /**
     * Gets the parent {@link SyntaxCollection} this unit belongs to.
     * 
     * @return The parent {@link SyntaxCollection}.
     * 
     * @throws IllegalStateException
     *             If this unit is currently detached (doesn't belong to any group).
     */
    SyntaxCollection<T> group();

    /**
     * Prepends the given unit before this one.
     * 
     * @param unit
     *            The unit to prepend.
     * @return this, for chaining.
     * 
     * @throws IllegalStateException
     *             If this unit is currently detached (doesn't belong to any group).
     */
    Groupable<T> prepend(T unit);

    /**
     * Appends the given unit after this one.
     * 
     * @param unit
     *            The unit to append.
     * @return this, for chaining.
     * 
     * @throws IllegalStateException
     *             If this unit is currently detached (doesn't belong to any group).
     */
    Groupable<T> append(T unit);

    /**
     * Detaches (removes) this unit from the parent {@link SyntaxCollection}.
     */
    void detach();

    /**
     * Gets whether this unit is detached. This should be true if either this unit was explicitly detached, or it is yet
     * to be added to a unit within the tree.
     * 
     * @return True if this unit is detached.
     */
    boolean isDetached();

    /**
     * Sets the parent group. This should only be called internally... calling it yourself may result in expected
     * behavior.
     * 
     * @param group
     *            The parent group.
     * @return this, for chaining.
     */
    Groupable<T> parent(SyntaxCollection<T> group);
}
