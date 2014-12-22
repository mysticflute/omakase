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

package com.salesforce.omakase.ast;

import com.salesforce.omakase.error.OmakaseException;
import com.salesforce.omakase.writer.StyleWriter;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;

/** Unit tests for {@link Comment}. */
@SuppressWarnings("JavaDoc")
public class CommentTest {
    @org.junit.Rule public final org.junit.rules.ExpectedException exception = ExpectedException.none();

    @Test
    public void content() {
        Comment c = new Comment("test");
        assertThat(c.content()).isEqualTo("test");
    }

    @Test
    public void isWritable() {
        assertThat(new Comment("test").isWritable()).isTrue();
    }

    @Test
    public void writeVerbose() throws IOException {
        Comment c = new Comment(" test ");
        StyleWriter writer = StyleWriter.verbose();
        assertThat(writer.writeSnippet(c)).isEqualTo("/* test */");
    }

    @Test
    public void writeInline() throws IOException {
        Comment c = new Comment(" test ");
        StyleWriter writer = StyleWriter.inline();
        assertThat(writer.writeSnippet(c)).isEqualTo("/* test */");
    }

    @Test
    public void writeCompressed() throws IOException {
        Comment c = new Comment(" test ");
        StyleWriter writer = StyleWriter.compressed();
        assertThat(writer.writeSnippet(c)).isEqualTo("/* test */");
    }

    @Test
    public void hasAnnotationTrueSpaced() {
        Comment c = new Comment(" @test");
        assertThat(c.hasAnnotation("test")).isTrue();
    }

    @Test
    public void hasAnnotationFalseCommentNotAnnotated() {
        Comment c = new Comment("test");
        assertThat(c.hasAnnotation("test")).isFalse();
    }

    @Test
    public void hasAnnotationFalseDifferentAnnotation() {
        Comment c = new Comment("@test2");
        assertThat(c.hasAnnotation("test")).isFalse();
    }

    @Test
    public void hasAnnotationFalseDoesntStartWithAnnotation() {
        Comment c = new Comment("test @test");
        assertThat(c.hasAnnotation("test")).isFalse();
    }

    @Test
    public void getAnnotationPresentNoArgs() {
        Comment c = new Comment("@test");
        CssAnnotation a = c.annotation("test").get();
        assertThat(a.arguments()).isEmpty();
    }

    @Test
    public void getAnnotationPresentMultipleArgs() {
        Comment c = new Comment("@test one two");
        CssAnnotation a = c.annotation("test").get();
        assertThat(a.arguments()).contains("one", "two");
    }

    @Test
    public void getAnnotationAbsentNoAnnotation() {
        Comment c = new Comment("test");
        assertThat(c.annotation("test").isPresent()).isFalse();
    }

    @Test
    public void getAnnotationAbsentDifferentAnnotation() {
        Comment c = new Comment("@test2");
        assertThat(c.annotation("test").isPresent()).isFalse();
    }

    @Test
    public void errorsIfTooManyArgs() {
        Comment c = new Comment("@test a b c d e f");
        exception.expect(OmakaseException.class);
        c.hasAnnotation("test");
    }

    @Test
    public void errorsIfAnnotationMixedWithComment() {
        Comment c = new Comment("@test arg. This is used to specify that...");
        exception.expect(OmakaseException.class);
        c.hasAnnotation("test");
    }

    @Test
    public void errorsIfMultipleAnnotationsInSameBlock() {
        Comment c = new Comment("@test @test");
        exception.expect(OmakaseException.class);
        c.hasAnnotation("test");
    }

    @Test
    public void getAnnotationPresent() {
        Comment c = new Comment("@test");
        assertThat(c.annotation().get().name()).isEqualTo("test");
    }

    @Test
    public void getAnnotationAbsent() {
        Comment c = new Comment("test");
        assertThat(c.annotation().isPresent()).isFalse();
    }

    @Test
    public void newCommentFromAnnotation() {
        CssAnnotation a = new CssAnnotation("test");
        Comment c = new Comment(a);
        assertThat(c.content()).isEqualTo("@test");
    }

    @Test
    public void newCommentFromAnnotationWithArgs() {
        CssAnnotation a = new CssAnnotation("test", "arg");
        Comment c = new Comment(a);
        assertThat(c.content()).isEqualTo("@test arg");
    }

    @Test
    public void fromAnnotationObjectHasAnnotationStringTrue() {
        CssAnnotation a = new CssAnnotation("test", "arg");
        Comment c = new Comment(a);
        assertThat(c.hasAnnotation("test")).isTrue();
    }

    @Test
    public void fromAnnotationObjectHasAnnotationStringFalse() {
        CssAnnotation a = new CssAnnotation("test", "arg");
        Comment c = new Comment(a);
        assertThat(c.hasAnnotation("test2")).isFalse();
    }

    @Test
    public void fromAnnotationObjectHasAnnotationObjectTrue() {
        CssAnnotation a = new CssAnnotation("test", "arg");
        Comment c = new Comment(a);
        assertThat(c.hasAnnotation(a)).isTrue();
    }

    @Test
    public void fromAnnotationObjectHasAnnotationDifferentInstance() {
        CssAnnotation a = new CssAnnotation("test", "arg");
        Comment c = new Comment(a);
        assertThat(c.hasAnnotation(new CssAnnotation("test", "arg"))).isTrue();
    }

    @Test
    public void fromAnnotationObjectHasAnnotationObjectFalse() {
        CssAnnotation a = new CssAnnotation("test", "arg");
        Comment c = new Comment(a);
        assertThat(c.hasAnnotation(new CssAnnotation("blah"))).isFalse();
    }

    @Test
    public void fromAnnotationObjectGetAnnotationByNamePresent() {
        CssAnnotation a = new CssAnnotation("test", "arg");
        Comment c = new Comment(a);
        assertThat(c.annotation("test").get()).isSameAs(a);
    }

    @Test
    public void fromAnnotationObjectGetAnnotationByNameAbsent() {
        CssAnnotation a = new CssAnnotation("test", "arg");
        Comment c = new Comment(a);
        assertThat(c.annotation("tes2t").isPresent()).isFalse();
    }

    @Test
    public void startsWithBangTrue() {
        Comment c = new Comment("!copyright");
        assertThat(c.startsWithBang()).isTrue();
    }

    @Test
    public void startsWithBangFalse() {
        Comment c = new Comment("blah!blah!");
        assertThat(c.startsWithBang()).isFalse();
    }
}
