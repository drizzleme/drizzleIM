package com.github.drizzleme.core.bo;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class PLoginInfo {
    private String loginName = null;
    private String loginPsw = null;
    private String extra = null;

    public PLoginInfo(String loginName, String loginPsw)
    {
        this(loginName, loginPsw, null);
    }

    public PLoginInfo(String loginName, String loginPsw, String extra)
    {
        this.loginName = loginName;
        this.loginPsw = loginPsw;
        this.extra = extra;
    }

    public String getLoginName()
    {
        return this.loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getLoginPsw()
    {
        return this.loginPsw;
    }

    public void setLoginPsw(String loginPsw)
    {
        this.loginPsw = loginPsw;
    }

    public String getExtra()
    {
        return extra;
    }

    public void setExtra(String extra)
    {
        this.extra = extra;
    }
}
