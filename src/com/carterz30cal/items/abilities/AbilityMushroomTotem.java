package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.ParticleFunctions;

import net.md_5.bungee.api.ChatColor;

public class AbilityMushroomTotem extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Shroom Rage");
		d.add("Challenge the Mushroom King");
		d.add(ChatColor.RED + "Consumed on use");
		return d;
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getItem() == null) return false;
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
		
		Location pl = d.player.getLocation().clone().add(0,1,0);
		new BukkitRunnable()
		{
			int tick = 0;
			@Override
			public void run()
			{
				if (tick == 150)
				{
					DMobManager.spawn("mushroom_king", new SpawnPosition(pl));
					cancel();
					return;
				}
				ParticleFunctions.moving(pl, Particle.CRIT, 15,0.4);
				tick++;
			}
			
		}.runTaskTimer(Dungeons.instance, 1, 1);
		
		e.getItem().setAmount(e.getItem().getAmount()-1);
		
		return true;
	}

}
