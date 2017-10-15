package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.util.JDBCUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.UnicodeDetector;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CiUtil {
    private static Logger log = Logger.getLogger(CiUtil.class);

    public CiUtil() {
    }

    public static String uploadTargetUserFile(File fFile, String fileName) throws Exception {
        String result = "";
        if(fFile != null && fileName != null && fileName.length() > 0) {
            result = uploadCampFilterFile(fFile, fileName);
        }

        return result;
    }

    public static String getMpmStoreFilePath() {
        String mpmPath = Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
        if(!mpmPath.endsWith(File.separator)) {
            mpmPath = mpmPath + File.separator;
        }

        mpmPath = mpmPath + "ci";
        File pathFile = new File(mpmPath);
        return !pathFile.exists() && !pathFile.mkdirs()?null:mpmPath;
    }

    public static String uploadCampFilterFile(File fFile, String fileName) throws Exception {
        String mpmPath = getMpmStoreFilePath();
        int nFileExtPos = fileName.lastIndexOf(".");
        String fileExt = fileName.substring(nFileExtPos + 1);
        String pureFileName = nFileExtPos < 0?fileName:fileName.substring(0, nFileExtPos);
        pureFileName = pureFileName.replaceAll(" ", "");
        String saveFileName = (pureFileName.length() > 15?pureFileName.substring(0, 15):pureFileName) + "_" + System.currentTimeMillis();
        if(saveFileName.length() > 40) {
            saveFileName = saveFileName.substring(saveFileName.length() - 40);
        }

        saveFileName = saveFileName + "." + fileExt;
        return uploadFile(fFile, mpmPath, saveFileName, false);
    }

    public static String uploadFile(File uploadFile, String storePath, String destFileName, boolean bCleanFolder) throws Exception {
        String remsg;
        try {
            if(storePath != null && storePath.length() >= 1) {
                File e = new File(storePath);
                if(!e.exists()) {
                    e.mkdirs();
                } else if(bCleanFolder) {
                    File[] uploadName = e.listFiles();

                    for(int stream = 0; stream < uploadName.length; ++stream) {
                        uploadName[stream].delete();
                    }
                }

                String var12 = storePath + File.separator + destFileName;
                FileInputStream var13 = new FileInputStream(uploadFile);
                FileOutputStream bos = new FileOutputStream(var12);
                byte[] buffer = new byte[8192];

                int bytesRead;
                while((bytesRead = var13.read(buffer, 0, 8192)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }

                bos.close();
                var13.close();
                return var12;
            } else {
                remsg = "-1";
                return remsg;
            }
        } catch (Exception var11) {
            log.error("", var11);
            remsg = "";
            throw var11;
        }
    }

    public static String convertLongMillsToStrTime(long longMills) {
        Calendar caldTmp = Calendar.getInstance();
        caldTmp.setTimeInMillis(longMills);
        StringBuffer res = (new StringBuffer()).append(caldTmp.get(1)).append("-").append(caldTmp.get(2) + 1).append("-").append(caldTmp.get(5)).append(" ").append(caldTmp.get(11)).append(":").append(caldTmp.get(12)).append(":").append(caldTmp.get(13));
        return res.toString();
    }

    public static String deleteFile(String fileName) {
        String remsg;
        try {
            File e = new File(fileName);
            if(e.exists()) {
                e.delete();
            }

            remsg = "file delete success！";
        } catch (Exception var3) {
            log.error("", var3);
            remsg = "file delete fail！";
        }

        return remsg;
    }

    public static synchronized String convertLongMillsToYYYYMMDDHHMMSS(long longMills) {
        Calendar caldTmp = Calendar.getInstance();
        if(longMills > 0L) {
            caldTmp.setTimeInMillis(longMills);
        } else {
            caldTmp.setTimeInMillis(System.currentTimeMillis());

            try {
                Thread.sleep(1L);
            } catch (InterruptedException var7) {
                log.error("convertLongMillsToYYYYMMDDHHMMSS sleep error");
            }
        }

        StringBuffer res = (new StringBuffer()).append(caldTmp.get(1));
        String tmpStr = String.valueOf(caldTmp.get(2) + 1);
        tmpStr = tmpStr.length() < 2?"0" + tmpStr:tmpStr;
        res.append(tmpStr);
        tmpStr = String.valueOf(caldTmp.get(5));
        tmpStr = tmpStr.length() < 2?"0" + tmpStr:tmpStr;
        res.append(tmpStr);
        res.append(caldTmp.get(11));
        tmpStr = String.valueOf(caldTmp.get(12));
        tmpStr = tmpStr.length() < 2?"0" + tmpStr:tmpStr;
        res.append(tmpStr);
        tmpStr = String.valueOf(caldTmp.get(13));
        tmpStr = tmpStr.length() < 2?"0" + tmpStr:tmpStr;
        res.append(tmpStr);
        tmpStr = String.valueOf(caldTmp.get(14));
        res.append(tmpStr);
        String serverId = Configure.getInstance().getProperty("CURRENT_SERVER_ID");
        if(StringUtil.isNotEmpty(serverId) && serverId.startsWith("S")) {
            String ServerIdCharAfterS = serverId.substring(1);
            res.append(ServerIdCharAfterS);
        }

        return res.toString();
    }

    public static String percentStr(int count, int sum, String fStr) {
        if(count != 0 && sum != 0) {
            DecimalFormat myformat = (DecimalFormat)NumberFormat.getPercentInstance();
            if(fStr != null && fStr.trim().length() != 0) {
                myformat.applyPattern(fStr);
            } else {
                myformat.applyPattern("0");
            }

            double rat = (double)count / (double)sum;
            return myformat.format(rat);
        } else {
            return "0";
        }
    }

    public static String escapeSQLLike(String likeStr) {
        String str = StringUtils.replace(likeStr, "|", "||");
        str = StringUtils.replace(likeStr, "_", "|_");
        str = StringUtils.replace(str, "%", "|%");
        return str;
    }

    public static String formatBigDecimal(BigDecimal dataValue) {
        if(dataValue == null) {
            return "";
        } else {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.applyPattern("##0.##");
            dataValue = dataValue.setScale(2, 4);
            return df.format(dataValue);
        }
    }

    public static String getCellContents4Excel(Cell c) {
        String contents = "";
        if(c != null) {
            CellType ct = c.getType();
            contents = c.getContents();
            if(ct == CellType.DATE) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                DateCell dc = (DateCell)c;
                contents = sdf.format(dc.getDate());
            }
        }

        return contents;
    }

    public static String[] getColors() {
        String[] colors = new String[]{"6F50DE", "71B11A", "F69215", "61A5DD", "7D5DB4", "AE5DB4", "AEB7B4", "CA7F78", "CA7F3D", "B0A1D5", "AA4BD5", "76D7BB", "76D72D", "E10709", "632309", "639B09", "F3F330", "A7A70A", "A73D0A", "00FF00", "FFFF00", "D9D919", "9932CD", "93DB70", "215E21", "4E2F2F", "C0D9D9", "E9C2A6", "426F42", "7F00FF", "7FFF00", "EAADEA", "BC1717", "38B0DE", "CC3299", "5C4033", "FF1CAE", "D8D8BF", "23238E", "A68064", "70DBDB", "E47833", "00FFFF", "5C3317", "B5A642", "871F78", "4F4F2F", "FF7F00", "6B238E", "8F8FBD"};
        return colors;
    }

    public static String getTxtType(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";
        if(head[0] == -1 && head[1] == -2) {
            code = "UTF-16";
        }

        if(head[0] == -2 && head[1] == -1) {
            code = "Unicode";
        }

        if(head[0] == -17 && head[1] == -69 && head[2] == -65) {
            code = "UTF-8";
        }

        inputStream.close();
        return code;
    }

    public static String genCustomAttrColumnName(int index) {
        String prefix = "ATTR_COL_";
        int prefixLength = prefix.length();
        byte length = 13;
        int indexLength = String.valueOf(index).length();
        int zeroNum = length - prefixLength - indexLength;

        for(int i = 0; i < zeroNum; ++i) {
            prefix = prefix + "0";
        }

        prefix = prefix + String.valueOf(index);
        return prefix;
    }

    public static int getChineseCharactersNum(String str) {
        String temp = null;
        Pattern p = Pattern.compile("[一-龥]+");
        Matcher m = p.matcher(str);

        int num;
        for(num = 0; m.find(); num += temp.length()) {
            temp = m.group(0);
        }

        return num;
    }

    public static String subStringByByte(String str, int len, String charset) {
        String result = null;

        try {
            if(str != null) {
                byte[] e = str.getBytes(charset);
                if(e.length <= len) {
                    result = str;
                } else if(len > 0) {
                    result = new String(e, 0, len, charset);
                    int length = result.length();
                    if(str.charAt(length - 1) != result.charAt(length - 1)) {
                        if(length < 2) {
                            result = null;
                        } else {
                            result = result.substring(0, length - 1);
                        }
                    }
                }
            }
        } catch (Exception var6) {
            log.error("按字节截取异常", var6);
        }

        return result;
    }

    public static Charset getFileCharset(File file) {
        long start = System.currentTimeMillis();
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ByteOrderMarkDetector());
        detector.add(JChardetFacade.getInstance());
        detector.add(ASCIIDetector.getInstance());
        detector.add(UnicodeDetector.getInstance());
        Charset charset = Charset.defaultCharset();

        try {
            charset = detector.detectCodepage(new BufferedInputStream(new FileInputStream(file)), 1000);
        } catch (Exception var6) {
            var6.printStackTrace();
            log.error("getFileCharset error", var6);
        }

        System.out.println("getFileCharset cost:" + (System.currentTimeMillis() - start));
        log.info("getFileCharset cost:" + (System.currentTimeMillis() - start));
        return charset;
    }

    public static String queryTempletTableNameKeyType(String templetTableName, String keyColName) throws Exception {
        String type = CommonConstants.MAIN_COLUMN_TYPE;
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = JDBCUtil.getConnection();
            StringBuffer e = new StringBuffer();
            e.append("select ").append(keyColName).append(" from ").append(templetTableName);
            pstm = conn.prepareStatement(e.toString());
            ResultSetMetaData rsd = pstm.executeQuery().getMetaData();
            type = rsd.getColumnTypeName(1) + "(" + rsd.getColumnDisplaySize(1) + ")";
            log.debug("清单主键列类型是：" + type);
        } catch (Exception var45) {
            log.debug("在BACK库里执行插入语句报错", var45);
            throw var45;
        } finally {
            if(pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException var44) {
                    log.debug("关闭PreparedStatement报错", var44);
                } finally {
                    if(conn != null) {
                        try {
                            JDBCUtil.closeConnection();
                        } catch (Exception var43) {
                            log.debug("关闭Connection报错", var43);
                        }
                    }

                }
            }

        }

        return type;
    }
}
