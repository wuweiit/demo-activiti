### 本Demo架构

基于Spring + junit + activiti 实现工作流操作

### activiti工程流引擎


MonthTest 测试 最简单直接的流程《审核周报流程》

```
启动-> sales组发布周报 -> manager组审核周报 -> 结束
```

### 工作流传递变量
MonthVarTest 测试 最简单直接的流程《审核周报流程》


```
变量为user1 = fozzie
     user2 = gonzo
启动-> sales组发布周报 -> manager组审核周报 -> 结束
```



### 业务与工作流整合参考

http://www.kafeitu.me/activiti/2012/03/22/workflow-activiti-action.html