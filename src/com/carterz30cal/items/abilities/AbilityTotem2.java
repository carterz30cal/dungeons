package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.ParticleFunctions;

import net.md_5.bungee.api.ChatColor;

public class AbilityTotem2 extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Ancient Power");
		d.add("Using this in a " + ChatColor.GOLD + "dig site");
		d.add("will summon the remains of");
		d.add("a mighty precursor");
		d.add(ChatColor.BLUE + "You need 600 mana to use this item");
		return d;
	}

	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
		if (!d.useMana(600)) return false;
		
		new BukkitRunnable()
		{
			int tick = 0;
			@Override
			public void run()
			{
				if (tick == 100)
				{
					DMobManager.spawn("digging_boss2", new SpawnPosition(-15,88,22065));
					cancel();
					return;
				}
				ParticleFunctions.moving(new Location(Dungeons.w,-15,89,22065), Particle.CRIT, 15,0.4);
				tick++;
			}
			
		}.runTaskTimer(Dungeons.instance, 1, 1);
		
		e.getItem().setAmount(e.getItem().getAmount()-1);
		
		return true;
	}
}
