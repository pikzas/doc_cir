package com.bjca.framework.page;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 *Administrator 下午04:29:25
 * 
 */
public class QueryParamater
{

     private Map<String, Object> map         = new HashMap<String, Object>();
     private Class<?>               refClass;                   // 调用class
     private int                 currentPage = 0;            // 当前页数
     private int                 neverypage  = 150;           // 每页显示多少
     private int                 fromIndex   = 0;
     private int                 toIndex     = 0;
     private int                 totalCount  = 0;
     private int                 pageCount   = 0;
    
     public static int ORDER_ASC=0;//升序
     public static int ORDER_DESC=1;//降序

     
    
  
     public int getPageCount()
     {
          pageCount = this.totalCount / neverypage;
          if (this.totalCount % this.neverypage != 0)
          {
               this.pageCount += 1;
          }
          return pageCount;
     }

     public void setPageCount(int pageCount)
     {
          this.pageCount = pageCount;
     }

     public int getTotalCount()
     {
          return totalCount;
     }

     public void setTotalCount(int totalCount)
     {
          this.totalCount = totalCount;
     }

     public Map<String, Object> getMap()
     {
          return map;
     }

     public void put(String key, Object value)
     {
          this.map.put(key, value);
     }

     public Class<?> getRefClass()
     {
          return refClass;
     }

     public void setRefClass(Class<?> refClass)
     {
          this.refClass = refClass;
     }

     public int getCurrentPage()
     {
          return currentPage;
     }

     public void setCurrentPage(int currentPage)
     {
          this.currentPage = currentPage;
     }

     public int getNeverypage()
     {
          return neverypage;
     }

     public void setNeverypage(int neverypage)
     {
          this.neverypage = neverypage;
     }

     public int getFromIndex()
     {
          this.fromIndex = this.currentPage * this.neverypage;
          return fromIndex;
     }

     public void setFromIndex(int fromIndex)
     {
          this.fromIndex = fromIndex;
     }

     public int getToIndex()
     {

          this.toIndex = this.fromIndex + this.neverypage;
          if (this.toIndex > this.totalCount)
          {
               this.toIndex = this.totalCount;
          }
          return toIndex;
     }

     public void setToIndex(int toIndex)
     {
          this.toIndex = toIndex;
     }
     

}
