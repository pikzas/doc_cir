package com.bjca.framework.threadpool;

/**
 * 
 *Administrator 下午06:22:49
 * 
 */
public class TestThread extends Thread
{

     private ThreadPool pool = null;
     private String     name = null;

     public TestThread(String name, ThreadPool pool)
     {
          this.name = name;
          this.pool = pool;
          this.start();
     }

     public void run()
     {
          System.out.println("---" + name + "----try to pop a thread object");
          Thread th = null;
          try
          {
               th = pool.pop(true);

          }
          catch (Exception e1)
          {
               // TODO Auto-generated catch block
               e1.printStackTrace();
          }
          if (th != null)
          {
               System.out.println("---" + name + "----pop a thread object");
          }
          try
          {
               Thread.currentThread().sleep(6000);
               pool.push();
               System.out.println("---" + name + "----push a thread object");
               Thread.currentThread().sleep(6000);
          }
          catch (InterruptedException e)
          {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
     }

     public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException
     {
          ThreadPool pool = ThreadPoolFactory.getNewThreadPool("test", 2, 1, 5, false, "java.lang.Thread");
          System.out.println("---------dddddd---------");
          TestThread myth = new TestThread("1", pool);
          for (int i = 2; i < 20; i++)
          {
               myth = new TestThread(i + "", pool);
          }

          /*
           * myth = new TestThread("3",pool); myth = new TestThread("4",pool); myth = new TestThread("5",pool);
           */

          try
          {
               Thread.currentThread().sleep(600000);
          }
          catch (InterruptedException e)
          {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
     }
}
