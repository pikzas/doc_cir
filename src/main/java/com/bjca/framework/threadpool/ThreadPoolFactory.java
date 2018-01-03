package com.bjca.framework.threadpool;

/**
 * 
 *Administrator
 *下午06:19:12
 *
 */
import java.util.Hashtable;

public class ThreadPoolFactory
{

     private static Hashtable<String, ThreadPool> threadPools = new Hashtable<String, ThreadPool>();

     /**
      * 得到指定名称的线程池对象；
      * 
      * @param threadPoolName
      *             线程池的名称；
      * @return
      */
     public static ThreadPool getThreadPool(String threadPoolName)
     {
          ThreadPool pool = null;
          try
          {
               pool = (ThreadPool) threadPools.get(threadPoolName);
          }
          catch (Exception ex)
          {
          }
          return pool;
     }

     /**
      * 创建并返回新的线程池，如果已经存在同名线程池则覆盖之。
      * 
      * @param threadPoolName
      *             线程池的名称；
      * @param threadPoolSize
      *             线程池的容量；
      * @param threadClassName
      *             线程池容纳的线程类；
      * @return
      */
     public static ThreadPool getNewThreadPool(String threadPoolName, int maxThreadCount, int initialInstanceCount,
               int threadPriority, boolean isAllowFill, String threadClassName)
     {
          ThreadPool pool = null;
          try
          {
               // 先删除现有的同名线程池；
               threadPools.remove(threadPoolName);
               pool = new ThreadPool(maxThreadCount, isAllowFill, threadClassName);
               threadPools.put(threadPoolName, pool);
               // 初始化线程池；
               pool.InitThreadPool(initialInstanceCount, threadPriority);
          }
          catch (Exception ex)
          {
          }
          return pool;
     }
}
