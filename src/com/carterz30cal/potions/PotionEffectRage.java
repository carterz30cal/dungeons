package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import com.carterz30cal.player.DungeonsPlayerStats;

public class PotionEffectRage extends AbsPotion 
{

	@Override
	public void text(List<String> lore)
	{
		lore.add("Lose " + (4 + level) + "% of your health");
		lore.add("Gain " + (9 + level) + "% more damage.");
		lore.add("Gain 5 bonus coins.");
	}

	public void stats(DungeonsPlayerStats s) 
	{
		s.bonuskillcoins += 5;
	}
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.health *= 1 - (0.01 * (4+level));
		s.damage *= 1 + (0.01 * (9+level));
	}
	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}

}
