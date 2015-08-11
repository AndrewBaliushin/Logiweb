package com.tsystems.javaschool.logiweb.service.aspects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aspect logs calls to methods of classes from package:
 * com.tsystems.javaschool.logiweb.service.impl.*
 * 
 * Class name, method name and method arguments are recorded.
 * 
 * Log level: INFO
 * 
 * @author Andrey Baliushin
 *
 */
@Component
@Aspect
public class LoggerAspect {

    private static final Logger LOG = Logger.getLogger(LoggerAspect.class);

    @Before("execution(* com.tsystems.javaschool.logiweb.service.impl.*.* (..))")
    public void beforeAdvice(JoinPoint jp) throws Throwable {
        Class<?> clazz = jp.getTarget().getClass();
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();

        LOG.info(clazz.getSimpleName() + "." + methodName + "("
                + convertArgsToString(args) + ") is called");

    }

    private String convertArgsToString(Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i].getClass().getSimpleName());
            sb.append(":");
            sb.append(args[i].toString());

            if (i != args.length - 1) { // not last one
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
