package com.github.drizzleme.client.ui.util;

import java.util.HashMap;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public abstract class RawCacheRoot<T> {
    /** 本地磁盘资源缓存中心（key=path,value=image对象）. */
    private HashMap<String,T> rawCache = new HashMap<String,T>();

    /**
     * 本地磁盘资源（如果缓存中已存在，则从中取之，否则从磁盘读取并缓存之）。.
     *
     * @param relativePath 本地磁盘资源相对于baseClass类的相对路径，比如它如果在/res/imgs/pic/下，baseClass在
     * /res下，则本地磁盘资源此处传过来的相对路径应该是/imgs/pic/some.png
     * @param baseClass 基准类，指定此类则获取本地磁盘资源时会以此类为基准取本地磁盘资源的相对物理目录
     * @return T
     */
    public T getRaw(String relativePath,Class baseClass)
    {
        T ic=null;

        String key = relativePath+baseClass.getCanonicalName();
        if(rawCache.containsKey(key))
            ic = rawCache.get(key);
        else
        {
            try
            {
                ic = getResource(relativePath, baseClass);
                rawCache.put(key, ic);
            }
            catch (Exception e)
            {
                System.out.println("取本地磁盘资源文件出错,path="+key+","+e.getMessage());
                e.printStackTrace();
            }
        }
        return ic;
    }

    /**
     * 本地资源获取方法实现.
     *
     * @param relativePath 相对路径
     * @param baseClass 基准类
     * @return the resource
     */
    protected abstract T getResource(String relativePath,Class baseClass);
}
