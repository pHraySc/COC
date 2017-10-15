package com.ailk.biapp.ci.market.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.entity.CiMarketScene;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiMarketAction extends CIBaseAction {
    private static Logger log = Logger.getLogger(CiMarketAction.class);
    private List<CiMarketTask> marketTaskTree;
    @Autowired
    private ICiMarketService ciMarketService;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    private List<CiMarketTask> firstLevelTasks;
    private List<CiMarketScene> firstLevelScenes;
    private String taskId;
    private String sceneId;
    private CiCustomLabelSceneRel customLabelSceneRel;
    private Pager pager;
    private String labelOrCustomId;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private LabelDetailInfo labelDetailInfo;
    private String childMarketTaskId;
    private CiMarketTask childMarketTask;
    private CiMarketTask parentMarketTask;
    private String fixedSearchFlag;

    public CiMarketAction() {
        this.fixedSearchFlag = ServiceConstants.FIXED_SEARCH_YES;
    }

    public void deleteCustomOrLabelSceneRel() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();

        try {
            this.ciMarketService.deleteCustomOrLabelSceneRel(this.customLabelSceneRel);
            if(!this.ciMarketService.isExistOtherSceneRel(this.customLabelSceneRel)) {
                if(this.customLabelSceneRel.getShowType().intValue() == ServiceConstants.RECOM_SHOW_TYPE_LABEL) {
                    CacheBase response = CacheBase.getInstance();
                    CiLabelInfo e = response.getEffectiveLabel(this.customLabelSceneRel.getLabelId() + "");
                    if(e != null) {
                        e.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
                        this.ciLabelInfoService.modify(e);
                        response.modifyEffectiveLabel(e.getLabelId() + "", e);
                    }
                } else if(StringUtil.isNotEmpty(this.customLabelSceneRel.getCustomGroupId())) {
                    this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.customLabelSceneRel.getCustomGroupId());
                    if(this.ciCustomGroupInfo != null) {
                        this.ciCustomGroupInfo.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
                        this.customersService.modifyCiCustomGroupInfo(this.ciCustomGroupInfo, this.getUserId());
                    }
                }
            }

            success = true;
            msg = "取消推荐成功";
        } catch (Exception var7) {
            log.error("删除客户群标签场景关系实体失败");
            success = false;
            msg = var7.getMessage();
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var6) {
            log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public String findMarketIndex() {
        this.firstLevelScenes = (List<CiMarketScene>)CacheBase.getInstance().getObjectList("CI_MARKET_SCENE_MAP");
        if(this.firstLevelScenes != null) {
            for(int i = 0; i < this.firstLevelScenes.size(); ++i) {
                CiMarketScene scene = (CiMarketScene)this.firstLevelScenes.get(i);
                if(i == 0) {
                    scene.setIconCls("header-adress-icon");
                }

                if(i == 1) {
                    scene.setIconCls("header-condition-icon");
                }

                if(i == 2) {
                    scene.setIconCls("header-not-user-icon");
                }
            }
        }

        if(StringUtil.isNotEmpty(this.customLabelSceneRel.getMarketTaskId())) {
            this.childMarketTask = this.ciMarketService.queryMarketTaskById(this.customLabelSceneRel.getMarketTaskId());
            if(StringUtil.isEmpty(this.childMarketTask.getParentId())) {
                this.parentMarketTask = this.childMarketTask;
            } else {
                this.parentMarketTask = this.ciMarketService.queryMarketTaskById(this.childMarketTask.getParentId());
            }
        }

        return "marketIndex";
    }

    public String findMarketList() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.customLabelSceneRel == null) {
            this.customLabelSceneRel = new CiCustomLabelSceneRel();
        }

        StringBuffer marketTaskIds = new StringBuffer();
        if(StringUtil.isNotEmpty(this.customLabelSceneRel.getMarketTaskId())) {
            CiMarketTask count = this.ciMarketService.queryMarketTaskById(this.customLabelSceneRel.getMarketTaskId());
            if(StringUtil.isNotEmpty(count.getParentId())) {
                marketTaskIds.append("\'").append(count.getMarketTaskId()).append("\'");
            } else {
                List totalPage = this.ciMarketService.queryMarketTaskByParentId(count.getMarketTaskId());
                if(totalPage != null) {
                    for(int list = 0; list < totalPage.size(); ++list) {
                        CiMarketTask item = (CiMarketTask)totalPage.get(list);
                        marketTaskIds.append("\'").append(item.getMarketTaskId()).append("\'");
                        if(list < totalPage.size() - 1) {
                            marketTaskIds.append(",");
                        }
                    }
                }
            }
        }

        this.customLabelSceneRel.setMarketTaskId(marketTaskIds.toString());
        int var6 = this.ciMarketService.queryMarketIndexListCount(this.customLabelSceneRel);
        int var7 = (int)Math.ceil((double)var6 / (double)this.pager.getPageSize());
        this.pager.setTotalPage(var7);
        this.pager.setTotalSize((long)var6);
        this.pager = this.pager.pagerFlip();
        List var8 = this.ciMarketService.queryMarketIndexList(this.pager.getPageNum(), this.pager.getPageSize(), this.customLabelSceneRel);
        this.pager.setResult(var8);
        return "marketList";
    }

    public String newMarketIndex() {
        this.fixedSearchFlag = ServiceConstants.FIXED_SEARCH_NO;
        CacheBase cache = CacheBase.getInstance();
        this.marketTaskTree = cache.getCiMarketTree();
        return "newMarketIndex";
    }

    public String findLabelDetail() {
        try {
            this.labelDetailInfo = (LabelDetailInfo)this.ciLabelInfoService.queryEffectiveLabelByLabelId(Integer.valueOf(this.labelOrCustomId)).get(0);
        } catch (Exception var2) {
            log.error("查询标签详细信息异常！", var2);
        }

        return "labelDetailTip";
    }

    public String findCustomGroupDetail() {
        try {
            this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.labelOrCustomId);
            IUser e = PrivilegeServiceUtil.getUserById(this.ciCustomGroupInfo.getCreateUserId());
            if(e != null) {
                this.ciCustomGroupInfo.setCreateUserName(e.getUsername());
            }

            if(this.ciCustomGroupInfo.getCreateTypeId() != null) {
                this.ciCustomGroupInfo.setCreateType(IdToName.getName("DIM_CUSTOM_CREATE_TYPE", this.ciCustomGroupInfo.getCreateTypeId()));
            }

            if(this.ciCustomGroupInfo.getCreateCityId() != null) {
                this.ciCustomGroupInfo.setCreateCityName(IdToName.getName("DIM_CITY", Integer.valueOf(this.ciCustomGroupInfo.getCreateCityId())));
            }

            String offsetStr = this.customersService.queryCustomerOffsetStr(this.ciCustomGroupInfo);
            this.ciCustomGroupInfo.setKpiDiffRule((this.ciCustomGroupInfo.getKpiDiffRule() == null?"":this.ciCustomGroupInfo.getKpiDiffRule()) + offsetStr);
            this.ciCustomGroupInfo.setDataDateStr(this.ciMarketService.queryCustomDataDate(this.ciCustomGroupInfo));
        } catch (Exception var3) {
            log.error("查询客户群详细信息异常！", var3);
        }

        return "customGroupDetailTip";
    }

    public List<CiMarketTask> getMarketTaskTree() {
        return this.marketTaskTree;
    }

    public void setMarketTaskTree(List<CiMarketTask> marketTaskTree) {
        this.marketTaskTree = marketTaskTree;
    }

    public List<CiMarketTask> getFirstLevelTasks() {
        return this.firstLevelTasks;
    }

    public void setFirstLevelTasks(List<CiMarketTask> firstLevelTasks) {
        this.firstLevelTasks = firstLevelTasks;
    }

    public List<CiMarketScene> getFirstLevelScenes() {
        return this.firstLevelScenes;
    }

    public void setFirstLevelScenes(List<CiMarketScene> firstLevelScenes) {
        this.firstLevelScenes = firstLevelScenes;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSceneId() {
        return this.sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public CiCustomLabelSceneRel getCustomLabelSceneRel() {
        return this.customLabelSceneRel;
    }

    public void setCustomLabelSceneRel(CiCustomLabelSceneRel customLabelSceneRel) {
        this.customLabelSceneRel = customLabelSceneRel;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getLabelOrCustomId() {
        return this.labelOrCustomId;
    }

    public void setLabelOrCustomId(String labelOrCustomId) {
        this.labelOrCustomId = labelOrCustomId;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public LabelDetailInfo getLabelDetailInfo() {
        return this.labelDetailInfo;
    }

    public void setLabelDetailInfo(LabelDetailInfo labelDetailInfo) {
        this.labelDetailInfo = labelDetailInfo;
    }

    public String getFixedSearchFlag() {
        return this.fixedSearchFlag;
    }

    public void setFixedSearchFlag(String fixedSearchFlag) {
        this.fixedSearchFlag = fixedSearchFlag;
    }

    public String getChildMarketTaskId() {
        return this.childMarketTaskId;
    }

    public void setChildMarketTaskId(String childMarketTaskId) {
        this.childMarketTaskId = childMarketTaskId;
    }

    public CiMarketTask getChildMarketTask() {
        return this.childMarketTask;
    }

    public void setChildMarketTask(CiMarketTask childMarketTask) {
        this.childMarketTask = childMarketTask;
    }

    public CiMarketTask getParentMarketTask() {
        return this.parentMarketTask;
    }

    public void setParentMarketTask(CiMarketTask parentMarketTask) {
        this.parentMarketTask = parentMarketTask;
    }
}
