package net.javacrumbs.lambdaextractor;

import java.lang.reflect.Method;

interface ParamNameReader {
    String[] getParamNames(Method method);
}
