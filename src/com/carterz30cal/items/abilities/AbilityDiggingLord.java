package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityDiggingLord extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{

		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Precursed Lord");
		d.add("Reduce damage taken by 35%");
		d.add("from ancient mobs.");
		return d;
	}

	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{
		if (mob != null && mob.tags.contains("ancient")) return 0.65;
		return 1;
	} 

}
