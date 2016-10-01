package net.javacrumbs.lambdaextractor;

import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class SpringParamNameReader implements ParamNameReader {
    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Override
    public String[] getParamNames(Method method) {
        return parameterNameDiscoverer.getParameterNames(method);
    }
}
