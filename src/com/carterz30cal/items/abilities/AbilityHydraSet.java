package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityHydraSet extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Uncontrollable Growth");
		d.add("For every person wearing Hydra armour");
		d.add("within 20 blocks, gain 20 armour and");
		d.add("3 damage.");
		d.add("You take 50% less damage from");
		d.add("the hydra.");
		return d;
	}

	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) 
	{
		if (mob == null) return 1;
		if (mob.id == "sands_hydrahead" || mob.id == "sands_hydraminion") return 0.5;
		return 1; 
	} 
	public void stats(DungeonsPlayerStats s) 
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p == s.p) continue;
			if (p.getLocation().distance(s.p.getLocation()) <= 20
					&& DungeonsPlayerManager.i.get(p).stats.settype == s.settype)
			{
				s.armour += 20;
				s.damage += 3;
			}
		}
	}
}
