package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import com.carterz30cal.player.DungeonsPlayerStats;

public class PotionEffectMidasFlask extends AbsPotion 
{

	@Override
	public void text(List<String> lore)
	{
		lore.add("Gain " + level + " bonus coins.");
	}

	public void stats(DungeonsPlayerStats s) 
	{
		s.bonuskillcoins += level;
	}
	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}

}
