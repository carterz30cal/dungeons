package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityAcidity extends AbsAbility
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Acidity");
		d.add("Deals 3x damage to Iron Golems");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		boolean has = false;
		for (Entity e : dMob.entities)
		{
			if (e.getType() == EntityType.IRON_GOLEM)
			{
				has = true;
				break;
			}
		}
		if (has) return damage * 3;
		else return damage;
	}
}
