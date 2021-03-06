package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityPrecursorCrown extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Precursor Greed");
		d.add("Deal +150% damage to ancients");
		d.add("Take +100% more damage from ancients");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		if (dMob.type.tags.contains("ancient")) return (int) (damage * 2.5);
		return damage;
	} 
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{ 
		if (mob == null) return 1;
		if (mob.tags.contains("ancient")) return 2;
		return 1;
	} 
}
