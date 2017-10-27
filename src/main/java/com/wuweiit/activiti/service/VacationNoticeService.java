package com.wuweiit.activiti.service;
/**
 * Created by marker on 2017/10/27.
 */

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 *
 * 请假业务通知
 *
 * @author marker
 *   2017-10-27 上午10:29
 **/
public class VacationNoticeService implements JavaDelegate {

    /**   */
    private Expression text1;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String str = (String) text1.getValue(execution);
        System.out.println("请假成功!!  !!!  :"  + str);



    }
}
