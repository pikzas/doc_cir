package com.bjca.ecopyright.system.model;

import com.bjca.framework.bean.BaseBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Menu extends BaseBean {

	private String id; //
	private String name; //菜单名称
	private String url; //菜单url
	private Integer sort; //菜单顺序
	private Integer level;//菜单层级
	private String description; //菜单描述
	private String pId; //父节点
	private Date createDate; //创建时间
	private Date updateDate; //修改时间
	
	private List<Menu> children = new ArrayList<>();//子节点数据
	public Menu(){

	}

	public Menu(String id, String name, String url, Integer sort, Integer level, String description, String pId, Date createDate, Date updateDate, List<Menu> children) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.sort = sort;
		this.level = level;
		this.description = description;
		this.pId = pId;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Menu menu = (Menu) o;

		return id.equals(menu.id);

	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + url.hashCode();
		result = 31 * result + sort.hashCode();
		result = 31 * result + level.hashCode();
		result = 31 * result + description.hashCode();
		result = 31 * result + pId.hashCode();
		result = 31 * result + createDate.hashCode();
		result = 31 * result + updateDate.hashCode();
		result = 31 * result + children.hashCode();
		return result;
	}
}