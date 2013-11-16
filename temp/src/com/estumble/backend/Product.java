package com.estumble.backend;

import com.huguesjohnson.ebayDemo.Listing;

import java.util.ArrayList;

import com.huguesjohnson.ebayDemo.Category;

enum ProductLikeStatus
{
	NO_ACTION,
	LIKED,
	DISLIKED;
}

public class Product {
	private ProductLikeStatus likeStatus;
	
	private final Listing listing;
	
	private final String name;
	private final String description;
	private final ArrayList<Keyword> keywords;
	private final ArrayList<Category> categories;
	
	public Product(Listing listing)
	{
		this.name = listing.getTitle();
		this.description = "GENERIC DESCRIPTION DAWG";
		this.listing = listing;
		this.keywords = new ArrayList<Keyword>();
		this.categories = new ArrayList<Category>();
	}
	
	public Product(String name, String description)
	{
		this.name = name;
		this.description = description;
		this.keywords = new ArrayList<Keyword>();
		this.categories = new ArrayList<Category>();
		this.listing = null;
	}

	public String getImageURL() {
		return this.listing.getImageUrl();
	}

	public void setImageURL(String imageURL) {
		this.listing.setImageUrl(imageURL);
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
	
	public ProductLikeStatus status()
	{
		return this.likeStatus;
	}
	
	public void like()
	{
		if(this.likeStatus == ProductLikeStatus.LIKED)
			return;
		
		this.likeStatus = ProductLikeStatus.LIKED;
		for(Keyword k : keywords)
		{
			k.increaseStanding();
		}
	}
	
	public void dislike()
	{
		if(this.likeStatus == ProductLikeStatus.DISLIKED)
			return;
		
		this.likeStatus = ProductLikeStatus.DISLIKED;

		for(Keyword k : keywords)
		{
			k.decreaseStanding();
		}
	}
	
	public Listing getListing()
	{
		return this.listing;
	}

	public void removeCategory(Category category) {
		this.categories.remove(category);
		
	}
	
}
