package com.ailk.biapp.ci.dataservice.task;

import com.ailk.biapp.ci.dataservice.service.ILabelAndIndex2MongoService;
import com.ailk.biapp.ci.dataservice.task.Data2MongTask;
import com.ailk.biapp.ci.entity.CiPersonCampaigns;
import com.ailk.biapp.ci.model.PersonCampaign;
import com.ailk.biapp.ci.util.JsonUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

public class ExportCampeg2FileThread extends Thread {
    private Logger logger = Logger.getLogger(ExportCampeg2FileThread.class);
    private ILabelAndIndex2MongoService labelAndIndex2MongoService;
    private String path;
    private String fileName;
    private String sql;
    private String phone2;

    public ExportCampeg2FileThread(String path, String fileName, String sql, String phone2, ILabelAndIndex2MongoService labelAndIndex2MongoService) {
        this.path = path;
        this.fileName = fileName;
        this.sql = sql;
        this.phone2 = phone2;
        this.labelAndIndex2MongoService = labelAndIndex2MongoService;
    }

    public void run() {
        this.export2File(this.path, this.fileName, this.sql, this.phone2);
    }

    private void export2File(String path, String fileName, String sql, String phone2) {
        try {
            long e = System.currentTimeMillis();
            String filePath = Data2MongTask.createFile(path, fileName, -1);
            File f = new File(filePath);
            this.logger.debug("file:" + f.getAbsolutePath());
            this.logger.debug("sql:" + sql);
            final BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
            final ParameterizedBeanPropertyRowMapper mapper = ParameterizedBeanPropertyRowMapper.newInstance(CiPersonCampaigns.class);
            this.labelAndIndex2MongoService.execBigSelectSql(sql, new RowCallbackHandler() {
                public void processRow(ResultSet resultset) throws SQLException {
                    try {
                        CiPersonCampaigns e = (CiPersonCampaigns)mapper.mapRow(resultset, 1);
                        PersonCampaign pc = new PersonCampaign();
                        pc.setMobile(e.getProductNo());
                        ArrayList campList = new ArrayList();
                        if(StringUtil.isNotEmpty(e.getCamp0())) {
                            campList.add(e.getCamp0());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp1())) {
                            campList.add(e.getCamp1());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp2())) {
                            campList.add(e.getCamp2());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp3())) {
                            campList.add(e.getCamp3());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp4())) {
                            campList.add(e.getCamp4());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp5())) {
                            campList.add(e.getCamp5());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp6())) {
                            campList.add(e.getCamp6());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp7())) {
                            campList.add(e.getCamp7());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp8())) {
                            campList.add(e.getCamp8());
                        }

                        if(StringUtil.isNotEmpty(e.getCamp9())) {
                            campList.add(e.getCamp9());
                        }

                        pc.setCampaigns(campList);
                        String json = JsonUtil.toJson(pc);
                        bw.write(json + "\t\n");
                    } catch (Exception var6) {
                        ExportCampeg2FileThread.this.logger.error("error", var6);
                    }

                }
            });
            bw.flush();
            bw.close();
            this.logger.info("Data2MongTask-> createPersonCampaigns-> end ");
            this.logger.debug("createPersonCampaigns cost:" + (System.currentTimeMillis() - e));
        } catch (FileNotFoundException var11) {
            this.logger.error("没有找到文件", var11);
            var11.printStackTrace();
        } catch (UnsupportedEncodingException var12) {
            this.logger.error("不支持encoding", var12);
            var12.printStackTrace();
        } catch (IOException var13) {
            this.logger.error("读入流异常", var13);
            var13.printStackTrace();
        } catch (Exception var14) {
            this.logger.error(var14);
            var14.printStackTrace();
        }

    }
}
