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
package net.javacrumbs.lambdaextractor;

import org.junit.Test;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;


public class ParameterNameExtractorTest {
    @Test
    public void shouldGetParametersOfSimpleSerializableLambda() {
        SerializableFunction lambda = name -> "a";
        assertEquals(singletonList("name"), ParameterNameExtractor.extractParameterNames(lambda, 1));
    }

    @Test
    public void shouldGetParametersOfBiFunction() {
        String value = "a";
        SerializableBiFunction lambda = (name1, name2) -> value;
        assertEquals(asList("name1", "name2"), ParameterNameExtractor.extractParameterNames(lambda, 2));
        assertEquals("name1", ParameterNameExtractor.extractFirstParameterName(lambda, 2));
    }

    @Test
    public void shouldGetParametersOfSimpleSerializableLambdaWithCapturedAttribute() {
        String value1 = "a";
        String value2 = "b";
        SerializableFunction lambda = name -> value1 + value2;
        assertEquals("name", ParameterNameExtractor.extractFirstParameterName(lambda, 1));
    }

    @Test
    public void shouldGetParametersOfFunctionCall() {
        SerializableListFunction lambda = name -> singletonList("value1");
        assertEquals("name", ParameterNameExtractor.extractFirstParameterName(lambda, 1));
    }

    private interface SerializableFunction extends Function<String, String>, Serializable {
    }

    private interface SerializableBiFunction extends BiFunction<String, Integer, String>, Serializable {
    }

    private interface SerializableListFunction extends Function<String, List<String>>, Serializable {
    }


}