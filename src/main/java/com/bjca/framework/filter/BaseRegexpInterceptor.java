package com.bjca.framework.filter;

import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 *Administrator 下午04:06:05
 * 
 */
public class BaseRegexpInterceptor extends HandlerInterceptorAdapter implements InitializingBean
{

     protected String[]         patterns;

     /**
      * ORO's compiled form of this pattern. ORO fields are transient as they're not serializable. They will be reinitialized on
      * deserialization by the initPatternRepresentation() method.
      */
     protected static Pattern[] compiledPatterns;

     public void setPatterns(String[] patterns)
     {
          this.patterns = patterns;
     }

     public String[] getPatterns()
     {
          return patterns;
     }

     public void afterPropertiesSet() throws Exception
     {
          if (patterns == null || patterns.length == 0)
          {
               throw new IllegalArgumentException("patterns required");
          }
     }

     protected boolean matchAnyone(String pattern)
     {
          for (int i = 0; i < patterns.length; i++)
          {
               if (matches(pattern, i))
               {
                    return true;
               }
          }
          return false;
     }

     protected boolean matches(String pattern, int patternIndex)
     {
          try
          {
               boolean matched = pattern.matches(patterns[patternIndex]);
               return matched;
          }
          catch (Exception e)
          {
               e.printStackTrace();
               return false;
          }
     }
}
