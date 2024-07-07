# Nukos 的特性

## 对 Carpet 客户端 tick freeze 状态检查的补丁

对应 Carpet Issues [#1924](https://github.com/gnembon/fabric-carpet/issues/1924)

疑似由于 Carpet 的客户端/服务端同步的问题，在客户端刚刚进入服务器的时候服务端更改 tick freeze 状态就会使客户端的 freeze 状态与服务端失去同步。

这在使用规则 [tickFreezeWhenNoPlayers](./rules.zh.md#tickfreezewhennoplayers) 时会带来严重问题。

Nukos Mod 通过每秒钟都向客户端同步一次 tick freeze 状态来解决这个问题——尽管这可能会带来更多的网络消耗，不过我们目前没有更好的办法，只能先使用这个方案让 tickFreezeWhenNoPlayers 可以正常工作。

这个修改位于服务端且只要安装模组就会生效。
