package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

import org.bukkit.ChatColor;

public class AbilityHealerRed extends AbsAbility 
{
	public static Map<DungeonsPlayer,Integer> healing = new HashMap<>();

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Healing");
		d.add("Heal all nearby players for 12% of your max health");
		d.add("every 1200 damage that you deal to enemies.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		healing.put(d, healing.getOrDefault(d, 0) + damage);
		if (healing.get(d) >= 1200)
		{
			healing.put(d, Math.max(0, healing.get(d) - 1200));
			int heal = Math.round(d.stats.health * 0.12f);
			int totalheal = 0;
			for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(),10,10,10))
			{
				if (!(e instanceof Player)) continue;
				DungeonsPlayer du = DungeonsPlayerManager.i.get((Player)e);
				if (du != null && du != d) 
				{
					du.heal(heal);
					totalheal += heal;
				}
			}
			if (totalheal > 0) d.player.sendMessage(ChatColor.GREEN + "You healed players around you by " + totalheal + "!");
		}
		
		return damage;
	}

}
