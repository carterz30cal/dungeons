package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityGolemSet extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{

		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Magical Construct");
		d.add("Increase your maximum mana by");
		d.add("15% of your armour stat.");
		return d;
	}

	public void finalStats(DungeonsPlayerStats s) 
	{
		s.mana += s.armour * 0.15d;
	}
}
