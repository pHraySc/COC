package com.ailk.biapp.ci.util;

import com.asiainfo.biframe.utils.config.Configure;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MongoDB {
    private static Logger log = Logger.getLogger(MongoDB.class);
    private static MongoDB instance;
    private Mongo mongo;
    private String mongoDBName;
    private String configPath;

    private MongoDB(String configPath) {
        try {
            File configFile = new File(this.getClass().getResource(configPath).toURI());
            Configure.getInstance().setConfFileName(configFile.getPath());
        } catch (Exception var15) {
            log.error("loading mongodb.properties error.");
        }

        this.configPath = configPath;
        ArrayList addresslist = new ArrayList();

        String maxWaitTime;
        try {
            String options = Configure.getInstance().getProperty("SERVER_ADDRESSES");
            log.debug("serverAddressStr:" + options);
            String[] autoConnectRetry = options.split(",");
            String[] connectionsPerHost = autoConnectRetry;
            int threadsAllowedToBlockForConnectionMultiplier = autoConnectRetry.length;

            for(int connectTimeout = 0; connectTimeout < threadsAllowedToBlockForConnectionMultiplier; ++connectTimeout) {
                maxWaitTime = connectionsPerHost[connectTimeout];
                log.debug("address:" + maxWaitTime);
                addresslist.add(new ServerAddress(maxWaitTime));
            }
        } catch (UnknownHostException var16) {
            log.error("mongodb address check error.");
        }

        MongoOptions var17 = new MongoOptions();
        String var18 = Configure.getInstance().getProperty("AUTO_CONNECT_RETRY");
        log.debug("[AUTO_CONNECT_RETRY]:" + var18);
        if(StringUtils.isNotEmpty(var18)) {
            var17.autoConnectRetry = Boolean.valueOf(var18).booleanValue();
        }

        String var19 = Configure.getInstance().getProperty("CONNECTIONS_PER_HOST");
        log.debug("[CONNECTIONS_PER_HOST]" + var19);
        if(StringUtils.isNotEmpty(var19)) {
            var17.connectionsPerHost = Integer.valueOf(var19).intValue();
        }

        String var20 = Configure.getInstance().getProperty("THREAD_ALLOWED");
        log.debug("[THREAD_ALLOWED]:" + var20);
        if(StringUtils.isNotEmpty(var20)) {
            var17.threadsAllowedToBlockForConnectionMultiplier = Integer.valueOf(var20).intValue();
        }

        String var21 = Configure.getInstance().getProperty("CONNECT_TIME_OUT");
        log.debug("[CONNECT_TIME_OUT]:" + var21);
        if(StringUtils.isNotEmpty(var21)) {
            var17.connectTimeout = Integer.valueOf(var21).intValue();
        }

        maxWaitTime = Configure.getInstance().getProperty("MAX_WAIT_TIME");
        log.debug("[MAX_WAIT_TIME]:" + maxWaitTime);
        if(StringUtils.isNotEmpty(maxWaitTime)) {
            var17.maxWaitTime = Integer.valueOf(maxWaitTime).intValue();
        }

        String socketTimeout = Configure.getInstance().getProperty("SOCKET_TIMEOUT");
        log.debug("[SOCKET_TIMEOUT]:" + socketTimeout);
        if(StringUtils.isNotEmpty(socketTimeout)) {
            var17.socketTimeout = Integer.valueOf(socketTimeout).intValue();
        }

        String slaveOk = Configure.getInstance().getProperty("SlAVE_OK");
        log.debug("[SlAVE_OK]:" + slaveOk);
        if(StringUtils.isNotEmpty(slaveOk)) {
            var17.slaveOk = Boolean.valueOf(slaveOk).booleanValue();
        }

        this.mongoDBName = Configure.getInstance().getProperty("MONGO_DBNAME");
        if(this.mongo != null) {
            try {
                this.mongo.close();
                this.mongo = null;
            } catch (Exception var14) {
                log.error("close mongo error.");
            }
        }

        try {
            this.mongo = new Mongo(addresslist, var17);
        } catch (MongoException var13) {
            log.error("mongo create error.");
        }

    }

    public static MongoDB getInstance() {
        if(instance == null) {
            instance = new MongoDB("/config/aibi_ci/mongodb.properties");
        }

        return instance;
    }

    public Mongo getMongo() {
        return this.mongo;
    }

    public DB getDB() {
        return this.mongo.getDB(this.mongoDBName);
    }

    public DB getDB(String dbName) {
        return this.mongo.getDB(dbName);
    }

    public MongoDB switchCluster() {
        if(Boolean.valueOf(Configure.getInstance().getProperty("MONGODB_SWITCH_CLUSTER")).booleanValue()) {
            if(this.configPath.equals("/config/aibi_ci/mongodb.properties")) {
                log.info("switchCluster,use config file :/config/aibi_ci/mongodb2.properties");
                System.out.println("switchCluster,use config file :/config/aibi_ci/mongodb2.properties");
                instance = new MongoDB("/config/aibi_ci/mongodb2.properties");
            } else if(this.configPath.equals("/config/aibi_ci/mongodb2.properties")) {
                log.info("switchCluster,use config file :/config/aibi_ci/mongodb.properties");
                System.out.println("switchCluster,use config file :/config/aibi_ci/mongodb.properties");
                instance = new MongoDB("/config/aibi_ci/mongodb.properties");
            }
        }

        return instance;
    }

    public DBObject findOne(String collectionName, String keystr, String value) {
        DB db = this.getDB();
        DBCollection collection = db.getCollection(collectionName);

        DBObject dbObject;
        try {
            dbObject = collection.findOne(new BasicDBObject(keystr, value));
            log.debug("dbObject1:" + dbObject);
            if(dbObject == null) {
                db = this.switchCluster().getDB();
                collection = db.getCollection(collectionName);
                dbObject = collection.findOne(new BasicDBObject(keystr, value));
                log.debug("dbObject1-2:" + dbObject);
            }
        } catch (MongoException var8) {
            db = this.switchCluster().getDB();
            collection = db.getCollection(collectionName);
            dbObject = collection.findOne(new BasicDBObject(keystr, value));
            log.debug("dbObject2:" + dbObject);
        }

        return dbObject;
    }

    public static void main(String[] args) {
        MongoDB mongoDB = getInstance();
        DB db = mongoDB.getDB("test");
        System.out.println(db.getName());
        DBCollection collection = db.getCollection("labelsAndIndexes");

        try {
            System.out.println(collection.findOne(new BasicDBObject("mobile", "19081705835")));
        } catch (MongoException var5) {
            mongoDB = mongoDB.switchCluster();
            db = mongoDB.getDB("test");
            System.out.println(db.getName());
            collection = db.getCollection("campaigns");
            System.out.println(collection.findOne(new BasicDBObject("mobile", "13581705835")));
            System.out.println("mongo exception");
            var5.printStackTrace();
        }

    }

    protected void finalize() throws Throwable {
        super.finalize();
        if(this.mongo != null) {
            try {
                this.mongo.close();
            } catch (Exception var2) {
                log.error("close mongo error.");
            }
        }

    }
}
