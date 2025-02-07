# Redis支持哪些数据类型

1. 字符串：redis没有直接使用C语言传统的字符串表示，而是自己实现的叫做简单动态字符串SDS的抽象类型。C语言的字符串不记录自身的长度信息，而SDS则保存了长度信息，这样将获取字符串长度的时间由O(N)降低到了O(1)，同时可以避免缓冲区溢出和减少修改字符串长度时所需的内存重分配次数。
2. 链表linkedlist：redis链表是一个双向无环链表结构，很多发布订阅、慢查询、监视器功能都是使用到了链表来实现，每个链表的节点由一个listNode结构来表示，每个节点都有指向前置节点和后置节点的指针，同时表头节点的前置和后置节点都指向NULL。
3. 字典hashtable：用于保存键值对的抽象数据结构。redis使用hash表作为底层实现，每个字典带有两个hash表，供平时使用和rehash时使用，hash表使用链地址法来解决键冲突，被分配到同一个索引位置的多个键值对会形成一个单向链表，在对hash表进行扩容或者缩容的时候，为了服务的可用性，rehash的过程不是一次性完成的，而是渐进式的。
4. 跳跃表skiplist：跳跃表是有序集合的底层实现之一，redis中在实现有序集合键和集群节点的内部结构中都是用到了跳跃表。redis跳跃表由zskiplist和zskiplistNode组成，zskiplist用于保存跳跃表信息（表头、表尾节点、长度等），zskiplistNode用于表示表跳跃节点，每个跳跃表的层高都是1-32的随机数，在同一个跳跃表中，多个节点可以包含相同的分值，但是每个节点的成员对象必须是唯一的，节点按照分值大小排序，如果分值相同，则按照成员对象的大小排序。
5. 整数集合intset：用于保存整数值的集合抽象数据结构，不会出现重复元素，底层实现为数组。
6. 压缩列表ziplist：压缩列表是为节约内存而开发的顺序性数据结构，他可以包含多个节点，每个节点可以保存一个字节数组或者整数值。

基于这些基础的数据结构，redis封装了自己的对象系统，包含字符串对象string、列表对象list、哈希对象hash、集合对象set、有序集合对象zset，每种对象都用到了至少一种基础的数据结构。

redis通过encoding属性设置对象的编码形式来提升灵活性和效率，基于不同的场景redis会自动做出优化。不同对象的编码如下：

1. 字符串对象string：int整数、embstr编码的简单动态字符串、raw简单动态字符串
2. 列表对象list：ziplist、linkedlist
3. 哈希对象hash：ziplist、hashtable
4. 集合对象set：intset、hashtable
5. 有序集合对象zset：ziplist、skiplist