package com.carterz30cal.utility;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;

public class RandomFunctions
{
	public static Random r;
	public static int random(int lB,int uB)
	{
		if (lB > uB) return r.nextInt((lB+1)-uB)+uB;
		return r.nextInt((uB+1)-lB)+lB;
	}
	public static float random(float lB,float uB)
	{
		int ilB = (int)lB;
		int iuB = (int)uB;
		return r.nextInt((iuB+1)-ilB)+ilB;
	}
	public static double random(double lB,double uB)
	{
		return (r.nextDouble()*(uB-lB))+lB;
	}
	
	public static Location offset(Location l, double range)
	{
		return l.add(random(-range,range),random(-range,range),random(-range,range));
	}
	
	public static <T> ArrayList<T> shuffle(ArrayList<T> list)
	{
		ArrayList<T> n = new ArrayList<T>();
		
		for (T o : list)
		{
			n.add(random(0,n.size()), o);
		}
		return n;
	}
	public static <T> T get(T[] array)
	{
		if (array == null) return null;
		return array[random(0,array.length-1)];
	}
}
