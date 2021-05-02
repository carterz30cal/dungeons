package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityCowDie extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Crystal Beef");
		d.add("Deals 8x damage to crystal cows");
		return d;
	}

	@Override
	public int onAttack(DungeonsPlayer d,DMob mob,int damage)
	{
		if (mob.type.tags.contains("crystalcow")) 
		{
			return damage * 8;
		}
		else return damage;
	}
}
