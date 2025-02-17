# 010-Redis-事务-如何使用事务

### MULTI

**MULTI**

标记一个事务块的开始。

事务块内的多条命令会按照先后顺序被放进一个队列当中，最后由 [EXEC](http://redisdoc.com/transaction/exec.html#exec) 命令原子性(atomic)地执行。

- **可用版本：**

  >= 1.2.0

- **时间复杂度：**

  O(1)。

- **返回值：**

  总是返回 `OK` 。

```
redis> MULTI            # 标记事务开始
OK

redis> INCR user_id     # 多条命令按顺序入队
QUEUED

redis> INCR user_id
QUEUED

redis> INCR user_id
QUEUED

redis> PING
QUEUED

redis> EXEC             # 执行
1) (integer) 1
2) (integer) 2
3) (integer) 3
4) PONG
```

## EXEC

**EXEC**

执行所有事务块内的命令。

假如某个(或某些) key 正处于 [WATCH](http://redisdoc.com/transaction/watch.html#watch) 命令的监视之下，且事务块中有和这个(或这些) key 相关的命令，那么 [EXEC](http://redisdoc.com/transaction/exec.html#exec) 命令只在这个(或这些) key 没有被其他命令所改动的情况下执行并生效，否则该事务被打断(abort)。

- **可用版本：**

  >= 1.2.0

- **时间复杂度：**

  事务块内所有命令的时间复杂度的总和。

- **返回值：**

  事务块内所有命令的返回值，按命令执行的先后顺序排列。当操作被打断时，返回空值 `nil` 。

```
# 事务被成功执行

redis> MULTI
OK

redis> INCR user_id
QUEUED

redis> INCR user_id
QUEUED

redis> INCR user_id
QUEUED

redis> PING
QUEUED

redis> EXEC
1) (integer) 1
2) (integer) 2
3) (integer) 3
4) PONG


# 监视 key ，且事务成功执行

redis> WATCH lock lock_times
OK

redis> MULTI
OK

redis> SET lock "huangz"
QUEUED

redis> INCR lock_times
QUEUED

redis> EXEC
1) OK
2) (integer) 1


# 监视 key ，且事务被打断

redis> WATCH lock lock_times
OK

redis> MULTI
OK

redis> SET lock "joe"        # 就在这时，另一个客户端修改了 lock_times 的值
QUEUED

redis> INCR lock_times
QUEUED

redis> EXEC                  # 因为 lock_times 被修改， joe 的事务执行失败
(nil)
```

## DISCARD

> 可用版本： >= 2.0.0
>
> 时间复杂度： O(1)。

取消事务，放弃执行事务块内的所有命令。

如果正在使用 `WATCH` 命令监视某个(或某些) key，那么取消所有监视，等同于执行命令 `UNWATCH` 。

#### 返回值

总是返回 `OK` 。

#### 代码示例

```
redis> MULTI
OK

redis> PING
QUEUED

redis> SET greeting "hello"
QUEUED

redis> DISCARD
OK
```

## WATCH

**WATCH key [key …]**

监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断。

- **可用版本：**

  >= 2.2.0

- **时间复杂度：**

  O(1)。

- **返回值：**

  总是返回 `OK` 。

```
redis> WATCH lock lock_times
OK
```

## UNWATCH

**UNWATCH**

取消 [WATCH](http://redisdoc.com/transaction/watch.html#watch) 命令对所有 key 的监视。

如果在执行 [WATCH](http://redisdoc.com/transaction/watch.html#watch) 命令之后， [EXEC](http://redisdoc.com/transaction/exec.html#exec) 命令或 [DISCARD](http://redisdoc.com/transaction/discard.html#discard) 命令先被执行了的话，那么就不需要再执行 [UNWATCH](http://redisdoc.com/transaction/unwatch.html#unwatch) 了。

因为 [EXEC](http://redisdoc.com/transaction/exec.html#exec) 命令会执行事务，因此 [WATCH](http://redisdoc.com/transaction/watch.html#watch) 命令的效果已经产生了；而 [DISCARD](http://redisdoc.com/transaction/discard.html#discard) 命令在取消事务的同时也会取消所有对 key 的监视，因此这两个命令执行之后，就没有必要执行 [UNWATCH](http://redisdoc.com/transaction/unwatch.html#unwatch) 了。

- **可用版本：**

  >= 2.2.0

- **时间复杂度：**

  O(1)

- **返回值：**

  总是 `OK` 。

```
redis> WATCH lock lock_times
OK

redis> UNWATCH
OK
```