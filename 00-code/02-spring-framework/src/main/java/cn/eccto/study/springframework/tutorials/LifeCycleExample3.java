package cn.eccto.study.springframework.tutorials;

import cn.eccto.study.springframework.tutorials.lifecycle.MyBean3;
import cn.eccto.study.springframework.tutorials.lifecycle.OtherBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author EricChen 2019/11/14 12:27
 */
@Configuration
class LifeCycleExample3 {

    @Bean
    public MyBean3 myBean3() {
        return new MyBean3();
    }

    @Bean
    public OtherBean otherBean() {
        return new OtherBean();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(LifeCycleExample3.class);

        context.registerShutdownHook();

        System.out.println("-- accessing bean --");
        MyBean3 bean = context.getBean(MyBean3.class);
        bean.doSomething();

        System.out.println("-- finished --");

    }
}