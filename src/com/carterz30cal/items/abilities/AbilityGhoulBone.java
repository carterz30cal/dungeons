package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityGhoulBone extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Ghoulbuster");
		desc.add("Deals +75% damage to ghouls");
		return desc;
	}

	@Override
	public int onAttack(DungeonsPlayer d,DMob mob,int damage)
	{
		if (mob.type.tags.contains("ghoul")) return (int) (damage * 1.75);
		else return damage;
	}
}
