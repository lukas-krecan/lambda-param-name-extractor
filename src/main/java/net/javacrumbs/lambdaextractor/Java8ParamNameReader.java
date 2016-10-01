package net.javacrumbs.lambdaextractor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class Java8ParamNameReader implements ParamNameReader {

    @Override
    public String[] getParamNames(Method method) {
        String[] result = new String[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < method.getParameterCount(); i++) {
            Parameter parameter = parameters[i];
            if (!parameter.isNamePresent()) {
                throw new IllegalStateException("You need to compile with javac -parameters or add spring-core with asm to the classpath");
            }
            result[i] = parameter.getName();
        }
        return result;
    }
}
