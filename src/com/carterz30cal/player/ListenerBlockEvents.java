package com.carterz30cal.player;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.tasks.TaskBlockReplace;

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
	public void onBlockBreak(BlockBreakEvent e)
	{
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) 
		{
			if (DungeonsPlayerManager.i.get(e.getPlayer()).inCrypt && e.getBlock().getType() == Material.COBWEB)
			{
				e.setDropItems(false);
				return;
			}
			else e.setCancelled(true);
		}
		else return;
		Material b = e.getBlock().getType();
		Dungeon d = DungeonManager.i.dungeons.getOrDefault(DungeonManager.i.hash(e.getPlayer().getLocation().getBlockZ()),DungeonManager.i.hub);
		DungeonsPlayer p = DungeonsPlayerManager.i.get(e.getPlayer());
		if (d.mining.blocks.containsKey(b))
		{
			DungeonMiningTable mine = new DungeonMiningTable();
			mine.xp = (int)Math.ceil(d.mining.xp);
			
			
			if (Math.random() <= p.stats.oreChance) mine.loot.put(d.mining.ores.get(b),1+(int)(Math.random()*3));
			for (AbsEnchant enchant : EnchantManager.get(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()))
			{
				DungeonMiningTable m = enchant.onMine(mine);
				if (m != null) mine = m;
			}
			for (String loot : mine.loot.keySet())
			{
				ItemStack o = ItemBuilder.i.build(loot, null);
				o.setAmount(mine.loot.get(loot));
				
				if (e.getPlayer().getInventory().firstEmpty() == -1) e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), o);
				else e.getPlayer().getInventory().addItem(o);
			}
			if (mine.loot.size() > 0)
			{
				new SoundTask(e.getPlayer().getLocation(),e.getPlayer(),Sound.ENTITY_ITEM_PICKUP,1f,0.5f).runTask(Dungeons.instance);
				p.level.give(mine.xp);
			}
			d.mining.progress();
			TaskBlockReplace tbr = new TaskBlockReplace(e.getBlock(),b);
			tbr.runTaskLater(Dungeons.instance, 100);
			Dungeons.instance.blocks.put(e.getBlock(), tbr);
			e.getBlock().setType(d.mining.blocks.get(b));
		}
	}
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent e)
	{
		e.setCancelled(!allowBlockPhysics && !allowedPhysics.contains(e.getChangedType()));
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
		else
		{
			e.getBlock().getState().update(true, false);
		}
	}
}
