# 080-Spring应用上下文刷新阶段-onRefresh()

[TOC]

## Spring应用上下文刷新阶段做了什么?

- AbstractApplicationContext#onRefresh()方法
  - 子类覆盖该方法
    - org.springframework.web.context.support.GenericWebApplicationContext#onRefresh
    - org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext#onRefresh()
    - org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#onRefresh
    - org.springframework.web.context.support.StaticWebApplicationContext#onRefresh

## 图示

![image-20201007151953236](../../assets/image-20201007151953236.png)

#### org.springframework.web.context.support.GenericWebApplicationContext#onRefresh

```java
@Override
protected void onRefresh() {
  this.themeSource = UiApplicationContextUtils.initThemeSource(this);
}
```

#### org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#onRefresh

```java
@Override
protected void onRefresh() {
  super.onRefresh();
  try {
    createWebServer();
  }
  catch (Throwable ex) {
    throw new ApplicationContextException("Unable to start web server", ex);
  }
}
```

