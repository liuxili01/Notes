# 030-Spring事件监听器-基于接口

[TOC]

## 一言蔽之

Spring事件监听器ApplicationListener 拓展自EventListener , 是一个单一类型事件处理

## Spring事件接口

- 拓展接口 : org.springframework.context.ApplicationListener
- 设计特点 : 单一类型事件处理
- 处理方法 :  void onApplicationEvent(E event);
- 事件类型 : org.springframework.context.ApplicationEvent

## 什么是单一类型事件处理

一次性只处理一类事件

## 源码

```java
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
	void onApplicationEvent(E event);
}
```

## 代码实例

## 注册事件

```java
public class ApplicationListenerDemo {

    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("接收到Spring事件" + event);
            }
        });
        applicationContext.refresh();
        applicationContext.start();
        applicationContext.stop();
        applicationContext.close();
    }
}
```

输出

```java
接收到Spring事件org.springframework.context.event.ContextRefreshedEvent
接收到Spring事件org.springframework.context.event.ContextStartedEvent
接收到Spring事件org.springframework.context.event.ContextStoppedEvent
接收到Spring事件org.springframework.context.event.ContextClosedEvent
```

