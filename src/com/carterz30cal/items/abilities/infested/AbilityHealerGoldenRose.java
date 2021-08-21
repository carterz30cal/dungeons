package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

//import net.md_5.bungee.api.ChatColor;

public class AbilityHealerGoldenRose extends AbsAbility 
{
	public static Map<DungeonsPlayer,Integer> cooldown = new HashMap<>();
	public static Map<DungeonsPlayer,Integer> roses = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Rose Power");
		d.add("Heal all nearby players and yourself by 15% of");
		d.add("your max health every 15 seconds.");
		d.add("Whenever you regain health, deal 9% of your melee damage");
		d.add("as magic damage to all mobs within 14 blocks. These mobs");
		d.add("also gain Slowness I for 7 seconds. 8 tick cooldown.");
		return d;
	}
	
	@Override
	public void onTick  (DungeonsPlayer d) 
	{
		int cool = cooldown.getOrDefault(d, 0);
		if (roses.getOrDefault(d, 0) > 0) roses.put(d, roses.getOrDefault(d, 0) - 1);
		if (cool < 300) 
		{
			cooldown.put(d, cool+1);
		}
		else
		{
			int heal = Math.round(d.stats.health * 0.15f);
			//int totalheal = 0;
			for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(),11,5,11))
			{
				if (!(e instanceof Player)) continue;
				DungeonsPlayer du = DungeonsPlayerManager.i.get((Player)e);
				if (du != null) 
				{
					du.heal(heal);
					//totalheal += heal;
				}
			}
			//if (totalheal > 0) d.player.sendMessage(ChatColor.GREEN + "You healed players around you by " + totalheal + "!");
			cooldown.put(d, 0);
		}
		
	}
	
	public void onHeal(DungeonsPlayer d,int amount) 
	{
		if (roses.getOrDefault(d, 0) > 0) return;
		roses.put(d, 8);
		Set<DMob> n = new HashSet<>();
		int rose = (int) (d.stats.damage * 0.09d);
		for (Entity e : Dungeons.w.getNearbyEntities(d.player.getLocation(), 14, 7, 14)) if (DMobManager.get(e) != null) n.add(DMobManager.get(e));
		for (DMob m : n) 
		{
			m.damage(rose, d, DamageType.MAGIC, false);
			((LivingEntity)m.entities.get(0)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,140,0,true));
		}
	}

}
