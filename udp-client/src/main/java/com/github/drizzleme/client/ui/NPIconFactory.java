package com.github.drizzleme.client.ui;

import com.github.drizzleme.client.ui.util.NPHelper;
import com.github.drizzleme.client.ui.util.RawCacheRoot;
import org.jb2011.ninepatch4j.NinePatch;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class NPIconFactory extends RawCacheRoot<NinePatch> {
    /** root path(relative this NPIconFactory.class). */
    public final static String IMGS_ROOT="res";

    /** The instance. */
    private static NPIconFactory instance = null;

    /**
     * Gets the single instance of __Icon9Factory__.
     *
     * @return single instance of __Icon9Factory__
     */
    public static NPIconFactory getInstance()
    {
        if(instance==null)
            instance = new NPIconFactory();
        return instance;
    }

    /* (non-Javadoc)
     * @see org.jb2011.lnf.beautyeye.utils.RawCache#getResource(java.lang.String, java.lang.Class)
     */
    @Override
    protected NinePatch getResource(String relativePath, Class baseClass)
    {
        return NPHelper.createNinePatch(baseClass.getResource(relativePath), false);
    }

    /**
     * Gets the raw.
     *
     * @param relativePath the relative path
     * @return the raw
     */
    public NinePatch getRaw(String relativePath)
    {
        return  getRaw(relativePath,this.getClass());
    }

    /**
     * Gets the tooltip bg.
     *
     * @return the tooltip bg
     */
    public NinePatch getToastBg()
    {
        return getRaw(IMGS_ROOT+"/toast_bg.9.png");
    }
}
