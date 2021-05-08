package com.carterz30cal.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.utility.InventoryHandler;

import net.md_5.bungee.api.ChatColor;

public class PlayerDelivery
{
	public UUID recipient;
	
	public long xp;
	public int coins;
	public String items;
	
	
	public void combine(PlayerDelivery delivery)
	{
		xp += delivery.xp;
		coins += delivery.coins;
		items = items + ";" + delivery.items;
	}
	public void deliver()
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(Bukkit.getPlayer(recipient));
		d.level.give(xp,false);
		d.coins += coins;
		
		Player p = d.player;
		p.sendMessage(ChatColor.GOLD + "Your delivery has arrived!");
		if (coins > 0) p.sendMessage(ChatColor.AQUA + "+ " + coins + " coins");
		if (xp > 0) p.sendMessage(ChatColor.AQUA + "+ " + xp + " xp");
		for (String s : items.split(";"))
		{
			String[] itemd = s.split(",");
			int amount = 1;
			if (itemd.length == 2) amount = Integer.parseInt(itemd[1]);
			ItemStack item = ItemBuilder.i.build(itemd[0], amount);
			InventoryHandler.addItem(d, item, true);
			p.sendMessage(ChatColor.AQUA + "+ " + item.getItemMeta().getDisplayName() + ChatColor.WHITE + " (x" + amount + ")");
		}
	}
}
