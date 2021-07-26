package cn.eccto.study.springframework.tutorials.spring5;

import org.springframework.context.support.GenericApplicationContext;

/**
 * 使用 {@link GenericApplicationContext} 的新方法 {@link GenericApplicationContext#registerBean} 注册 bean
 *
 * @author JonathanChen 2019/11/27 18:06
 */
public class RegisterBeanExample1 {
    //using registerBean(beanClass, customizers)
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean(OrderService.OrderServiceImpl.class);//not using customizer
        gac.refresh();
        System.out.println("context refreshed");
        OrderService os = gac.getBean(OrderService.class);
        os.placeOrder("Laptop", 2);
        System.out.println("-----------");
        //retrieving the bean one more time
        os = gac.getBean(OrderService.class);
        os.placeOrder("Desktop", 2);
        gac.close();
    }
}
