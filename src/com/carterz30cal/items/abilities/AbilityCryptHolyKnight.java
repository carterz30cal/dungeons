package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityCryptHolyKnight extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Holy Knight");
		d.add("While in a crypt, gain 50% damage");
		d.add("and double your regen.");
		return d;
	}
	
	@Override
	public void stats(DungeonsPlayerStats s)
	{
		if (!s.o.inCrypt) return;
		s.damagemod += 0.50;
	}
	@Override
	public void finalStats(DungeonsPlayerStats s)
	{
		if (!s.o.inCrypt) return;
		s.regen *= 2;
	}
}
