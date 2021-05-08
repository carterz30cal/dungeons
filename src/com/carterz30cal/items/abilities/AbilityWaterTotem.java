package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.ParticleFunctions;

import net.md_5.bungee.api.ChatColor;

public class AbilityWaterTotem extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Titan's Return");
		d.add("Resummon the titan at will");
		d.add(ChatColor.RED + "Consumed on use");
		return d;
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getItem() == null) return false;
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
		
		for (Player p : DungeonManager.i.warps.get("waterway").players) p.sendMessage(d.player.getDisplayName() + ChatColor.RED + " has summoned the titan!");
		
		new BukkitRunnable()
		{
			int tick = 0;
			@Override
			public void run()
			{
				if (tick == 150)
				{
					DMobManager.spawn("drenched_titan", new SpawnPosition(-66,103,21002));
					cancel();
					return;
				}
				ParticleFunctions.moving(new Location(Dungeons.w,-66,103,21002), Particle.CRIT, 15,0.4);
				tick++;
			}
			
		}.runTaskTimer(Dungeons.instance, 1, 1);
		
		e.getItem().setAmount(e.getItem().getAmount()-1);
		
		return true;
	}

}
