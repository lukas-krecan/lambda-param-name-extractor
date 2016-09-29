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
     * @param lambdaParametersCount number of lambda parameters
     */
    public static List<String> extractParameterNames(Serializable lambda, int lambdaParametersCount) {
        SerializedLambda serializedLambda = serialized(lambda);

        Method lambdaMethod = lambdaMethod(serializedLambda);
        return Collections.unmodifiableList(Arrays
            .stream(lambdaMethod.getParameters())
            .skip(lambdaMethod.getParameterCount() - lambdaParametersCount)
            .map(ParameterNameExtractor::getParamName).collect(toList())
        );
    }

    /**
     * Extracts name of the first lambda parameter
     * @param lambda Serializable lambda
     * @param lambdaParametersCount number of lambda parameters
     */
    public static String extractFirstParameterName(Serializable lambda, int lambdaParametersCount) {
        return extractParameterNames(lambda, lambdaParametersCount).get(0);
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

    /**
     * <ol>
     *     <li>Serializes lambda to SerializedLambda</li>
     *     <li>Finds containing class - class where the lambda method is implemented</li>
     *     <li>Finds the method in the containing class</li>
     * </ol>
     */
    private static Method lambdaMethod(SerializedLambda serializedLambda) {
        Class<?> containingClass = getClassForName(serializedLambda.getImplClass());
        String implMethodName = serializedLambda.getImplMethodName();
        return getMethod(containingClass, implMethodName);
    }

    private static Class<?> getClassForName(String className) {
        try {
            String normalizedClassName = className.replaceAll("/", ".");
            return Class.forName(normalizedClassName);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Method getMethod(Class<?> clazz, String methodName) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> isSameMethod(methodName, method))
            .findFirst()
            .orElseThrow(UnableToGuessMethodException::new);
    }

    private static boolean isSameMethod(String methodName, Method method) {
        return Objects.equals(method.getName(), methodName);
    }

    static class UnableToGuessMethodException extends IllegalStateException {
    }
}
