package us.codecraft.springlearning;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

/**
 * @author yihua.huang@dianping.com
 */
public class AspectJExpressionPointcutTest {

	@Test
	public void test() throws Exception {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression("execution(* us.codecraft.springlearning.*.*(..))");
        boolean matches = aspectJExpressionPointcut.getClassFilter().matches(HelloWorldService.class);
        Assert.assertTrue(matches);
        boolean helloWorld = aspectJExpressionPointcut.getMethodMatcher().matches(HelloWorldService.class.getDeclaredMethod("helloWorld"), HelloWorldService.class);
        Assert.assertTrue(helloWorld);
    }
}
