package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import com.carterz30cal.player.DungeonsPlayerStats;

public class PotionEffectRegeneration extends AbsPotion 
{

	@Override
	public void text(List<String> lore)
	{
		lore.add("Gain " + (level*2) + " regen.");
	}

	public void stats(DungeonsPlayerStats s) 
	{
		s.regen += 2*level;
	}
	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}

}
