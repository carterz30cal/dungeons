package com.carterz30cal.utility;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;

public class StringManipulator
{
	public static String[] progressChars = {"|","-","=","+","*"};
	public static int[] progressLengths =  {50,15,15,15,15};
	public static ChatColor[] rainbowOrder = {ChatColor.RED,ChatColor.LIGHT_PURPLE,ChatColor.DARK_PURPLE,ChatColor.DARK_BLUE,
			ChatColor.BLUE,ChatColor.AQUA,ChatColor.GREEN,ChatColor.YELLOW,ChatColor.GOLD};
    public static String[] romanNumerals = {"I","II","III","IV","V","VI","VII","VIII","IX","X"};
    private static String[] times = {"d","h","m","s"};
    private static int[] timesn = {1728000,72000,1200,20};
	public static String capitalise(String inp)
	{
		return inp.substring(0,1).toUpperCase() + inp.substring(1).toLowerCase();
	}
	
	public static boolean contains(String[] data,String thing)
	{
		if (data == null) return false;
		for (String s : data) if (s.equals(thing)) return true;
		return false;
	}
	public static String get(HashMap<Integer,String[]> data,String thing)
	{
		for (String[] d : data.values())
		{
			String n = get(d,thing);
			if (n != null) return n;
		}
		return null;
	}
	public static String get(String[] data,String thing)
	{
		if (data == null) return null;
		for (String s : data) if (s.split(":")[0].equals(thing)) return s.split(":")[1];
		return null;
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

	
	public static String time(int ticks,int period)
	{
		if (ticks < 20) return "Now!";
		String ret = "";
		int rem = ticks;
		for (int i = 0; i < times.length;i++)
		{
			if (Math.floorDiv(period, timesn[i]) > 0) 
			{
				ret = ret + Math.floorDiv(rem, timesn[i]) + times[i] + " ";
				rem -= (timesn[i] * Math.floorDiv(rem, timesn[i]));
			}
		}
		return ret;
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
	public static ArrayList<String> toArray(String orig)
	{
		ArrayList<String> n = new ArrayList<String>();
		n.add(orig);
		return n;
	}
}
