package com.carterz30cal.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.areas.AbsDungeonEvent;
import com.carterz30cal.areas.EventTicker;
import com.carterz30cal.bosses.BossManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemLootbox;
import com.carterz30cal.items.Shop;
import com.carterz30cal.items.ShopManager;
import com.carterz30cal.items.magic.ItemWand;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.InventoryHandler;

public class ListenerGUIEvents implements Listener
{
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent e)
	{
		Player p = (Player)e.getWhoClicked();
		DungeonsPlayer data = DungeonsPlayerManager.i.get(p);
		
		if (e.getCurrentItem() != null && e.getCurrentItem().isSimilar(ItemBuilder.menuItem)) 
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
		for (AbsDungeonEvent ev : EventTicker.events) if (ev.eventInteract(e)) return;
		if (e.getAction() == Action.LEFT_CLICK_AIR)
		{
			if (e.getItem() != null && ItemBuilder.get(e.getItem()) instanceof ItemWand) new WandGUI(p);
		}
		if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.getItem() != null && e.getClickedBlock().getType() == Material.JUKEBOX)
			{
				String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,null);

				if (it != null && BossManager.bossTypes.containsKey(it)) if (BossManager.summon(it)) e.getItem().setAmount(e.getItem().getAmount()-1);
			}
		}
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.getItem() != null && ItemBuilder.get(e.getItem()) instanceof ItemWand) ((ItemWand)ItemBuilder.get(e.getItem())).use(DungeonsPlayerManager.i.get(p),e.getItem());
			if (e.getItem() != null && e.getItem().isSimilar(ItemBuilder.menuItem))
			{
				new GUI(MenuType.MAINMENU,e.getPlayer());
			}
			else if (e.getItem() != null && e.getItem().hasItemMeta())
			{
				Item item = ItemBuilder.i.items.get(e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,null));
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
		Shop shop = ShopManager.shops.get(e.getRightClicked().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, ""));
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
			
			if (sp != "uielement" && mod != "uielement") player.skills.add("magic", 500);
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
		player.gui = null;
	}
}
