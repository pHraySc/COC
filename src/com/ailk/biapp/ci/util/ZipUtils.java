package com.ailk.biapp.ci.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public abstract class ZipUtils {
    public ZipUtils() {
    }

    public static void zip(String source, String dest) throws IOException {
        FileOutputStream os = new FileOutputStream(dest);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        ZipOutputStream zos = new ZipOutputStream(bos);
        zos.setEncoding("GBK");
        File file = new File(source);
        String basePath = null;
        if(file.isDirectory()) {
            basePath = file.getPath();
        } else {
            basePath = file.getParent();
        }

        zipFile(file, basePath, zos);
        bos.close();
        zos.closeEntry();
        zos.close();
    }

    public static String unzipSingleFile(String zipFile, String dest) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        Enumeration en = zip.getEntries();
        ZipEntry entry = null;
        byte[] buffer = new byte[1024];
        boolean length = true;
        InputStream input = null;
        BufferedOutputStream bos = null;
        File file = null;

        label35:
        while(en.hasMoreElements()) {
            entry = (ZipEntry)en.nextElement();
            if(!entry.isDirectory()) {
                input = zip.getInputStream(entry);
                file = new File(dest, entry.getName());
                if(!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                bos = new BufferedOutputStream(new FileOutputStream(file));

                while(true) {
                    int length1 = input.read(buffer);
                    if(length1 == -1) {
                        bos.close();
                        input.close();
                        break label35;
                    }

                    bos.write(buffer, 0, length1);
                }
            }

            file = new File(dest, entry.getName());
            if(!file.exists()) {
                file.mkdir();
            }
        }

        zip.close();
        if(file == null) {
            throw new IOException("创建文件失败");
        } else {
            return file.getAbsolutePath();
        }
    }

    public static void unzip(String zipFile, String dest) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        Enumeration en = zip.getEntries();
        ZipEntry entry = null;
        byte[] buffer = new byte[1024];
        boolean length = true;
        InputStream input = null;
        BufferedOutputStream bos = null;
        File file = null;

        while(true) {
            while(en.hasMoreElements()) {
                entry = (ZipEntry)en.nextElement();
                if(entry.isDirectory()) {
                    file = new File(dest, entry.getName());
                    if(!file.exists()) {
                        file.mkdir();
                    }
                } else {
                    input = zip.getInputStream(entry);
                    file = new File(dest, entry.getName());
                    if(!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    bos = new BufferedOutputStream(new FileOutputStream(file));

                    while(true) {
                        int length1 = input.read(buffer);
                        if(length1 == -1) {
                            bos.close();
                            input.close();
                            break;
                        }

                        bos.write(buffer, 0, length1);
                    }
                }
            }

            zip.close();
            return;
        }
    }

    private static void zipFile(File source, String basePath, ZipOutputStream zos) throws IOException {
        File[] files = new File[0];
        if(source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[]{source};
        }

        byte[] buf = new byte[1024];
        boolean length = false;
        File[] arr$ = files;
        int len$ = files.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            File file = arr$[i$];
            String pathName;
            if(file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + "/";
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                FileInputStream is = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));

                int var13;
                while((var13 = bis.read(buf)) > 0) {
                    zos.write(buf, 0, var13);
                }

                bis.close();
                is.close();
                zos.close();
            }
        }

    }
}
