package com.ailk.biapp.ci.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

public class MD5Util {
    private static final Logger log = Logger.getLogger(MD5Util.class);
    protected static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static MessageDigest messagedigest = null;

    public MD5Util() {
    }

    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    public static String getFileMD5String(File file) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            byte[] e = new byte[1024];
            boolean numRead = false;

            int numRead1;
            while((numRead1 = fis.read(e)) > 0) {
                messagedigest.update(e, 0, numRead1);
            }
        } catch (Exception var13) {
            log.error("获取文件MD5校验码失败", var13);
            var13.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException var12) {
                    log.error("关闭文件流异常", var12);
                    var12.printStackTrace();
                }
            }

        }

        return bufferToHex(messagedigest.digest());
    }

    public static String getFileMD5String_old(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(MapMode.READ_ONLY, 0L, file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;

        for(int l = m; l < k; ++l) {
            appendHexPair(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 240) >> 4];
        char c1 = hexDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static void main(String[] args) throws IOException {
        long begin = System.currentTimeMillis();
        File file = new File("C:/phone.zip");
        String md5 = getFileMD5String(file);
        long end = System.currentTimeMillis();
        System.out.println("md5:" + md5 + " time:" + (end - begin) / 1000L + "s");
    }

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var2) {
            String msg = MD5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util。";
            log.error(msg, var2);
            var2.printStackTrace();
        }

    }
}
