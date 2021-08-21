package com.carterz30cal.enchants;

import java.util.List;
import java.util.Map;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayer;

public abstract class AbsEnch
{
	public int level;
	
	public abstract List<String> description();
	public void stats(DungeonsPlayer d,Map<String,Double> attributes) {}
	public void onMine(DungeonMiningTable mine) {};
	public int rarity() {return 1;}
	public int max() {return 1;}
	
	public static void add(Map<String,Double> attributes,String attribute,double attr)
	{
		double a = attributes.getOrDefault(attribute, 0d);
		attributes.put(attribute, a + attr);
	}
}
