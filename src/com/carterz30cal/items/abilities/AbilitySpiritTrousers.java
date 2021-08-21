package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilitySpiritTrousers extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Spirit Power");
		d.add("While a ghost, gain 20 damage");
		return d;
	}
	
	public void stats(DungeonsPlayerStats s) 
	{
		if (s.persistentdata.contains("ghost"))
		{
			s.damage += 20;
		}
	}

}
