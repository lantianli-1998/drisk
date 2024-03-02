package com.roy.drisk.commonservice.util;

import org.apache.tools.ant.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class MethodNameUtil {
    private static final List<String> OMITTED_PARAM_PREFIXES = new ArrayList<>();

    static {
        OMITTED_PARAM_PREFIXES.add("java.lang.");
        OMITTED_PARAM_PREFIXES.add("java.time.");
        OMITTED_PARAM_PREFIXES.add("java.util.");
        OMITTED_PARAM_PREFIXES.add("com.roy.drisk.");
    }

    private static void appendParams(StringBuilder sb, Method method) {
        Type[] params = method.getGenericParameterTypes();
        for (int j = 0; j < params.length; j++) {
            String param = params[j].getTypeName();
            if (method.isVarArgs() && (j == params.length - 1))
                param = param.replaceFirst("\\[\\]$", "...");
            for (String prefix : OMITTED_PARAM_PREFIXES) {
                param = StringUtils.replace(param, prefix, "");
            }
            sb.append(param);
            if (j < (params.length - 1))
                sb.append(',');
        }
    }

    public static String methodName(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName()).append('(');
        appendParams(sb, method);
        sb.append(')');
        return sb.toString();
    }

    public static String methodNameWithClass(Method method) {
        StringBuilder sb = new StringBuilder();
        String className = method.getDeclaringClass().getTypeName();
        sb.append(className.substring(className.lastIndexOf('.') + 1))
                .append('.').append(method.getName()).append('(');
        appendParams(sb, method);
        sb.append(')');
        return sb.toString();
    }
}
