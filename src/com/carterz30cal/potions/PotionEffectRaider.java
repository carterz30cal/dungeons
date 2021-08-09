package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import com.carterz30cal.player.DungeonsPlayerStats;

public class PotionEffectRaider extends AbsPotion 
{

	@Override
	public void text(List<String> lore)
	{
		lore.add("Increase all combat stats by " + (8 + 2*level) + "%");
		lore.add("while in the Temple. Decrease xp gain by 5%");
	}

	@Override
	public void finalStats(DungeonsPlayerStats s) 
	{
		double inc = 1 + (0.02 * (level+4));
		s.miningXp *= 0.95;
		s.damage *= inc;
		s.damagemod *= inc;
		s.damageSweep *= inc;
		s.mana *= inc;
	}
	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}

}
