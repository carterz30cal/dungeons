package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

import net.md_5.bungee.api.ChatColor;

public class AbilityHealerGreen extends AbsAbility 
{
	public static Map<DungeonsPlayer,Integer> cooldown = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Healing");
		d.add("Heal all nearby players by 25% of");
		d.add("your max health every 10 seconds.");
		return d;
	}
	
	@Override
	public void onTick  (DungeonsPlayer d) 
	{
		int cool = cooldown.getOrDefault(d, 0);
		if (cool < 200) 
		{
			cooldown.put(d, cool+1);
		}
		else
		{
			int heal = Math.round(d.stats.health * 0.25f);
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
			cooldown.put(d, 0);
		}
		
	}

}
