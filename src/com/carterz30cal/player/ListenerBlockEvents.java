package com.carterz30cal.player;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.tasks.TaskBlockReplace;

public class ListenerBlockEvents implements Listener 
{
	private Random r;
	
	public ListenerBlockEvents()
	{
		r = new Random();
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) e.setCancelled(true);
		else return;
		Material b = e.getBlock().getType();
		Dungeon d = DungeonManager.i.dungeons.getOrDefault(DungeonManager.i.hash(e.getPlayer().getLocation().getBlockZ()),DungeonManager.i.hub);
		DungeonsPlayer p = DungeonsPlayerManager.i.get(e.getPlayer());
		if (d.ores.containsKey(b))
		{
			if (p.skills.add("mining", (int) Math.round(d.orexp*p.stats.miningXp))) p.skills.sendLevelMessage("mining", e.getPlayer());
			if (Math.random() <= p.stats.oreChance) 
			{
				ItemStack ore = ItemBuilder.i.build(d.ores.get(b),null);
				ore.setAmount((int) Math.round(Math.random()*3));
				e.getPlayer().getInventory().addItem(ore);
				float pitch = (float)(0.4 + (r.nextDouble()*0.4));
				new SoundTask(e.getPlayer().getLocation(),e.getPlayer(),Sound.BLOCK_NOTE_BLOCK_CHIME,pitch,0.7f).runTaskLater(Dungeons.instance, 1);
			}
			new TaskBlockReplace(e.getBlock(),b).runTaskLater(Dungeons.instance, 100);
			e.getBlock().setType(d.oreReplacement);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) e.setCancelled(true);
	}
}
