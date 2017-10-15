package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

public class ThreadPool {
    private Logger log;
    private final ExecutorService service;
    private final ExecutorService singleThreadService;
    private final Map<String, ExecutorService> threadMap;

    public static ThreadPool getInstance() {
        return ThreadPool.ThreadPoolHolder.instance;
    }

    private ThreadPool() {
        this.log = Logger.getLogger(ThreadPool.class);
        int threadNum = Runtime.getRuntime().availableProcessors() * 2;

        try {
            threadNum = NumberUtils.toInt(Configure.getInstance().getProperty("THREAD_NUM"), threadNum);
        } catch (Exception var10) {
            ;
        }

        HashMap threadPoolMap = new HashMap();

        try {
            String e = Configure.getInstance().getProperty("USE_CITY_THREAD_POOL");
            if(StringUtil.isNotEmpty(e) && "true".equals(e)) {
                CacheBase cache = CacheBase.getInstance();
                Map cityThreadConfigMap = cache.getCityThreadConfigMap();
                Iterator i$ = cityThreadConfigMap.keySet().iterator();

                while(i$.hasNext()) {
                    Object key = i$.next();
                    Object value = cityThreadConfigMap.get(key);
                    ExecutorService eachCityThreadService = Executors.newFixedThreadPool(((Integer)value).intValue());
                    threadPoolMap.put(key.toString(), eachCityThreadService);
                    this.log.info("地市ID：" + key.toString() + "================线程数：" + value);
                }
            }
        } catch (Exception var11) {
            this.log.error("从缓存中获取地市线程池配置信息出错", var11);
        }

        this.threadMap = threadPoolMap;
        this.singleThreadService = Executors.newFixedThreadPool(1);
        this.service = Executors.newFixedThreadPool(threadNum);
    }

    public int getSize() {
        ThreadPoolExecutor pool = (ThreadPoolExecutor)this.service;
        return pool.getQueue().size();
    }

    public void execute(Runnable r) {
        try {
            this.service.execute(r);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.log.info("add task [" + r.toString() + "] ThreadPool status:" + this.showStatus());
    }

    public void execute(Runnable r, boolean isSingleThread) {
        if(!isSingleThread) {
            this.execute(r);
        } else {
            try {
                this.singleThreadService.execute(r);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        this.log.info("add task [" + r.toString() + "] to SingleThread ,ThreadPool status:" + this.showStatus());
    }

    public void execute(Runnable r, boolean isSingleThread, String cityId) {
        if(!isSingleThread) {
            try {
                if(StringUtil.isNotEmpty(cityId) && this.threadMap != null && this.threadMap.size() > 0) {
                    ExecutorService ex = (ExecutorService)this.threadMap.get(cityId);
                    if(ex != null) {
                        ex.execute(r);
                        this.log.debug("使用的线程的cityId为：" + cityId + ",该线程池的线程数为" + ((ThreadPoolExecutor)ex).getMaximumPoolSize());
                    } else {
                        this.execute(r, true);
                    }
                } else {
                    this.execute(r, true);
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        } else {
            try {
                this.singleThreadService.execute(r);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        this.log.info("add task [" + r.toString() + "] to SingleThread ,ThreadPool status:" + this.showStatus());
    }

    public String showStatus() {
        StringBuffer info = new StringBuffer();
        info.append("FixedThreadPool:");
        ThreadPoolExecutor executor = (ThreadPoolExecutor)this.service;
        info.append("ActiveCount:").append(executor.getActiveCount()).append(",");
        info.append("CompletedTaskCount:").append(executor.getCompletedTaskCount()).append(",");
        info.append("TaskCount:").append(executor.getTaskCount()).append(",");
        info.append("PoolSize:").append(executor.getPoolSize()).append(",");
        info.append("CorePoolSize:").append(executor.getCorePoolSize()).append(",");
        info.append("LargestPoolSize:").append(executor.getLargestPoolSize()).append(",");
        BlockingQueue queue = executor.getQueue();
        info.append("QueueSize:").append(queue.size()).append(",");
        info.append("BlockingQueue:");
        if(queue != null) {
            info.append("[");
            Iterator singleExecutor = queue.iterator();

            while(singleExecutor.hasNext()) {
                Runnable i$ = (Runnable)singleExecutor.next();
                info.append(i$);
            }

            info.append("]");
        }

        info.append("\nSingleThreadPool:");
        ThreadPoolExecutor singleExecutor1 = (ThreadPoolExecutor)this.singleThreadService;
        info.append("ActiveCount:").append(singleExecutor1.getActiveCount()).append(",");
        info.append("CompletedTaskCount:").append(singleExecutor1.getCompletedTaskCount()).append(",");
        info.append("TaskCount:").append(singleExecutor1.getTaskCount()).append(",");
        info.append("PoolSize:").append(singleExecutor1.getPoolSize()).append(",");
        info.append("CorePoolSize:").append(singleExecutor1.getCorePoolSize()).append(",");
        info.append("LargestPoolSize:").append(singleExecutor1.getLargestPoolSize()).append(",");
        queue = singleExecutor1.getQueue();
        info.append("QueueSize:").append(queue.size()).append(",");
        info.append("BlockingQueue:");
        if(queue != null) {
            info.append("[");
            Iterator i$1 = queue.iterator();

            while(i$1.hasNext()) {
                Runnable r = (Runnable)i$1.next();
                info.append(r);
            }

            info.append("]");
        }

        return info.toString();
    }

    public void execute(Callable r) throws Exception {
        try {
            this.service.submit(r);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw var3;
        }

        this.log.info("add task [" + r.toString() + "] to ThreadPool status:" + this.showStatus());
    }

    public void execute(Callable r, boolean isSingleThread) throws Exception {
        if(!isSingleThread) {
            this.execute(r);
        } else {
            try {
                this.singleThreadService.submit(r);
            } catch (Exception var4) {
                var4.printStackTrace();
                throw var4;
            }

            this.log.info("add task [" + r.toString() + "] to SingleThread ,ThreadPool status:" + this.showStatus());
        }

    }

    public void execute(Callable r, boolean isSingleThread, String cityId) throws Exception {
        if(!isSingleThread) {
            try {
                if(StringUtil.isNotEmpty(cityId) && this.threadMap != null && this.threadMap.size() > 0) {
                    ExecutorService ex = (ExecutorService)this.threadMap.get(cityId);
                    if(ex != null) {
                        ex.submit(r);
                        this.log.info("使用的线程的cityId为：" + cityId + ",该线程池的线程数为" + ((ThreadPoolExecutor)ex).getMaximumPoolSize());
                    } else {
                        this.execute(r, true);
                    }
                } else {
                    this.execute(r, true);
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        } else {
            try {
                this.singleThreadService.submit(r);
            } catch (Exception var5) {
                var5.printStackTrace();
                throw var5;
            }

            this.log.info("add task [" + r.toString() + "] to SingleThread ,ThreadPool status:" + this.showStatus());
        }

    }

    public List<Runnable> shutdownNow() {
        ThreadPoolExecutor pool = (ThreadPoolExecutor)this.service;
        return pool.shutdownNow();
    }

    static class ThreadPoolHolder {
        static final ThreadPool instance = new ThreadPool();

        ThreadPoolHolder() {
        }
    }
}
