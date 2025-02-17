package vip.ericchen.study.designpatterns.creational.singletonPattern.inner;

import vip.ericchen.study.designpatterns.commons.Singleton;

/**
 * 注册内部类方式的单例模式,线程安全
 *
 * @author EricChen 2019/12/31 23:48
 */
public class InnerClassSingleton {
    private InnerClassSingleton() {
        //防止反射攻击
        throw new IllegalArgumentException("HungrySingleton not allow be constructed");
    }

    public static Singleton getInstance() {
        return LazyHolder.LAZY;
    }

    private static class LazyHolder {
        private static final Singleton LAZY = new Singleton();
    }
    /**
     * 重写 readResolve 方法`java.io.ObjectStreamClass` 类在反序列初始化对象时会去判断这个方法
     * 还是会创建两次,但是发生在 JVM 层面,相对安全,之前反序列化的对象也会被 GC 回收
     *
     * @see java.io.ObjectStreamClass#invokeReadResolve
     */
    private Object readResolve() {
        return LazyHolder.LAZY;
    }
}
