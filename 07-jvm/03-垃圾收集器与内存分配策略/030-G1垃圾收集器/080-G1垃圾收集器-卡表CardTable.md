# 080-G1垃圾收集器-卡表CardTable

[TOC]

## 卡表（Card Table)

有个场景，老年代的对象可能引用新生代的对象，由于新生代的垃圾收集通常很频繁，那标记存活对象的时候，需要扫描从老年代到新生代的所有引用对象。

因为该对象拥有对新生代对象的引用，那么这个引用也会被称为GC Roots。那不是每次YGC时又得做全堆扫描？

**显然不是，对于HotSpot JVM，使用了卡标记（Card Marking）技术来解决老年代到新生代的引用问题。**

具体是，使用卡表（Card Table）和写屏障（Write Barrier）来进行标记并加快对GC Roots的扫描。卡表的设计师将堆内存平均分成2的N次方大小（默认512字节）个卡，并且维护一个卡表，用来储存每个卡的标识位。当对一个对象引用进行写操作时（对象引用改变），写屏障逻辑将会标记对象所在的卡页为脏页。在YGC只需要扫描卡表中的脏卡，将脏中的对象加入到YGC的GC Roots里面。当完成所有脏卡扫描时候，虚拟机会将卡表的脏卡标志位清空。

## 伪共享

在高并发环境下，每次对引用的更新，无论是否更新了老年代对新生代对象的引用，都会进行一次写屏障操作,频繁的写屏障很容易发生虚共享(false sharing), 从而带来性能开销。

举个例子：假设CPU缓存行大小为64字节，由于一个卡表项占1个字节，这意味着，64个卡表项将共享同一个缓存行。

HotSpot每个卡页为512字节，那么一个缓存行将对应64个卡页一共 64*512=32KB。如果不同线程对对象引用的更新操作，恰好位于同一个32KB区域内，这将导致同时更新卡表的同一个缓存行，从而造成缓存行的写回、无效化或者同步操作，间接影响程序性能。

在JDK 7中引入了VM参数-XX:+UseCondCardMark ，意思就是现在不采用无条件写屏障，而是先检查此卡是否已经是脏页，如果是将不再标记。这样就减少了并发下的虚共享问题。但是这样却不能避免对未标记的页进行并发标记。