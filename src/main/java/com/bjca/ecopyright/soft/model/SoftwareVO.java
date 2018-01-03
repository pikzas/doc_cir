package com.bjca.ecopyright.soft.model;

import com.bjca.framework.bean.BaseBean;

/**
 * Created by pikzas on 2016/8/16.
 */
public class SoftwareVO extends BaseBean {
    private String category;
    private String total;
    private String color;
    
    
    
    
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
}
