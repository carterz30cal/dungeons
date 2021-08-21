package com.carterz30cal.items.abilities;

import org.bukkit.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.DungeonsPlayerStats;
import com.carterz30cal.utility.InventoryHandler;

import net.md_5.bungee.api.ChatColor;

public class AbilityGhostBoots extends AbsAbility
{
	public static Map<DungeonsPlayer,Integer> cooldowns = new HashMap<>();
	public static Map<DungeonsPlayer,GhostTask> tasks = new HashMap<>();
	
	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Ghostly Spirit");
		d.add("If you are below 10% max health, become a ghost");
		d.add("for 12 seconds. While you are a ghost, you will");
		d.add("deal 35 true damage to every mob around you and");
		d.add("heal nearby players for 18 health every second.");
		d.add("As a ghost, your stats are heavily reduced.");
		d.add("After 12 seconds, be revived at 60% health.");
		d.add("This ability has a cooldown of 100 seconds.");
		return d;
	}

	// nerf ghost stats
	public void stats(DungeonsPlayerStats s) 
	{
		if (tasks.containsKey(s.o))
		{
			s.health = 100;
			s.armour = 0;
			s.mana = 0;
		}
	}
	
	public void onEnd(DungeonsPlayer d) 
	{
		GhostTask t = tasks.get(d);
		if (t != null) t.end();
	}; // when server is closed.
	public void onLogOut(DungeonsPlayer d) 
	{
		GhostTask t = tasks.get(d);
		if (t != null) t.end();
	}; 
	public boolean allowTarget(DungeonsPlayer d, DMob m) 
	{
		return tasks.getOrDefault(d, null) == null;
	} 
	public void onTick  (DungeonsPlayer d) 
	{
		if (cooldowns.getOrDefault(d, 0) == 0 && d.getHealthPercent() < 0.1)
		{
			d.player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You became a ghost!");
			
			cooldowns.put(d, 2200);
			new GhostTask(d);
		}
		else if (cooldowns.containsKey(d)) cooldowns.put(d, Math.max(cooldowns.getOrDefault(d, 0) - 1,0));
	}
}

class GhostTask extends BukkitRunnable
{
	public DungeonsPlayer d;
	public Location tether;
	public ItemStack[] armour;
	
	private int ticks;
	public GhostTask(DungeonsPlayer player)
	{
		d = player;
		tether = d.player.getLocation();
		armour = d.player.getEquipment().getArmorContents();
		
		d.player.getEquipment().setArmorContents(new ItemStack[4]);
		d.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,200,0));
		d.player.setAllowFlight(true);
		d.player.setFlying(true);
		d.setHealth(1);
		AbilityGhostBoots.tasks.put(d, this);
		d.stats.persistentdata.add("ghost");
		
		for (Entity e : Dungeons.w.getNearbyEntities(tether, 10, 10, 10))
		{
			if (e instanceof Mob) ((Mob)e).setTarget(null);
		}
		
		runTaskTimer(Dungeons.instance,1,1);
	}
	
	public void end()
	{
		if (Bukkit.getOnlinePlayers().contains(d.player))
		{
			d.setHealth(0.6);
			for (ItemStack i : d.player.getEquipment().getArmorContents()) if (i != null) InventoryHandler.addItem(d,i,false);
			d.player.getEquipment().setArmorContents(armour);
			d.player.setAllowFlight(false);
			d.player.setFlying(false);
		}
		else
		{
			for (ItemStack i : armour) InventoryHandler.addItem(d,i,true);
		}
		d.stats.persistentdata.remove("ghost");
		AbilityGhostBoots.tasks.remove(d);
		try {if (!isCancelled()) cancel();} catch (IllegalStateException e) {}
	}
	
	@Override
	public void run()
	{
		ticks++;
		if (ticks == 200)
		{
			end();
			cancel();
			return;
		}
		else	
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, d.player.getLocation().add(0,1,0), 30, 0.2, 0.3, 0.2, 0, new Particle.DustOptions(Color.GRAY,0.6f));
			
			if (ticks % 20 == 19)
			{
				Set<DMob> attacked = new HashSet<>();
				for (Entity e : Dungeons.w.getNearbyEntities(tether, 10, 10, 10))
				{
					DMob mob = DMobManager.get(e);
					if (mob != null && !attacked.contains(mob)) mob.damage(35, d.player, true);
					else if (e instanceof Player)
					{
						DungeonsPlayer healing = DungeonsPlayerManager.i.get((Player)e);
						if (healing != null) healing.heal(18);
					}
				}
			}
			
			if (ticks % 10 != 0) return;
			// tether bit
			d.player.spawnParticle(Particle.VILLAGER_HAPPY, tether, 30, 0.3, 0.3, 0.3, 0);
			Location line = d.player.getLocation().add(0,1,0).subtract(tether);
			for (int p = 0; p < 50; p++) d.player.spawnParticle(Particle.VILLAGER_HAPPY, tether.clone().add(line.clone().multiply(p/50)), 1, 0, 0, 0, 0);
		}
	}
}






