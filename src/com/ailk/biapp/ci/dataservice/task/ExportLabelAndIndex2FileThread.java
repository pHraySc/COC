package com.ailk.biapp.ci.dataservice.task;

import com.ailk.biapp.ci.dataservice.service.ILabelAndIndex2MongoService;
import com.ailk.biapp.ci.dataservice.task.Data2MongTask;
import com.ailk.biapp.ci.model.IndexValue;
import com.ailk.biapp.ci.model.PersonLabelsAndIndexes;
import com.ailk.biapp.ci.util.JsonUtil;
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

public class ExportLabelAndIndex2FileThread extends Thread {
    private Logger logger = Logger.getLogger(ExportLabelAndIndex2FileThread.class);
    private ILabelAndIndex2MongoService labelAndIndex2MongoService;
    private String path;
    private String fileName;
    private String sql;
    private String phone2;

    public ExportLabelAndIndex2FileThread(String path, String fileName, String sql, String phone2, ILabelAndIndex2MongoService labelAndIndex2MongoService) {
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
        final String phone = phone2;

        try {
            String e = Data2MongTask.createFile(path, fileName, -1);
            File f = new File(e);
            this.logger.debug("file:" + f.getAbsolutePath());
            this.logger.debug("sql:" + sql);
            final BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
            this.labelAndIndex2MongoService.execBigSelectSql(sql, new RowCallbackHandler() {
                public void processRow(ResultSet resultset) throws SQLException {
                    try {
                        PersonLabelsAndIndexes e = new PersonLabelsAndIndexes();
                        ArrayList labels = new ArrayList();
                        ArrayList indexes = new ArrayList();
                        e.setLabels(labels);
                        e.setIndexes(indexes);
                        int col = 1;

                        String json;
                        while(col <= resultset.getMetaData().getColumnCount()) {
                            json = resultset.getMetaData().getColumnName(col);
                            String value = resultset.getString(col);
                            ++col;
                            if(value != null) {
                                IndexValue iv = new IndexValue();
                                if(json.startsWith("L")) {
                                    if("1".equals(value.trim())) {
                                        labels.add(json);
                                    }
                                } else if(json.startsWith("I")) {
                                    iv.setId(json);
                                    iv.setValue(value);
                                    indexes.add(iv);
                                } else if(json.equalsIgnoreCase(phone)) {
                                    e.setMobile(value);
                                }
                            }
                        }

                        json = JsonUtil.toJson(e);
                        bw.write(json + "\t\n");
                    } catch (Exception var10) {
                        ExportLabelAndIndex2FileThread.this.logger.error("error", var10);
                    }

                }
            });
            bw.flush();
            bw.close();
            this.logger.info("Data2MongTask-> createLabelsAndIndexes-> end ");
        } catch (FileNotFoundException var9) {
            this.logger.error("没有找到文件", var9);
            var9.printStackTrace();
        } catch (UnsupportedEncodingException var10) {
            this.logger.error("不支持encoding", var10);
            var10.printStackTrace();
        } catch (IOException var11) {
            this.logger.error("读入流异常", var11);
            var11.printStackTrace();
        } catch (Exception var12) {
            this.logger.error(var12);
            var12.printStackTrace();
        }

    }
}
