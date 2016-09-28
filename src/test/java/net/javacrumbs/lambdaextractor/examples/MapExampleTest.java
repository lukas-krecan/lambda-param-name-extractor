/**
 * Copyright 2009-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.lambdaextractor.examples;

import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static net.javacrumbs.lambdaextractor.ParameterNameExtractor.extractFirstParameterName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

public class MapExampleTest {
    @Test
    public void testCreateMap() {
        Map<String, String> map = map(
            key1 -> "value1",
            key2 -> "value2"
        );
        assertThat(map).containsExactly(entry("key1", "value1"), entry("key2", "value2"));
    }

    @Test
    public void testCreateMapExtenalValue() {
        String value2 = "value2";

        Map<String, String> map = map(
            key1 -> "value1",
            key2 -> value2
        );
        assertThat(map).containsExactly(entry("key1", "value1"), entry("key2", "value2"));
    }

    @SafeVarargs
    public static <T> Map<String, T> map(Entry<T>... entries) {
        Map<String, T> result = new HashMap<>();
        for (Entry<T> entry: entries) {
            String name = extractFirstParameterName(entry);
            T value = entry.apply(name);
            result.put(name, value);
        }
        return result;
    }

    @FunctionalInterface
    public static interface Entry<T> extends Serializable {
        public T apply(String name);
    }
}
