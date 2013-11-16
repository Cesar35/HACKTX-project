package com.huguesjohnson.ebayDemo;

import java.util.ArrayList;

import com.estumble.backend.Product;

public class Category {
	public final String name;
	
	public final ArrayList<Product> products;
	public final ArrayList<Category> subCategories;
	public Category parent;
	
	
	public Category(String s)
	{
		this(s,null);
	}
	
	public Category(String n,Category p){
		this.name = n;
		this.products = new ArrayList<Product>();
		this.subCategories = new ArrayList<Category>();
		parent = p;
	}
	
	public boolean addProduct(Product p)
	{
		return this.products.add(p);
	}
	
	public boolean addCategory(Category sub)
	{
		return subCategories.add(sub);
	}
	
	public void setParent(Category p)
	{
		this.parent = p;
	}
	
}
