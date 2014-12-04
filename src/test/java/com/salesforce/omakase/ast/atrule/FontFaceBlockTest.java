/*
 * Copyright (C) 2014 salesforce.com, inc.
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

import com.salesforce.omakase.ast.Status;
import com.salesforce.omakase.ast.declaration.KeywordValue;
import com.salesforce.omakase.ast.declaration.PropertyName;
import com.salesforce.omakase.ast.declaration.PropertyValue;
import com.salesforce.omakase.ast.declaration.QuotationMode;
import com.salesforce.omakase.ast.declaration.StringValue;
import com.salesforce.omakase.ast.declaration.UrlFunctionValue;
import com.salesforce.omakase.data.Keyword;
import com.salesforce.omakase.data.Property;
import com.salesforce.omakase.test.functional.StatusChangingBroadcaster;
import com.salesforce.omakase.writer.StyleWriter;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Unit tests for {@link FontFaceBlock}.
 *
 * @author nmcwilliams
 */
@SuppressWarnings({"JavaDoc", "FieldCanBeLocal", "SpellCheckingInspection"})
public class FontFaceBlockTest {
    private PropertyName samplePropertyName;
    private PropertyValue samplePropertyValue;
    private FontDescriptor descriptor;
    private FontFaceBlock block;

    @Before
    public void setup() {
        samplePropertyName = PropertyName.using(Property.FONT_FAMILY);
        samplePropertyValue = PropertyValue.of(StringValue.of(QuotationMode.DOUBLE, "My Font"));
        descriptor = new FontDescriptor(samplePropertyName, samplePropertyValue);
        block = new FontFaceBlock();
    }

    @Test
    public void testAddFontDescriptor() {
        block.fontDescriptors().append(descriptor);
        assertThat(block.fontDescriptors()).containsExactly(descriptor);
    }

    @Test
    public void testPropagateBroadcast() {
        assertThat(descriptor.status()).isSameAs(Status.UNBROADCASTED);
        assertThat(block.status()).isSameAs(Status.UNBROADCASTED);

        block.fontDescriptors().append(descriptor);

        block.propagateBroadcast(new StatusChangingBroadcaster());
        assertThat(descriptor.status()).isNotSameAs(Status.UNBROADCASTED);
        assertThat(block.status()).isNotSameAs(Status.UNBROADCASTED);
    }

    @Test
    public void isWritableTrue() {
        block.fontDescriptors().append(descriptor);
        assertThat(block.isWritable()).isTrue();
    }

    @Test
    public void isNotWritableWhenNoFontDescriptors() {
        assertThat(block.isWritable()).isFalse();
    }

    @Test
    public void writeVerbose() {
        block = new FontFaceBlock();

        PropertyName n1 = PropertyName.using(Property.FONT_FAMILY);
        KeywordValue v1 = KeywordValue.of("MyFont");

        PropertyName n2 = PropertyName.using(Property.SRC);
        UrlFunctionValue v2 = new UrlFunctionValue("MyFont.ttf");

        PropertyName n3 = PropertyName.using(Property.FONT_WEIGHT);
        KeywordValue v3 = KeywordValue.of(Keyword.BOLD);

        block.fontDescriptors().append(new FontDescriptor(n1, PropertyValue.of(v1)));
        block.fontDescriptors().append(new FontDescriptor(n2, PropertyValue.of(v2)));
        block.fontDescriptors().append(new FontDescriptor(n3, PropertyValue.of(v3)));

        String expected = " {\n" +
            "  font-family: MyFont;\n" +
            "  src: url(MyFont.ttf);\n" +
            "  font-weight: bold;\n" +
            "}";

        StyleWriter writer = StyleWriter.verbose();
        assertThat(writer.writeSnippet(block)).isEqualTo(expected);
    }

    @Test
    public void writeInline() {
        block = new FontFaceBlock();

        PropertyName n1 = PropertyName.using(Property.FONT_FAMILY);
        KeywordValue v1 = KeywordValue.of("MyFont");

        PropertyName n2 = PropertyName.using(Property.SRC);
        UrlFunctionValue v2 = new UrlFunctionValue("MyFont.ttf");

        PropertyName n3 = PropertyName.using(Property.FONT_WEIGHT);
        KeywordValue v3 = KeywordValue.of(Keyword.BOLD);

        block.fontDescriptors().append(new FontDescriptor(n1, PropertyValue.of(v1)));
        block.fontDescriptors().append(new FontDescriptor(n2, PropertyValue.of(v2)));
        block.fontDescriptors().append(new FontDescriptor(n3, PropertyValue.of(v3)));

        String expected = " {font-family:MyFont; src:url(MyFont.ttf); font-weight:bold}";

        StyleWriter writer = StyleWriter.inline();
        assertThat(writer.writeSnippet(block)).isEqualTo(expected);
    }

    @Test
    public void writeCompressed() {
        block = new FontFaceBlock();

        PropertyName n1 = PropertyName.using(Property.FONT_FAMILY);
        KeywordValue v1 = KeywordValue.of("MyFont");

        PropertyName n2 = PropertyName.using(Property.SRC);
        UrlFunctionValue v2 = new UrlFunctionValue("MyFont.ttf");

        PropertyName n3 = PropertyName.using(Property.FONT_WEIGHT);
        KeywordValue v3 = KeywordValue.of(Keyword.BOLD);

        block.fontDescriptors().append(new FontDescriptor(n1, PropertyValue.of(v1)));
        block.fontDescriptors().append(new FontDescriptor(n2, PropertyValue.of(v2)));
        block.fontDescriptors().append(new FontDescriptor(n3, PropertyValue.of(v3)));

        String expected = "{font-family:MyFont;src:url(MyFont.ttf);font-weight:bold}";

        StyleWriter writer = StyleWriter.compressed();
        assertThat(writer.writeSnippet(block)).isEqualTo(expected);
    }

    @Test
    public void testCopy() {
        block.fontDescriptors().append(descriptor);
        FontFaceBlock copy = (FontFaceBlock)block.copy();
        assertThat(copy.fontDescriptors()).hasSameSizeAs(block.fontDescriptors());
    }
}