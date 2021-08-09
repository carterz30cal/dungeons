package com.carterz30cal.potions;

public class ActivePotion
{
	public PotionType type;
	public int level;
	public AbsPotion ability;
	
	public ActivePotion(PotionType t,int l)
	{
		type = t;
		level = l;
		ability = (AbsPotion) type.create(level);
	}
}
