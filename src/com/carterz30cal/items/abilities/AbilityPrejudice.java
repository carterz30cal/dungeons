package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityPrejudice extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Prejudice");
		d.add("Deals +70% damage to ancients");
		d.add("Heal 17❤ every hit");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		d.heal(17);
		if (dMob.type.tags.contains("ancient")) return (int) (damage * 1.7);
		else return damage;
	}

}
