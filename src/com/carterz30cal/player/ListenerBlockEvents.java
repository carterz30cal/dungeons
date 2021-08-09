package com.carterz30cal.player;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.tasks.TaskBlockReplace;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.RandomFunctions;

public class ListenerBlockEvents implements Listener 
{
	public static boolean allowBlockPhysics = false;
	public static final Set<Material> allowedPhysics = new HashSet<Material>();
	
	public ListenerBlockEvents()
	{
		allowedPhysics.add(Material.WEEPING_VINES_PLANT);
		allowedPhysics.add(Material.TWISTING_VINES_PLANT);
	}
	
	@EventHandler
	public void onCraftAttempt(CraftItemEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
	{
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		event.setCancelled(true);
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		
		if (d.player.getGameMode() == GameMode.CREATIVE) return;
		
		Material m = e.getBlock().getType();
		
		Dungeon du = d.area;
		if (du.mining.blocks.containsKey(m))
		{
			DungeonMiningTable mine = new DungeonMiningTable();
			mine.owner = d;
			mine.xp = (int)Math.ceil(du.mining.xp);
			
			
			int ores = (int) Math.floor((double)d.stats.fortune / 100);
			if (RandomFunctions.random(1, 100) <= d.stats.fortune - (ores*100)) ores++;
			if (ores > 0 && !du.mining.ores.get(m).equals("bad_item")) mine.loot.put(du.mining.ores.get(m), ores * RandomFunctions.random(3, 6));
			for (AbsEnchant enchant : EnchantManager.get(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()))
			{
				if (enchant == null) continue;
				DungeonMiningTable table = enchant.onMine(mine);
				if (table != null) mine = table;
			}
			for (String loot : mine.loot.keySet())
			{
				ItemStack o = ItemBuilder.i.build(loot, null);
				o.setAmount(mine.loot.get(loot));
				
				InventoryHandler.addItem(d, o, true);
			}

			d.level.giveFlat(mine.xp);
			d.player.playSound(d.player.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
			du.mining.progress();
			
			int chance = du.mining.chance;
			int out = du.mining.outof;
			double mult = 1;
			for (AbsEnchant en : d.stats.ench) mult = en.setRareOreMultiplier(d, mult);
			double c = 1/(((double)chance*mult)/out);
			
			if (d.inCrypt)
			{
				e.getBlock().setType(du.mining.blocks.get(m));
			}
			else if (m == du.mining.rareore)
			{
				e.getBlock().setType((Material) RandomFunctions.get(du.mining.rarecorrection.toArray()));
			}
			else if (Dungeons.instance.blocks.containsKey(e.getBlock()))
			{
				Dungeons.instance.blocks.get(e.getBlock()).run();
			}
			else if (RandomFunctions.random(1, (int)c) <= chance && m != du.mining.rareore && du.mining.replace)
			{
				e.getBlock().setType(du.mining.rareore);
				TaskBlockReplace tbr = new TaskBlockReplace(e.getBlock(),m);
				Dungeons.instance.blocks.put(e.getBlock(), tbr);
			}
			else 
			{
				e.getBlock().setType(du.mining.blocks.get(m));
				if (du.mining.replace)
				{
					TaskBlockReplace tbr = new TaskBlockReplace(e.getBlock(),m);
					tbr.runTaskLater(Dungeons.instance, 100);
					Dungeons.instance.blocks.put(e.getBlock(), tbr);
				}
			}
			
			
			
		}
		else e.setCancelled(true);
	}
	
	
	
	
	@EventHandler
	public void onBlockDamage(BlockDamageEvent e)
	{
		/*
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		if (d.area.mining.blocks.containsKey(e.getBlock().getType())) 
		{
			if (d.mining != null) d.mining.end();
			new TaskMining(d,e.getBlock());
		}
		*/
	}
	
	@EventHandler
	public void onExplode(BlockExplodeEvent e)
	{
		e.setYield(0);
	}
	
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockForm(BlockFormEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent e)
	{
		e.setCancelled(!allowBlockPhysics && !allowedPhysics.contains(e.getChangedType()));
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) 
		{
			e.getBlock().getState().update(true, false);
			return;
		}
		
		e.setCancelled(true);
	}
}
