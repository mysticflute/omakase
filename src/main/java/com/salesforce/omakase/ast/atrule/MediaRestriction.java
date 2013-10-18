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

package com.salesforce.omakase.ast.atrule;

import com.salesforce.omakase.parser.token.ConstantEnum;
import com.salesforce.omakase.writer.StyleAppendable;
import com.salesforce.omakase.writer.StyleWriter;
import com.salesforce.omakase.writer.Writable;

import java.io.IOException;

/**
 * For media queries, represents the keywords 'and' and 'or'.
 *
 * @author nmcwilliams
 */
public enum MediaRestriction implements ConstantEnum, Writable {
    /** 'only' keyword */
    ONLY("only"),
    /** 'not' keyword */
    NOT("not");

    private final String constant;

    MediaRestriction(String constant) {
        this.constant = constant;
    }

    @Override
    public String constant() {
        return constant;
    }

    @Override
    public boolean caseSensitive() {
        return false;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public void write(StyleWriter writer, StyleAppendable appendable) throws IOException {
        appendable.append(constant);
    }
}
