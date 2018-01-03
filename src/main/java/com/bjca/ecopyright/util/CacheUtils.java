package com.bjca.ecopyright.util;

import com.bjca.framework.page.ReflectUtil;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * @Author by pikzas.
 * @Date 2016-11-07
 */
//@Aspect
//@Component
public class CacheUtils {

    Log log = LogFactory.getLog(CacheUtils.class);
    @Autowired
    private MemcachedClient memcachedClient;


    public Object doAround(ProceedingJoinPoint call) {
        //返回最终结果
        Object result = null;
        //定义版本号，默认为1
        String version = "1";
        String targetClassName = null;
        String versionKey = null;
        try {
            targetClassName=getCglibProxyTargetObject(call.getThis()).getClass().getName();
            versionKey = targetClassName.hashCode()+"";
            log.debug(targetClassName);
        } catch (Exception e) {
            log.debug("获取AOP代理类的目标实现类异常！");
            e.printStackTrace();
        }
        Signature signature = call.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        if(method.isAnnotationPresent(Cache.class)){    //实现类上有CACHE注解 说明要进入缓存
            Cache cache = method.getAnnotation(Cache.class);
            if(cache!=null){
                //获取注解中缓存过期时间
                int expireTime = cache.expireTime();

                //获取版本号
                if(null != memcachedClient.get(versionKey)){
                    version = memcachedClient.get(versionKey).toString();
                }
                //获取缓存key 包名+"."+方法名+参数json字符串+版本号
                String cacheKey = targetClassName+"."+methodName+ JsonUtils.objectToJsonString(call.getArgs())+version;
                //获取方法名+参数key-value 的json +版本号 转 MD5
                String key = cacheKey.hashCode()+"";
                //存入memcached的最终key值
                result =memcachedClient.get(key);

                if(null == result){ //缓存中没有数据
                    try {
                        result = call.proceed();    //放行 获取结果
                        if(version.equals("1")){    //第一个版本 应该将版本信息也放入缓存中
                            memcachedClient.set(targetClassName.hashCode()+"",expireTime,version);
                        }
                        if(null!=result){
                            memcachedClient.set(key,expireTime, result);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }  else {   //缓存中有数据
                    log.debug("***************************"+targetClassName+"."+methodName+"  Get Data From Cache......"+"***************************");
                    return result;
                }

            }
        }else if(method.isAnnotationPresent(Flush.class)){  //实现类上有Flush注解 说明要更新缓存
            //如果修改操作时
            Flush flush = method.getAnnotation(Flush.class);
            if(flush!=null){
                try {
                    result = call.proceed();
                }catch (Throwable e){
                    e.printStackTrace();
                }
                //获取当前版本号
                if(null != memcachedClient.get(versionKey)){
                    version = memcachedClient.get(versionKey).toString();
                }
                //修改后，版本号+1 此处设定vkey缓存过期时间
                memcachedClient.replace(versionKey,300, Integer.parseInt(version.toString()) + 1);//此处默认时间300秒
            }
        }else{  //没有注解 什么都不做
            try {
                result = call.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {

        Object dynamicAdvisedInterceptor = ReflectUtil.getFieldValue(proxy, "CGLIB$CALLBACK_0");

        Object target = ((AdvisedSupport) ReflectUtil.getFieldValue(dynamicAdvisedInterceptor, "advised")).getTargetSource().getTarget();

        return target;
    }


}
