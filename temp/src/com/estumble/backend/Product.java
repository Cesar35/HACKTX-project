package com.estumble.backend;

import java.util.ArrayList;

import com.huguesjohnson.ebayDemo.Category;

public class Product {
	private boolean liked;
	private boolean disliked;
	private String imageURL;
	
	private final String name;
	private final String description;
	private final ArrayList<Keyword> keywords;
	private final ArrayList<Category> categories;
	
	public Product(String name, String description)
	{
		this.name = name;
		this.description = description;
		this.keywords = new ArrayList<Keyword>();
		this.categories = new ArrayList<Category>();
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public boolean addKeyword(String s)
	{
		return addKeyword(Keyword.createKeyword(s));
	}
	
	public boolean addKeyword(Keyword k)
	{
		return keywords.add(k);
	}
	
	public boolean addCategory(Category c)
	{
		return categories.add(c);
	}
	
	public static Product parseProduct(String s)
	{
		//TODO: Implement method!
		return new Product("help","something to help you!");
	}
	
	public void like()
	{
		if(this.liked)
			return;
		
		this.liked = true;
		this.disliked = false;
		for(Keyword k : keywords)
		{
			k.increaseStanding();
		}
	}
	
	public void dislike()
	{
		if(this.disliked)
			return;
		
		this.disliked = true;
		this.liked = false;
		for(Keyword k : keywords)
		{
			k.decreaseStanding();
		}
	}
	
}
