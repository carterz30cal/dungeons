package com.carterz30cal.gui;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.EnchantHandler;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemLootbox;
import com.carterz30cal.items.Shop;
import com.carterz30cal.items.ShopManager;
import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.tasks.TaskGUI;
import com.carterz30cal.tasks.TaskGUICrafting;

public class ListenerGUIEvents implements Listener
{
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent e)
	{
		Player p = (Player)e.getWhoClicked();
		DungeonsPlayer data = DungeonsPlayerManager.i.get(p);
		
		if (e.getCurrentItem() != null && e.getCurrentItem().isSimilar(ItemBuilder.menuItem)) 
		{
			new TaskGUI(new GUI(MenuType.MAINMENU,p),p).runTaskLater(Dungeons.instance, 1);
			e.setCancelled(true);
			return;
		}
		else
		{
			if (data.gui == null) return;
			else e.setCancelled(data.gui.handleClick(e,e.getRawSlot(),p));
		}
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDrop(PlayerDropItemEvent e)
	{
		if (e.getItemDrop().getItemStack().isSimilar(ItemBuilder.menuItem))
		{
			e.setCancelled(true);
		}
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.getItem() != null && e.getItem().isSimilar(ItemBuilder.menuItem))
			{
				new TaskGUI(new GUI(MenuType.MAINMENU,e.getPlayer()),p).runTaskLater(Dungeons.instance, 1);
			}
			else if (e.getItem() != null)
			{
				Item item = ItemBuilder.i.items.get(e.getItem().getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING));
				if (item != null && item.type.equals("lootbox"))
				{
					e.getItem().setAmount(e.getItem().getAmount()-1);
					new SoundTask(e.getPlayer().getLocation(),e.getPlayer(),Sound.BLOCK_NOTE_BLOCK_PLING,0.6f,0.6f).runTaskLater(Dungeons.instance, 1);
					new SoundTask(e.getPlayer().getLocation(),e.getPlayer(),Sound.BLOCK_NOTE_BLOCK_PLING,0.6f,0.7f).runTaskLater(Dungeons.instance, 7);
					new SoundTask(e.getPlayer().getLocation(),e.getPlayer(),Sound.BLOCK_NOTE_BLOCK_PLING,0.6f,0.8f).runTaskLater(Dungeons.instance, 14);
					
					new LootboxGUI((ItemLootbox)item,e.getPlayer());
				}
			}
		}
		
	}
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent e)
	{
		Shop shop = ShopManager.positions.get(e.getRightClicked());
		if (shop != null)
		{
			new ShopGUI(shop,e.getPlayer());
		}
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		Player p = (Player)e.getPlayer();
		DungeonsPlayer player = DungeonsPlayerManager.i.get(p);
		if (player.gui == null) return;
		if (player.gui.type == MenuType.BACKPACK)
		{
			int slot = 0;
			while (slot < 54)
			{
				if (player.gui.inventory.getItem(slot) == null) 
				{
					player.backpack[slot] = null;

				}
				else player.backpack[slot] = new BackpackItem(player.gui.inventory.getItem(slot),slot);
				slot++;
			}
		}
		else if (player.gui.type == MenuType.ENCHANTING && player.gui.drop)
		{
			int[] slots = {GUICreator.top(),GUICreator.top()+9,GUICreator.top()+18};
			
			for (int slot : slots)
			{
				ItemStack item = player.gui.inventory.getItem(slot);
				if (item == null) continue;
				if (EnchantHandler.eh.isUIElement(item)) continue;
				if (player.player.getInventory().firstEmpty() == -1) 
				{
					player.player.getWorld().dropItem(player.player.getLocation(), item);
				}
				else
				{
					player.player.getInventory().addItem(item);
				}
			}
		}
		else if (player.gui.type == MenuType.CRAFTING)
		{
			for (int slot : TaskGUICrafting.slots)
			{
				ItemStack item = player.gui.inventory.getItem(slot);
				if (!EnchantHandler.eh.isUIElement(item)) 
				{
					if (player.player.getInventory().firstEmpty() == -1) player.player.getWorld().dropItem(player.player.getLocation(), item);
					else player.player.getInventory().addItem(item);
				}
			}
		}
		else if (player.gui.type == MenuType.RECIPES)
		{
			ItemStack item = player.gui.inventory.getItem(10);
			if (!EnchantHandler.eh.isUIElement(item))
			{
				if (player.player.getInventory().firstEmpty() == -1) player.player.getWorld().dropItem(player.player.getLocation(), item);
				else player.player.getInventory().addItem(item);
			}
		}
		else if (player.gui instanceof AnvilGUI)
		{
			switch (player.gui.page)
			{
			case 1:
				if (player.gui.inventory.getItem(19) != null) player.player.getInventory().addItem(player.gui.inventory.getItem(19));
				if (player.gui.inventory.getItem(22) != null) player.player.getInventory().addItem(player.gui.inventory.getItem(22));
				if (player.gui.inventory.getItem(25) != null) player.player.getInventory().addItem(player.gui.inventory.getItem(25));
				break;
			case 2:
				if (player.gui.inventory.getItem(19) != null) player.player.getInventory().addItem(player.gui.inventory.getItem(19));
				if (player.gui.inventory.getItem(25) != null) player.player.getInventory().addItem(player.gui.inventory.getItem(25));
				break;
			}
		}
		else if (player.gui instanceof LootboxGUI)
		{
			for (ItemStack item : player.gui.inventory.getContents())
			{
				if (!EnchantHandler.eh.isUIElement(item)) player.player.getInventory().addItem(item);
			}
		}
		player.gui = null;
	}
}
