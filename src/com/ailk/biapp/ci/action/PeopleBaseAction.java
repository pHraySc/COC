package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiTagTypeModel;
import com.ailk.biapp.ci.service.ICiPeopleBaseService;
import com.ailk.biapp.ci.util.JsonUtil;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PeopleBaseAction extends CIBaseAction {
    private Logger log = Logger.getLogger(PeopleBaseAction.class);
    private String phoneNum;
    private List<CiTagTypeModel> ciTagTypeModelList;
    @Autowired
    private ICiPeopleBaseService ciPeopleBaseService;

    public PeopleBaseAction() {
    }

    public String index() {
        this.log.debug("跳转到人为本首页！");
        return "peopleBaseIndex";
    }

    public void findPhoneNum() {
        HashMap returnMap = new HashMap();

        try {
            Integer response = this.ciPeopleBaseService.queryPhoneNumExists(this.phoneNum);
            returnMap.put("count", response);
        } catch (Exception var5) {
            this.log.error("查询手机号是否存在发生异常！", var5);
            throw new CIServiceException();
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public String queryProductNoTag() {
        try {
            this.ciTagTypeModelList = this.ciPeopleBaseService.queryProductNoTag(this.phoneNum);
            return "queryPeopleBaseContent";
        } catch (Exception var2) {
            this.log.error("查询手机号标签信息异常！", var2);
            throw new CIServiceException();
        }
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<CiTagTypeModel> getCiTagTypeModelList() {
        return this.ciTagTypeModelList;
    }

    public void setCiTagTypeModelList(List<CiTagTypeModel> ciTagTypeModelList) {
        this.ciTagTypeModelList = ciTagTypeModelList;
    }
}
