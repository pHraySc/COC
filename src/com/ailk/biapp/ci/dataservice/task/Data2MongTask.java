package com.ailk.biapp.ci.dataservice.task;

import com.ailk.biapp.ci.dataservice.service.ICampaign2MongoService;
import com.ailk.biapp.ci.dataservice.service.ICiCustomer2MongoService;
import com.ailk.biapp.ci.dataservice.service.ILabelAndIndex2MongoService;
import com.ailk.biapp.ci.dataservice.task.ExportCampeg2FileThread;
import com.ailk.biapp.ci.dataservice.task.ExportLabelAndIndex2FileThread;
import com.ailk.biapp.ci.entity.CiNewestLabelDate;
import com.ailk.biapp.ci.entity.CiPersonIndexInfo;
import com.ailk.biapp.ci.entity.DimCrmLabelType;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Campaigns;
import com.ailk.biapp.ci.model.CiCustomCampsegRel;
import com.ailk.biapp.ci.model.LabelAndIndexType;
import com.ailk.biapp.ci.model.PeopleLabelInfo;
import com.ailk.biapp.ci.task.AbstractJob;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class Data2MongTask extends AbstractJob {
    public static final byte[] LOCK = new byte[0];
    private static Logger logger = Logger.getLogger(Data2MongTask.class);
    @Autowired
    private ICiCustomer2MongoService ciCustomer2MongoService;
    @Autowired
    private ICampaign2MongoService campaign2MongoService;
    @Autowired
    private ILabelAndIndex2MongoService labelAndIndex2MongoService;
    private ExecutorService pools = Executors.newFixedThreadPool(15);
    private List<Future> fus = new ArrayList();

    public Data2MongTask() {
    }

    public void work() {
        try {
            logger.info("Data2MongTask- start ");
            long e = System.currentTimeMillis();
            byte[] var3 = LOCK;
            synchronized(LOCK) {
                logger.info("Data2MongTask- in lock ");
                String path = null;

                try {
                    path = Configure.getInstance().getProperty("CI", "IMPORT_PATH_FOR_MONGO");
                } catch (Exception var19) {
                    logger.debug("查找文件路径错误", var19);
                    var19.printStackTrace();
                }

                String privDataDate = this.readFirstLineOfFile(path + "datadate");
                List dateList = this.labelAndIndex2MongoService.queryNewestLabelDate();
                String dataDate = null;
                String dataDateDay = null;
                if(dateList != null && dateList.size() > 0) {
                    dataDate = ((CiNewestLabelDate)dateList.get(0)).getMonthNewestDate();
                    dataDateDay = ((CiNewestLabelDate)dateList.get(0)).getDayNewestDate();
                }

                if(StringUtil.isEmpty(dataDate)) {
                    logger.error("没有找到最新月");
                    throw new CIServiceException("没有找到最新月");
                }

                logger.debug("privDataDate:" + privDataDate + ",dataDateDay:" + dataDateDay);
                if(privDataDate.equals(dataDateDay)) {
                    logger.info("no need run ,return");
                    return;
                }

                this.deleteFiles();
                logger.debug("step1");
                List list = this.ciCustomer2MongoService.queryNeedProcessCiCustomCampsegRel();
                logger.debug("list size:" + list.size());
                Iterator labels = list.iterator();

                while(labels.hasNext()) {
                    CiCustomCampsegRel indexes = (CiCustomCampsegRel)labels.next();
                    if(this.ciCustomer2MongoService.process(indexes)) {
                        this.ciCustomer2MongoService.updateDeelStatus(indexes, 1);
                    }
                }

                logger.debug("step2");
                this.ciCustomer2MongoService.toWidthTable();
                this.createPersonCampaigns(path);
                this.createCampaigns(path);
                this.labelAndIndex2MongoService.collectAllTag(dataDate, dataDateDay);
                List labels1 = this.createLabels(path);
                List indexes1 = this.createIndexes(path);
                this.createLabelAndIndexType(path, labels1, indexes1);
                this.createLabelsAndIndexes(path);
                long start2 = System.currentTimeMillis();
                logger.info("export file cost:" + (System.currentTimeMillis() - e));
                logger.info("Data2Mong-> shell-> start ");
                String shellPath = null;

                try {
                    shellPath = Configure.getInstance().getProperty("CI", "MONGO_SHELL_PATH");
                } catch (Exception var18) {
                    logger.error("查找Shell文件路径错误", var18);
                    var18.printStackTrace();
                }

                try {
                    runShell(shellPath);
                } catch (Exception var17) {
                    logger.error("调用Shell出错", var17);
                    var17.printStackTrace();
                }

                this.writeDateFile(path + "datadate", dataDateDay);
                logger.info("Data2Mong-> shell-> end ,cost:" + (System.currentTimeMillis() - start2));
            }

            logger.info("out lock ");
            logger.info("Data2MongTask cost:" + (System.currentTimeMillis() - e));
            logger.info("Data2MongTask- exit ");
        } catch (Exception var21) {
            logger.error("run task error", var21);
        }

    }

    private void createLabelsAndIndexes(String path) {
        logger.info("Data2MongTask-> createLabelsAndIndexes-> start ");
        List list = this.labelAndIndex2MongoService.queryNewestLabelDate();
        String dataDate = null;
        if(list != null && list.size() > 0) {
            dataDate = ((CiNewestLabelDate)list.get(0)).getMonthNewestDate();
        }

        if(StringUtil.isEmpty(dataDate)) {
            logger.error("没有找到最新月");
            throw new CIServiceException("没有找到最新月");
        } else {
            String phone2 = null;

            try {
                phone2 = Configure.getInstance().getProperty("CI", "RELATED_COLUMN");
            } catch (Exception var11) {
                logger.error("没有查询到配置的列名", var11);
                phone2 = "PRODUCT_NO";
            }

            Future f;
            for(int start = 0; start < 10; ++start) {
                StringBuffer sql = new StringBuffer();
                sql.append("select * from CI_PERSON_LABEL_AND_INDEX_").append(dataDate).append(" where ").append(phone2).append(" like \'%").append(start).append("\'");
                String done = "labelsAndIndexes" + start;
                ExportLabelAndIndex2FileThread e = new ExportLabelAndIndex2FileThread(path, done, sql.toString(), phone2, this.labelAndIndex2MongoService);
                f = this.pools.submit(e);
                this.fus.add(f);
            }

            long var12 = System.currentTimeMillis();

            while(true) {
                boolean var13 = true;
                Iterator var14 = this.fus.iterator();

                while(var14.hasNext()) {
                    f = (Future)var14.next();
                    if(!f.isDone()) {
                        var13 = false;
                        break;
                    }
                }

                if(var13) {
                    logger.debug("wait tasks finish cost:" + (System.currentTimeMillis() - var12));
                    return;
                }

                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException var10) {
                    logger.error("sleep error", var10);
                    var10.printStackTrace();
                }
            }
        }
    }

    private void createLabelAndIndexType(String path, List<PeopleLabelInfo> labels, List<CiPersonIndexInfo> indexes) {
        logger.info("Data2MongTask-> createLabelAndIndexType-> start ");
        FileWriter fw = null;

        try {
            String e = createFile(path, "configs", -1);
            File f = new File(e);
            fw = new FileWriter(f, false);
            List types = this.labelAndIndex2MongoService.queryDimCrmLabelTypeList();
            Iterator i$ = types.iterator();

            while(i$.hasNext()) {
                DimCrmLabelType t = (DimCrmLabelType)i$.next();
                LabelAndIndexType type = new LabelAndIndexType();
                type.setId(t.getTypeId());
                type.setClass_name(t.getTypeName());
                ArrayList elements = new ArrayList();
                Iterator i$1 = labels.iterator();

                while(i$1.hasNext()) {
                    PeopleLabelInfo i = (PeopleLabelInfo)i$1.next();
                    if(i.getTypeId().intValue() == t.getTypeId().intValue()) {
                        elements.add(i.getLabelId());
                    }
                }

                i$1 = indexes.iterator();

                while(i$1.hasNext()) {
                    CiPersonIndexInfo i1 = (CiPersonIndexInfo)i$1.next();
                    if(i1.getTypeId().intValue() == t.getTypeId().intValue()) {
                        elements.add(i1.getIndexId());
                    }
                }

                type.setElement_ids(elements);
                fw.write(JsonUtil.toJson(type) + "\t\n");
            }

            fw.flush();
            logger.info("Data2MongTask-> createLabelAndIndexType-> end ");
        } catch (FileNotFoundException var29) {
            logger.error("没有找到文件", var29);
            var29.printStackTrace();
        } catch (UnsupportedEncodingException var30) {
            logger.error("不支持encoding", var30);
            var30.printStackTrace();
        } catch (IOException var31) {
            logger.error("读入流异常", var31);
            var31.printStackTrace();
        } catch (Exception var32) {
            logger.error(var32);
            var32.printStackTrace();
        } finally {
            if(fw != null) {
                try {
                    fw.close();
                    fw = null;
                } catch (IOException var28) {
                    var28.printStackTrace();
                }
            }

        }

    }

    private List<PeopleLabelInfo> createLabels(String path) {
        logger.info("Data2MongTask-> createLabels-> start ");
        FileWriter fw = null;
        List labels = null;

        try {
            String e = createFile(path, "labels", -1);
            File f = new File(e);
            fw = new FileWriter(f, false);
            labels = this.labelAndIndex2MongoService.queryPeopleLabelList();
            Iterator i$ = labels.iterator();

            while(i$.hasNext()) {
                PeopleLabelInfo l = (PeopleLabelInfo)i$.next();
                fw.write(JsonUtil.toJson(l) + "\t\n");
            }

            fw.flush();
            logger.info("Data2MongTask-> createLabels-> end ");
        } catch (FileNotFoundException var23) {
            logger.error("没有找到文件", var23);
            var23.printStackTrace();
        } catch (UnsupportedEncodingException var24) {
            logger.error("不支持encoding", var24);
            var24.printStackTrace();
        } catch (IOException var25) {
            logger.error("读入流异常", var25);
            var25.printStackTrace();
        } catch (Exception var26) {
            logger.error(var26);
            var26.printStackTrace();
        } finally {
            if(fw != null) {
                try {
                    fw.close();
                    fw = null;
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
            }

        }

        return labels;
    }

    private List<CiPersonIndexInfo> createIndexes(String path) {
        logger.info("Data2MongTask-> createIndexes-> start ");
        FileWriter fw = null;
        List indexes = null;

        try {
            String e = createFile(path, "indexes", -1);
            File f = new File(e);
            fw = new FileWriter(f, false);
            indexes = this.labelAndIndex2MongoService.queryPersonIndexList();
            Iterator i$ = indexes.iterator();

            while(i$.hasNext()) {
                CiPersonIndexInfo i = (CiPersonIndexInfo)i$.next();
                fw.write(JsonUtil.toJson(i) + "\t\n");
            }

            fw.flush();
            logger.info("Data2MongTask-> createIndexes-> end ");
        } catch (FileNotFoundException var23) {
            logger.error("没有找到文件", var23);
            var23.printStackTrace();
        } catch (UnsupportedEncodingException var24) {
            logger.error("不支持encoding", var24);
            var24.printStackTrace();
        } catch (IOException var25) {
            logger.error("读入流异常", var25);
            var25.printStackTrace();
        } catch (Exception var26) {
            logger.error(var26);
            var26.printStackTrace();
        } finally {
            if(fw != null) {
                try {
                    fw.close();
                    fw = null;
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
            }

        }

        return indexes;
    }

    private void createCampaigns(String path) {
        logger.info("Data2MongTask-> createCampaigns-> start ");
        FileWriter fw = null;

        try {
            String e = createFile(path, "allCampaigns", -1);
            File f = new File(e);
            fw = new FileWriter(f, false);
            List campseg = this.campaign2MongoService.queryCiCustomCampsegRelList();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Iterator i$ = campseg.iterator();

            while(i$.hasNext()) {
                CiCustomCampsegRel c = (CiCustomCampsegRel)i$.next();
                Campaigns campaigns = new Campaigns(c.getCampsegId(), c.getCampsegName(), sdf.format(c.getApproveEndTime()));
                fw.write(JsonUtil.toJson(campaigns) + "\t\n");
            }

            fw.flush();
            logger.info("Data2MongTask-> createCampaigns-> end ");
        } catch (FileNotFoundException var25) {
            logger.error("没有找到文件", var25);
            var25.printStackTrace();
        } catch (UnsupportedEncodingException var26) {
            logger.error("不支持encoding", var26);
            var26.printStackTrace();
        } catch (IOException var27) {
            logger.error("读入流异常", var27);
            var27.printStackTrace();
        } catch (Exception var28) {
            logger.error(var28);
            var28.printStackTrace();
        } finally {
            if(fw != null) {
                try {
                    fw.close();
                    fw = null;
                } catch (IOException var24) {
                    var24.printStackTrace();
                }
            }

        }

    }

    private void createPersonCampaigns(String path) {
        logger.info("Data2MongTask-> createPersonCampaigns-> start ");
        String fileName = "campaigns";
        ExportCampeg2FileThread campThread = new ExportCampeg2FileThread(path, fileName, "select * from CI_PERSON_CAMPAIGNS", (String)null, this.labelAndIndex2MongoService);
        Future fu = this.pools.submit(campThread);
        this.fus.add(fu);
        logger.info("Data2MongTask-> createPersonCampaigns-> end ");
    }

    private boolean deleteFiles() {
        logger.info("Data2MongTask-> deleteFiles-> start ");
        boolean success = false;

        try {
            String e = Configure.getInstance().getProperty("CI", "IMPORT_PATH_FOR_MONGO");
            if((new File(e)).exists()) {
                ;
            }

            success = true;
        } catch (Exception var3) {
            logger.error("删除目录及文件出错", var3);
            success = false;
        }

        logger.info("Data2MongTask-> deleteFiles-> end ");
        return success;
    }

    public static String createFile(String path, String fileName, int num) {
        String filePath = null;
        if(num < 0) {
            filePath = path + File.separator + fileName + ".txt";
        } else {
            filePath = path + File.separator + fileName + num + ".txt";
        }

        try {
            FileUtil.createFile(filePath, (String)null);
        } catch (Exception var5) {
            logger.error("在MongoDB创建文件:" + filePath + "出错", var5);
        }

        return filePath;
    }

    public void execShell(String shell) {
        try {
            Runtime e = Runtime.getRuntime();
            e.exec(shell);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void runShell(String shStr) throws Exception {
        Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shStr}, (String[])null, (File)null);
        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader(ir);
        String line = null;

        while((line = input.readLine()) != null) {
            logger.info(line);
        }

        process.waitFor();
    }

    private String readFirstLineOfFile(String filePath) {
        File f = new File(filePath);
        logger.debug("file:" + f.getAbsolutePath());
        if(f.exists()) {
            BufferedReader fileReader = null;

            String var5;
            try {
                fileReader = new BufferedReader(new FileReader(f));
                String e = fileReader.readLine();
                fileReader.close();
                if(e == null) {
                    var5 = "";
                    return var5;
                }

                var5 = e.trim();
            } catch (FileNotFoundException var18) {
                logger.debug("file not found:" + f.getAbsolutePath());
                return "";
            } catch (Exception var19) {
                logger.debug("read file error:" + f.getAbsolutePath());
                return "";
            } finally {
                if(fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException var17) {
                        logger.error(var17.getMessage(), var17);
                    }
                }

            }

            return var5;
        } else {
            return "";
        }
    }

    private void writeDateFile(String filePath, String date) {
        File f = new File(filePath);
        logger.debug("file:" + f.getAbsolutePath());
        FileWriter fw = null;

        try {
            fw = new FileWriter(f);
            fw.write(date);
            fw.flush();
        } catch (FileNotFoundException var19) {
            logger.error("没有找到文件");
            var19.printStackTrace();
        } catch (UnsupportedEncodingException var20) {
            logger.error("不支持encoding");
            var20.printStackTrace();
        } catch (IOException var21) {
            logger.error("读入流异常");
            var21.printStackTrace();
        } finally {
            if(fw != null) {
                try {
                    fw.close();
                    fw = null;
                } catch (IOException var18) {
                    var18.printStackTrace();
                }
            }

        }

    }

    public static void main(String[] args) throws Exception {
        runShell("/home/soft/coc" + File.separator + "" + "test" + ".sh");
    }

    static {
        try {
            File configFile = new File(Data2MongTask.class.getResource("/config/aibi_ci/ci-beijing.properties").toURI());
            Configure.getInstance().addConfFileName("CI", configFile.getPath());
            Configure.getInstance().setConfFileName(configFile.getPath());
        } catch (Exception var2) {
            logger.error(" can not init config from /config/aibi_ci/ci-beijing.properties," + var2.getMessage());
        }

    }
}
