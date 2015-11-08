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

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * Extracts parameter name from lambda. Inspired by
 * https://github.com/benjiman/lambda-type-references/blob/master/src/main/java/com/benjiweber/typeref/MethodFinder.java
 */
public class ParameterNameExtractor {
    private ParameterNameExtractor() {
        // empty
    }

    /**
     * Extracts names of a serializable lambda parameters
     * @param lambda Serializable lambda
     * @return
     */
    public static List<String> extractParameterNames(Serializable lambda) {
        Method method = lambdaMethod(lambda);
        return Collections.unmodifiableList(Arrays.stream(method.getParameters()).map(ParameterNameExtractor::getParamName).collect(toList()));
    }

    /**
       * Extracts names of a serializable lambda parameters
       * @param lambda Serializable lambda
       * @return
       */
      public static String extractFirstParameterName(Serializable lambda) {
          Method method = lambdaMethod(lambda);
          return getParamName(method.getParameters()[0]);
      }

    private static String getParamName(Parameter parameter) {
        if (!parameter.isNamePresent()) {
            throw new IllegalStateException("You need to compile with javac -parameters for parameter reflection to work; You also need java 8u60 or newer to use it with lambdas");
        }
        return parameter.getName();
    }

    private static SerializedLambda serialized(Serializable keyValue) {
        try {
            Method replaceMethod = keyValue.getClass().getDeclaredMethod("writeReplace");
            replaceMethod.setAccessible(true);
            return (SerializedLambda) replaceMethod.invoke(keyValue);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Class<?> getContainingClass(SerializedLambda serialized) {
        try {
            String className = serialized.getImplClass().replaceAll("/", ".");
            return Class.forName(className);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Method lambdaMethod(Serializable keyValue) {
        SerializedLambda lambda = serialized(keyValue);
        Class<?> containingClass = getContainingClass(lambda);
        return asList(containingClass.getDeclaredMethods())
            .stream()
            .filter(method -> Objects.equals(method.getName(), lambda.getImplMethodName()))
            .findFirst()
            .orElseThrow(UnableToGuessMethodException::new);
    }

    static class UnableToGuessMethodException extends IllegalStateException {
    }
}
