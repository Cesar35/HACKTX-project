package com.huguesjohnson.ebayDemo;


public class Category {
	public String CategoryName;
	public String CategoryID;
	public String CategoryLevel;
	public String CategoryParentID;
	public String LeafCategory;
	
	public String getCategoryName() {
		return CategoryName;
	}
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}
	public String getCategoryID() {
		return CategoryID;
	}
	public void setCategoryID(String categoryID) {
		CategoryID = categoryID;
	}
	public String getCategoryLevel() {
		return CategoryLevel;
	}
	public void setCategoryLevel(String categoryLevel) {
		CategoryLevel = categoryLevel;
	}
	public String getCategoryParentID() {
		return CategoryParentID;
	}
	public void setCategoryParentID(String categoryParentID) {
		CategoryParentID = categoryParentID;
	}
	public String getLeafCategory() {
		return LeafCategory;
	}
	public void setLeafCategory(String leafCategory) {
		LeafCategory = leafCategory;
	}
	
}