package com.huguesjohnson.ebayDemo;

import java.util.ArrayList;
import java.util.HashMap;


public class Category {
	public static final HashMap<String,Category> availableCategories = new HashMap<String,Category>();
	
	public final String CategoryID;
	
	private Category parent;
	private final ArrayList<Category> children;
	
	public String CategoryName;
	
	public String CategoryLevel;
	public String CategoryParentID;
	public String LeafCategory;
	
	private Category(String catID)
	{
		this.CategoryID = catID;
		this.children = new ArrayList<Category>();
	}
	
	public static Category createCategory(String catID)
	{
		if(availableCategories.get(catID) == null)
			availableCategories.put(catID, new Category(catID));
		return getCategory(catID);
	}
	
	public static Category getCategory(String catID)
	{
		return availableCategories.get(catID);
	}
	
	public String getCategoryName() {
		return CategoryName;
	}
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}
	public String getCategoryID() {
		return CategoryID;
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
	
	private void addChild(Category cat)
	{
		this.children.add(cat);
	}
	
	private void removeChild(Category cat)
	{
		this.children.remove(cat);
	}
	
	public ArrayList<Category> getChildren()
	{
		return new ArrayList<Category>(this.children);
	}
	
	public void setCategoryParentID(String categoryParentID) {
		CategoryParentID = categoryParentID;
		Category parent = Category.getCategory(categoryParentID);
		if(parent != null)
		{
			if(this.parent != null)
			{
				this.parent.removeChild(this);
			}
			this.parent = parent;
			this.parent.addChild(this);
		}
	}
	
	public String getLeafCategory() {
		return LeafCategory;
	}
	public void setLeafCategory(String leafCategory) {
		LeafCategory = leafCategory;
	}
	
}