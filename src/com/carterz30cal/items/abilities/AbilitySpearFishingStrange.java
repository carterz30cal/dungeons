package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilitySpearFishingStrange extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Fishin'..?");
		d.add("Deals 5 damage to guardians");
		d.add("and no damage to everything else");
		return d;
	}

	@Override
	public int onAttack(DungeonsPlayer d,DMob mob,int damage)
	{
		if (mob.type.tags.contains("spearfish"))
		{
			if (mob.type.tags.contains("fish2")) return 5;
			else return 0;
		}
		else return damage;
	}
}
