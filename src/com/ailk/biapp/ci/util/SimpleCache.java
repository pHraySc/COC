package com.ailk.biapp.ci.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimpleCache {
    private final Map<String, Object> objects;
    private final Map<String, Long> expire;
    private final long defaultExpire;
    private final ExecutorService threads;

    public static SimpleCache getInstance() {
        return SimpleCache.SimpleCacheHolder.instance;
    }

    public SimpleCache() {
        this(100L);
    }

    public SimpleCache(long defaultExpire) {
        this.objects = new ConcurrentHashMap();
        this.expire = new ConcurrentHashMap();
        this.defaultExpire = defaultExpire;
        this.threads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        Executors.newScheduledThreadPool(2).scheduleWithFixedDelay(this.removeExpired(), 2L, 5L, TimeUnit.SECONDS);
    }

    private Runnable removeExpired() {
        return new Runnable() {
            public void run() {
                Iterator i$ = SimpleCache.this.expire.keySet().iterator();

                while(i$.hasNext()) {
                    String name = (String)i$.next();
                    if(System.currentTimeMillis() > ((Long)SimpleCache.this.expire.get(name)).longValue() && ((Long)SimpleCache.this.expire.get(name)).longValue() != -1L) {
                        SimpleCache.this.threads.execute(SimpleCache.this.createRemoveRunnable(name));
                    }
                }

            }
        };
    }

    private Runnable createRemoveRunnable(final String name) {
        return new Runnable() {
            public void run() {
                SimpleCache.this.objects.remove(name);
                SimpleCache.this.expire.remove(name);
            }
        };
    }

    public long getExpire() {
        return this.defaultExpire;
    }

    public void put(String name, Object obj) {
        this.put(name, obj, this.defaultExpire);
    }

    public void put(String name, Object obj, long expireTime) {
        this.objects.put(name, obj);
        this.expire.put(name, Long.valueOf(expireTime != -1L?System.currentTimeMillis() + expireTime * 1000L:-1L));
    }

    public Object get(String name) {
        Long expireTime = (Long)this.expire.get(name);
        if(expireTime == null) {
            return null;
        } else if(System.currentTimeMillis() > expireTime.longValue() && expireTime.longValue() != -1L) {
            this.threads.execute(this.createRemoveRunnable(name));
            return null;
        } else {
            return this.objects.get(name);
        }
    }

    public <R> R get(String name, Class<R> type) {
        return (R)this.get(name);
    }

    static class SimpleCacheHolder {
        static SimpleCache instance = new SimpleCache(-1L);

        SimpleCacheHolder() {
        }
    }
}
