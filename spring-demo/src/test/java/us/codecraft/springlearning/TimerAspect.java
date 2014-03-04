package us.codecraft.springlearning;

import org.aopalliance.aop.Advice;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author yihua.huang@dianping.com
 */
public class TimerAspect implements Advice {

    private HelloWorldService helloWorldService;

    public void timer(ProceedingJoinPoint joinPoint) throws Throwable {
        long time = System.nanoTime();
        joinPoint.proceed();
        System.out.println(System.nanoTime()-time);
    }

    public void setHelloWorldService(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }
}
