/**
 * Copyright 2009-2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.lambdaextractor.examples;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static net.javacrumbs.lambdaextractor.examples.OldFashionedMapTest.Entry.entry;
import static org.assertj.core.api.Assertions.assertThat;

public class OldFashionedMapTest {

    @Test
    public void testCreateUgly() {
        Map<String, String> map = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        assertThat(map).containsExactly(MapEntry.entry("key1", "value1"), MapEntry.entry("key2", "value2"));
    }

























    @Test
    public void testCreateStaticMethods() {
        Map<String, String> map = map(
            entry("key1", "value1"),
            entry("key2", "value2")
        );
        assertThat(map).containsExactly(MapEntry.entry("key1", "value1"), MapEntry.entry("key2", "value2"));
    }

    @SafeVarargs
    public static <S, T> Map<S, T> map(Entry<S, T>... entries) {
        Map<S, T> result = new HashMap<>();
        for (Entry<S, T> entry : entries) {
            result.put(entry.key, entry.value);
        }
        return result;
    }

    public static class Entry<S, T> {
        private final S key;
        private final T value;

        private Entry(S key, T value) {
            this.key = key;
            this.value = value;
        }

        public static <S, T> Entry<S, T> entry(S key, T value) {
            return new Entry<>(key, value);
        }
    }
}
