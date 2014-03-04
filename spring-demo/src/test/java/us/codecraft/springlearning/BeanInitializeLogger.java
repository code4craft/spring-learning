package us.codecraft.springlearning;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author yihua.huang@dianping.com
 */
public class BeanInitializeLogger implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("Initialize bean " + beanName + " start!");
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("Initialize bean " + beanName + " end!");
		return bean;
	}
}
