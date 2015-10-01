package net.javacrumbs.lambdaextractor;

import org.junit.Test;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;


public class ParameterNameExtractorTest {
    @Test
    public void shouldGetParametersOfSimpleSerializableLambda() {
        SerializableFunction lambda = name -> "a";
        assertEquals(singletonList("name"), ParameterNameExtractor.extractParameterNames(lambda));
    }

    @Test
    public void shouldGetParametersOfBiFunction() {
        SerializableBiFunction lambda = (name1, name2) -> "a";
        assertEquals(asList("name1", "name2"), ParameterNameExtractor.extractParameterNames(lambda));
    }

    private interface SerializableFunction extends Function<String, String>, Serializable {}

    private interface SerializableBiFunction extends BiFunction<String, Integer, String>, Serializable {}

}