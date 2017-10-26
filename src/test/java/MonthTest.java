/**
 * Created by marker on 2017/10/26.
 */

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
public class MonthTest {


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
    public void monthtest() {


        // 部署流程定义
        repositoryService.createDeployment().addClasspathResource("test.bpmn20.xml").deploy();
        // 启动流程实例
        String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();
        // 获得第一个任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("sales").list();
        for (Task task : tasks) {
            System.out.println("跟踪任务 ，任务名称: " + task.getName());
            // 认领任务这里由foozie认领，因为fozzie是sales组的成员
            taskService.claim(task.getId(), "fozzie");
        }



        // 查看fozzie现在是否能够获取到该任务
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        for (Task task : tasks) {
            System.out.println("Task for fozzie: " + task.getName());
            // 执行(完成)任务
            taskService.complete(task.getId());
        }




        // 现在fozzie的可执行任务数就为0了
        System.out.println(" fozzie的剩余任务量: "
                + taskService.createTaskQuery().taskAssignee("fozzie").count());


        // 获得第二个任务
        tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            System.out.println("跟踪任务是否有效， group:" + task.getName());
            // 认领任务这里由kermit认领，因为kermit是management组的成员
            taskService.claim(task.getId(), "kermit");
        }
        // 完成第二个任务结束流程
        for (Task task : tasks) {
            System.out.println("审核完成任务：" + task.getName());
            taskService.complete(task.getId());
        }
        // 核实流程是否结束,输出流程结束时间
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procId).singleResult();
        System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
    }

}
