# 6.5-MySQL查询优化器的局限性

[TOC]

## 前言

MySQL的万能“嵌套循环”并不是对每种查询都是最优的。

不过还好，MySQL查询优化器只对少部分查询不适用，而且我们往往可以通过改写查询让MySQL高效地完成工作。还有一个好消息，MySQL 5.6版本正式发布后，会消除很多MySQL原本的限制，让更多的查询能够以尽可能高的效率完成。

