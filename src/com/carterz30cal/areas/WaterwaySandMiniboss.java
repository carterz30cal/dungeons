package com.carterz30cal.areas;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.BoundingBox;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.md_5.bungee.api.ChatColor;

public class WaterwaySandMiniboss extends AbsDungeonEvent 
{
	public DMob miniboss;
	public BoundingBox area = new BoundingBox(new Location(Dungeons.w,-47,92,21062),new Location(Dungeons.w,-33,104,21075));
	
	private List<Block> modified = new ArrayList<>();
	private Location tele = new Location(Dungeons.w,-40, 93, 21064);
	
	private List<DMob> heads = new ArrayList<>();
	
	private Location[] hydra_light_blue = {
			new Location(Dungeons.w,-40,94,21076),new Location(Dungeons.w,-40,94,21075),new Location(Dungeons.w,-40,94,21074),new Location(Dungeons.w,-40,94,21073),
			new Location(Dungeons.w,-40,94,21072),new Location(Dungeons.w,-40,94,21071),new Location(Dungeons.w,-40,95,21071),
			new Location(Dungeons.w,-37,94,21072),new Location(Dungeons.w,-38,94,21072),new Location(Dungeons.w,-39,94,21072),new Location(Dungeons.w,-43,94,21072),
			new Location(Dungeons.w,-40,94,21072),new Location(Dungeons.w,-41,94,21072),new Location(Dungeons.w,-42,94,21072)};
	private Location[] hydra_blue = {
			new Location(Dungeons.w,-37,94,21070),new Location(Dungeons.w,-43,94,21070),new Location(Dungeons.w,-40,95,21070)
	};
	public void spawn()
	{
		//-42,93,21062 corner 1 door
		//-37,98,21062 corner 2 door
		for (DungeonsPlayer p : area.getWithin()) p.player.teleport(tele);
		for (int x = -42; x <= -37;x++)
		{
			for (int y = 93; y <= 98;y++)
			{
				Block c = Dungeons.w.getBlockAt(x, y, 21062);
				if (c.getType() == Material.AIR)
				{
					modified.add(c);
					c.setType(Material.RED_TERRACOTTA);
				}
			}
		}
		for (Location l : hydra_light_blue)
		{
			Block b = Dungeons.w.getBlockAt(l);
			b.setType(Material.LIGHT_BLUE_TERRACOTTA);
			modified.add(b);
		}
		for (Location l : hydra_blue)
		{
			Block b = Dungeons.w.getBlockAt(l);
			b.setType(Material.BLUE_TERRACOTTA);
			modified.add(b);
		}
	}
	@Override
	public void end()
	{
		for (Block c : modified)
		{
			c.setType(Material.AIR);
		}
		modified.clear();
	}
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (!e.hasItem() || e.getItem() == null || e.getClickedBlock() == null) return false;

		String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"");
		
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		
		if (it.equals("essence_of_sand") && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.END_PORTAL_FRAME)
		{
			if (!area.getWithin().contains(d)) 
			{
				e.getPlayer().sendMessage(ChatColor.RED + "Not in boss room!");
				return true;
			}
			else if (miniboss != null) e.getPlayer().sendMessage(ChatColor.RED + "Boss is alive!");
			else
			{
				spawn();
				e.getItem().setAmount(e.getItem().getAmount()-1);
			}
			
		}
		return false;
	}
}
