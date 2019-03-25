package com.szmengran.hbase.entity;

import java.sql.Timestamp;

import com.szmengran.mybatis.utils.Table;

/**
 * @Package com.szmengran.entity
 * @Description: 文件信息
 * @date 2018年9月25日 下午5:12:06
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@Table(id = "fileid")
public class T_common_file{
    private String fileid;
    private String userid;
    private String type;
    private String path;
    private String suffix;
    private String orgname;
    private Long filesize;
    private String validstatus;
    private Timestamp createstamp;
    private Timestamp updatestamp;
    public String getFileid() {
        return fileid;
    }
    public void setFileid(String fileid) {
        this.fileid = fileid;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getOrgname() {
        return orgname;
    }
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public Long getFilesize() {
        return filesize;
    }
    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }
    public String getValidstatus() {
        return validstatus;
    }
    public void setValidstatus(String validstatus) {
        this.validstatus = validstatus;
    }
    public Timestamp getCreatestamp() {
        return createstamp;
    }
    public void setCreatestamp(Timestamp createstamp) {
        this.createstamp = createstamp;
    }
    public Timestamp getUpdatestamp() {
        return updatestamp;
    }
    public void setUpdatestamp(Timestamp updatestamp) {
        this.updatestamp = updatestamp;
    }
}
