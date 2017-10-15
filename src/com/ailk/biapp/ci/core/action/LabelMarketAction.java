package com.ailk.biapp.ci.core.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.search.service.ISearchService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LabelMarketAction extends CIBaseAction {
    private Logger log = Logger.getLogger(LabelMarketAction.class);
    private HashMap<String, String> pojo = new HashMap();
    private Pager pager;
    private CiLabelInfo ciLabelInfo;
    private String labelKeyWord;
    private List<LabelDetailInfo> labelDetailList;
    @Autowired
    private ISearchService searchService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    private List<HashMap<String, String>> labelLevelPath = new ArrayList();

    public LabelMarketAction() {
    }

    public String findEffectiveLabelIndex() {
        String dataScope = (String)this.pojo.get("dataScope");
        String topLabelIdStr = (String)this.pojo.get("topLabelId");
        String categoriesId = (String)this.pojo.get("categoriesId");
        topLabelIdStr = null;
        Integer topLabelId = null;
        if(StringUtil.isNotEmpty(topLabelIdStr)) {
            topLabelId = Integer.valueOf(topLabelIdStr.trim());
        }

        if(StringUtils.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.ciLabelInfo == null) {
            this.ciLabelInfo = new CiLabelInfo();
        }

        if(StringUtil.isNotEmpty(this.ciLabelInfo.getLabelName())) {
            this.labelKeyWord = this.ciLabelInfo.getLabelName();
        }

        try {
            String e = "time";
            long var18 = 0L;
            if(StringUtil.isNotEmpty(this.labelKeyWord)) {
                this.labelKeyWord = this.labelKeyWord.trim();
            }

            if(StringUtil.isNotEmpty(categoriesId)) {
                this.ciLabelInfo.setLabelIdLevelDesc(categoriesId);
                CacheBase cache = CacheBase.getInstance();
                CiLabelInfo labelInfo = cache.getEffectiveLabel(categoriesId);
                String labelIdLevelDesc = labelInfo.getLabelIdLevelDesc();
                if(StringUtil.isNotEmpty(labelIdLevelDesc)) {
                    String[] labelIds = labelIdLevelDesc.split("/");

                    for(int labelMap2 = 0; labelMap2 < labelIds.length; ++labelMap2) {
                        if(StringUtil.isNotEmpty(labelIds[labelMap2])) {
                            String label_id = labelIds[labelMap2];
                            CiLabelInfo label = cache.getEffectiveLabel(label_id);
                            String label_name = "";
                            if(null != label) {
                                label_name = label.getLabelName();
                            }

                            HashMap labelMap = new HashMap();
                            labelMap.put("labelId", label_id);
                            labelMap.put("labelName", label_name);
                            this.labelLevelPath.add(labelMap);
                        }
                    }

                    HashMap var19 = new HashMap();
                    var19.put("labelId", labelInfo.getLabelId() + "");
                    var19.put("labelName", labelInfo.getLabelName());
                    this.labelLevelPath.add(var19);
                }
            }

            return "labelIndex";
        } catch (Exception var17) {
            String msg = "标签列表查询发生异常";
            this.log.error(msg, var17);
            throw new CIServiceException(var17);
        }
    }

    public String findEffectiveLabelList() {
        String dataScope = (String)this.pojo.get("dataScope");
        String topLabelIdStr = (String)this.pojo.get("topLabelId");
        String categoriesId = (String)this.pojo.get("categoriesId");
        topLabelIdStr = null;
        Integer topLabelId = null;
        if(StringUtil.isNotEmpty(topLabelIdStr)) {
            topLabelId = Integer.valueOf(topLabelIdStr.trim());
        }

        if(StringUtils.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.ciLabelInfo == null) {
            this.ciLabelInfo = new CiLabelInfo();
        }

        if(StringUtil.isNotEmpty(this.ciLabelInfo.getLabelName())) {
            this.labelKeyWord = this.ciLabelInfo.getLabelName();
        }

        try {
            String e = "time";
            long msg1 = 0L;
            if(StringUtil.isNotEmpty(this.labelKeyWord)) {
                this.labelKeyWord = this.labelKeyWord.trim();
            }

            if(StringUtil.isNotEmpty(categoriesId)) {
                this.ciLabelInfo.setLabelIdLevelDesc(categoriesId);
            }

            String searchByDBFlag = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
            if(StringUtil.isNotEmpty(searchByDBFlag) && "false".equalsIgnoreCase(searchByDBFlag)) {
                msg1 = this.searchService.searchLabelCount(this.labelKeyWord, dataScope, this.ciLabelInfo);
            } else {
                msg1 = this.ciLabelInfoService.queryEffectiveLabelNum(this.labelKeyWord, topLabelId, dataScope, this.ciLabelInfo);
            }

            Pager p2 = new Pager(msg1);
            this.pager.setTotalPage(p2.getTotalPage());
            this.pager.setTotalSize(p2.getTotalSize());
            this.pager = this.pager.pagerFlip();
            if(StringUtil.isNotEmpty(searchByDBFlag) && "false".equalsIgnoreCase(searchByDBFlag)) {
                this.labelDetailList = this.searchService.searchLabelForPage(this.pager, this.labelKeyWord, dataScope, this.ciLabelInfo);
            } else {
                this.labelDetailList = this.ciLabelInfoService.queryEffectiveLabel(this.pager, e, this.labelKeyWord, topLabelId, dataScope, this.ciLabelInfo);
            }

            return "labelList";
        } catch (Exception var10) {
            String msg = "标签列表查询发生异常";
            this.log.error(msg, var10);
            throw new CIServiceException(var10);
        }
    }

    public void queryLabelCategoryList() {
        List list = this.ciLabelInfoService.queryCiLabelInfoList(Integer.valueOf(0), Integer.valueOf(1));
    }

    public HashMap<String, String> getPojo() {
        return this.pojo;
    }

    public void setPojo(HashMap<String, String> pojo) {
        this.pojo = pojo;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }

    public String getLabelKeyWord() {
        return this.labelKeyWord;
    }

    public void setLabelKeyWord(String labelKeyWord) {
        this.labelKeyWord = labelKeyWord;
    }

    public List<LabelDetailInfo> getLabelDetailList() {
        return this.labelDetailList;
    }

    public void setLabelDetailList(List<LabelDetailInfo> labelDetailList) {
        this.labelDetailList = labelDetailList;
    }

    public List<HashMap<String, String>> getLabelLevelPath() {
        return this.labelLevelPath;
    }

    public void setLabelLevelPath(List<HashMap<String, String>> labelLevelPath) {
        this.labelLevelPath = labelLevelPath;
    }
}
