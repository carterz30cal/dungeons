package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilitySpearFishingStrange extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Fishin'..?");
		d.add("Deals 4 damage to guardians");
		d.add("and no damage to everything else");
		return d;
	}

	@Override
	public int onArrowLand(DungeonsPlayer d,DMob mob,int damage)
	{
		if (mob.type.tags.contains("spearfish") && mob.type.tags.contains("fish2")) mob.damage(4, d, DamageType.TRUE,false);
		return damage;
	}
}
