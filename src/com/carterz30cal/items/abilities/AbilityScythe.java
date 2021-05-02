package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Entity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityScythe extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Scythe");
		d.add("Also attacks every mob in a 2 block radius");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		Set<DMob> mobs = new HashSet<>();
		for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(), 2, 1, 2))
		{
			DMob m = DMobManager.get(e);
			if (m != null && dMob != m) mobs.add(m);
		}
		for (DMob mob : mobs) mob.damage(d.stats.damage, d, DamageType.PHYSICAL);
		return damage;
	}
}
