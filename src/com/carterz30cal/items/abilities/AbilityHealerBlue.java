package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

import net.md_5.bungee.api.ChatColor;

public class AbilityHealerBlue extends AbsAbility 
{
	public static Map<DungeonsPlayer,Integer> healing = new HashMap<>();
	public static Map<DungeonsPlayer,Integer> cooldown = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Healing");
		d.add("Heal all nearby players by 14% of your max health");
		d.add("every 8 seconds. For every 450 healing this does");
		d.add("attack all enemies within 10 blocks for 60 + 40%");
		d.add("of your maximum mana as magic damage.");
		return d;
	}
	
	@Override
	public void onTick  (DungeonsPlayer d) 
	{
		int cool = cooldown.getOrDefault(d, 0);
		if (cool < 160) cooldown.put(d, cool+1);
		else
		{
			int heal = Math.round(d.stats.health * 0.14f);
			int totalheal = 0;
			for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(),10,10,10))
			{
				if (!(e instanceof Player)) continue;
				DungeonsPlayer du = DungeonsPlayerManager.i.get((Player)e);
				if (du != null && du != d) 
				{
					du.heal(heal);
					totalheal += heal;
					healing.put(d, healing.getOrDefault(d,0) + heal);
				}
			}
			if (totalheal > 0) d.player.sendMessage(ChatColor.GREEN + "You healed players around you by " + totalheal + "!");
			cooldown.put(d, 0);
		}
		
		if (healing.getOrDefault(d,0) >= 450)
		{
			healing.put(d, Math.max(healing.get(d) - 450,0));
			for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(),10,10,10))
			{
				DMob mob = DMobManager.get(e);
				if (mob != null) mob.damage(60 + (int)(d.stats.mana*0.4f), d, DamageType.MAGIC,false);
			}
		}
		
	}

}
