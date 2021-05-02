package com.carterz30cal.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.InventoryHandler;

import net.md_5.bungee.api.ChatColor;

public class TradeGUI extends GUI
{
	public TradeGUI linked;
	
	public boolean accepted;
	
	public List<ItemStack> items;
	public Player owner;
	
	private static final int[] ownSlots = {0,1,2,3,9,10,11,12};
	private static final int[] otherSlots = {5,6,7,8,14,15,16,17};
	
	private ItemStack[] contents;
	public TradeGUI(Player p)
	{
		super(p);
		
		owner = p;
		items = new ArrayList<>();
		inventory = Bukkit.createInventory(null, 27,"Trade");
		contents = new ItemStack[27];
		
		for (int i = 0; i < 27; i++)
		{
			if (i % 9 == 4) contents[i] = GUICreator.pane(Material.LIME_STAINED_GLASS_PANE);
		}
		
		contents[18] = GUICreator.item(Material.RED_STAINED_GLASS, ChatColor.RED + "Click to accept!", null);
		contents[19] = GUICreator.item(Material.GOLD_BLOCK, ChatColor.GOLD + "Add 50 coins", null);
		contents[26] = GUICreator.item(Material.YELLOW_STAINED_GLASS, ChatColor.GOLD + "Player hasn't accepted", null);
		inventory.setContents(contents);
		render(p);
	}
	public void refresh()
	{
		inventory.setContents(contents);
		
		// acceptance stuff
		
		if (accepted) inventory.setItem(18, GUICreator.item(Material.LIME_CONCRETE, ChatColor.GREEN + "Accepted!", null));
		if (linked.accepted) inventory.setItem(26, GUICreator.item(Material.LIME_CONCRETE, ChatColor.GREEN + "Accepted!", null));
		
		
		
		int coins = getCoins(true);
		
		int slot = 0;
		if (coins > 0)
		{
			slot++;
			inventory.setItem(0,GUICreator.item(Material.GOLD_NUGGET, ChatColor.GOLD + "" + coins + " coins",null, true));
		}
		
		for (ItemStack i : items)
		{
			if (i == null) continue;
			inventory.setItem(ownSlots[slot],i);
			slot++;
		}
		coins = getCoins(false);
		
		slot = 0;
		if (coins > 0)
		{
			slot++;
			inventory.setItem(5,GUICreator.item(Material.GOLD_NUGGET, ChatColor.GOLD + "" + coins + " coins",null, true));
		}
		
		for (ItemStack i : linked.items)
		{
			if (i == null) continue;
			inventory.setItem(otherSlots[slot],i);
			slot++;
		}
		
		
	}
	
	@Override
	public boolean handleDrag(InventoryDragEvent e,Player p)
	{
		return true;
	}
	public void unaccept()
	{
		accepted = false;
		linked.accepted = false;
		refresh();
		linked.refresh();
	}
	@Override
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		boolean in = false;
		for (int o : ownSlots) if (position == o) in = true;
		
		if (in && inventory.getItem(position) != null)
		{
			if (ItemBuilder.get(inventory.getItem(position)) == null
					&& !ItemBuilder.getItem(inventory.getItem(position)).equals("book"))
			{
				DungeonsPlayerManager.i.get(owner).coins += getCoins(true);
				items.removeIf((ItemStack i) -> i == null);
			}
			else
			{
				InventoryHandler.addItem(DungeonsPlayerManager.i.get(owner), inventory.getItem(position),false);
				items.remove(inventory.getItem(position));
			}
			
			unaccept();
		}
		else if (position == 18)
		{
			accepted = true;
			if (linked.accepted) finalise();
			else
			{
				refresh();
				linked.refresh();
			}
		}
		else if (position == 19)
		{
			unaccept();
			if (DungeonsPlayerManager.i.get(owner).coins < 50) return true;
			DungeonsPlayerManager.i.get(owner).coins -= 50;
			addItem(null);
		}
		else if (position > 26)
		{
			if (ItemBuilder.get(e.getCurrentItem()) == null && !ItemBuilder.getItem(e.getCurrentItem()).equals("book")) return true;
			addItem(e.getCurrentItem().clone());
			e.getCurrentItem().setAmount(0);
		}
		
		
		return true;
	}
	public void handleClose(InventoryCloseEvent e)
	{
		if (accepted && linked.accepted) return;
		DungeonsPlayer you = DungeonsPlayerManager.i.get(owner);
		if (you.gui == null) return;
		you.gui = null;
		you.coins += getCoins(true);
		items.removeIf((ItemStack i) -> i == null);
		
		for (ItemStack i : items) InventoryHandler.addItem(you, i, false);
		
		linked.owner.closeInventory();
	}
	public void finalise()
	{
		DungeonsPlayer you = DungeonsPlayerManager.i.get(owner);
		DungeonsPlayer other = DungeonsPlayerManager.i.get(linked.owner);

		you.coins += getCoins(false);
		other.coins += getCoins(true);
		items.removeIf((ItemStack i) -> i == null);
		linked.items.removeIf((ItemStack i) -> i == null);
		
		for (ItemStack i : items) InventoryHandler.addItem(other, i, false);
		for (ItemStack i : linked.items) InventoryHandler.addItem(you, i, false);
		
		you.player.playSound(you.player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
		you.player.sendMessage(ChatColor.GOLD + "Trade complete!");
		other.player.playSound(other.player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
		other.player.sendMessage(ChatColor.GOLD + "Trade complete!");
		
		owner.closeInventory();
		linked.owner.closeInventory();
	}
	
	
	
	
	
	public int getCoins(boolean self)
	{
		int coins = 0;

		List<ItemStack> checking;
		if (self) checking = items;
		else checking = linked.items;
		
		for (ItemStack i : checking)
		{
			if (i == null) coins += 50;
		}
		
		return coins;
	}
	public boolean canAdd(boolean coins)
	{
		boolean hasCoins = false;
		int itemcount = 0;
		for (ItemStack i : items)
		{
			if (i == null) hasCoins = true;
			else itemcount++;
		}
		
		if (hasCoins) itemcount++;
		return itemcount < 8;
	}
	public boolean addItem(ItemStack item)
	{
		if (!canAdd(item == null)) return false;
		accepted = false;
		items.add(item);
		unaccept();
		return true;
	}
}
