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

package com.salesforce.omakase.plugin.other;

import com.salesforce.omakase.ast.declaration.value.UnquotedIEFilter;
import com.salesforce.omakase.broadcast.annotation.Rework;
import com.salesforce.omakase.broadcast.annotation.Validate;
import com.salesforce.omakase.parser.refiner.UnquotedIEFilterStrategy;
import com.salesforce.omakase.parser.refiner.RefinableStrategy;
import com.salesforce.omakase.plugin.SyntaxPlugin;

/**
 * This plugin enables unquoted IE proprietary filters.
 * <p/>
 * For example:
 * <pre>
 * {@code filter: progid:DXImageTransform.Microsoft.Shadow(color='#969696', Direction=145, Strength=3);}
 * </pre>
 * <p/>
 * See http://msdn.microsoft.com/en-us/library/ms532847(v=vs.85).aspx for more information on filters.
 * <p/>
 * Note that this is <em>not needed</em> for quoted filters, commonly used with the {@code -ms-filter} property instead of {@code
 * filter}. For example:
 * <pre>
 *  {@code -ms-filter: "progid:DXImageTransform.Microsoft.Shadow(color='#969696', Direction=145, Strength=3)";}
 * </pre>
 * <p/>
 * Enabling this plugin will allow the parser to "understand" this proprietary syntax. The whole property-value will be output
 * as-is once it discovers that it starts with the special "progid:" prefix. You can subscribe to created {@link UnquotedIEFilter}
 * objects as you would other standard syntax units using {@link Rework}, {@link Validate}, etc...
 * <p/>
 * Example usage:
 * <pre>
 * <code>Omakase.source(input).request(new UnquotedIEFilterPlugin()).(...).process()</code>
 * </pre>
 *
 * @author nmcwilliams
 * @see UnquotedIEFilter
 */
public class UnquotedIEFilterPlugin implements SyntaxPlugin {
    private static final RefinableStrategy STRATEGY = new UnquotedIEFilterStrategy();

    @Override
    public RefinableStrategy getRefinableStrategy() {
        return STRATEGY;
    }
}