package com.ailk.biapp.ci.search.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelInfoJDao;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.entity.CiUserAttentionLabel;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.search.model.CustomGroupModelForSearch;
import com.ailk.biapp.ci.search.service.ISearchService;
import com.ailk.biapp.ci.search.utils.SearchUtils;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements ISearchService {
    private Logger log = Logger.getLogger(SearchServiceImpl.class);
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private DimTableServiceImpl dimTableServiceImpl;
    @Autowired
    private ICiLabelInfoJDao ciLabelInfoJDao;

    public SearchServiceImpl() {
    }

    public List<CustomGroupModelForSearch> queryAllCustomGroupInfo() throws CIServiceException {
        return null;
    }

    public List<CiLabelInfo> queryAllLabelInfo() throws CIServiceException {
        List allLabelList = this.ciLabelInfoService.queryAllEffectiveLabel();
        return allLabelList;
    }

    public void createAllCustomerGroupIndex() throws CIServiceException {
    }

    public void createAllLabelIndex() throws Exception {
        long startTime = System.currentTimeMillis();
        this.log.info("创建全量标签索引：createAllLabelIndex    start");
        System.out.println("创建全量标签索引：createAllLabelIndex    start");
        List labelModelList = this.ciLabelInfoService.queryEffectiveLabel((Pager)null, (String)null, "", (Integer)null, "all", new CiLabelInfo());
        SearchUtils searchUtil = new SearchUtils();
        String indexType = "L";
        Directory dir = searchUtil.getDirectory(indexType);
        IndexWriterConfig iwc = new IndexWriterConfig(SearchUtils.getLuceneVersion(), SearchUtils.getAnalyzer());
        iwc.setOpenMode(OpenMode.CREATE);
        IndexWriterConfig iwc2 = new IndexWriterConfig(SearchUtils.getLuceneVersion(), SearchUtils.getAnalyzer());
        iwc2.setOpenMode(OpenMode.CREATE);
        IndexWriter ramWriter = null;
        IndexWriter fsWriter = null;

        try {
            ramWriter = new IndexWriter(dir, iwc);
            FSDirectory e = FSDirectory.open(new File(searchUtil.getIndexPath(indexType)));
            fsWriter = new IndexWriter(e, iwc2);
            CacheBase cache = CacheBase.getInstance();
            if(null != labelModelList && labelModelList.size() > 0) {
                Iterator iterator = labelModelList.iterator();

                while(iterator.hasNext()) {
                    LabelDetailInfo labelModelForSearch = (LabelDetailInfo)iterator.next();
                    Document document = new Document();
                    document.add(new StringField("labelId", this.getNotNullStringValue(labelModelForSearch.getLabelId()), Store.YES));
                    TextField labelName = new TextField("labelName", this.getNotNullStringValue(labelModelForSearch.getLabelName()), Store.YES);
                    labelName.setBoost(100.0F);
                    document.add(labelName);
                    TextField createDesc = new TextField("createDesc", this.getNotNullStringValue(labelModelForSearch.getCreateDesc()), Store.YES);
                    createDesc.setBoost(90.0F);
                    document.add(createDesc);
                    TextField busiCaliber = new TextField("busiCaliber", this.getNotNullStringValue(labelModelForSearch.getBusiCaliber()), Store.YES);
                    busiCaliber.setBoost(80.0F);
                    document.add(busiCaliber);
                    StringField publishTime = new StringField("publishTime", this.getNotNullStringValue(labelModelForSearch.getPublishTime()) + "", Store.YES);
                    document.add(publishTime);
                    StringField dataDate = new StringField("dataDate", this.getNotNullStringValue(labelModelForSearch.getDataDate()) + "", Store.YES);
                    document.add(dataDate);
                    StringField publishTimeStr = new StringField("publishTimeStr", this.getNotNullStringValue(labelModelForSearch.getPublishTime().replaceAll("[- :]", "")) + "", Store.YES);
                    document.add(publishTimeStr);
                    TextField labelSceneIds = new TextField("labelSceneIds", this.getNotNullStringValue(labelModelForSearch.getLabelSceneIds()), Store.YES);
                    document.add(labelSceneIds);
                    StringField labelSceneNames = new StringField("labelSceneNames", this.getNotNullStringValue(labelModelForSearch.getLabelSceneNames()), Store.YES);
                    document.add(labelSceneNames);
                    StringField currentLabelPath = new StringField("currentLabelPath", this.getNotNullStringValue(labelModelForSearch.getCurrentLabelPath()), Store.YES);
                    document.add(currentLabelPath);
                    StringField labelIdLevelDesc = new StringField("labelIdLevelDesc", this.getNotNullStringValue(labelModelForSearch.getLabelIdLevelDesc()), Store.YES);
                    document.add(labelIdLevelDesc);
                    StringField updateCycle = new StringField("updateCycle", this.getNotNullStringValue(labelModelForSearch.getUpdateCycle()), Store.YES);
                    document.add(updateCycle);
                    StringField updateCycleName = new StringField("updateCycleName", this.getNotNullStringValue(labelModelForSearch.getUpdateCycleName()), Store.YES);
                    document.add(updateCycleName);
                    StringField customNum = new StringField("customNum", this.getNotNullStringValue(labelModelForSearch.getCustomNum()), Store.YES);
                    document.add(customNum);
                    StringField avgScore = new StringField("avgScore", labelModelForSearch.getAvgScore() == null?"":labelModelForSearch.getAvgScore() + "", Store.YES);
                    document.add(avgScore);
                    StringField useTimes = new StringField("useTimes", this.getNotNullStringValue(labelModelForSearch.getUseTimes()), Store.YES);
                    document.add(useTimes);
                    StringField labelTypeId = new StringField("labelTypeId", labelModelForSearch.getLabelTypeId() == null?"":labelModelForSearch.getLabelTypeId() + "", Store.YES);
                    document.add(labelTypeId);
                    StringField isSysRecom = new StringField("isSysRecom", labelModelForSearch.getIsSysRecom() == null?"0":labelModelForSearch.getIsSysRecom() + "", Store.YES);
                    document.add(isSysRecom);
                    String labelId = labelModelForSearch.getLabelId();
                    CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(labelId);
                    CiLabelExtInfo ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
                    StringBuffer dimValueSB = new StringBuffer();
                    if(5 == ciLabelInfo.getLabelTypeId().intValue()) {
                        CiMdaSysTableColumn dimValue = ciLabelExtInfo.getCiMdaSysTableColumn();
                        this.appendDimValueStringBuffer(dimValueSB, dimValue);
                    }

                    if(8 == ciLabelInfo.getLabelTypeId().intValue()) {
                        Set dimValue1 = ciLabelExtInfo.getCiLabelVerticalColumnRels();
                        Iterator i$ = dimValue1.iterator();
                        if(i$.hasNext()) {
                            CiLabelVerticalColumnRel r = (CiLabelVerticalColumnRel)i$.next();
                            if(5 == r.getLabelTypeId().intValue()) {
                                CiMdaSysTableColumn ciMdaSysTableColumn = r.getCiMdaSysTableColumn();
                                this.appendDimValueStringBuffer(dimValueSB, ciMdaSysTableColumn);
                            }
                        }
                    }

                    TextField dimValue2 = new TextField("dimValue", this.getNotNullStringValue(dimValueSB.toString()), Store.YES);
                    dimValue2.setBoost(70.0F);
                    document.add(dimValue2);
                    ramWriter.addDocument(document);
                }

                ramWriter.commit();
                fsWriter.addIndexes(new Directory[]{dir});
            }
        } catch (Exception var49) {
            this.log.error("创建全量标签索引：createAllLabelIndex error", var49);
            var49.printStackTrace();
            throw new CIServiceException();
        } finally {
            try {
                if(ramWriter != null) {
                    ramWriter.close();
                }

                if(fsWriter != null) {
                    fsWriter.close();
                }
            } catch (IOException var48) {
                this.log.error("关闭索引writer异常", var48);
                var48.printStackTrace();
                throw new CIServiceException();
            }

            System.out.println("创建全量标签索引：createAllLabelIndex    end,共耗时：" + (System.currentTimeMillis() - startTime));
            this.log.info("创建全量标签索引：createAllLabelIndex    end,共耗时：" + (System.currentTimeMillis() - startTime));
        }

    }

    private String getNotNullStringValue(String value) {
        return value == null?"":value;
    }

    public List<LabelDetailInfo> searchLabelForPage(Pager pager, String keyWord, String dataScope, CiLabelInfo searchBean) {
        BooleanQuery query = new BooleanQuery();
        Boolean flag = Boolean.valueOf(true);
        ArrayList labelModelList = new ArrayList();
        if(StringUtil.isNotEmpty(keyWord)) {
            String date = QueryParser.escape(keyWord);
            flag = Boolean.valueOf(false);
            String[] startTime = new String[]{"labelName", "busiCaliber", "createDesc", "dimValue"};
            MultiFieldQueryParser endTime = new MultiFieldQueryParser(SearchUtils.getLuceneVersion(), startTime, SearchUtils.getAnalyzer());
            endTime.setDefaultOperator(Operator.OR);
            Query reader = null;

            try {
                reader = endTime.parse(date);
            } catch (ParseException var42) {
                var42.printStackTrace();
            }

            reader.setBoost(2.0F);
            query.add(reader, Occur.MUST);
        }

        int first;
        String var51;
        if(StringUtil.isNotEmpty(searchBean.getSceneId())) {
            flag = Boolean.valueOf(false);
            BooleanQuery var45 = new BooleanQuery();
            int var47 = 1;
            String[] var48 = null;
            var51 = searchBean.getSceneId();
            if(null != var51 && var51.length() >= 1) {
                var51 = var51.substring(0, var51.length() - 1);
                var48 = var51.split(",");
                var47 += var48.length;
            }

            String[] e = new String[var47];
            String[] searcher = new String[var47];
            Occur[] topDocs = new Occur[var47];

            for(first = 0; first < var48.length; ++first) {
                e[first] = "labelSceneIds";
                searcher[first] = var48[first];
                topDocs[first] = Occur.SHOULD;
            }

            e[var47 - 1] = "labelSceneIds";
            searcher[var47 - 1] = "0";
            topDocs[var47 - 1] = Occur.SHOULD;

            for(first = 0; first < var47; ++first) {
                var45.add(new TermQuery(new Term(e[first], searcher[first])), topDocs[first]);
            }

            query.add(var45, Occur.MUST);
        }

        if(StringUtil.isNotEmpty(searchBean.getUpdateCycle())) {
            flag = Boolean.valueOf(false);
            query.add(new TermQuery(new Term("updateCycle", searchBean.getUpdateCycle() + "")), Occur.MUST);
        }

        Date var46 = new Date();
        String var49 = "";
        String var50 = "";
        if(dataScope.equals("oneDay")) {
            var51 = DateUtil.date2String(var46, "yyyy-MM-dd");
            var49 = var51 + "000000";
            var50 = var51 + "235959";
        }

        String var52;
        if(dataScope.equals("oneMonth")) {
            var51 = DateUtil.date2String(var46, "yyyy-MM-dd");
            var52 = DateUtil.getFrontDay(30, var51, "yyyy-MM-dd");
            var49 = var52 + "000000";
            var50 = var51 + "235959";
        }

        if(dataScope.equals("threeMonth")) {
            var51 = DateUtil.date2String(var46, "yyyy-MM-dd");
            var52 = DateUtil.getFrontDay(90, var51, "yyyy-MM-dd");
            var49 = var52 + "000000";
            var50 = var51 + "235959";
        }

        if(StringUtil.isNotEmpty(searchBean.getStartDate()) || StringUtil.isNotEmpty(searchBean.getEndDate())) {
            if(StringUtil.isNotEmpty(searchBean.getStartDate())) {
                var49 = DateUtil.date2String(searchBean.getStartDate(), "yyyyMMdd") + "000000";
            }

            if(StringUtil.isNotEmpty(searchBean.getEndDate())) {
                var50 = DateUtil.date2String(searchBean.getEndDate(), "yyyyMMdd") + "235959";
            } else {
                var50 = "99999999999999";
            }
        }

        if(StringUtil.isNotEmpty(var49) || StringUtil.isNotEmpty(var50)) {
            flag = Boolean.valueOf(false);
            BytesRef var56 = new BytesRef(var49.replaceAll("[- :]", "").getBytes());
            BytesRef var53 = new BytesRef(var50.replaceAll("[- :]", "").getBytes());
            TermRangeQuery var54 = new TermRangeQuery("publishTimeStr", var56, var53, true, true);
            query.add(var54, Occur.MUST);
        }

        WildcardQuery var55;
        Term var58;
        if(StringUtil.isNotEmpty(searchBean.getLabelIdLevelDesc())) {
            var58 = new Term("labelIdLevelDesc", "*" + searchBean.getLabelIdLevelDesc() + "*");
            var55 = new WildcardQuery(var58);
            query.add(var55, Occur.MUST);
        }

        if(flag.booleanValue()) {
            var58 = new Term("labelId", "*");
            var55 = new WildcardQuery(var58);
            query.add(var55, Occur.MUST);
        }

        DirectoryReader var61 = null;

        try {
            SearchUtils var59 = new SearchUtils();
            var61 = DirectoryReader.open(var59.getDirectory("L"));
            IndexSearcher var57 = new IndexSearcher(var61);
            Object var60 = null;
            if(StringUtil.isEmpty(pager.getOrderBy())) {
                var60 = var57.search(query, 10000);
            } else {
                String[] var62 = pager.getOrderBy().split(",");
                String[] pageSize = pager.getOrder().split(",");
                boolean pageNum = true;
                if(var62.length != pageSize.length) {
                    throw new CIServiceException("排序字段和排序方式个数不匹配！");
                }

                if(var62[0].equals("USE_TIMES")) {
                    if(pageSize[0].equals("asc")) {
                        pageNum = false;
                    }

                    var60 = var57.search(query, 10000, new Sort(new SortField[]{new SortField("isSysRecom", Type.INT, pageNum), new SortField("useTimes", Type.INT, pageNum)}));
                } else if(var62[0].equals("PUBLISH_TIME")) {
                    if(pageSize[0].equals("asc")) {
                        pageNum = false;
                    }

                    var60 = var57.search(query, 10000, new Sort(new SortField("publishTime", Type.STRING, pageNum)));
                }
            }

            first = (pager.getPageNum() - 1) * pager.getPageSize() + 1;
            int var63 = pager.getPageSize();
            int var64 = pager.getPageNum();
            int end = Math.min(var64 * var63, ((TopDocs)var60).totalHits);
            ScoreDoc[] scoreDocs = ((TopDocs)var60).scoreDocs;
            CacheBase cache = CacheBase.getInstance();
            String userId = PrivilegeServiceUtil.getUserId();
            if(scoreDocs.length > 0) {
                for(int i = first - 1; i < end; ++i) {
                    int doc = scoreDocs[i].doc;
                    Document document = var57.doc(doc);
                    LabelDetailInfo labelModel = new LabelDetailInfo();

                    labelModel.setLabelId(document.get("labelId"));
                    labelModel.setLabelName(document.get("labelName"));
                    labelModel.setUpdateCycle(document.get("updateCycle"));
                    labelModel.setBusiCaliber(document.get("busiCaliber"));
                    labelModel.setDataDate(document.get("dataDate"));
                    labelModel.setCurrentLabelPath(document.get("currentLabelPath"));
                    labelModel.setLabelSceneNames(document.get("labelSceneNames"));
                    labelModel.setCustomNum(document.get("customNum"));
                    labelModel.setLabelTypeId(Integer.valueOf(document.get("labelTypeId")));
                    labelModel.setUseTimes(document.get("useTimes"));
                    Double avgScore = null;
                    String avgScoreStr = document.get("avgScore");
                    if(StringUtil.isNotEmpty(avgScoreStr)) {
                        avgScore = Double.valueOf(avgScoreStr);
                    }

                    labelModel.setAvgScore(avgScore);
                    labelModel.setPublishTime(document.get("publishTime"));
                    LabelDetailInfo labelDetailInfo = cache.getHotLabelByKey("HOT_LABELS", labelModel.getLabelId());
                    if(labelDetailInfo != null) {
                        labelModel.setIsHot("true");
                    }

                    CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(document.get("labelId")));
                    labelModel.setIsSysRecom(labelInfo.getIsSysRecom());
                    CiUserAttentionLabelId id = new CiUserAttentionLabelId();
                    id.setLabelId(Integer.valueOf(document.get("labelId")));
                    id.setUserId(userId);
                    CiUserAttentionLabel po = this.ciLabelInfoService.queryUserAttentionLabelRecord(new Integer(document.get("labelId")), userId);
                    if(po == null) {
                        labelModel.setIsAttention("false");
                    } else {
                        labelModel.setIsAttention("true");
                    }

                    Long customNum = this.ciLabelInfoService.getCustomNum(labelModel.getDataDate(), Integer.valueOf(labelModel.getLabelId()), Integer.valueOf(-1), Integer.valueOf(-1), Long.valueOf(labelModel.getCustomNum() == null?0L:Long.valueOf(labelModel.getCustomNum()).longValue()), (String)null);
                    labelModel.setCustomNum(customNum.toString());
                    labelModelList.add(labelModel);
                }
            }
        } catch (Exception var43) {
            this.log.error("查询标签分页列表失败", var43);
            throw new CIServiceException("查询标签分页列表失败 ");
        } finally {
            if(var61 != null) {
                try {
                    var61.close();
                } catch (IOException var41) {
                    this.log.error("关闭索引reader异常", var41);
                }
            }

        }

        return labelModelList;
    }

    public long searchLabelCount(String keyWord, String dataScope, CiLabelInfo searchBean) throws CIServiceException {
        BooleanQuery query = new BooleanQuery();
        Boolean flag = Boolean.valueOf(true);
        if(StringUtil.isNotEmpty(keyWord)) {
            String date = QueryParser.escape(keyWord);
            flag = Boolean.valueOf(false);
            String[] startTime = new String[]{"labelName", "busiCaliber", "createDesc", "dimValue"};
            MultiFieldQueryParser endTime = new MultiFieldQueryParser(SearchUtils.getLuceneVersion(), startTime, SearchUtils.getAnalyzer());
            endTime.setDefaultOperator(Operator.OR);
            Query reader = null;

            try {
                reader = endTime.parse(date);
            } catch (ParseException var16) {
                var16.printStackTrace();
            }

            query.add(reader, Occur.MUST);
        }

        String var23;
        if(StringUtil.isNotEmpty(searchBean.getSceneId())) {
            flag = Boolean.valueOf(false);
            BooleanQuery var17 = new BooleanQuery();
            int var19 = 1;
            String[] var20 = null;
            var23 = searchBean.getSceneId();
            if(null != var23 && var23.length() >= 1) {
                var23 = var23.substring(0, var23.length() - 1);
                var20 = var23.split(",");
                var19 += var20.length;
            }

            String[] totalCount = new String[var19];
            String[] e = new String[var19];
            Occur[] searcher = new Occur[var19];

            int topDocs;
            for(topDocs = 0; topDocs < var20.length; ++topDocs) {
                totalCount[topDocs] = "labelSceneIds";
                e[topDocs] = var20[topDocs];
                searcher[topDocs] = Occur.SHOULD;
            }

            totalCount[var19 - 1] = "labelSceneIds";
            e[var19 - 1] = "0";
            searcher[var19 - 1] = Occur.SHOULD;

            for(topDocs = 0; topDocs < var19; ++topDocs) {
                var17.add(new TermQuery(new Term(totalCount[topDocs], e[topDocs])), searcher[topDocs]);
            }

            query.add(var17, Occur.MUST);
        }

        if(StringUtil.isNotEmpty(searchBean.getUpdateCycle())) {
            flag = Boolean.valueOf(false);
            query.add(new TermQuery(new Term("updateCycle", searchBean.getUpdateCycle() + "")), Occur.MUST);
        }

        Date var18 = new Date();
        String var21 = "";
        String var22 = "";
        String var24;
        if(dataScope.equals("oneDay")) {
            var23 = DateUtil.date2String(var18, "yyyy-MM-dd");
            var24 = DateUtil.getFrontDay(1, var23, "yyyy-MM-dd");
            var21 = var24 + "000000";
            var22 = var24 + "235959";
        }

        if(dataScope.equals("oneMonth")) {
            var23 = DateUtil.date2String(var18, "yyyy-MM-dd");
            var24 = DateUtil.getFrontDay(30, var23, "yyyy-MM-dd");
            var21 = var24 + "000000";
            var22 = var23 + "235959";
        }

        if(dataScope.equals("threeMonth")) {
            var23 = DateUtil.date2String(var18, "yyyy-MM-dd");
            var24 = DateUtil.getFrontDay(90, var23, "yyyy-MM-dd");
            var21 = var24 + "000000";
            var22 = var23 + "235959";
        }

        if(StringUtil.isNotEmpty(searchBean.getStartDate()) || StringUtil.isNotEmpty(searchBean.getEndDate())) {
            if(StringUtil.isNotEmpty(searchBean.getStartDate())) {
                var21 = DateUtil.date2String(searchBean.getStartDate(), "yyyyMMdd") + "000000";
            }

            if(StringUtil.isNotEmpty(searchBean.getEndDate())) {
                var22 = DateUtil.date2String(searchBean.getEndDate(), "yyyyMMdd") + "235959";
            } else {
                var22 = "99999999235959";
            }
        }

        if(StringUtil.isNotEmpty(var21) || StringUtil.isNotEmpty(var22)) {
            flag = Boolean.valueOf(false);
            BytesRef var27 = new BytesRef(var21.replaceAll("[- :]", "").getBytes());
            BytesRef var25 = new BytesRef(var22.replaceAll("[- :]", "").getBytes());
            TermRangeQuery var26 = new TermRangeQuery("publishTimeStr", var27, var25, true, true);
            query.add(var26, Occur.MUST);
        }

        WildcardQuery var28;
        Term var30;
        if(StringUtil.isNotEmpty(searchBean.getLabelIdLevelDesc())) {
            var30 = new Term("labelIdLevelDesc", "*" + searchBean.getLabelIdLevelDesc() + "*");
            var28 = new WildcardQuery(var30);
            query.add(var28, Occur.MUST);
        }

        if(flag.booleanValue()) {
            var30 = new Term("labelId", "*");
            var28 = new WildcardQuery(var30);
            query.add(var28, Occur.MUST);
        }

        boolean var31 = false;

        int var34;
        try {
            SearchUtils var29 = new SearchUtils();
            DirectoryReader var33 = DirectoryReader.open(var29.getDirectory("L"));
            IndexSearcher var32 = new IndexSearcher(var33);
            TopDocs var35 = var32.search(query, 10000);
            var34 = var35.totalHits;
            var33.close();
        } catch (IndexNotFoundException var14) {
            this.log.error("索引未建立", var14);
            var34 = 0;
        } catch (IOException var15) {
            this.log.error("查询标签总数失败", var15);
            throw new CIServiceException("查询标签总数失败 ");
        }

        return (long)var34;
    }

    public void deleteLabelIndexByLabelId(Integer labelId) throws CIServiceException {
        IndexWriter fsWriter = null;
        IndexWriter ramWriter = null;
        String indexType = "L";
        SearchUtils searchUtil = new SearchUtils();
        IndexWriterConfig iwc = new IndexWriterConfig(SearchUtils.getLuceneVersion(), SearchUtils.getAnalyzer());
        IndexWriterConfig fwc = new IndexWriterConfig(SearchUtils.getLuceneVersion(), SearchUtils.getAnalyzer());

        try {
            FSDirectory e = FSDirectory.open(new File(searchUtil.getIndexPath(indexType)));
            Directory ramDir = searchUtil.getDirectory("L");
            fsWriter = new IndexWriter(e, fwc);
            ramWriter = new IndexWriter(ramDir, iwc);
            fsWriter.deleteDocuments(new Term("labelId", labelId + ""));
            ramWriter.deleteDocuments(new Term("labelId", labelId + ""));
            fsWriter.forceMergeDeletes();
            fsWriter.commit();
            ramWriter.commit();
            fsWriter.close();
            ramWriter.close();
        } catch (Exception var18) {
            this.log.error("删除标签索引失败", var18);
            throw new CIServiceException("删除标签索引失败");
        } finally {
            try {
                if(ramWriter != null) {
                    ramWriter.close();
                }

                if(fsWriter != null) {
                    fsWriter.close();
                }
            } catch (IOException var17) {
                this.log.error("关闭索引writer异常", var17);
                var17.printStackTrace();
            }

        }

    }

    private void appendDimValueStringBuffer(StringBuffer dimValue, CiMdaSysTableColumn ciMdaSysTableColumn) {
        String dimId = ciMdaSysTableColumn.getDimTransId();
        DimTableDefine dimTableDefine = this.dimTableServiceImpl.findDefineById(dimId);
        if(dimTableDefine != null) {
            String dimTableName = dimTableDefine.getDimTablename();
            this.log.info("生成DimId为：       [" + dimId + "],DimTablename为[" + dimTableName + "]的维度值的索引      start");
            String dimValueCol = dimTableDefine.getDimValueCol();
            List enumTables = null;

            try {
                enumTables = this.ciLabelInfoJDao.findLabelAllEnumValue(dimValueCol, dimTableName);
            } catch (Exception var10) {
                this.log.debug("执行查询语句报错", var10);
            }

            Iterator i$ = enumTables.iterator();

            while(i$.hasNext()) {
                Map map = (Map)i$.next();
                dimValue.append(" " + map.get("ENUM_NAME"));
            }

            this.log.info("生成DimId为：       [" + dimId + "],DimTablename为[" + dimTableName + "]的维度值的索引      end");
        } else {
            this.log.error("未能正确获得维表信息,维表ID：" + dimId);
        }

    }

    public void addLabelIndexByLabelId(Integer labelId) throws CIServiceException {
        new ArrayList();
        List labelModelList = this.ciLabelInfoService.queryEffectiveLabelByLabelId(labelId);
        SearchUtils searchUtil = new SearchUtils();
        String indexType = "L";
        Directory dir = searchUtil.getDirectory(indexType);
        IndexWriterConfig iwc = new IndexWriterConfig(SearchUtils.getLuceneVersion(), SearchUtils.getAnalyzer());
        iwc.setOpenMode(OpenMode.APPEND);
        IndexWriterConfig iwc2 = new IndexWriterConfig(SearchUtils.getLuceneVersion(), SearchUtils.getAnalyzer());
        iwc2.setOpenMode(OpenMode.APPEND);
        IndexWriter ramWriter = null;
        IndexWriter fsWriter = null;

        try {
            FSDirectory e = FSDirectory.open(new File(searchUtil.getIndexPath(indexType)));
            ramWriter = new IndexWriter(dir, iwc);
            fsWriter = new IndexWriter(e, iwc2);
            CacheBase cache = CacheBase.getInstance();
            if(null != labelModelList && labelModelList.size() > 0) {
                Iterator iterator = labelModelList.iterator();

                while(iterator.hasNext()) {
                    LabelDetailInfo labelModelForSearch = (LabelDetailInfo)iterator.next();
                    Term term = new Term("labelId", labelModelForSearch.getLabelId());
                    Document document = new Document();
                    document.add(new StringField("labelId", this.getNotNullStringValue(labelModelForSearch.getLabelId()), Store.YES));
                    TextField labelName = new TextField("labelName", this.getNotNullStringValue(labelModelForSearch.getLabelName()), Store.YES);
                    labelName.setBoost(100.0F);
                    document.add(labelName);
                    TextField createDesc = new TextField("createDesc", this.getNotNullStringValue(labelModelForSearch.getCreateDesc()), Store.YES);
                    createDesc.setBoost(80.0F);
                    document.add(createDesc);
                    TextField busiCaliber = new TextField("busiCaliber", this.getNotNullStringValue(labelModelForSearch.getBusiCaliber()), Store.YES);
                    busiCaliber.setBoost(60.0F);
                    document.add(busiCaliber);
                    StringField publishTime = new StringField("publishTime", this.getNotNullStringValue(labelModelForSearch.getPublishTime()) + "", Store.YES);
                    document.add(publishTime);
                    StringField publishTimeStr = new StringField("publishTimeStr", this.getNotNullStringValue(labelModelForSearch.getPublishTime().replaceAll("[- :]", "")) + "", Store.YES);
                    document.add(publishTimeStr);
                    TextField labelSceneIds = new TextField("labelSceneIds", this.getNotNullStringValue(labelModelForSearch.getLabelSceneIds()), Store.YES);
                    document.add(labelSceneIds);
                    StringField labelSceneNames = new StringField("labelSceneNames", this.getNotNullStringValue(labelModelForSearch.getLabelSceneNames()), Store.YES);
                    document.add(labelSceneNames);
                    StringField currentLabelPath = new StringField("currentLabelPath", this.getNotNullStringValue(labelModelForSearch.getCurrentLabelPath()), Store.YES);
                    document.add(currentLabelPath);
                    StringField labelIdLevelDesc = new StringField("labelIdLevelDesc", this.getNotNullStringValue(labelModelForSearch.getLabelIdLevelDesc()), Store.YES);
                    document.add(labelIdLevelDesc);
                    StringField updateCycle = new StringField("updateCycle", this.getNotNullStringValue(labelModelForSearch.getUpdateCycle()), Store.YES);
                    document.add(updateCycle);
                    StringField updateCycleName = new StringField("updateCycleName", this.getNotNullStringValue(labelModelForSearch.getUpdateCycleName()), Store.YES);
                    document.add(updateCycleName);
                    StringField dataDate = new StringField("dataDate", this.getNotNullStringValue(labelModelForSearch.getDataDate()), Store.YES);
                    document.add(dataDate);
                    StringField customNum = new StringField("customNum", this.getNotNullStringValue(labelModelForSearch.getCustomNum()), Store.YES);
                    document.add(customNum);
                    StringField avgScore = new StringField("avgScore", labelModelForSearch.getAvgScore() == null?"":labelModelForSearch.getAvgScore() + "", Store.YES);
                    document.add(avgScore);
                    StringField useTimes = new StringField("useTimes", this.getNotNullStringValue(labelModelForSearch.getUseTimes()), Store.YES);
                    document.add(useTimes);
                    StringField labelTypeId = new StringField("labelTypeId", labelModelForSearch.getLabelTypeId() == null?"":labelModelForSearch.getLabelTypeId() + "", Store.YES);
                    document.add(labelTypeId);
                    StringField isSysRecom = new StringField("isSysRecom", labelModelForSearch.getIsSysRecom() == null?"0":labelModelForSearch.getIsSysRecom() + "", Store.YES);
                    document.add(isSysRecom);
                    String labelIdT = labelModelForSearch.getLabelId();
                    CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(labelIdT);
                    CiLabelExtInfo ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
                    StringBuffer dimValueSB = new StringBuffer();
                    if(5 == ciLabelInfo.getLabelTypeId().intValue()) {
                        CiMdaSysTableColumn dimValue = ciLabelExtInfo.getCiMdaSysTableColumn();
                        this.appendDimValueStringBuffer(dimValueSB, dimValue);
                    }

                    if(8 == ciLabelInfo.getLabelTypeId().intValue()) {
                        Set dimValue1 = ciLabelExtInfo.getCiLabelVerticalColumnRels();
                        Iterator i$ = dimValue1.iterator();
                        if(i$.hasNext()) {
                            CiLabelVerticalColumnRel r = (CiLabelVerticalColumnRel)i$.next();
                            if(5 == r.getLabelTypeId().intValue()) {
                                CiMdaSysTableColumn ciMdaSysTableColumn = r.getCiMdaSysTableColumn();
                                this.appendDimValueStringBuffer(dimValueSB, ciMdaSysTableColumn);
                            }
                        }
                    }

                    TextField dimValue2 = new TextField("dimValue", this.getNotNullStringValue(dimValueSB.toString()), Store.YES);
                    dimValue2.setBoost(40.0F);
                    document.add(dimValue2);
                    ramWriter.updateDocument(term, document);
                }

                ramWriter.commit();
                fsWriter.addIndexes(new Directory[]{dir});
                ramWriter.close();
                fsWriter.close();
            }
        } catch (Exception var50) {
            this.log.error("新增标签索引失败", var50);
        } finally {
            try {
                if(ramWriter != null) {
                    ramWriter.close();
                }

                if(fsWriter != null) {
                    fsWriter.close();
                }
            } catch (IOException var49) {
                this.log.error("关闭索引writer异常", var49);
                var49.printStackTrace();
            }

        }

    }
}
