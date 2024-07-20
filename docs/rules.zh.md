# Nukos 的 Carpet 规则

## tickFreezeWhenNoPlayers

> 本地化名称 : tickFreezeWhenNoPlayers | 无玩家时停止Tick \
> 分类 : `nukos` | `优化` | `experimental` | `tick` \
> 自版本 : `0.1.0` \
> values : `true`, <u>`false`</u> \
> 需要匹配客户端 : 不需要

当检测到服务器无人在线后，自动通过 `/tick freeze` 暂停游戏；检测到有任何玩家登录后，自动让游戏重新正常运行。

可以在服务器运行时调整此选项，mod 将会在玩家登录/退出和这个选项被修改设置时自动检测当前状态并将游戏设置为冻结/取消冻结。

只会在 Dedicated Server （专用服务器，即通过 server.jar 等启动的没有自带客户端的独立服务器） 环境下生效。在单人游戏和开放局域网联机的单人游戏里不会生效。

同时，如果在配置文件中存在 `config/carpet_nukos_addition/on-tick-freeze.mcfunction` 或 `config/carpet_nukos_addition/on-tick-unfreeze.mcfunction` 两个文件，则在暂停/继续游戏的时候将会在暂停/继续游戏后**逐行**执行这两个文件，以便于进行一些自定义的对游戏的控制。（目前的实现仅将文件中的每一行作为一个命令，在名为 `Nukos-Automatics` 的虚拟控制台上执行，并未实现完整的 MC Function API）

## tickFreezeWhenNoPlayersUseDeepFreeze

> 本地化名称 : tickFreezeWhenNoPlayersUseDeepFreeze | 自动停止 tick 使用深度冻结 \
> 分类 : `nukos` | `优化` | `experimental` | `tick` \
> 自版本 : `0.6.0` \
> values : `true`, <u>`false`</u> \
> 需要匹配客户端 : 不需要

