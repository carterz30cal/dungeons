package com.carterz30cal.gui;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.areas.AbsDungeonEvent;
import com.carterz30cal.areas.EventTicker;
//import com.carterz30cal.crypts.CryptMap;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemEngine;
import com.carterz30cal.items.ItemLootbox;
import com.carterz30cal.items.Shop;
import com.carterz30cal.items.ShopManager;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.items.magic.ItemWand;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.quests.Quest;
import com.carterz30cal.quests.QuestNpc;
import com.carterz30cal.utility.BoundingBox;
import com.carterz30cal.utility.InventoryHandler;

public class ListenerGUIEvents implements Listener
{
	public static ArrayList<Inventory> cancelled;
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent e)
	{
		Player p = (Player)e.getWhoClicked();
		DungeonsPlayer data = DungeonsPlayerManager.i.get(p);
		
		if (e.getClick() == ClickType.LEFT)
		{
			String i = ItemBuilder.getItem(e.getCursor());
			Item c = ItemBuilder.get(e.getCurrentItem());
			if (i != null && i.equals("luxium") && c instanceof ItemEngine)
			{
				ItemEngine engine = (ItemEngine)c;
				ItemMeta added = e.getCurrentItem().getItemMeta();
				int d = 0;
				while (e.getCursor().getAmount() > 0 && ItemBuilder.getFuel(added) < engine.capacity)
				{
					ItemBuilder.addFuel(added, 25);
					e.getCursor().setAmount(e.getCursor().getAmount()-1);
					d++;
				}
				if (d > 0) 
				{
					p.sendMessage(ChatColor.GOLD + "Added " + ChatColor.YELLOW + (d*25) + "✦" + ChatColor.GOLD + " to your Engine!");
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.6f, 1);
				}
				e.getCurrentItem().setItemMeta(ItemBuilder.i.updateMeta(added, data));
				e.setCancelled(true);
			}
		}
		if (e.getClick() == ClickType.NUMBER_KEY)
		{
			p.sendMessage(ChatColor.RED + "Please don't use hotkeys to move items.");
			e.setCancelled(true);
			return;
		}
		else if (e.getClick() == ClickType.SWAP_OFFHAND)
		{
			p.sendMessage(ChatColor.RED + "Offhand swapping is currently disabled!");
			e.setCancelled(true);
			return;
		}
		if (e.getCurrentItem() != null && (e.getCurrentItem().isSimilar(ItemBuilder.menuItem)))
		{
			new GUI(MenuType.MAINMENU,p);
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
	public void onInventoryDrag(InventoryDragEvent e)
	{
		Player p = (Player)e.getWhoClicked();
		DungeonsPlayer data = DungeonsPlayerManager.i.get(p);

		if (data.gui == null) return;
		else e.setCancelled(data.gui.handleDrag(e,p));
	}
	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent e)
	{
		e.getPlayer().sendMessage(ChatColor.RED + "Offhand is currently disabled!");
		e.setCancelled(true);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDrop(PlayerDropItemEvent e)
	{
		if (e.getItemDrop().getItemStack().isSimilar(ItemBuilder.menuItem))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteractClean(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		Action a = e.getAction();
		
		// don't allow barrel opening in crypts
		if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.BARREL)
		{
			if (!d.canOpen)
			{
				e.setUseInteractedBlock(Result.DENY);
				e.setCancelled(true);
			}
			return;
		}
		else if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST && new BoundingBox(new Location(Dungeons.w,-200,0,23800),new Location(Dungeons.w,200,255,24200)).isInside(d.player.getLocation())) return;
		else if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.BREWING_STAND && e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			e.setUseInteractedBlock(Result.DENY);
			e.setCancelled(true);
			
			new BrewingGUI(p);
			return;
		}
		
		for (AbsDungeonEvent ev : EventTicker.events) if (ev.eventInteract(e)) return;
		for (AbsAbility b : d.stats.abilities) 
		{
			if (b.onInteract(d, e)) 
			{
				e.setCancelled(true); 
				return;
			}
		}
		if (i == null) return;
		
		
		// left clicks
		if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)
		{
			if (ItemBuilder.get(e.getItem()) instanceof ItemWand) new WandGUI(p);
		}
		else if (a != Action.PHYSICAL) // all right clicks
		{
			Item it = ItemBuilder.get(i);
			if (i.isSimilar(ItemBuilder.menuItem)) new GUI(MenuType.MAINMENU,e.getPlayer());
			else if (it == null) return;
			else if (it instanceof ItemWand) ((ItemWand)it).use(d,i);
			else if (it instanceof ItemLootbox)
			{
				i.setAmount(i.getAmount()-1);
				
				Location l = p.getLocation();
				new SoundTask(l,p,Sound.BLOCK_NOTE_BLOCK_PLING,0.6f,0.6f).runTaskLater(Dungeons.instance, 1);
				new SoundTask(l,p,Sound.BLOCK_NOTE_BLOCK_PLING,0.6f,0.7f).runTaskLater(Dungeons.instance, 7);
				new SoundTask(l,p,Sound.BLOCK_NOTE_BLOCK_PLING,0.6f,0.8f).runTaskLater(Dungeons.instance, 14);
				
				new LootboxGUI((ItemLootbox)it,e.getPlayer());
			}
			else if (i.getType() != Material.BOW && it.material != Material.MILK_BUCKET) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent e)
	{
		QuestNpc questn = Quest.quests.getOrDefault(e.getRightClicked().getPersistentDataContainer().get(QuestNpc.kQuest, PersistentDataType.STRING)
				,null);
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		if (questn != null)
		{
			ArrayList<Quest> questl = questn.quests;
			
			if (d.questcooldown > 0) return;
			Quest quest = null;
			for (Quest q : questl)
			{
				if (d.questProgress.getOrDefault(q.id, "start").equals("finished")) continue;
				quest = q;
				break;
			}
			if (quest == null && !questl.isEmpty())
			{
				d.questcooldown = 12;
				return;
			}
			
			quest.interact(e.getPlayer());
			d.questcooldown = 10;
		}
		else
		{
			Shop shop = ShopManager.shops.get(e.getRightClicked().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, ""));
			if (shop != null)
			{
				new ShopGUI(shop,e.getPlayer());
			}
			else 
			{
				PlayerInteractEvent ev = new PlayerInteractEvent(e.getPlayer(),Action.RIGHT_CLICK_AIR,e.getPlayer().getInventory().getItemInMainHand(),null,null);
				for (AbsAbility a : d.stats.abilities) a.onInteract(d, ev);
			}
		}
		
	}
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e)
	{
		e.setCancelled(!DungeonsPlayerManager.i.get((Player) e.getPlayer()).canOpen);
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		Player p = (Player)e.getPlayer();
		DungeonsPlayer player = DungeonsPlayerManager.i.get(p);
		if (player == null) return;
		if (player.gui == null) return;
		if (player.gui instanceof BackpackGUI)
		{
			((BackpackGUI)player.gui).save(player);
		}
		else if (player.gui.type == MenuType.ENCHANTING && player.gui.drop)
		{
			int[] slots = {GUICreator.top(),GUICreator.top()+9,GUICreator.top()+18};
			
			for (int slot : slots)
			{
				ItemStack item = player.gui.inventory.getItem(slot);
				if (item == null) continue;
				if (ItemBuilder.isUIElement(item)) continue;
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
		else if (player.gui.type == MenuType.RECIPES)
		{
			ItemStack item = player.gui.inventory.getItem(10);
			if (!ItemBuilder.isUIElement(item))
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
			case 3:
				for (int i = 0; i < 9; i++)
				{
					ItemStack c = player.gui.inventory.getItem(AnvilGUI.craftingSlots[i]);
					if (c != null) player.player.getInventory().addItem(c);
				}
			}
		}
		else if (player.gui instanceof WandGUI)
		{
			ItemStack wand = player.player.getInventory().getItemInMainHand();
			ItemMeta m = wand.getItemMeta();
			
			String sp = player.gui.inventory.getItem(22).getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING);
			String mod = player.gui.inventory.getItem(24).getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING);
			if (sp != "uielement") m.getPersistentDataContainer().set(ItemWand.kSpell, PersistentDataType.STRING, sp);
			else m.getPersistentDataContainer().remove(ItemWand.kSpell);
			if (mod != "uielement") m.getPersistentDataContainer().set(ItemWand.kModifier, PersistentDataType.STRING, mod);
			else m.getPersistentDataContainer().remove(ItemWand.kModifier);
			
			//if (sp != "uielement" && mod != "uielement") player.skills.add("magic", 500);
			m = ItemBuilder.i.updateMeta(m, player);
			wand.setItemMeta(m);
		}
		else if (player.gui instanceof LootboxGUI)
		{
			for (ItemStack item : player.gui.inventory.getContents())
			{
				if (!ItemBuilder.isUIElement(item)) InventoryHandler.addItem(player, item);
			}
		}
		else player.gui.handleClose(e);
		player.gui = null;
	}
}
