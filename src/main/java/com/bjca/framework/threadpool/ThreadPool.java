package com.bjca.framework.threadpool;

import java.util.Vector;

/**
 * 
 *Administrator 下午06:18:57
 * 
 */
public class ThreadPool
{

     /**
      * 默认的最大客户连接数；
      */
     public final static int DEFAULT_INITIAL_INSTANCE_COUNT = 10;

     private String          threadClassName                = null;

     private Vector<Thread>  threadPool                     = new Vector<Thread>();

     /**
      * 允许的可使用的最大线程数；通过限制最大线程数来限制服务器的最大用户连接数；
      */
     private int             maxThreadCount                 = DEFAULT_INITIAL_INSTANCE_COUNT;

     /**
      * 当前的正在运行的线程数，即池外线程数；
      */
     private Integer         curThreadCount                 = 0;

     /**
      * 线程池已空是，否允许额外创建线程，以满足调用者；
      */
     private boolean         isAllowOverflow                = false;

     /**
      * 内部同步对象；
      */
     private static Object   waitForThread                  = new Object();

     public ThreadPool(int maxThreadCount, boolean isAllowFill, String threadClassName)
     {
          this.maxThreadCount = maxThreadCount;
          this.threadClassName = threadClassName;
          this.isAllowOverflow = isAllowFill;
     }

     public void InitThreadPool(int initialInstanceCount, int threadPriority)
     {
          try
          {
               for (int i = 0; i < initialInstanceCount; i++)
               {
                    Thread thread = (Thread) (Class.forName(this.threadClassName).newInstance());
                    thread.setPriority(threadPriority);
                    threadPool.addElement(thread);
               }
          }
          catch (Exception ex)
          {
               System.out.println("------------->Fail to initial thread pool [class name: " + this.threadClassName + " ]");
          }
     }

     /**
      * 获得一个线程对象。
      * 
      * @param isBlock
      *             是否阻塞直到获得一个线程为止；
      * @return
      * @throws InterruptedException
      * @throws ClassNotFoundException
      * @throws IllegalAccessException
      * @throws InstantiationException
      */
     public Thread pop(boolean isBlock) throws InterruptedException, InstantiationException, IllegalAccessException,
               ClassNotFoundException
     {
          Thread thread = null;
          /**
           * 1.首先尝试直接从池中获取对象；
           */
          try
          {
               thread = (Thread) threadPool.remove(0);
               synchronized (this.curThreadCount)
               {
                    this.curThreadCount++;
               }
               return thread;
          }
          catch (Exception ex)
          {
          }

          /**
           * 2.如果直接获取失败，则继续判断是否已经达到池的最大容量;
           * 
           */
          synchronized (this.curThreadCount)
          {
               /**
                * 2.1 若未到最大容量或池允许溢出，则直接创建对象并返回。
                */
               if ((this.curThreadCount < this.maxThreadCount) || (this.isAllowOverflow))
               {
                    thread = (Thread) (Class.forName(this.threadClassName).newInstance());
                    // 池外对象数加1；
                    this.curThreadCount++;
                    return thread;
               }
          }

          if (isBlock)
          {
               /**
                * 2.2 若已到达最大容量，如果允许溢出，则直接创建对象并返回，否则等待直到从池中取得对象为止。
                */
               synchronized (waitForThread)
               {
                    while (threadPool.size() == 0)
                    {
                         waitForThread.wait(1000);
                    }
                    thread = threadPool.remove(0);
                    synchronized (this.curThreadCount)
                    {
                         this.curThreadCount++;
                    }
               }
          }
          return thread;
     }

     /**
      * 将线程对象放回线程池中，以便下次使用；
      * 
      * @return true——表示push成功；false——表示push失败；
      */
     public boolean push()
     {
          synchronized (this.curThreadCount)
          {
               if (threadPool.size() < this.maxThreadCount)
               {
                    if (!threadPool.contains(Thread.currentThread()))
                    {
                         threadPool.addElement(Thread.currentThread());
                    }
               }
               // 池外对象数减1；
               this.curThreadCount--;
          }
          synchronized (waitForThread)
          {
               // 通知等待线程；
               waitForThread.notify();
          }
          return true;
     }

     public boolean isAllowOverflow()
     {
          return isAllowOverflow;
     }
}
