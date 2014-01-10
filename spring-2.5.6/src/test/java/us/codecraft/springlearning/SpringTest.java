package us.codecraft.springlearning;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author yihua.huang@dianping.com
 */
public class SpringTest {

    @Test
    public void test(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:/config/spring/appcontext*.xml");
        HelloWorldService helloWorldService = (HelloWorldService)applicationContext.getBean("helloWorldService", HelloWorldService.class);
        helloWorldService.helloWorld();
    }
}
