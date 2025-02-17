# 025-线程池的使用

[TOC]

## 线程池的创建

## ThreadPoolExecutor

ThreadpoolExecutor 有多个重载的构造方法，我们可以基于它最完整的构造方法来分析 先来解释一下每个参数的作用

```java
public ThreadPoolExecutor(int corePoolSize, //核心线程数量
    int maximumPoolSize, //最大线程数
    long keepAliveTime, //超时时间,超出核心线程数量以外的线程空余存活时间
    TimeUnit unit, //存活时间单位
    BlockingQueue<Runnable> workQueue, //保存执行任务的队列 
    ThreadFactory threadFactory,//创建新线程使用的工厂 
    RejectedExecutionHandler handler //当任务无法执行的时候的处理方式s
)
```

线程池初始化时是没有创建线程的，线程池里的线程的初始化与其他线程一样，但是在完成任务以后，该线程不会自行销毁，而是以挂起的状态返回到线程池。直到应用程序再次向线程池发出请求时，线程池里挂起的线程就会再度激活执行任务。这样既节省了建立线程所造成的性能损耗，也可以让多个任务反复重用同一线程，从而在应用程序生存期内节约大量开销

### 使用 Executors 工具类

只需要直接使用 Executors 的工厂方法，就可以使用线程池:

- [newFixedThreadPool](024-5种常用线程池.md#newFixedThreadPool) 

  > 创建**固定数量的线程池**，线程数不变，当有一个任务提交 时，若线程池中空闲，则立即执行，若没有，则会被暂缓在一个任务队列中，等待有空闲的 线程去执行。

- [newSingleThreadExecutor](024-5种常用线程池.md#newSingleThreadExecutor) 

  > 创建一个线程的线程池，若空闲则执行，若没有空闲线程则暂缓 在任务队列中。 

- [newCachedThreadPool](024-5种常用线程池.md#newCachedThreadPool) 

  > 加入同步队列的任务会被马上执行,同步队列里面最多只有一个任务

- [newScheduledThreadPool](024-5种常用线程池.md#newScheduledThreadPool) 

  > 创建一个可以指定线程的数量的线程池，但是这个线程池还带有 延迟和周期性执行任务的功能，类似定时器。

-  [newWorkStealingPool](024-5种常用线程池.md#newWorkStealingPool) 

  >  创建持有足够线程的线程池来达到快速运行的目的,在内部通过使用多个队列来减少各个线程调度产生的竞争

## 简单使用

```java
/**
 * <p>
 * 简单的线程池使用
 * </p>
 *
 * @author EricChen 2020/04/05 22:24
 */
public class ThreadPoolTest implements Runnable {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 100; i++) {
            executorService.execute(new ThreadPoolTest());
        }
        executorService.shutdown();
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ThreadName:" + Thread.currentThread().getName());
    }
}
```

