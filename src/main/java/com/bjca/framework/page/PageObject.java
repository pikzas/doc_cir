package com.bjca.framework.page;

import java.util.Collection;

/**
 * 
 *Administrator 下午10:21:13
 * 
 */
public class PageObject
{

     public PageObject()
     {
     }

     public int getTotal()
     {
          return total;
     }

     public void setTotal(int total)
     {
          this.total = total;
     }

     /**
      * 当前第几页
      * 
      * @return
      */
     public int getCurrentPage()
     {
          return currentPage;
     }

     public void setCurrentPage(int currentPage)
     {
          this.currentPage = currentPage;
     }

     /**
      * 每页显示的条数
      * 
      * @return
      */
     public int getNeverypage()
     {
          return neverypage;
     }

     public void setNeverypage(int neverypage)
     {
          this.neverypage = neverypage;
     }

     public Collection<?> getResults()
     {
          return results;
     }

     public void setResults(Collection<?> results)
     {
          this.results = results;
     }

     public String getUrl()
     {
          return url;
     }

     public void setUrl(String url)
     {
          this.url = url;
     }

     /*
      * public int getPages() { if(neverypage < 1) setNeverypage(Config.getConfig().getInt("web.pagelist", 15)); return total %
      * neverypage != 0 ? total / neverypage + 1 : total / neverypage; }
      */

     private int        total;
     private int        currentPage;
     private int        neverypage;
     private Collection<?> results;
     private String     url;
}
