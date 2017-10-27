/**
 * Created by marker on 2017/10/26.
 */

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流测试
 * （模拟周报审核）
 *
 *
 * 1)      单元测试完成后，我们可以将该bpmn20.xml文件导入之前我们部署的activiti-explorer应用中：点击流程的流程设计工作区，点击导入，将刚才我们编写的文件导入进去。
 * 2)      导入之后在右上角点击部署。
 * 3)      在已部署流程定义中我们可以看到这个流程，及它的流程图。
 * 4)      点击启动流程，该流程就会被启动，再点击任务中，列队就会有该任务了，而且是分配给sales的，这正是我们定义流程时所分配给的用户组啊。注意，现在只有sales组的用户才可以看到此任务！
 * 5)      sales组的用户进入之后点击“签收”，该任务就分配给该用户了，然后该用户就可以进行处理，也就是在代办任务和受邀里。
 * 6)      进去之后点击完成任务，该任务就流到下一个节点，也就是流转到management组中去了，要由management组的用户去处理。
 * 7)      于是这时候，队列中management组就有一个新的任务了，等待management组的成员来“签收”，并完成任务。该流程也就结束了。
 * 8)      此时就可以查看历史任务了，就是我们这里的“已归档”。用户完成的任务会在这里显示。
 *     这就是整个Demo的编写、测试过程。这样一个小小的流程基本能够体现出Activiti的功能及使用方法。
 *
 * @author marker
 * @create 2017-10-26 下午3:03
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class VacationTest {


    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    ManagementService managementService;






    @Test
    public void test() {


        // 部署流程定义
        repositoryService.createDeployment().addClasspathResource("workflow/process.bpmn20.xml").name("请假申请流程").deploy();



        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("title", "请假申请单");// 标题
        variables.put("day", "3");// 请假天数
        variables.put("name", "fozzie");// 请假人



        // 启动流程实例
        String procId = runtimeService.startProcessInstanceByKey("process", variables).getId();


        // fozzie 的审批任务
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("kermit").list();
        for (Task task : tasks) {
//            System.out.printf(task.getDescription());
            System.out.println("领导拒绝请假申请！");
            // 认领任务这里由foozie认领，因为fozzie是sales组的成员
            String taskId = task.getId();
            taskService.claim(taskId, "kermit");// 签收
            taskService.setVariable(taskId, "agree",false);// 拒绝申请
            taskService.setVariable(taskId, "name","fozzie");// 拒绝申请
            taskService.complete(task.getId());// 完成
        }


        // fozzie  继续申请
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        for (Task task : tasks) {

            System.out.println("fozzie 继续申请 ");
            // 认领任务这里由foozie认领，因为fozzie是sales组的成员
            String taskId = task.getId();
            taskService.claim(taskId, "fozzie");// 签收
            taskService.setVariable(taskId, "name","fozzie");//
            taskService.setVariable(taskId, "abandon",false);//
            taskService.complete(task.getId());// 完成
        }


        // fozzie 放弃申请
//        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
//        for (Task task : tasks) {
//            System.out.println("fozzie 放弃申请 ");
//            // 认领任务这里由foozie认领，因为fozzie是sales组的成员
//            String taskId = task.getId();
//            taskService.claim(taskId, "fozzie");// 签收
//            taskService.setVariable(taskId, "name","fozzie");//
//            taskService.setVariable(taskId, "abandon",true);// 放弃
//            taskService.complete(task.getId());// 完成
//        }






        // 领导同意
        tasks = taskService.createTaskQuery().taskAssignee("kermit").list();
        for (Task task : tasks) {
            System.out.println("领导签收并同意申请！" );
            // 执行(完成)任务
            String taskId = task.getId();
            taskService.claim(taskId, "kermit");// 签收
            taskService.setVariable(taskId, "agree",true);// 同意申请
            taskService.complete(task.getId());
        }








        // 核实流程是否结束,输出流程结束时间
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procId).singleResult();
        System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
    }







}
