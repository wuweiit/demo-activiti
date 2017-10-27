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



### 请假审批流程


VacationTest


### 启动 activiti-explorer.war

直接放在Tomcat的webapps文件夹即可，若需要自定义数据库，将db.properties 配置为你的库

注意：别忘记添加数据库驱动到lib里去。


### 业务与工作流整合参考

http://www.kafeitu.me/activiti/2012/03/22/workflow-activiti-action.html
http://blog.csdn.net/lovemenghaibin/article/details/50608300


### eclipse activiti插件安装
http://www.cnblogs.com/strinkbug/p/4876819.html