package com.xsm.client.config;

import com.xsm.client.proxy.ProxyFactory;
import com.xsm.common.RpcInterface;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Set;

/**
 * @author xsm
 * @Date 2020/5/20 23:20
 */
@Slf4j
public class RpcSpringConfig implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections("com.xsm");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(RpcInterface.class);
        for (Class<?> clazz : typesAnnotatedWith) {
            beanFactory.registerSingleton(clazz.getSimpleName(), ProxyFactory.create(clazz));
        }
        log.info("afterPropertiesSet is {}", typesAnnotatedWith);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
