package us.codecraft.springlearning;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author yihua.huang@dianping.com
 */
@Aspect
public class TimerAspect {

    @Around("execution(* us.codecraft.springlearning.*Service.*(..))")
    public void timer(ProceedingJoinPoint joinPoint) throws Throwable {
        long time = System.nanoTime();
        joinPoint.proceed();
        System.out.println(System.nanoTime()-time);
    }
}
