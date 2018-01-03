package com.bjca.ecopyright.soft.model;

import com.bjca.framework.bean.BaseBean;

/**
 * Created by pikzas on 2016/8/16.
 */
public class SoftwareAuditVO extends BaseBean {
    private String adminID;
    private String total;
    private String type;

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
