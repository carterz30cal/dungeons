package com.carterz30cal.player;

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
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) e.setCancelled(true);
		else return;
		Material b = e.getBlock().getType();
		Dungeon d = DungeonManager.i.dungeons.getOrDefault(DungeonManager.i.hash(e.getPlayer().getLocation().getBlockZ()),DungeonManager.i.hub);
		DungeonsPlayer p = DungeonsPlayerManager.i.get(e.getPlayer());
		if (d.mining.blocks.containsKey(b))
		{
			int xp = (int)Math.ceil(d.mining.xp * p.stats.miningXp);
			if (p.skills.add("mining", xp)) p.skills.sendLevelMessage("mining", e.getPlayer());
			
			if (Math.random() <= p.stats.oreChance) 
			{
				ItemStack ore = ItemBuilder.i.build(d.mining.ores.get(b),null);
				ore.setAmount(1+(int)(Math.random()*3));
				e.getPlayer().getInventory().addItem(ore);
				
				new SoundTask(e.getPlayer().getLocation(),e.getPlayer(),Sound.ENTITY_ITEM_PICKUP,1f,0.5f).runTask(Dungeons.instance);
				d.mining.progress();
			}
			new TaskBlockReplace(e.getBlock(),b).runTaskLater(Dungeons.instance, 100);
			e.getBlock().setType(d.mining.blocks.get(b));
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) e.setCancelled(true);
	}
}