在 [tickFreezeWhenNoPlayers](#tickfreezewhennoplayers) 通过 `tick freeze` 冻结游戏时，使用 `freeze deep` 而非普通的 `freeze`。

对于普通的 freeze 和 deep freeze 的更多信息，详见 Carpet [#634](https://github.com/gnembon/fabric-carpet/pull/634)。

## ignoringPlayersOnAutoTickFreeze

> 本地化名称 : ignoringPlayersOnAutoTickFreeze | 自动停止 tick 应忽略的玩家 \
> 分类 : `nukos` | `优化` | `experimental` | `tick` \
> 自版本 : `0.6.0` \
> values : <u><code></code></u>, `MyBot`, `*bot,*Bot` \
> 需要匹配客户端 : 不需要

在 [tickFreezeWhenNoPlayers](#tickfreezewhennoplayers) 检测在线玩家列表时，忽略在此指定的玩家。在这里指定的玩家会被永远看作未在线，即如果服务器只有在此列表中的玩家，则服务器会被认为没有玩家在线而停止 tick。

每个用户名使用英文 `,` 分隔开来，也可以使用 `*` 作为通配符来匹配任意剩余内容。名称之间**不能**有空格。

## ignoringPlayersOnSleeping

> 本地化名称 : ignoringPlayersOnSleeping | 玩家睡觉计算应忽略的玩家 \
> 分类 : `nukos` | `生存` | `tick` \
> 自版本 : `0.6.0` \
> values : <u><code></code></u>, `MyBot`, `*bot,*Bot` \
> 需要匹配客户端 : 不需要

设置哪些玩家应该在服务器统计正在睡觉/未在睡觉的玩家时应被忽略。设置为被忽略的玩家将不会在统计睡觉状态时被统计，就像是它们处在观察者模式当中一样。

这个设置的目的是使得挂机用的玩家不会影响普通玩家的睡觉状态，也可以用于让处于创造模式的管理员不会影响普通生存玩家的睡觉状态。

如果将此条目设置为空的话，将不会对游戏机制做出修改，以保证与其它更改了睡觉机制的 mod 的兼容性。

和 [ignoringPlayersOnAutoTickFreeze](#ignoringplayersonautotickfreeze) 相同, 每个用户名使用英文 `,` 分隔开来，也可以使用 `*` 作为通配符来匹配任意剩余内容。名称之间**不能**有空格。

## anvilItemCostRollupAlgorithm

> 本地化名称 : anvilItemUseCostRollupAlgorithm | 物品的铁砧累加惩罚算法 \
> 分类 : `nukos` | `生存` | `铁砧` \
> 自版本 : `0.1.0` \
> values : <u>`vanilla`</u> | `linear:<int>` | `no-change` | `fixed:<int>` \
> 需要匹配客户端 : 不需要

控制一个物品在铁砧上被使用之后，其累加惩罚应该如何被计算。

这个算法应该为一个函数，输入为这个物品旧的*累积惩罚*的值，输出为这个物品新的*累积惩罚*的值，其新的*累积惩罚*的值决定了这个物品**下一次**在铁砧上被使用时，将会在基础消费上增加多少的*经验等级*消费。

有以下几个选项可以选择：

- `vanilla` - 原版的累积惩罚累加算法，其算法可以被简单的描述为 `f(x) = 2x + 1`，即每次都将累积惩罚值翻倍。  
  如果选择此选项，则 nukos 不会更改任何算法执行逻辑，如果其它的 mod 更改了原版逻辑，则它们会正常生效。
- `linear:<int>` - 一个由 Nukos 自定义的算法，可以线性的增加物品的*累积惩罚*。其中，`<int>` 可以设置为任何的非负整数值，此值将会被增加到物品的旧的累积惩罚后，得到新的累积惩罚的值。这个算法可以被描述为 `f(x) = x + a`，其中 `a` 即为在规则中所设置的 `<int>` 的值。  
  eg: `linear:1`
- `no-change` - 一个由 Nukos 自定义的算法，将会使物品的*累积惩罚*保持不变。这个算法可以被简单的描述为 `f(x) = x`。
- `fixed:<int>` - 一个由 Nukos 自定义的算法，将会使物品的*累积惩罚*设置为一个固定的值。其中，`<int>` 可以设置为任何的非负整数值，此值将会直接作为物品新的累积惩罚。其算法可以被描述为 `f(x) = a`，其中 `a` 为规则中设置的 `<int>` 的值。

注意由于累积惩罚是在物品在铁砧上使用**之后**才会生效的原因，设置此算法后，你可能会需要已有的物品在铁砧上使用一次之后，才能观察到铁砧的经验消耗的增加与预期一致。这是因为，物品**这一次**在铁砧上的额外消耗，其实是在它**上一次**使用铁砧时被设置的。

这个设置与 `anvilAlgorithm` 设置相独立，即使 `anvilAlgorithm` 为 `vanilla`，此设置仍然可以正常生效。

## anvilAlgorithm

> 本地化名称 : anvilItemUseCostRollupAlgorithm | 物品的铁砧累加惩罚算法 \
> 分类 : `nukos` | `生存` | `铁砧` \
> 自版本 : `0.1.0` \
> values : <u>`vanilla`</u> | `vanilla-reforged` \
> 需要匹配客户端 : 不需要

设置铁砧的*铁砧算法*。包含了铁砧的经验消耗算法，附魔累加算法，物品修复算法，物品重命名的名称序列化等等。mod 大部分的铁砧机制设置都需要此算法设置为非 `vanilla` 才能生效。唯一的例外是 [anvilItemCostRollupAlgorithm](#anvilitemcostrollupalgorithm) 算法与这个算法独立，可以单独产生效果。

有两个可选项：

- `vanilla` - MC 原版算法。如果设置为此值，则 mod 将会将几乎所有*铁砧算法*交由原版算法执行，使得此 mod 中绝大部分依赖这个设置的对铁砧算法进行更细分的调整的规则都将不会生效。
- `vanilla-reforged` - 由 Carpet Nukos 重写后的 MC 原版算法。在没有自定义下面列出的任何更加细分调整的规则的情况下应该和原版算法表现一致（你可以为不一致的行为[打开 issue](https://github.com/suk-ws/carpet-nukos-addition/issues/new)）。支援以下列出的所有对*铁砧算法*进行调整的规则。

### anvilUseRenameCost

> 本地化名称 : anvilUseRenameCost | 铁砧使用重命名物品消费 \
> 分类 : `nukos` | `生存` | `铁砧` \
> 自版本 : `0.2.0`  \
> values : <u>`true`</u> | `false` \
> 需要匹配客户端 : *部分*（仅在消费为 0 的情况下需要）

控制铁砧是否对于重命名收取费用。

默认为 `true`，即为原版行为：重命名花费 `1` 级经验。设置为 `false` 将会让重命名完全免费。

由于在原版客户端上，在消费为 0 时无法取出物品，这会导致没有安装本 mod 的原版客户端无法对物品进行*仅重命名*的操作：表现为重命名后的物品可以显示在输出槽位但是无法取出。

这个设置不影响*仅重命名*时最高 `39` 的消费限制；也与后面的 [anvilCustomNameSerializer](#anvilcustomnameserializer) 设置无关。

### anvilUseItemCost

> 本地化名称 : anvilUseItemCost | 铁砧使用物品的累加惩罚 \
> 分类 : `nukos` | `生存` | `铁砧` \
> 自版本 : `0.2.0`  \
> values : <u>`true`</u> | `false` \
> 需要匹配客户端 : 不需要

控制物品在铁砧上使用的*累积惩罚*是否应该被使用。

默认为 `true`，即为原版行为：材料物品的铁砧*累积惩罚*将会叠加到基础消费上，得到最终的本次使用铁砧的消费。

当此选项设置为 `false` 后，铁砧将不会将材料的*累积惩罚*叠加到消费上，使得基础消费会直接成为本次使用铁砧的最终消费。

此选项和 [anvilItemCostRollupAlgorithm](#anvilitemcostrollupalgorithm) 是互相独立的：即使此选项关闭，物品的*累积惩罚*不再会被算到最终消费里，使得*累积惩罚*本身变成了无效字段，但是*累积惩罚*将仍然会根据 anvilItemCostRollupAlgorithm 的设置被自增。

### anvilTooExpensiveLimit

> 本地化名称 : anvilTooExpensiveLimit | 铁砧“太贵”上限 \
> 分类 : `nukos` | `生存` | `铁砧` \
> 自版本 : `0.2.0`  \
> values : <u>`40`</u> | `2147483647` | `<int>` \
> 需要匹配客户端 : *部分*（仅在最终消费大于默认的 `40` 且仍小于已设置的数值的情况下）

控制铁砧应在最终*经验等级*消费超过多少时显示 <font color=#dd0000><b><i>过于昂贵</i></b></font> 字样并拒绝给出输出。

原版的默认值是 `40`，你可以在此设置为任何的非负整数以允许更高消费的附魔合成等等，也可以设置为更小的数以对铁砧进行更多的限制。当一个铁砧合成的最终经验等级消费超过设置的值之后，输出栏的物品将会被清空（即无法合成），同时将会把最终消费设置为 `Integer.MAX` 以确保客户端将会正确显示 *过于昂贵*。

如果此值设置为大于 `40` ，在最终消费大于 `40` 但小于设置的最大允许值的情况下，服务端将会正确回报一个大于 `40` 的最终消费和输出物品。但是在没有被修改的原版客户端里，由于 Mojang 在客户端**硬编码**了大于 `40` 后就显示 *过于昂贵* 并不允许取出输出物品的逻辑，所以如果客户端没有安装此 mod（或者一些别的修复此问题的 mod）的话，大于 `40` 消费的物品仍然是无法取出的。

### anvilCustomNameSerializer

> 本地化名称 : anvilCustomNameSerializer |铁砧重命名物品的文字序列化器 \
> 分类 : `nukos` | `生存` | `铁砧` \
> 自版本 : `0.4.0`  \
> values : <u>`vanila`</u> | `escaped` | `escape-and` | `mini-message` \
> 需要匹配客户端 : *部分*（在使用一个带有格式的名称的物品时）

更改铁砧对文本进行序列化的算法，以此来使铁砧的重命名支持更加高级的富文本格式。

支持以下几种格式：
- `vanilla` - 以原版规则设置物品的新名称：不允许使用基于 `§` 的*格式化文本*（将会在文本框中时就被移除），同时名称最长只有 50 字节大小。
- `escaped` - 允许在文本框中使用基于 `§` 的*格式化文本*，文本将会被格式化为标准的 *JSON 文本*，同时限制文本大小为格式化后最长 50 字节的可显示文本。 \
  由于 MC 的客户端字体框的限制，`§` 字符在文本框中是不可见的，但仍然能够在结果中生效。
- `escape-and` - 允许在文本框中使用基于 `§` 的*格式化文本*，同时，也可以使用 `&` 来代替 `§` 作为*格式化文本*的格式标识（这时你需要 `\&` 来输入一个正常的 `&`）。`&` 字符可以在客户端的输入框中正常显示，同时也更好键入。这可以用来作为一个更加用户友好的 `escaped` 替代方案。 \
  除了可以使用 `&` 来代替 `§` 之外，其其它工作流程都是与 `escaped` 相同的。
- `mini-message` - 在允许使用 `§` 的基础上，支持了 [MiniMessage](https://docs.advntr.dev/minimessage/format.html) 文本格式，使得可以编写更加复杂和高级的文本。 \
  Carpet Nukos 的 mini-message on anvils 支持了 MiniMessage 的 `color`, `reset`, `decorations`, `gradient`, `transition`, `rainbow`, `translatable`, `translatableFallback`, `keybind` 特性集，而出于一些安全性和兼容性等考虑，我们决定不支持 `click`, `hover`, `insertion`, `font`, `newline`, `selector`, `score`, `nbt` 特性集。 \
  这些使用 mini message 标签编写的格式将会被最终序列化为 MC 的原生 *JSON 文本*，其中，由于 `gradient`, `transition`, `rainbow` 的特殊性，它们被编译为 *JSON 文本*后再反序列化为 mini message 文本将会变为大量的 `color` 标签，使得其不容易再次编辑，一定程度上对反复命名提供了一些难度，所以请注意使用这些标签。 \
  在 mini message 格式下编写的 `§` 格式根据 mini message 库的行为，可能不会被格式化为 *JSON 文本*，而是会在文本中留下 `§` 符号。尽管这不影响客户端渲染（因为客户端都会在渲染时再次解析 `§` 符号），但可能会造成一些兼容性问题。

由于原版客户端硬编码了文本框 50 个字符的输入上限，这会导致在原版客户端上使用富文本时富文本的格式化标签也占用了字符上限本身（服务端是只计算可显示文字数量的）。此 mod 通过将客户端的文本框字符上限改为了 1000 一定程度上解决了这个问题。

铁砧将名称设置到输出物品上的行为是一个服务端行为，所以可以正常的在原版客户端上使用此规则为物品设置富文本名称。

由于铁砧在铁砧的重命名输入框中显示物品名称是一个客户端行为，而且原版并没有处理物品的*自定义名称*为富文本的情况，这会导致一个带有富文本的物品放在铁砧的主要输入口后，其富文本会在客户端被抹去。这会导致在**原版客户端**下对一个拥有富文本的物品使用铁砧后其富文本会**消失**（这个过程是**被认为是一次重命名**的）。有两种方法可以解决这个问题：在铁砧里的输入框里手动设置一个和物品的富文本名称相同的富文本名称；或者在客户端安装此 mod。

### anvilCanRepairUseIronBlock

> 本地化名称 : anvilCanRepairUseIronBlock |铁块修复铁砧 \
> 分类 : `nukos` | `生存` | `铁砧` \
> 自版本 : `0.5.0`  \
> values : <u>`false`</u> | `true` \
> 需要匹配客户端 : 不需要

可以通过使用*铁块*右键一个有破损的铁砧来修复这个铁砧。

每个*铁块*（每次右键）将能够修复一级这个铁砧的损坏程度（从*破损的铁砧*到*开裂的铁砧*，或从*开裂的铁砧*到*铁砧*），这个*铁块*将被消耗，同时玩家的*铁块*的*使用统计数据*将会增加 1。右键一个没有破损的铁砧没有效果。

这个动作将会产生 *PP 更新*，不会产生 *NC 更新*。
