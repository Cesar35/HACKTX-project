package com.estumble.backend;

import java.util.HashMap;

//Simple multiton class so we avoid duplicate keywords.
public class Keyword {
	private static final HashMap<String,Keyword> globalKeywords = new HashMap<String,Keyword>();
	
	public final String value;
	
	private int standing;
	
	private Keyword(String val)
	{
		this.value = val;
		this.standing = 0;
	}
	
	//Works as getKeyword, but will create keyword if it does
	//not exist.
	public static Keyword createKeyword(String value)
	{
		if(!isKeyword(value))
		{
			globalKeywords.put(value, new Keyword(value));
		}
		return getKeyword(value);
	}
	
	public static Keyword getKeyword(String value)
	{
		return globalKeywords.get(value);
	}
	
	public static boolean isKeyword(String value)
	{
		return globalKeywords.get(value) != null;
	}
	
	public static boolean clearKeyword(String value)
	{
		Keyword k = getKeyword(value);
		if(k != null)
		{
			//TODO: Implement this.
			return true;
		}
		return false;
	}
	
	//We can use any sort of heuristic we want here
	//but I figure we will do something simple for now
	// - Benb
	public void increaseStanding() {
		standing += 1;
	}

	//We can use any sort of heuristic we want here
	//but I figure we will do something simple for now
	// - Benb
	public void decreaseStanding() {
		standing -= 1;
	}

}
