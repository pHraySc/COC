//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiDataSource;
import com.ailk.biapp.ci.entity.CilabelCount;
import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.ziputil.extend.ZipUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.demo.Write;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class FileUtil {
    private static final Logger LOG = Logger.getLogger(FileUtil.class);

    public FileUtil() {
    }

    public static void downLoadFile(HttpServletResponse response, String fileName) {
        response.reset();

        try {
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));
            response.setContentType("application/msexcel;charset=UTF-8");
        } catch (UnsupportedEncodingException var3) {
            LOG.error(var3);
            var3.printStackTrace();
        }

    }

    public static Map<String, String> loadFileToMapByLine(String path) {
        Map<String, String> map = new HashMap();
        try {
            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while ((str = br.readLine()) != null) {
                if (!str.contains(",")) {
                    continue;
                }
                String rl = str.substring(0, str.indexOf(",")).trim();
                map.put(rl, rl);
            }
            br.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void export(HttpServletRequest request, HttpServletResponse response, String header, String content) {
        try {
            ServletOutputStream os = response.getOutputStream();
            exportExcelFile(os, request, response, header, content);
        } catch (IOException var6) {
            LOG.error(var6);
            var6.printStackTrace();
        }

    }

    public static void exportExcelFile(OutputStream os, HttpServletRequest request, HttpServletResponse response, String header, String content) {
        WritableWorkbook wwb = null;
        //创建一个工作薄，就是整个Excel文档

        try {
            wwb = Workbook.createWorkbook(os);  //os作为文件的输出流
            /*
            Workbook不但能用来创建工作薄，也可以读取现有的工作薄，比如：
            Workbook.getWorkbook(java.io.File file);
            Workbook是一个很重要工具类，里面方法基本上都是static的
             */

            WritableSheet e = wwb.createSheet("日志详情", 0);
            //创建工作表，两个参数分别是工作表名字和插入位置

            String[] headerList = header.split(",");
            int headerNum = 0;
            String[] row = headerList;
            int col = headerList.length;

            for (int rowList = 0; rowList < col; ++rowList) {
                String i = row[rowList];
                e.addCell(new Label(headerNum, 0, i));
                ++headerNum;
            }

            int var35 = 1;
            col = 0;
            String[] var36 = content.split("\\|\\|");

            for (int var37 = 0; var37 < var36.length; ++var37) {
                String[] con = var36[var37].toString().split("&&");
                String[] arr$ = con;
                int len$ = con.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    String detail = arr$[i$];
                    e.addCell(new Label(col++, var35, detail));
                }

                ++var35;
                col = 0;
            }

            wwb.write();
        } catch (IOException var31) {
            LOG.error(var31);
            var31.printStackTrace();
        } catch (RowsExceededException var32) {
            LOG.error(var32);
            var32.printStackTrace();
        } catch (WriteException var33) {
            LOG.error(var33);
            var33.printStackTrace();
        } finally {
            try {
                if (wwb != null) {
                    wwb.close();
                }

                if (os != null) {
                    os.close();
                }
            } catch (IOException var30) {
                LOG.error(var30);
                var30.printStackTrace();
            }

        }

    }

    public static void exportToExcel(HttpServletRequest request, HttpServletResponse response, List<CiBrandHisModel> brandHisModelList, List<CiCityHisModel> cityHisModelList, List<CiVipHisModel> vipHisModelList, List<String> date) {
        try {
            ServletOutputStream os = response.getOutputStream();
            writeToExcel(os, brandHisModelList, cityHisModelList, vipHisModelList, date);
        } catch (IOException var8) {
            LOG.error(var8);
            var8.printStackTrace();
        }

    }

    public static void exportDataSourceToExcel(HttpServletRequest request, HttpServletResponse response, List<CiDataSource> dataSourceList) {
        try {
            ServletOutputStream os = response.getOutputStream();
            LOG.info("Sc In 1");
            writeDataSourceToExcel(os, dataSourceList);
            LOG.info("Sc In 2");
        } catch (IOException e) {
            LOG.error("Exception Here!" + e);
            e.printStackTrace();
        }

    }

    public static void exportLabelCountToExcel(HttpServletRequest request, HttpServletResponse response, List labelCountList) {
        try {
            ServletOutputStream os = response.getOutputStream();
            writeLabelCountToExcel(os, labelCountList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeLabelCountToExcel(ServletOutputStream os, List<CilabelCount> labelCountList) throws IOException {
        WritableWorkbook wwb = null;

        int row = 1;
        int col = 0;

        try {
            wwb = Workbook.createWorkbook(os);
            WritableSheet ws = wwb.createSheet("标签统计信息", 0);
            ws.addCell(new Label(0, 0, "一级分类ID"));
            ws.addCell(new Label(1, 0, "一级分类名"));
            ws.addCell(new Label(2, 0, "一级分类状态"));

            ws.addCell(new Label(3, 0, "二级分类ID"));
            ws.addCell(new Label(4, 0, "二级分类名"));
            ws.addCell(new Label(5, 0, "二级分类状态"));

            ws.addCell(new Label(6, 0, "父标签ID"));
            ws.addCell(new Label(7, 0, "父标签名"));
            ws.addCell(new Label(8, 0, "父标签类型"));
            ws.addCell(new Label(9, 0, "父标签业务口径"));
            ws.addCell(new Label(10, 0, "父标签更新周期"));
            ws.addCell(new Label(11, 0, "父标签数据日期"));
            ws.addCell(new Label(12, 0, "父标签用户数"));
            ws.addCell(new Label(13, 0, "父标签创建时间"));

            ws.addCell(new Label(14, 0, "子标签ID"));
            ws.addCell(new Label(15, 0, "子标签名"));
            ws.addCell(new Label(16, 0, "子标签类型"));
            ws.addCell(new Label(17, 0, "子标签业务口径"));
            ws.addCell(new Label(18, 0, "子标签更新周期"));
            ws.addCell(new Label(19, 0, "子标签数据日期"));
            ws.addCell(new Label(20, 0, "子标签用户数"));
            ws.addCell(new Label(21, 0, "子标签创建时间"));


            if (null != labelCountList && labelCountList.size() > 0) {
                for (CilabelCount cilabelCount : labelCountList) {
                    ws.addCell(new Label(col++, row, cilabelCount.getfLevelSortId()));
                    ws.addCell(new Label(col++, row, cilabelCount.getfLevelSortName()));
                    ws.addCell(new Label(col++, row, cilabelCount.getfLevelSortStatus()));

                    ws.addCell(new Label(col++, row, cilabelCount.getsLevelSortId()));
                    ws.addCell(new Label(col++, row, cilabelCount.getsLevelSortName()));
                    ws.addCell(new Label(col++, row, cilabelCount.getsLevelSortStatus()));

                    ws.addCell(new Label(col++, row, cilabelCount.getDadLabelId()));
                    ws.addCell(new Label(col++, row, cilabelCount.getDadLabelName()));
                    ws.addCell(new Label(col++, row, cilabelCount.getDadLabelType()));
                    ws.addCell(new Label(col++, row, cilabelCount.getDadLabelBusiCaliber()));
                    ws.addCell(new Label(col++, row, cilabelCount.getDadLabelUpdateCycle()));
                    ws.addCell(new Label(col++, row, cilabelCount.getDadLabelDataDate()));
                    ws.addCell(new Label(col++, row, cilabelCount.getDadLabelCustomNum()));
                    ws.addCell(new Label(col++, row, cilabelCount.getDadLabelCreateTime()));

                    ws.addCell(new Label(col++, row, cilabelCount.getSonLabelId()));
                    ws.addCell(new Label(col++, row, cilabelCount.getSonLabelName()));
                    ws.addCell(new Label(col++, row, cilabelCount.getSonLabelType()));
                    ws.addCell(new Label(col++, row, cilabelCount.getSonLabelBusiCaliber()));
                    ws.addCell(new Label(col++, row, cilabelCount.getSonLabelUpdateCycle()));
                    ws.addCell(new Label(col++, row, cilabelCount.getSonLabelDataDate()));
                    ws.addCell(new Label(col++, row, cilabelCount.getSonLabelCustomNum()));
                    ws.addCell(new Label(col++, row, cilabelCount.getSonLabelCreateTime()));

                    row++;

                    col = 0;
                }
            }

            wwb.write();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeDataSourceToExcel(OutputStream os, List dataSourceList) {
        WritableWorkbook wwb = null;

        int row = 1;
        int col = 0;
        try {
            wwb = Workbook.createWorkbook(os);
            LOG.info("Sc In 3");
            WritableSheet ws = wwb.createSheet("数据源信息", 0);
            ws.addCell(new Label(0, 0, "标签名"));
            ws.addCell(new Label(1, 0, "标签ID"));
            ws.addCell(new Label(2, 0, "数据源ID"));
            ws.addCell(new Label(3, 0, "数据源表名"));
            ws.addCell(new Label(6, 0, "最近更新时间"));
            ws.addCell(new Label(4, 0, "提供源表对应联系人"));
            ws.addCell(new Label(5, 0, "是否缺失"));
            LOG.info("Sc In q");

            int index;
            Map<String, Object> dataSource;
            if (dataSourceList != null && dataSourceList.size() > 0) {
                for (index = 0; index < dataSourceList.size(); index++) {
                    dataSource = (Map) dataSourceList.get(index);
                    ws.addCell(new Label(col++, row, (String) dataSource.get("label_name")));
                    ws.addCell(new Label(col++, row, (String) dataSource.get("label_id")));
                    ws.addCell(new Label(col++, row, (String) dataSource.get("data_src_code")));
                    ws.addCell(new Label(col++, row, (String) dataSource.get("data_src_code")));
                    ws.addCell(new Label(col++, row, (String) dataSource.get("data_src_tab_name")));
                    ws.addCell(new Label(col++, row, (String) dataSource.get("data_date")));
                    LOG.info("Sc In w");
                    ++row;

                    col = 0;
                }
            }
//            int k = 1;
//            if(null != dataSourceList && dataSourceList.size() > 0) {
//                for (Iterator<CiDataSource> i = dataSourceList.iterator(); i.hasNext(); ++k) {
//                    CiDataSource dataSource = i.next();
//
//                    ws.addCell(new Label(col++, row, dataSource.getLabelName().toString()));
//                    ws.addCell(new Label(col++, row, dataSource.getLabelId().toString()));
//                    ws.addCell(new Label(col++, row, dataSource.getDataSrcCode().toString()));
//                    ws.addCell(new Label(col++, row, dataSource.getDataSrcTabName().toString()));
//                    ws.addCell(new Label(col++, row, dataSource.getContactTodataSourceTable().toString()));
//                    ws.addCell(new Label(col++, row, dataSource.getIsMissing().toString()));
//
//                    ++row;
//
//                    col = 0;
//                }
//            }
            LOG.info("Sc In 4");
            wwb.write();
            LOG.info("Sc In 5");
        } catch (IOException e) {
            LOG.error("Exception Here!" + e);
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeToExcel(OutputStream os, List<CiBrandHisModel> brandHisModelList, List<CiCityHisModel> cityHisModelList, List<CiVipHisModel> vipHisModelList, List<String> date) {
        WritableWorkbook wwb = null;

        try {
            int e = 1;
            int col = 0;
            wwb = Workbook.createWorkbook(os);
            WritableSheet ws1 = wwb.createSheet("品牌", 0);
            WritableSheet ws2 = wwb.createSheet("分公司", 1);
            WritableSheet ws3 = wwb.createSheet("VIP等级", 2);
            ws1.addCell(new Label(0, 0, "品牌"));
            ws2.addCell(new Label(0, 0, "分公司"));
            ws3.addCell(new Label(0, 0, "VIP等级"));
            //三个参数分别表示col+1列，row+1行，标题内容是title
            //再将创建好的标签添加到工作表中addCell，这里标签就是对应的每个单元格的内容
            int k = 1;

            for (Iterator i = date.iterator(); i.hasNext(); ++k) {
                String model = (String) i.next();
                if (null != model && model.length() == 6) {
                    model = model.substring(0, 4) + "-" + model.substring(4, 6);
                } else if (null != model && model.length() == 8) {
                    model = model.substring(0, 4) + "-" + model.substring(4, 6) + "-" + model.substring(6, 8);
                }

                ws1.addCell(new Label(k, 0, model));
                ws2.addCell(new Label(k, 0, model));
                ws3.addCell(new Label(k, 0, model));
            }

            int var32;
            if (brandHisModelList != null && brandHisModelList.size() > 0) {
                for (var32 = 0; var32 < brandHisModelList.size(); ++var32) {
                    CiBrandHisModel var33 = (CiBrandHisModel) brandHisModelList.get(var32);
                    if (var33.getParentId().intValue() == -1) {
                        ws1.addCell(new Label(col++, e, IdToName.getName("DIM_BRAND", var33.getBrandId())));
                    }

                    if (var33.getParentId().intValue() != -1) {
                        ws1.addCell(new Label(col++, e, "    " + IdToName.getName("DIM_BRAND", var33.getBrandId())));
                    }

                    ws1.addCell(new Label(col++, e, var33.getHis1().toString()));
                    ws1.addCell(new Label(col++, e, var33.getHis2().toString()));
                    ws1.addCell(new Label(col++, e, var33.getHis3().toString()));
                    ws1.addCell(new Label(col++, e, var33.getHis4().toString()));
                    ws1.addCell(new Label(col++, e, var33.getHis5().toString()));
                    ws1.addCell(new Label(col++, e, var33.getHis6().toString()));
                    ++e;
                    col = 0;
                }
            }

            e = 1;
            byte var31 = 0;
            if (cityHisModelList != null && cityHisModelList.size() > 0) {
                for (var32 = 0; var32 < cityHisModelList.size(); ++var32) {
                    CiCityHisModel var34 = (CiCityHisModel) cityHisModelList.get(var32);
                    col = var31 + 1;
                    ws2.addCell(new Label(var31, e, IdToName.getName("DIM_CITY", var34.getCityId())));
                    ws2.addCell(new Label(col++, e, var34.getHis1().toString()));
                    ws2.addCell(new Label(col++, e, var34.getHis2().toString()));
                    ws2.addCell(new Label(col++, e, var34.getHis3().toString()));
                    ws2.addCell(new Label(col++, e, var34.getHis4().toString()));
                    ws2.addCell(new Label(col++, e, var34.getHis5().toString()));
                    ws2.addCell(new Label(col++, e, var34.getHis6().toString()));
                    ++e;
                    var31 = 0;
                }
            }

            e = 1;
            var31 = 0;
            if (vipHisModelList != null && vipHisModelList.size() > 0) {
                for (var32 = 0; var32 < vipHisModelList.size(); ++var32) {
                    CiVipHisModel var35 = (CiVipHisModel) vipHisModelList.get(var32);
                    col = var31 + 1;
                    ws3.addCell(new Label(var31, e, IdToName.getName("DIM_VIP_LEVEL", var35.getVipLevelId())));
                    ws3.addCell(new Label(col++, e, var35.getHis1().toString()));
                    ws3.addCell(new Label(col++, e, var35.getHis2().toString()));
                    ws3.addCell(new Label(col++, e, var35.getHis3().toString()));
                    ws3.addCell(new Label(col++, e, var35.getHis4().toString()));
                    ws3.addCell(new Label(col++, e, var35.getHis5().toString()));
                    ws3.addCell(new Label(col++, e, var35.getHis6().toString()));
                    ++e;
                    var31 = 0;
                }
            }

            wwb.write();
        } catch (IOException var27) {
            LOG.error(var27);
            var27.printStackTrace();
        } catch (RowsExceededException var28) {
            LOG.error(var28);
            var28.printStackTrace();
        } catch (WriteException var29) {
            LOG.error(var29);
            var29.printStackTrace();
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (IOException var26) {
                LOG.error(var26);
                var26.printStackTrace();
            }

        }

    }

    public static boolean list2File(List<Map<String, Object>> datas, String title, String columns, String fileName, String encode, String dimCols, String waterMarkCode) {
        LOG.debug("The size of datas is " + datas.size() + ";titles=" + title + ";columns=" + columns + ";fileName=" + fileName + ";encode=" + encode + ";newTile=" + title);
        boolean flag = false;
        if (fileName.toLowerCase().endsWith("csv") || fileName.toLowerCase().endsWith("txt")) {
            flag = writeToTextFile(datas, title, columns, fileName, encode, dimCols);
        }

        return flag;
    }

    public static boolean list2Txt(List<Map<String, Object>> datas, String title, String columns, String fileName, String encode, String dimCols, String waterMarkCode) {
        LOG.debug(" The size of datas is " + datas.size() + ";titles = " + title + ";columns = " + columns + ";fileName = " + fileName + ";encode = " + encode + ";newTitle = " + title);
        boolean flag = false;
        flag = writeToTxt(datas, title, columns, fileName, encode, dimCols);
        return flag;
    }

    private static boolean writeToTxt(List<Map<String, Object>> datas, String title, String columns, String fileName, String encode, String dimCols) {
        boolean flag = true;
        BufferedWriter osw = null;
        short bufferdsize = 2048;

        try {
            createLocDir(fileName);
            boolean e = (new File(fileName)).exists();
            osw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), encode), bufferdsize);
            if (!e) {
                ArrayList data = new ArrayList();
                int dataNum = 0;

                for (Iterator i$ = datas.iterator(); i$.hasNext(); osw.flush()) {
                    Map m = (Map) i$.next();
                    String[] txt = columns.split(",");
                    int cycleNum = txt.length;

                    String o;
                    for (int i$1 = 0; i$1 < cycleNum; ++i$1) {
                        o = txt[i$1];
                        data.add(m.get(o.toUpperCase()) == null ? "" : m.get(o.toUpperCase()).toString());
                    }

                    if (data.size() > 0) {
                        StringBuffer var29 = new StringBuffer();
                        cycleNum = 0;

                        for (Iterator var30 = data.iterator(); var30.hasNext(); ++cycleNum) {
                            o = (String) var30.next();
                            if (cycleNum > 0) {
                                var29.append("|");
                            }

                            var29.append(o == null ? "" : o.toString());
                        }

                        osw.write(var29.toString());
                        ++dataNum;
                        if (dataNum < datas.size()) {
                            osw.write("\n");
                        }

                        data.clear();
                    }
                }
            }
        } catch (Exception var27) {
            flag = false;
            LOG.error("createFile(" + fileName + ") error:", var27);
            var27.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (Exception var26) {
                var26.printStackTrace();
            }

        }

        return flag;
    }

    private static boolean writeToTextFile(List<Map<String, Object>> datas, String title, String columns, String fileName, String encode, String dimCols) {
        boolean flag = true;
        CsvListWriter cw = null;
        BufferedWriter osw = null;
        short bufferdsize = 2048;

        try {
            createLocDir(fileName);
            boolean e = (new File(fileName)).exists();
            osw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), encode), bufferdsize);
            cw = new CsvListWriter(osw, CsvPreference.STANDARD_PREFERENCE);
            if (!e) {
                if (StringUtils.isNotEmpty(title)) {
                    cw.write(title.split(","));
                } else {
                    cw.write(columns.split(","));
                }
            }

            ArrayList data = new ArrayList();

            for (Iterator i$ = datas.iterator(); i$.hasNext(); cw.flush()) {
                Map m = (Map) i$.next();
                String[] arr$ = columns.split(",");
                int len$ = arr$.length;

                for (int i$1 = 0; i$1 < len$; ++i$1) {
                    String col = arr$[i$1];
                    if (m.get(col.toUpperCase()) != null) {
                        data.add(m.get(col.toUpperCase()).toString());
                    } else {
                        data.add("");
                    }
                }

                if (data.size() > 0) {
                    cw.write((String[]) data.toArray(new String[0]));
                    data.clear();
                }
            }
        } catch (Exception var27) {
            flag = false;
            LOG.error("createFile(" + fileName + ") error:", var27);
            var27.printStackTrace();
        } finally {
            try {
                if (cw != null) {
                    cw.close();
                }
            } catch (Exception var26) {
                var26.printStackTrace();
            }

        }

        return flag;
    }

    public static boolean sql2File(String sql, String ds, Object[] params, String title, String columns, String fileName, String encode, String dimCols, int pageSize, boolean split, boolean quote, String waterMarkCode) {
        LOG.debug("sql= " + sql + ";titles=" + title + ";ds=" + ds + ";columns=" + columns + ";fileName=" + fileName + ";encode=" + encode);
        long t1 = System.currentTimeMillis();
        boolean flag = true;

        try {
            JdbcBaseDao e = (JdbcBaseDao) SystemServiceLocator.getInstance().getService("jdbcBaseDao");
            int start = 1;

            while (true) {
                String pagedSql = e.getBackDataBaseAdapter().getPagedSql(sql, start, pageSize);
                LOG.debug("sql2File==>pagedSQL: " + pagedSql);
                List datas = e.getBackSimpleJdbcTemplate().queryForList(pagedSql, params);
                if (datas == null) {
                    break;
                }

                String file = fileName;
                if (split) {
                    if (datas.size() < pageSize && start == 1) {
                        file = fileName;
                    } else {
                        file = fileName.replace(".", "_" + start + ".");
                    }
                }

                flag = list2File(datas, title, columns, file, encode, dimCols, waterMarkCode);
                if (datas.size() < pageSize || !flag) {
                    break;
                }

                ++start;
            }
        } catch (Exception var24) {
            flag = false;
            LOG.error("createFile(" + fileName + ") error:", var24);
            var24.printStackTrace();
        } finally {
            LOG.info("The cost of sql2File is :  " + (System.currentTimeMillis() - t1) + "ms");
        }

        return flag;
    }

    public static boolean sql2Txt(String sql, String jndiName, Object[] params, final String title, final String columns, final String fileName, final String encode, final String dimCols, boolean quote, final String waterMarkCode, final int bufferedRowSize) {
        LOG.debug("sql2Txt:sql = " + sql + ";titles = " + title + ";jndiName = " + jndiName + ";columns = " + columns + ";fileName = " + fileName + ";encode = " + encode);
        long t1 = System.currentTimeMillis();
        boolean flag = true;

        try {
            JdbcBaseDao e = (JdbcBaseDao) SystemServiceLocator.getInstance().getService("jdbcBaseDao");
            JdbcTemplate jt = e.getJdbcTemplate(jndiName);
            final ColumnMapRowMapper mapper = new ColumnMapRowMapper();
            if (bufferedRowSize <= 0) {
                LOG.warn("bufferedRowSize <= 0 , use default 1024 .");
                // bufferedRowSize = 1024;
            }

            final ArrayList datas = new ArrayList();
            jt.query(sql, new RowCallbackHandler() {
                public void processRow(ResultSet rs) throws SQLException {
                    if (rs == null) {
                        FileUtil.LOG.error("ResultSet is NULL");
                    }

                    try {
                        Map e = mapper.mapRow(rs, 1);
                        if (e == null) {
                            FileUtil.LOG.error("row is NULL");
                        }

                        if (datas.size() < bufferedRowSize) {
                            datas.add(e);
                        } else {
                            datas.add(e);
                            FileUtil.list2Txt(datas, title, columns, fileName, encode, dimCols, waterMarkCode);
                            datas.clear();
                        }

                    } catch (Exception var3) {
                        FileUtil.LOG.error("spring mapRow转换错误", var3);
                        throw new SQLException("spring mapRow转换错误", var3);
                    }
                }
            });
            if (datas.size() > 0) {
                list2Txt(datas, title, columns, fileName, encode, dimCols, waterMarkCode);
                datas.clear();
            }
        } catch (Exception var24) {
            flag = false;
            LOG.error("createFile(" + fileName + ") error:", var24);
            var24.printStackTrace();
        } finally {
            LOG.info("The cost of sql2File2 is :  " + (System.currentTimeMillis() - t1) + "ms");
        }

        return flag;
    }

    public static boolean sql2File(String sql, String jndiName, Object[] params, final String title, final String columns, final String fileName, final String encode, final String dimCols, boolean quote, final String waterMarkCode, final int bufferedRowSize) {
        LOG.debug("sql2File2:sql= " + sql + ";titles=" + title + ";jndiName=" + jndiName + ";columns=" + columns + ";fileName=" + fileName + ";encode=" + encode);
        long t1 = System.currentTimeMillis();
        boolean flag = true;

        try {
            JdbcBaseDao e = (JdbcBaseDao) SystemServiceLocator.getInstance().getService("jdbcBaseDao");
            JdbcTemplate jt = e.getJdbcTemplate(jndiName);
            final ColumnMapRowMapper mapper = new ColumnMapRowMapper();
            if (bufferedRowSize <= 0) {
                LOG.warn("bufferedRowSize <=0 , use default 1024 .");
                // bufferedRowSize = 1024;
            }

            final ArrayList datas = new ArrayList();
            jt.query(sql, new RowCallbackHandler() {
                public void processRow(ResultSet rs) throws SQLException {
                    if (rs == null) {
                        FileUtil.LOG.error("ResultSet is NULL");
                    }

                    try {
                        Map e = mapper.mapRow(rs, 1);
                        if (e == null) {
                            FileUtil.LOG.error("row is NULL");
                        }

                        if (datas.size() < bufferedRowSize) {
                            datas.add(e);
                        } else {
                            datas.add(e);
                            FileUtil.list2File(datas, title, columns, fileName, encode, dimCols, waterMarkCode);
                            datas.clear();
                        }

                    } catch (Exception var3) {
                        FileUtil.LOG.error("spring mapRow转换错误", var3);
                        throw new SQLException("spring mapRow转换错误", var3);
                    }
                }
            });
            if (datas.size() > 0) {
                list2File(datas, title, columns, fileName, encode, dimCols, waterMarkCode);
                datas.clear();
            }
        } catch (Exception var24) {
            flag = false;
            LOG.error("createFile(" + fileName + ") error:", var24);
            var24.printStackTrace();
        } finally {
            LOG.info("The cost of sql2File2 is :  " + (System.currentTimeMillis() - t1) + "ms");
        }

        return flag;
    }

    public static File createLocDir(String fileName) throws Exception {
        File f = new File(fileName);
        File dir = null;
        if (f != null && !f.exists()) {
            if (f.isDirectory()) {
                dir = f;
            } else {
                dir = f.getParentFile();
            }

            if (dir != null && !dir.exists()) {
                boolean result = dir.mkdirs();
                if (!result) {
                    LOG.error("can not mkdir [" + dir + "] ,please check OS User\'S privilege!");
                    throw new Exception("can not mkdir [" + dir + "] ,please check OS User\'S privilege!");
                }
            }
        } else {
            dir = f.isFile() ? f.getParentFile() : f;
        }

        return dir;
    }

    public static void createFile(String fileName, String content) throws Exception {
        if (StringUtils.isEmpty(fileName)) {
            throw new Exception("file name is null!");
        } else {
            File f = new File(fileName);
            if (f.exists()) {
                throw new Exception("The file " + fileName + " has exists!");
            } else {
                createLocDir(fileName);
                if (f.getName().contains(".")) {
                    if (StringUtils.isEmpty(content)) {
                        f.createNewFile();
                    } else {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                        bw.append(content.replace("\\n", System.getProperty("line.separator")));
                        bw.flush();
                        bw.close();
                    }
                }

            }
        }
    }

    public static void deleteFile(String fileName, boolean onlyChild) throws Exception {
        if (StringUtils.isEmpty(fileName)) {
            LOG.info("file name is null!");
        }

        File f = new File(fileName);
        if (!f.exists()) {
            LOG.warn("The file or directory " + fileName + " not exists!");
        }

        if (f.isFile()) {
            if (!f.delete()) {
                LOG.warn("delete file[" + fileName + "] fail!Please check it!");
            }
        } else {
            File[] arr$ = f.listFiles();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                File ff = arr$[i$];
                deleteFile(ff.getAbsolutePath(), false);
            }

            if (!onlyChild && !f.delete()) {
                LOG.warn("delete file[" + fileName + "] fail!Please check it!");
            }
        }

    }

    public static void renameFile(String fileName, String toFileName) throws Exception {
        if (!StringUtils.isEmpty(fileName) && !StringUtils.isEmpty(toFileName)) {
            File f = new File(fileName);
            if (!f.exists()) {
                throw new Exception("The file or directory " + fileName + " not exists!");
            } else {
                File tf = new File(toFileName);
                if (f.isFile() && !tf.getName().contains(".") || f.isDirectory() && tf.getName().contains(".")) {
                    throw new Exception("The type of fileName[" + fileName + "] and toFileName[" + toFileName + "] must be identical!");
                } else if (!f.renameTo(tf)) {
                    throw new Exception("rename[from=" + fileName + ";to=" + toFileName + "] fail!Please check it!");
                }
            }
        } else {
            throw new Exception("The filename or tofilename is null!");
        }
    }

    public static File[] getFileList(String fileDir) throws Exception {
        File dir = new File(fileDir);
        ArrayList files = new ArrayList();
        if (dir.isDirectory()) {
            File[] arr$ = dir.listFiles();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                File f = arr$[i$];
                if (f.isFile() && !f.isHidden()) {
                    files.add(f);
                }
            }
        } else {
            LOG.warn(fileDir + " is not dir!");
        }

        return (File[]) files.toArray(new File[0]);
    }

    public static byte[] readFileByte(File file) throws Exception {
        FileInputStream fis = null;
        FileChannel fc = null;
        Object data = null;

        byte[] data1;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            data1 = new byte[(int) fc.size()];
            fc.read(ByteBuffer.wrap(data1));
        } catch (Exception var16) {
            LOG.error("readFileByte error:", var16);
            var16.printStackTrace();
            throw var16;
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }

        }

        return data1;
    }

    public static byte[] readFileByte(String filename) throws Exception {
        if (StringUtils.isEmpty(filename)) {
            LOG.warn("Invalid file name: " + filename);
            return null;
        } else {
            File file = new File(filename);
            BufferedInputStream bufferedInputStream = null;
            long len = file.length();
            byte[] bytes = new byte[(int) len];

            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                int e = bufferedInputStream.read(bytes);
                if ((long) e != len) {
                    throw new IOException("read file[" + filename + "] error");
                }
            } catch (Exception var11) {
                LOG.error("read file bytes error:", var11);
                throw var11;
            } finally {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }

            }

            return bytes;
        }
    }

    public static boolean writeByteFile(byte[] bytes, File file) throws Exception {
        FileOutputStream fos = null;
        boolean flag = true;

        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (FileNotFoundException var16) {
            flag = false;
            LOG.error("writeByteFile error:", var16);
            var16.printStackTrace();
        } catch (IOException var17) {
            flag = false;
            LOG.error("writeByteFile error:", var17);
            var17.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

        }

        return flag;
    }

    public static Boolean unZip(String unzipfile, String outPath) {
        try {
            ZipInputStream e = new ZipInputStream(new FileInputStream(unzipfile));

            while (true) {
                ZipEntry entry;
                while ((entry = e.getNextEntry()) != null) {
                    File myFile;
                    if (entry.isDirectory()) {
                        myFile = new File(outPath, entry.getName());
                        if (!myFile.exists() && !myFile.mkdirs()) {
                            return Boolean.valueOf(false);
                        }

                        e.closeEntry();
                    } else {
                        myFile = new File(entry.getName());
                        FileOutputStream fout = new FileOutputStream(outPath + myFile.getPath());
                        DataOutputStream dout = new DataOutputStream(fout);
                        byte[] b = new byte[1024];
                        boolean len = false;

                        int len1;
                        while ((len1 = e.read(b)) != -1) {
                            dout.write(b, 0, len1);
                        }

                        dout.close();
                        fout.close();
                        e.closeEntry();
                    }
                }

                return Boolean.valueOf(true);
            }
        } catch (IOException var9) {
            var9.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    public static void moveFile(String from, String toDir, String errDir) throws Exception {
        try {
            File e = new File(toDir);
            if (!e.exists()) {
                boolean erDir = e.mkdirs();
                if (!erDir) {
                    throw new Exception("can not mkdir [" + e + "] ,please check OS User\'S privilege!");
                }
            } else if (e.isFile()) {
                throw new Exception("The dest dir[" + toDir + "] must be directory!");
            }

            String var13 = StringUtils.isEmpty(errDir) ? toDir + "/errors" : errDir;
            File f = new File(from);
            File[] fs = null;
            if (!f.exists()) {
                throw new Exception("file " + from + " not exists!");
            } else {
                if (f.isFile()) {
                    fs = new File[]{f};
                } else {
                    fs = f.listFiles();
                }

                File[] arr$ = fs;
                int len$ = fs.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    File file = arr$[i$];
                    if (file.isDirectory()) {
                        moveFile(file.getAbsolutePath(), toDir + File.separator + file.getName(), var13);
                        file.delete();
                        LOG.debug(file.getName() + " deleted");
                    } else {
                        File moveFile = new File(toDir + File.separator + file.getName());
                        if (moveFile.exists()) {
                            moveFileToErrDir(moveFile, errDir);
                        }

                        file.renameTo(moveFile);
                    }
                }

            }
        } catch (Exception var12) {
            LOG.error("moveFile error[fromDir=" + from + ";toDir=" + toDir + ";errDir=" + errDir + "]:", var12);
            var12.printStackTrace();
            throw var12;
        }
    }

    private static void moveFileToErrDir(File moveFile, String errDir) throws Exception {
        int i = 0;

        String errFile;
        for (errFile = errDir + File.separator + "rnError" + moveFile.getName(); (new File(errFile)).exists(); errFile = errDir + File.separator + i + "rnError" + moveFile.getName()) {
            ++i;
        }

        moveFile.renameTo(new File(errFile));
        LOG.debug(moveFile.getName() + " to errDir:" + errDir);
    }

    public static byte[] getFileByte(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);

        try {
            copy(in, out);
        } catch (IOException var3) {
            LOG.error("getFileByte error：", var3);
            var3.printStackTrace();
        }

        return out.toByteArray();
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] e = new byte[4096];
            boolean nrOfBytes = true;

            int nrOfBytes1;
            while ((nrOfBytes1 = in.read(e)) != -1) {
                out.write(e, 0, nrOfBytes1);
            }

            out.flush();
        } catch (IOException var17) {
            ;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException var16) {
                ;
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException var15) {
                ;
            }

        }

    }

    public static void zipFileByPassword(String srcFile, String destFile, String password, String encode) throws Exception {
        File file = null;
        File destZipFile = null;
        File[] files = null;
        if (!StringUtils.isEmpty(srcFile) && !StringUtils.isEmpty(destFile)) {
            if (StringUtils.isEmpty(password)) {
                throw new Exception("password is null!");
            } else {
                file = new File(srcFile);
                destZipFile = new File(destFile);
                if (!file.exists()) {
                    throw new Exception("source file[" + srcFile + "] not exists!");
                } else {
                    if (!file.isDirectory()) {
                        files = new File[]{file};
                    } else {
                        files = getFileList(srcFile);
                    }

                    if (destZipFile.isDirectory()) {
                        throw new Exception("dest file[" + destFile + "] must be file,but directory!");
                    } else {
                        try {
                            createLocDir(destFile);
                            byte[] e = ZipUtil.getEncryptZipByte(files, password, encode);
                            writeByteFile(e, new File(destFile));
                        } catch (Exception var8) {
                            if (destZipFile != null && destZipFile.exists()) {
                                destZipFile.delete();
                            }

                            LOG.error("zipFileByPassword error:", var8);
                            var8.printStackTrace();
                            throw var8;
                        }
                    }
                }
            }
        } else {
            throw new Exception("source file\'s directory or dest zip file is null!");
        }
    }

    public static void zipFileUnPassword(String srcFile, String destFile, String encode) throws Exception {
        File file = null;
        File destZipFile = null;
        if (!StringUtils.isEmpty(srcFile) && !StringUtils.isEmpty(destFile)) {
            file = new File(srcFile);
            destZipFile = new File(destFile);
            if (!file.exists()) {
                LOG.error("source file[" + srcFile + "] not exists!");
                throw new Exception("source file[" + srcFile + "] not exists!");
            } else if (destZipFile.isDirectory()) {
                LOG.error("dest file[" + destFile + "] must be file,but directory!");
                throw new Exception("dest file[" + destFile + "] must be file,but directory!");
            } else {
                long t1 = System.currentTimeMillis();
                createLocDir(destFile);
                BufferedOutputStream os = null;
                ZipOutputStream zipOut = null;
                org.apache.tools.zip.ZipEntry entry = null;
                short bufferSize = 2048;
                BufferedInputStream in = null;

                try {
                    os = new BufferedOutputStream(new FileOutputStream(destZipFile), bufferSize);
                    zipOut = new ZipOutputStream(os);
                    zipOut.setEncoding(encode);
                    File[] e = null;
                    if (file.isFile()) {
                        e = new File[]{file};
                    } else {
                        e = getFileList(srcFile);
                    }

                    File[] arr$ = e;
                    int len$ = e.length;

                    for (int i$ = 0; i$ < len$; ++i$) {
                        File f = arr$[i$];
                        entry = new org.apache.tools.zip.ZipEntry(f.getName());
                        zipOut.putNextEntry(entry);
                        in = new BufferedInputStream(new FileInputStream(f), bufferSize);
                        byte[] data = new byte[bufferSize];

                        int count;
                        while ((count = in.read(data, 0, bufferSize)) != -1) {
                            zipOut.write(data, 0, count);
                        }

                        zipOut.closeEntry();
                        in.close();
                    }

                    zipOut.flush();
                } catch (Exception var23) {
                    if (destZipFile != null && destZipFile.exists()) {
                        destZipFile.delete();
                    }

                    LOG.error("zipFileUnPassword error:", var23);
                    var23.printStackTrace();
                    throw var23;
                } finally {
                    LOG.debug("The cost of compressing files :  " + (System.currentTimeMillis() - t1) + "ms");
                    zipOut.close();
                    os.close();
                }

            }
        } else {
            LOG.error("source file or dest zip file is null!");
            throw new Exception("source file or dest zip file is null!");
        }
    }

    public static boolean zipFile(String srcFile, String destFile, String password, String encode) {
        boolean flag = true;

        try {
            if (StringUtils.isEmpty(password)) {
                zipFileUnPassword(srcFile, destFile, encode);
            } else {
                zipFileByPassword(srcFile, destFile, password, encode);
            }
        } catch (Exception var6) {
            flag = false;
            LOG.error("zipFile[srcFile=" + srcFile + ";destFile=" + destFile + ";password=" + password + "] error: ", var6);
            var6.printStackTrace();
        }

        return flag;
    }


}
