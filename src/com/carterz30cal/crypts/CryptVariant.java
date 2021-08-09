package com.carterz30cal.crypts;


import org.bukkit.ChatColor;

import com.carterz30cal.utility.StringManipulator;

public enum CryptVariant
{
	CORRUPT("The incessant plague has corrupted this crypt.."),
	LIVING("This crypt is home to the evils of nature.");
	
	private String message;
	
	public String getMessage()
	{
		return ChatColor.DARK_PURPLE + message;
	}
	@Override
	public String toString()
	{
		return StringManipulator.capitalise(this.name());
	}
	private CryptVariant(String msg)
	{
		message = msg;
	}
}
