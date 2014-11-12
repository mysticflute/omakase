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

package com.salesforce.omakase.parser.raw;

import com.google.common.base.Optional;
import com.salesforce.omakase.Message;
import com.salesforce.omakase.ast.RawSyntax;
import com.salesforce.omakase.ast.atrule.AtRule;
import com.salesforce.omakase.broadcast.Broadcaster;
import com.salesforce.omakase.parser.AbstractParser;
import com.salesforce.omakase.parser.ParserException;
import com.salesforce.omakase.parser.Source;
import com.salesforce.omakase.parser.refiner.GenericRefiner;
import com.salesforce.omakase.parser.token.TokenFactory;
import com.salesforce.omakase.parser.token.Tokens;

import java.util.List;

/**
 * Parses an {@link AtRule}.
 *
 * @author nmcwilliams
 * @see AtRule
 */
public final class RawAtRuleParser extends AbstractParser {
    @Override
    public boolean parse(Source source, Broadcaster broadcaster, GenericRefiner refiner) {
        TokenFactory tf = tokenFactory();

        source.skipWhitepace();
        source.collectComments();

        // save off current line and column
        int startLine = source.originalLine();
        int startColumn = source.originalColumn();

        // must begin with '@'
        if (!source.optionallyPresent(Tokens.AT_RULE)) return false;

        // read the name
        Optional<String> name = source.readIdent();
        if (!name.isPresent()) throw new ParserException(source, Message.MISSING_AT_RULE_NAME);

        // read everything up until the end of the at-rule expression (usually a semicolon or open bracket).
        int line = source.originalLine();
        int column = source.originalColumn();
        String content = source.until(tf.atRuleExpressionEnd()).trim();
        RawSyntax expression = content.isEmpty() ? null : new RawSyntax(line, column, content);

        // skip whitespace after the expression
        source.skipWhitepace();
        List<String> comments = source.flushComments();

        RawSyntax block = null;

        // parse the termination (usually ';' or the start of an at-rule block), then parse the block
        if (!source.optionallyPresent(tf.atRuleTermination()) && tf.atRuleBlockBegin().matches(source.current())) {
            line = source.originalLine();
            column = source.originalColumn();
            content = source.chompEnclosedValue(tf.atRuleBlockBegin(), tf.atRuleBlockEnd()).trim();
            block = content.isEmpty() ? null : new RawSyntax(line, column, content);
        }

        // expression content must be present
        if (expression == null && block == null) throw new ParserException(source, Message.MISSING_AT_RULE_VALUE);

        source.flushComments(); // ignore any comments that were in the block, the block itself will handle them

        // create and broadcast the new rule
        AtRule rule = new AtRule(startLine, startColumn, name.get(), expression, block, refiner);
        rule.comments(comments);
        broadcaster.broadcast(rule);

        return true;
    }
}
