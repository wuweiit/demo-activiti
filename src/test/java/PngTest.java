/**
 * Created by marker on 2017/10/26.
 */

import com.alibaba.fastjson.util.IOUtils;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流测试
 * （ 获取工作流png ）
 *
 * @author marker
 * @create 2017-10-26 下午3:03
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class PngTest {


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



    //查看bpmn 资源图片
    @Test
    public void viewImage() throws Exception{


        // 部署流程定义
        String deploymentId = repositoryService.createDeployment().addClasspathResource("testVar.bpmn20.xml").deploy().getId();

        String imageName=null;
        //取得某个部署的资源的名称  deploymentId
        List<String> resourceNames = repositoryService.getDeploymentResourceNames(deploymentId);
        // buybill.bpmn  buybill.png
        if(resourceNames!=null && resourceNames.size()>0){
            for(String temp :resourceNames){
                if(temp.indexOf(".png")>0){
                    imageName = temp;
                }
            }
        }

        /**
         * 读取资源
         * deploymentId:部署的id
         * resourceName：资源的文件名
         */
        InputStream resourceAsStream =  repositoryService.getResourceAsStream(deploymentId, imageName);

        //把文件输入流写入到文件中
        File file=new File("/Users/marker/Desktop/" + imageName);



        FileUtils.copyInputStreamToFile(resourceAsStream, file);
    }




}
