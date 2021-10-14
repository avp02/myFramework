package com.vizorgames.interview;

import com.vizorgames.interview.annotation.Inject;
import com.vizorgames.interview.context.ApplicationContext;
import com.vizorgames.interview.factory.BeanFactory;
import com.vizorgames.interview.ioc.Injector;

public class App {

    public ApplicationContext run() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);
        return applicationContext;
    }

    public static void main(String[] args) {
        App app =new App();
        ApplicationContext context = app.run();
        Injector injector = context.getBeanContext(Injector.class);
        injector.getProvider(Injector.class);
    }

}
