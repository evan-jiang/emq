package com.tdpark.domain;
public class White {
    /*** 主键 ***/
    private long id;
    /*** 允许域名 ***/
    private String domain;
    /*** 权限ID ***/
    private String app_id;
    /*** 秘钥 ***/
    private String secret;
    /*** 主键 ***/
    public long getId(){
        return this.id;
    }
    /*** 主键 ***/
    public void setId(long id){
        this.id = id;
    }
    /*** 允许域名 ***/
    public String getDomain(){
        return this.domain;
    }
    /*** 允许域名 ***/
    public void setDomain(String domain){
        this.domain = domain;
    }
    /*** 权限ID ***/
    public String getApp_id(){
        return this.app_id;
    }
    /*** 权限ID ***/
    public void setApp_id(String app_id){
        this.app_id = app_id;
    }
    /*** 秘钥 ***/
    public String getSecret(){
        return this.secret;
    }
    /*** 秘钥 ***/
    public void setSecret(String secret){
        this.secret = secret;
    }

}