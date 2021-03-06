package com.carterz30cal.utility;

public class Mafs 
{
	public static double getCircleX(int degrees,double radius)
	{
		return radius * Math.sin(degrees);
	}
	public static double getCircleZ(int degrees,double radius)
	{
		return radius * Math.cos(degrees);
	}
	
	public static double getCircleX(double degrees,double radius)
	{
		return radius * Math.sin(degrees);
	}
	public static double getCircleZ(double degrees,double radius)
	{
		return radius * Math.cos(degrees);
	}
	
	public static int manhatten(int x1,int z1,int x2,int z2)
	{
		return Math.abs(x2-x1) + Math.abs(z2-z1);
	}
}
