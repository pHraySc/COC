package com.ailk.biapp.ci.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FtpUtil {
    private static final Logger log = Logger.getLogger(FtpUtil.class);
    private static final String UN_UPLOADED = "_unUploaded";
    private FTPClient ftpClient = new FTPClient();
    public static final int BINARY_FILE_TYPE = 2;
    public static final int ASCII_FILE_TYPE = 0;

    public FtpUtil() {
    }

    public boolean connectServer(String server, int port, String user, String password, String path) throws SocketException, IOException {
        this.ftpClient.setControlEncoding("GBK");
        this.ftpClient.connect(server, port);
        boolean flag = false;
        log.debug("Connected to " + server + "  server response:" + this.ftpClient.getReplyCode());
        if(this.ftpClient.login(user, password)) {
            log.debug("log in success");
            flag = true;
        } else {
            log.debug("log in failed user:" + user);
        }

        this.ftpClient.setFileType(2);
        if(path.length() != 0) {
            if(this.ftpClient.changeWorkingDirectory(path)) {
                log.debug("ftp:working directory change to:" + path);
            } else {
                log.debug("ftp:change working directory to:" + path + " failed");
            }
        }

        return flag;
    }

    public void closeServer() {
        if(this.ftpClient.isConnected()) {
            try {
                this.ftpClient.logout();
                this.ftpClient.disconnect();
            } catch (Exception var2) {
                log.error("", var2);
            }

            log.debug("ftp:disconnnect");
        }

    }

    public long checkFile(String fileName) throws IOException {
        long fileLength = -1L;
        FTPFile[] ftpFiles = this.ftpClient.listFiles();
        FTPFile[] arr$ = ftpFiles;
        int len$ = ftpFiles.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            FTPFile ftpFile = arr$[i$];
            if(ftpFile.isFile() && fileName.equals(ftpFile.getName())) {
                fileLength = ftpFile.getSize();
                break;
            }
        }

        log.debug("ftp:check file [" + fileName + "] length length=" + fileLength);
        return fileLength;
    }

    public boolean download(String remoteFileName, String localFileName) throws IOException {
        boolean flag = false;
        File outfile = new File(localFileName);
        FileOutputStream oStream = null;

        boolean var7;
        try {
            oStream = new FileOutputStream(outfile);
            flag = this.ftpClient.retrieveFile(remoteFileName, oStream);
            return flag;
        } catch (IOException var12) {
            flag = false;
            var7 = flag;
        } finally {
            if(oStream != null) {
                oStream.close();
            }

        }

        return var7;
    }

    public boolean deleteFile(String pathName) throws IOException {
        return this.ftpClient.deleteFile(pathName);
    }

    public static boolean ftp(String address, String port, String user, String password, String localpath, String remotepath) {
        boolean flag = true;
        long t1 = System.currentTimeMillis();
        FTPClient client = null;

        try {
            if(StringUtils.isEmpty(address) || StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {
                throw new Exception("address or user or password is null");
            }

            client = new FTPClient();
            client.connect(address, StringUtils.isEmpty(port)?21:Integer.valueOf(port).intValue());
            client.login(user, password);
            if(!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                throw new Exception("ftp login fail:[user=" + user + "] or password or port[port=" + port + "] wrong,please check it!");
            }

            log.debug("ftp login success!");
            String e = remotepath;
            File locfile = new File(localpath);
            if(!locfile.exists()) {
                throw new Exception("localpath[" + localpath + "] not exist!");
            }

            if(locfile.isFile()) {
                if(remotepath.endsWith("/")) {
                    e = remotepath + locfile.getName();
                } else if(!remotepath.contains(".") && !remotepath.endsWith("/")) {
                    e = remotepath + "/" + locfile.getName();
                }

                upload(client, localpath, e + "_unUploaded");
            } else {
                flag = uploadAll(client, localpath, remotepath + "_unUploaded");
            }

            log.debug("ftp rename [" + e + "_unUploaded" + "] to [" + e + "]");
            client.rename((new File(e + "_unUploaded")).getName(), (new File(e)).getName());
        } catch (Exception var21) {
            flag = false;
            log.error("ftp error:", var21);
            var21.printStackTrace();
        } finally {
            if(client != null) {
                try {
                    client.disconnect();
                    client.logout();
                } catch (Exception var20) {
                    ;
                }
            }

            log.debug("The cost of uploading file: " + (System.currentTimeMillis() - t1) / 1000L + "s.");
        }

        return flag;
    }

    private static void upload(FTPClient client, String local, String remote) throws Exception {
        log.debug("FTP from:" + local + " to : " + remote);
        client.enterLocalPassiveMode();
        client.setFileType(2);
        String remoteFileName = remote;
        if(remote.contains("/")) {
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
            if(!createDirecroty(client, remote)) {
                log.warn("Create direcroty of remote(" + remote + ") fail!");
                throw new Exception("Create direcroty of remote(" + remote + ") fail!please check FTP user\'s permission!");
            }
        }

        File f = new File(local);
        uploadFile(client, remoteFileName, f);
    }

    private static void uploadFile(FTPClient client, String remoteFile, File localFile) throws IOException {
        log.debug("upload file: " + localFile.getName());
        FileInputStream in = new FileInputStream(localFile);

        try {
            client.storeFile(remoteFile, in);
        } catch (Exception var9) {
            client.enterLocalActiveMode();
            client.storeFile(remoteFile, in);
        } finally {
            in.close();
        }

    }

    private static boolean createDirecroty(FTPClient client, String remote) throws IOException {
        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        if(!directory.equalsIgnoreCase("/") && !client.changeWorkingDirectory(directory)) {
            boolean start = false;
            boolean end = false;
            int start1;
            if(directory.startsWith("/")) {
                start1 = 1;
            } else {
                start1 = 0;
            }

            int end1 = directory.indexOf("/", start1);

            do {
                String subDirectory = remote.substring(start1, end1);
                if(!client.changeWorkingDirectory((start1 == 1?"/":"") + subDirectory)) {
                    if(!client.makeDirectory(subDirectory)) {
                        success = false;
                        return success;
                    }

                    client.changeWorkingDirectory(subDirectory);
                }

                start1 = end1 + 1;
                end1 = directory.indexOf("/", start1);
            } while(end1 > start1);
        }

        return success;
    }

    private static boolean uploadAll(FTPClient client, String filename, String uploadpath) throws Exception {
        boolean success = false;
        File file = new File(filename);
        if(!file.exists()) {
            return success;
        } else if(!file.isDirectory()) {
            return success;
        } else {
            File[] files = file.listFiles();
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                File f = arr$[i$];
                if(f.exists()) {
                    if(f.isDirectory()) {
                        uploadAll(client, f.getAbsoluteFile().toString(), uploadpath);
                    } else {
                        String local = f.getCanonicalPath().replaceAll("\\\\", "/");
                        String remote = uploadpath.endsWith("/")?uploadpath + local.substring(local.lastIndexOf("/") + 1):uploadpath + "/" + local.substring(local.lastIndexOf("/") + 1);
                        upload(client, local, remote);
                        client.changeWorkingDirectory("/");
                    }
                }
            }

            return true;
        }
    }
}
