package com.carterz30cal.utility;

import org.bukkit.ChatColor;

public class StringManipulator
{
	public static String[] progressChars = {"|","-","=","+","*"};
	public static int[] progressLengths =  {50,15,15,15,15};
	public static ChatColor[] rainbowOrder = {ChatColor.RED,ChatColor.LIGHT_PURPLE,ChatColor.DARK_PURPLE,ChatColor.DARK_BLUE,
			ChatColor.BLUE,ChatColor.AQUA,ChatColor.GREEN,ChatColor.YELLOW,ChatColor.GOLD};
    public static String[] romanNumerals = {"I","II","III","IV","V","VI","VII","VIII","IX","X"};
	public static String capitalise(String inp)
	{
		return inp.substring(0,1).toUpperCase() + inp.substring(1).toLowerCase();
	}
	
	public static String progressBar(float percent,int selChar,boolean colourblind)
	{
		int todo = 0;
		String bar = ChatColor.GREEN + "";
		if (colourblind) bar = ChatColor.BLUE + "";
		while (todo < Math.round(percent*progressLengths[selChar]))
		{
			bar += progressChars[selChar];
			todo++;
		}
		if (todo < progressLengths[selChar]) bar += ChatColor.RED.toString();
		while (todo < progressLengths[selChar])
		{
			bar += progressChars[selChar];
			todo++;
		}
		return bar;
	}

	public static String rainbow(String original)
	{
		String rainbowed = "";
		double pos = 0;
		for (Character letter : original.toCharArray())
		{
			if (pos == rainbowOrder.length) pos = 0;
			rainbowed += rainbowOrder[(int)pos] + letter.toString();
			pos += 0.5;
		}
		return rainbowed;
	}
}
