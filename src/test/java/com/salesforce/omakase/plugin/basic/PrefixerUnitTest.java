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

package com.salesforce.omakase.plugin.basic;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Unit tests for {@link Prefixer}.
 *
 * @author nmcwilliams
 */
@SuppressWarnings("JavaDoc")
public class PrefixerUnitTest {
    @Test
    public void customShouldNotSupportAnythingByDefault() {
        assertThat(Prefixer.customBrowserSupport().support().supportedBrowsers()).isEmpty();
    }

    @Test
    public void defaultRearrangedFalse() {
        assertThat(Prefixer.defaultBrowserSupport().rearrange()).isFalse();
    }

    @Test
    public void setRearrange() {
        Prefixer prefixer = Prefixer.defaultBrowserSupport().rearrange(true);
        assertThat(prefixer.rearrange()).isTrue();
    }

    @Test
    public void defaultPruneFalse() {
        assertThat(Prefixer.defaultBrowserSupport().prune()).isFalse();
    }

    @Test
    public void setPrune() {
        Prefixer prefixer = Prefixer.defaultBrowserSupport().prune(true);
        assertThat(prefixer.prune()).isTrue();
    }
}