package com.bjca.framework.cache;

import java.util.Date;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * 
 *Administrator 下午02:24:35
 * 
 */
public class BaseCache extends GeneralCacheAdministrator
{

     private static final long  serialVersionUID = -4397192926052141162L;

     public static final String PERIOD_DAY       = "D";
     public static final String PERIOD_WEEK      = "W";
     public static final String PERIOD_MONTH     = "M";
     public static final String PERIOD_YEAR      = "Y";
     public static final String PERIOD_FOREVER   = "F";

     private static BaseCache   instance;
     private static Object      lock             = new Object();

     protected int              refreshPeriod    = 24 * 60 * 60 * 1000;  // 过期时间(单位为秒);

     public BaseCache(int refreshPeriod)
     {
          super();

          this.refreshPeriod = refreshPeriod;
     }

     /**
      * 添加被缓存的对象;
      */
     public void addCacheObject(String key, Object value)
     {

          // WebRunLog.debug(key + "-" + this.refreshPeriod);
          this.putInCache(key, value);

     }

     public Object getCacheObject(String key)
     {
          Object object = null;
          try
          {
               object = this.getFromCache(key, this.refreshPeriod);
          }
          catch (NeedsRefreshException e)
          {

          }
          // System.out.println("返回"+object);
          return object;

     }

     /**
      * 删除被缓存的对象
      * 
      * @param key
      */
     public void removeCacheByKey(String key)
     {

          this.flushEntry(key);

     }

     /**
      * 删除所有被缓存的对象
      */
     public void removeAll(Date date)
     {

          this.flushAll(date);
     }

     public static BaseCache newInstance(String key)
     {
          int refreshPeriod = 0;

          if (key.startsWith(BaseCache.PERIOD_DAY))
          {
               refreshPeriod = 86400;
          }

          return BaseCache.newInstance(refreshPeriod);
     }

     public static BaseCache newInstance()
     {
          return BaseCache.newInstance(1800);
     }

     public static BaseCache newInstance(int refreshPeriod)
     {
          if (instance == null)
          {
               synchronized (lock)
               {
                    if (instance == null)
                    {
                         instance = new BaseCache(refreshPeriod);
                    }
               }
          }

          return instance;
     }

     public int getRefreshPeriod()
     {
          return refreshPeriod;
     }

     public void main(String arg[])
     {

          // BaseCache cache = BaseCache.newInstance();
          // cache.set(_);

     }
}
