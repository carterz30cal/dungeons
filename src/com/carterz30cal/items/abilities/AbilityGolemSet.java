package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityGolemSet extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{

		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Hunk");
		d.add("Reduce damage taken by 5%");
		d.add("Reduce it by another 10% if");
		d.add("the attacker was a Defender");
		return d;
	}

	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{
		if (mob == null) return 1;
		if (mob.tags.contains("defender")) return 0.85;
		else return 0.95; 
	} 
}
