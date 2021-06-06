package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.ParticleFunctions;

import net.md_5.bungee.api.ChatColor;

public class AbilityTotem2 extends AbsAbility
{
	public static Location summonLoc = new Location(Dungeons.w,-15,89,22065);
	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Ancient Power");
		d.add("Using this in a " + ChatColor.GOLD + "dig site");
		d.add("will summon the remains of");
		d.add("a mighty precursor");
		d.add(ChatColor.BLUE + "You need 400 mana to use this item");
		return d;
	}

	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
		else if (!d.useMana(400)) return false;
		else if (d.player.getLocation().distance(summonLoc) > 15)
		{
			d.player.sendMessage(ChatColor.RED + "You're too far away!");
			return false;
		}
		
		new BukkitRunnable()
		{
			ItemStack save = e.getItem().clone();
			int tick = 0;
			@Override
			public void run()
			{
				if (tick == 100)
				{
					String item = ItemBuilder.getItem(save);
					String mob = "digging" + item.split("_")[2] + "_boss";
					DMobManager.spawn(mob, new SpawnPosition(summonLoc));
					cancel();
					return;
				}
				ParticleFunctions.moving(summonLoc, Particle.CRIT, 15,0.4);
				tick++;
			}
			
		}.runTaskTimer(Dungeons.instance, 1, 1);
		
		e.getItem().setAmount(e.getItem().getAmount()-1);
		
		return true;
	}
}
