package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityPrejudice extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Prejudice");
		d.add("Deals +33% damage to ancients");
		d.add("Heal 12❤ every hit");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		if (dMob.type.tags.contains("ancient"))
		{
			d.heal(12);
			return (int) (damage * 1.33);
			
		}
		return damage;
	}

}
