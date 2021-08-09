package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import com.carterz30cal.player.DungeonsPlayerStats;

public class PotionEffectIronskin extends AbsPotion 
{

	@Override
	public void text(List<String> lore)
	{
		lore.add("Gain " + (level*3) + "% armour.");
	}

	public void finalStats(DungeonsPlayerStats s) 
	{
		s.armour *= 1 + (0.03 * level);
	}
	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}

}
