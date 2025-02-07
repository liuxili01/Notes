# 030-Final的基础语义

[TOC]

## 基础语义

- 被final修饰的类不可以被继承
- 被final修饰的方法不可以被重写(override)
- 被final修饰的变量不可以被改变(如果修饰引用,那么表示引用不可变,引用指向的内容可变)
- 被final修饰的方法,JVM会尝试将其内联,以提高运行效率
- 被final修饰的常量,在编译阶段会存入常量池中

除此之外,编译器对final域要遵守的两个重排序规则:

- 在构造函数内对一个final域的写入,与随后把这个被构造对象的引用赋值给一个引用变量,这两个操作之 间不能重排序 
- 初次读一个对象(包含final域的)的引用,与随后初次读这个final域,这两个操作之间不能重排序

在处理器中的实现

- **写 final域的重排序规则会要求编译器在 final 域的写之后,构造函数 return 之前插入一个 StoreStore 屏障**
- **读 final 域的重排序规则要求编译器在读 final 域的操作前面插入一个 LoadLoad 屏障**

## final不想被改变的理由

final关键字能够告诉编译器一块数据是恒定不变的，thinking in java 中提到的不想被改变的两种理由：设计和效率。

### final修饰引用

> 引用变量无法保证多线程安全修改,因此需要使用同步原语volitile 或者 Lock 或者用并发容器保证线程安全

某个被final修饰的引用一旦被初始化指向一个对象后，就不可以将它改为指向另一个对象，**需要注意的是该对象本身所属的类行为是不会受到限制的。**

- List 初始化后如果想要修改其引用则无法通过编译，但是strList对象对于的List类的行为是不会受到改变的，如add方法

- List.of (JDK 9)方法创建的list是“不可变”的，如果试图去修改不可变list中的内容则会抛出异常；

### 内部类的变量要修饰成final

匿名内部类访问外部类中的局部变量时，为什么要将该变量声明为final类型的？（JDK8 之后不需要手动添加final关键字了）

**原因：匿名内部类对象的生命周期比外部类中的局部变量长；**

- 局部变量的生命周期：当有方法调用并使用到该变量时，变量入栈，方法执行结束后，出栈，变量就销亡了；

- 对象的生命周期：当没有引用指向这个对象，GC会在某个时候将其回收，也就是销毁了。

问题：成员方法执行完了，局部变量销毁了，但是对象还仍然存活（没有被GC），这时候对象要去引用该局部变量就引用不到了。

解决方法：java中的内部类访问外部变量时，必须将该变量声明为final，并且inner class会copy一份该变量，而不是直接去使用该局部变量，这样就可以防止数据不一致的问题了。

java的改进：JDK8 后，如果有内部类访问局部变量，java会自动将该变量修饰成final类型的，所以我们不需要再去手动添加该关键字。

## Final的内存语义

 [031-final的内存语义.md](031-final的内存语义.md)