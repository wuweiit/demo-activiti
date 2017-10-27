package com.wuweiit.activiti.task;
/**
 * Created by marker on 2017/10/27.
 */

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author marker
 *  2017-10-27 上午10:37
 **/
public class UserTask implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        String name = (String) delegateTask.getVariable("name");
        String day  = (String) delegateTask.getVariable("day");


        String text = name + " 申请请假 "+ day + "天。";


        delegateTask.setDescription(text);

    }
}
