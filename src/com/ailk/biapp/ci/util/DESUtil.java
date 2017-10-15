package com.ailk.biapp.ci.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class DESUtil {
    private static final Logger log = Logger.getLogger(DESUtil.class);
    private SecretKey key;

    public DESUtil() {
    }

    public DESUtil(String str) {
        this.setKey(str);
    }

    public SecretKey getKey() {
        return this.key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    public void setKey(String strKey) {
        try {
            DESKeySpec e = new DESKeySpec(strKey.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            this.key = keyFactory.generateSecret(e);
            if(this.key == null) {
                KeyGenerator _generator = KeyGenerator.getInstance("DES");
                _generator.init(new SecureRandom(strKey.getBytes()));
                this.key = _generator.generateKey();
                _generator = null;
            }
        } catch (Exception var5) {
            log.error("生成DES密钥失败", var5);
            var5.printStackTrace();
        }

    }

    public String encryptStr(String strMing) {
        Object byteMi = null;
        Object byteMing = null;
        String strMi = "";

        try {
            byte[] byteMing1 = strMing.getBytes("UTF-8");
            byte[] byteMi1 = this.encryptByte(byteMing1);
            strMi = new String(Base64.encodeBase64(byteMi1), "UTF-8");
        } catch (Exception var10) {
            log.error("对字符串DES加密失败", var10);
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + var10);
        } finally {
            byteMing = null;
            byteMi = null;
        }

        return strMi;
    }

    public String decryptStr(String strMi) {
        Object byteMing = null;
        Object byteMi = null;
        String strMing = "";

        try {
            byte[] byteMi1 = Base64.decodeBase64(strMi.getBytes("UTF-8"));
            byte[] byteMing1 = this.decryptByte(byteMi1);
            strMing = new String(byteMing1, "UTF-8");
        } catch (Exception var10) {
            log.error("对字符串DES解密失败", var10);
            var10.printStackTrace();
        } finally {
            byteMing = null;
            byteMi = null;
        }

        return strMing;
    }

    private byte[] encryptByte(byte[] byteS) {
        byte[] byteFina = null;

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(1, this.key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception var9) {
            log.error("对byte数组DES加密失败", var9);
            var9.printStackTrace();
        } finally {
            cipher = null;
        }

        return byteFina;
    }

    private byte[] decryptByte(byte[] byteD) {
        byte[] byteFina = null;

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(2, this.key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception var9) {
            log.error("对byte数组DES解密失败", var9);
            var9.printStackTrace();
        } finally {
            cipher = null;
        }

        return byteFina;
    }

    public void encryptFile(String file, String destFile) {
        FileInputStream is = null;
        FileOutputStream out = null;
        CipherInputStream cis = null;

        try {
            Cipher e = Cipher.getInstance("DES");
            e.init(1, this.key);
            is = new FileInputStream(file);
            out = new FileOutputStream(destFile);
            cis = new CipherInputStream(is, e);
            byte[] buffer = new byte[1024];

            int r;
            while((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
        } catch (Exception var18) {
            log.error("对文件DES加密失败", var18);
            var18.printStackTrace();
        } finally {
            try {
                if(cis != null) {
                    cis.close();
                }

                if(is != null) {
                    is.close();
                }

                if(out != null) {
                    out.close();
                }
            } catch (IOException var17) {
                log.error("关闭流异常", var17);
                var17.printStackTrace();
            }

        }

    }

    public void decryptFile(String file, String dest) {
        FileInputStream is = null;
        FileOutputStream out = null;
        CipherOutputStream cos = null;

        try {
            Cipher e = Cipher.getInstance("DES");
            e.init(2, this.key);
            is = new FileInputStream(file);
            out = new FileOutputStream(dest);
            cos = new CipherOutputStream(out, e);
            byte[] buffer = new byte[1024];

            int r;
            while((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
        } catch (Exception var18) {
            log.error("对文件DES解密失败", var18);
            var18.printStackTrace();
        } finally {
            try {
                if(cos != null) {
                    cos.close();
                }

                if(is != null) {
                    is.close();
                }

                if(out != null) {
                    out.close();
                }
            } catch (IOException var17) {
                log.error("关闭流异常", var17);
                var17.printStackTrace();
            }

        }

    }

    public static void main(String[] args) throws Exception {
        String key = "1RmnGiuzp1iomKP1ZPOJE8";
        System.out.println("key : " + key);
        long s = System.currentTimeMillis();
        DESUtil des = new DESUtil(key);
        des.encryptFile("C:/phone.zip", "D:/phone.zip");
        des.decryptFile("D:/phone.zip", "E:/phone.zip");
        long e = System.currentTimeMillis();
        System.out.println("共使用 " + (e - s) + "ms");
        String str1 = "COC向广告平台推送test";
        String str2 = des.encryptStr(str1);
        String deStr = des.decryptStr(str2);
        System.out.println(" 加密前： " + str1);
        System.out.println(" 加密后： " + str2);
        System.out.println(" 解密后： " + deStr);
    }
}
