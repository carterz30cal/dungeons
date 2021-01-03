package com.carterz30cal.mobs;

import org.bukkit.entity.EntityType;

@Deprecated
public class MobTypeSlime extends DungeonMobType 
{
	public int size;
	public int splitAmount;
	public String split;
	
	public MobTypeSlime()
	{
		super();
		type = EntityType.SLIME;
	}
}
