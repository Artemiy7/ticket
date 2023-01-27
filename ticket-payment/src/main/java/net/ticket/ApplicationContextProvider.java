package net.ticket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContextStatic;

    public static ApplicationContext getApplicationContext() {
        return applicationContextStatic;
    }
    @Override
    public void setApplicationContext(@Autowired ApplicationContext applicationContext) throws BeansException {
        applicationContextStatic = applicationContext;
    }
}
