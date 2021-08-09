package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityTempleLooter extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Overconfidence");
		d.add("Whilst in the Temple:");
		d.add("- Gain 50% more regen");
		d.add("- Gain 100 damage");
		d.add("- Lose 150 armour");
		return d;
	}
	
	public void stats(DungeonsPlayerStats s) 
	{
		if (!s.o.inTemple()) return;
		s.damage += 100;
		s.armour -= 150;
	}

	public void finalStats(DungeonsPlayerStats s) 
	{
		if (!s.o.inTemple()) return;
		s.regen *= 1.5;
	}

}
