package com.base.framework.core.utils;

import java.util.concurrent.*;

/**
 *  默认线程池
 *  @since                   ：0.0.1
 *  @author                  ：zc.ding@foxmail.com
 */

public class ThreadPoolUtil {

    private static volatile ExecutorService FIXED_THREAD_POOL;
    private final static String FIXED_THREAD_POOL_LOCK = "FIXED_THREAD_POOL";

    private static volatile ExecutorService WORK_STEALING_POOL;
    private final static String WORK_STEALING_POOL_LOCK = "WORK_STEALING_POOL";

    private static volatile ExecutorService CACHED_THREAD_POOL;
    private final static String CACHED_THREAD_POOL_LOCK = "CACHED_THREAD_POOL";

    private ThreadPoolUtil(){}

    /**
     *  通过FixedThreadPool池执行业务操作
     *  <p>
     *       核心线程数：50， 最大200， 线程空闲时间0秒
     *  </p>
     *  @param callable  业务操作
     *  @return java.util.concurrent.Future<T>
     *  @date  Date              ：2018/9/28
     *  @author                  ：zc.ding@foxmail.com
     */
    public static <T> Future<T> callFixedThreadPool(Callable<T> callable){
        initFixedThreadPool();
        return FIXED_THREAD_POOL.submit(callable);
    }

    /**
     *  通过FixedThreadPool池执行业务操作
     *  <p>
     *       核心线程数：50， 最大200， 线程空闲时间0秒
     *  </p>
     *  @param runnable  业务操作
     */
    public static void callFixedThreadPool(Runnable runnable){
        initFixedThreadPool();
        FIXED_THREAD_POOL.execute(runnable);
    }

    /**
     *  通过WorkStealThreadPool池执行业务操作
     *  @param callable  业务操作
     *  @return java.util.concurrent.Future<T>
     *  @date  Date              ：2018/9/28
     *  @author                  ：zc.ding@foxmail.com
     */
    public static <T> Future<T> callWorkStealPool(Callable<T> callable){
        initWorkStealPool();
        return WORK_STEALING_POOL.submit(callable);
    }

    /**
     *  通过WorkStealThreadPool池执行业务操作
     *  @param runnable  业务操作
     *  @date  Date              ：2018/9/28
     *  @author                  ：zc.ding@foxmail.com
     */
    public static void callWorkStealPool(Runnable runnable){
        initWorkStealPool();
        WORK_STEALING_POOL.execute(runnable);
    }

    /**
     *  通过CachedThreadPool池执行业务操作
     *  <p>
     *      核心线程数20，最大增加至500线程，60s存活时间
     *  </p>
     *  @param callable  业务操作
     *  @return java.util.concurrent.Future<T>
     *  @date  Date              ：2018/9/28
     *  @author                  ：zc.ding@foxmail.com
     */
    public static <T> Future<T> callCachedThreadPool(Callable<T> callable){
        initCachedThreadPool();
        return CACHED_THREAD_POOL.submit(callable);
    }

    /**
     *  通过CachedThreadPool池执行业务操作
     *  <p>
     *      核心线程数20，最大增加至500线程，60s存活时间
     *  </p>
     *  @param runnable  业务操作
     *  @date  Date              ：2018/9/28
     *  @author                  ：zc.ding@foxmail.com
     */
    public static void callCachedThreadPool(Runnable runnable){
        initCachedThreadPool();
        CACHED_THREAD_POOL.execute(runnable);
    }

    /**
     * 初始化固定线程池
     */
    private static void initFixedThreadPool(){
        if(FIXED_THREAD_POOL == null){
            synchronized (FIXED_THREAD_POOL_LOCK){
                if(FIXED_THREAD_POOL == null){
                    FIXED_THREAD_POOL = new ThreadPoolExecutor(50, 200, 0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat("Fix-Thread-%d").build());
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> FIXED_THREAD_POOL.shutdown()));
                }
            }
        }
    }

    /**
     * 初始化任务窃取线程池
     */
    private static void initWorkStealPool(){
        if(WORK_STEALING_POOL == null){
            synchronized (WORK_STEALING_POOL_LOCK){
                if(WORK_STEALING_POOL == null){
                    WORK_STEALING_POOL = Executors.newWorkStealingPool();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> WORK_STEALING_POOL.shutdown()));
                }
            }
        }
    }

    /**
     * 初始化缓存线程池
     */
    private static void initCachedThreadPool(){
        if(CACHED_THREAD_POOL == null){
            synchronized (CACHED_THREAD_POOL_LOCK){
                if(CACHED_THREAD_POOL == null){
                    CACHED_THREAD_POOL = new ThreadPoolExecutor(20, 500, 60L, TimeUnit.MILLISECONDS,
                            new SynchronousQueue<>(), new ThreadFactoryBuilder().setNameFormat("CacheThread-%d").build());
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> CACHED_THREAD_POOL.shutdown()));
                }
            }
        }
    }
}
