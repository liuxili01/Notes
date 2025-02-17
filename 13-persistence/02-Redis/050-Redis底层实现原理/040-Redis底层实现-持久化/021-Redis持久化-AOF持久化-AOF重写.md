# 021-Redis持久化-AOF持久化-AOF重写

[TOC]

### 什么是AOF重写

aof 日志在长期运行过程中会变得无比强大,数据库重启的时候需要加载 aof 日志进行指令的重返,这个时间无比漫长,所以要对 AOF 定期重写, 给 AOF 瘦身

> bgrewriteaof 指令 用于对 AOF日志进行瘦身

AOF 文件重写并不是对原文件进行重新整理，而是直接读取服务器现有的键值对， 然后用一条命令去代替之前记录这个键值对的多条命令，生成一个新的文件后去替换原 来的 AOF 文件。

**原理就是开辟一个子进程对内存进行遍历,序列化到一个新的 aof 日志文件中,序列化完成之后再讲操作期间发生的增量 aof日志追加到这个新的 aof 日志文件中,追加完毕之后就立即替代就的 aof 文件了,瘦身工作就完成了**

```
# 重写触发机制 
auto-aof-rewrite-percentage 100 
auto-aof-rewrite-min-size 64mb
```

| 参数                         | 说明                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| auto-aof-rewrite-percentag e | 默认值为 100。aof 自动重写配置，当目前 aof 文件大小超过上一次重写的 aof 文件大小的 百分之多少进行重写，即当 aof 文件增长到一定大小的时候，Redis 能够调用 bgrewriteaof 对日志文件进行重写。当前 AOF 文件大小是上次日志重写得到 AOF 文件大小的二倍(设 置为 100)时，自动启动新的日志重写过程。 |
| auto-aof-rewrite-min-size    | 默认 64M。设置允许重写的最小 aof 文件大小，避免了达到约定百分比但尺寸仍然很小的 情况还要重写。 |

#### 重写过程中,AOF 文件被更改了怎么办

> 使用子线程进行 AOF 重写

![image-20200427220314513](../../../../assets/image-20200427220314513.png)

子进程在进行 AOF 重写的时候,主进程需要执行以下三个任务

1. 处理命令请求
2. 将写命令追加到现有的 AOF 文件中
3. 将写命令追加到 AOF 重写缓存中

| 参数                      | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| no-appendfsync-on-rewrite | 在 aof 重写或者写入 rdb 文件的时候，会执行大量 IO，此时对于 everysec 和 always 的 aof 模式来说，执行 fsync 会造成阻塞过长时间，no-appendfsync-on-rewrite 字段设置为默认设 置为 no。如果对延迟要求很高的应用，这个字段可以设置为 yes，否则还是设置为 no，这 样对持久化特性来说这是更安全的选择。设置为 yes 表示 rewrite 期间对新写操作不 fsync, 暂时存在内存中,等 rewrite 完成后再写入，默认为 no，建议修改为 yes。Linux 的默认 fsync 策略是 30 秒。可能丢失 30 秒数据。 |
| aof-load-truncated        | aof 文件可能在尾部是不完整的，当 redis 启动的时候，aof 文件的数据被载入内存。重启 可能发生在 redis 所在的主机操作系统宕机后，尤其在 ext4 文件系统没有加上 data=ordered 选项，出现这种现象。redis 宕机或者异常终止不会造成尾部不完整现象，可以选择让 redis 退出，或者导入尽可能多的数据。如果选择的是 yes，当截断的 aof 文件被导入的时候， 会自动发布一个 log 给客户端然后 load。如果是 no，用户必须手动 redis-check-aof 修复 AOF 文件才可以。默认值为 yes。 |

