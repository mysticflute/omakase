/*
 * Copyright (C) 2013 salesforce.com, inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.salesforce.omakase.ast.extended;

import com.salesforce.omakase.plugin.basic.ConditionalsRefiner;
import com.salesforce.omakase.util.As;
import com.salesforce.omakase.ast.AbstractSyntax;
import com.salesforce.omakase.ast.Statement;
import com.salesforce.omakase.ast.Stylesheet;
import com.salesforce.omakase.ast.atrule.AtRuleBlock;
import com.salesforce.omakase.ast.collection.SyntaxCollection;
import com.salesforce.omakase.broadcast.annotation.Description;
import com.salesforce.omakase.broadcast.annotation.Subscribable;
import com.salesforce.omakase.plugin.basic.Conditionals;
import com.salesforce.omakase.plugin.basic.ConditionalsManager;
import com.salesforce.omakase.writer.StyleAppendable;
import com.salesforce.omakase.writer.StyleWriter;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.salesforce.omakase.broadcast.BroadcastRequirement.REFINED_AT_RULE;

/**
 * An extension to the standard CSS syntax that allows for conditional at-rules.
 * <p/>
 * Example of a conditional at-rule:
 * <pre>
 * {@code @}if(ie7) { .test{color:red} }
 * </pre>
 * <p/>
 * This block will output its inner statements if its condition (argument) is contained within a specified set of strings that
 * should evaluate to "true".
 * <p/>
 * For more information on using and configuring conditionals see the main readme file.
 *
 * @author nmcwilliams
 * @see Conditionals
 * @see ConditionalsRefiner
 */
@Subscribable
@Description(value = "conditionals", broadcasted = REFINED_AT_RULE)
public final class ConditionalAtRuleBlock extends AbstractSyntax implements AtRuleBlock {
    private final SyntaxCollection<Stylesheet, Statement> statements;
    private final ConditionalsManager manager;
    private final String condition;

    /**
     * See the notes on the {@link #ConditionalAtRuleBlock(int, int, ConditionalsManager, String, SyntaxCollection)} constructor.
     * This is the same except that it defaults to no line and column numbers. This should be used for dynamically created
     * conditional at-rule blocks.
     *
     * @param manager
     *     The {@link ConditionalsManager} instance.
     * @param condition
     *     The condition for this particular conditional at-rule block.
     * @param statements
     *     The inner statements of the block. These will be printed out if the condition is contained within the trueConditions
     *     set.
     */
    public ConditionalAtRuleBlock(ConditionalsManager manager, String condition, SyntaxCollection<Stylesheet,
        Statement> statements) {
        this(-1, -1, manager, condition, statements);
    }

    /**
     * Creates a new {@link ConditionalAtRuleBlock} instance with the given true conditions, condition, and set of statements.
     * <p/>
     * The given set of conditions represents the set of strings that equal the "true" values. During printing/writing of the CSS
     * source, this block will only be printed if its given condition is contained with the trueConditions set. Note that this is
     * case-sensitive. It is highly recommended to enforce a single case (e.g., lower-case) among the given condition and set of
     * trueConditions in order to ensure this works properly.
     * <p/>
     * It is acceptable for the given Set to be changed after being passed to this class, allowing for producing multiple
     * variations of the CSS source from a single parse operation. For example, set the trueConditions, write out the source,
     * change the trueConditions, write out the source again, etc... . However also note that this also makes this class not
     * thread-safe, depending on how the given set is used or altered outside of this class.
     *
     * @param line
     *     The line number.
     * @param column
     *     The column number.
     * @param manager
     *     The {@link ConditionalsManager} instance.
     * @param condition
     *     The condition for this particular conditional at-rule block.
     * @param statements
     *     The inner statements of the block. These will be printed out if the condition is contained within the trueConditions
     *     set.
     */
    public ConditionalAtRuleBlock(int line, int column, ConditionalsManager manager, String condition,
        SyntaxCollection<Stylesheet, Statement> statements) {
        super(line, column);
        this.condition = checkNotNull(condition, "condition cannot be null");
        this.manager = checkNotNull(manager, "manager cannot be null");
        this.statements = checkNotNull(statements, "statements cannot be null");
    }

    /**
     * Gets the condition (argument) of the conditional at-rule block.
     *
     * @return The lower-cased condition string.
     */
    public String condition() {
        return condition;
    }

    /**
     * Gets the {@link SyntaxCollection} of statements within this conditional at-rule block.
     *
     * @return The collection of statements within this conditional at-rule block.
     */
    public SyntaxCollection<Stylesheet, Statement> statements() {
        return statements;
    }

    @Override
    public boolean isWritable() {
        return manager.isPassthroughMode() || manager.hasCondition(condition);
    }

    @Override
    public void write(StyleWriter writer, StyleAppendable appendable) throws IOException {
        if (manager.isPassthroughMode()) {
            appendable.append("@if(").append(condition).append(')');
            appendable.spaceIf(!writer.isCompressed());
            appendable.append('{');
            appendable.newlineIf(!writer.isCompressed());
        }

        for (Statement statement : statements) {
            writer.writeInner(statement, appendable);
        }

        if (manager.isPassthroughMode()) {
            appendable.newlineIf(!writer.isCompressed());
            appendable.append('}');
        }
    }

    @Override
    public String toString() {
        return As.string(this)
            .indent()
            .add("condition", condition)
            .add("manager", manager)
            .add("statements", statements)
            .toString();
    }
}
