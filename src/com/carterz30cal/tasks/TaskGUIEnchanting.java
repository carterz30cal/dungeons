package com.carterz30cal.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.EnchantHandler;
import com.carterz30cal.gui.GUI;
import com.carterz30cal.gui.GUICreator;
import com.carterz30cal.gui.MenuType;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskGUIEnchanting extends BukkitRunnable
{
	public ItemStack item;
	public int slot;
	public Player player;
	
	public TaskGUIEnchanting(ItemStack i,int s,Player p)
	{
		item = i;
		slot = s;
		player = p;
	}
	@Override
	public void run()
	{
		ItemStack[] slots = {
				GUICreator.item(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE + "Enchantable Item Slot", null, 1),
				GUICreator.item(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.GOLD + "Book Slot", null, 1),
				GUICreator.item(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GRAY + "Catalyst Slot", null, 1)
		};
		GUI g = DungeonsPlayerManager.i.get(player).gui;
		g.inventory.setItem(slot, item);
		int row = Math.floorDiv(slot, 9);
		ItemStack pane = GUICreator.pane(Material.RED_STAINED_GLASS_PANE);
		if (!EnchantHandler.eh.isUIElement(item))
		{
			if (DungeonsPlayerManager.i.get(player).colourblindMode) pane = GUICreator.pane(Material.BLUE_STAINED_GLASS_PANE);
			else pane = GUICreator.pane(Material.GREEN_STAINED_GLASS_PANE);
		}
		for (int i = row*9;i < (row+1)*9;i++)
		{
			if (!EnchantHandler.eh.isUIElement(g.inventory.getItem(i))) continue;
			else if (i-(row*9) == 4) g.inventory.setItem(4+(row*9), slots[row]);
			else g.inventory.setItem(i, pane);
		}
		ItemStack enchantItem = g.inventory.getItem(GUICreator.top());
		ItemStack enchantBook = g.inventory.getItem(GUICreator.top()+9);
		ItemStack catalyst = g.inventory.getItem(GUICreator.top()+18);
		if (!EnchantHandler.eh.isUIElement(enchantItem)
				&& !EnchantHandler.eh.isUIElement(enchantBook)
				&& !EnchantHandler.eh.isUIElement(catalyst))
		{
			g.actions.clear();
			ItemStack reqCatalyst = ItemBuilder.i.build(EnchantHandler.eh.getRequiredCatalyst(enchantItem, enchantBook),null);
			if (catalyst.isSimilar(reqCatalyst))
			{
				for (int r = 3; r < 6;r++)
				{
					g.inventory.setItem((r*9)+3,GUICreator.pane(Material.PURPLE_STAINED_GLASS_PANE));
					g.inventory.setItem((r*9)+4,GUICreator.pane(Material.PURPLE_STAINED_GLASS_PANE));
					g.inventory.setItem((r*9)+5,GUICreator.pane(Material.PURPLE_STAINED_GLASS_PANE));
				}
				g.inventory.setItem(40, ItemBuilder.i.enchantItem(enchantItem, enchantBook));
			}
			else
			{
				for (int r = 3; r < 6;r++)
				{
					g.inventory.setItem((r*9)+3,GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
					g.inventory.setItem((r*9)+4,GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
					g.inventory.setItem((r*9)+5,GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
				}
				g.inventory.setItem(40, GUICreator.item(Material.BARRIER, 
						"This enchant requires a " + reqCatalyst.getItemMeta().getDisplayName(),
						null, 1));
			}
		} 
		else
		{
			for (int r = 3; r < 6;r++)
			{
				g.inventory.setItem((r*9)+3,GUICreator.pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
				g.inventory.setItem((r*9)+4,GUICreator.pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
				g.inventory.setItem((r*9)+5,GUICreator.pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
			}
			
			g.inventory.setItem(49, GUICreator.item(Material.RED_STAINED_GLASS_PANE, "Back", null, 1));
			g.actions.put(49,MenuType.MAINMENU);
		}
		new TaskGUI(g,player).runTaskLater(Dungeons.instance, 1);
	}
	
}
