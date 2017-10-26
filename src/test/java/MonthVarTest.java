/**
 * Created by marker on 2017/10/26.
 */

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流测试
 * （模拟周报审核）
 *  动态指定审批人。
 *
 *
 * @author marker
 * @create 2017-10-26 下午3:03
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class MonthVarTest {


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
        repositoryService.createDeployment().addClasspathResource("testVar.bpmn20.xml").deploy();


        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user1", "fozzie");// 一级审批人
        variables.put("user2", "gonzo");// 二级审批人


        // 启动流程实例
        String procId = runtimeService.startProcessInstanceByKey("financialReport2", variables).getId();

        // 核实流程是否结束,输出流程结束时间
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procId).singleResult();
        System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
    }

}
