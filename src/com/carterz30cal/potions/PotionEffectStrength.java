package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import com.carterz30cal.player.DungeonsPlayerStats;

public class PotionEffectStrength extends AbsPotion 
{

	@Override
	public void text(List<String> lore)
	{
		lore.add("Gain " + (level*10) + " damage.");
	}

	public void stats(DungeonsPlayerStats s) 
	{
		s.damage += 10*level;
	}
	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}

}
