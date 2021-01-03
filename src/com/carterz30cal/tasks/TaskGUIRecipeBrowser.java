package com.carterz30cal.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.crafting.Recipe;
import com.carterz30cal.crafting.RecipeManager;
import com.carterz30cal.gui.GUI;
import com.carterz30cal.gui.GUICreator;
import com.carterz30cal.items.ItemBuilder;

public class TaskGUIRecipeBrowser extends BukkitRunnable
{
	public GUI g;
	public ItemStack inp;
	
	public static int[] slots = {
			3 ,4 ,5 ,
			12,13,14,
			21,22,23,
	};
	public TaskGUIRecipeBrowser (GUI gui, ItemStack input)
	{
		g = gui;
		inp = input;
	}
	@Override
	public void run() 
	{
		if (ItemBuilder.isUIElement(inp))
		{
			g.inventory.setItem(10, GUICreator.item(Material.ORANGE_STAINED_GLASS_PANE, "Item Slot", null, 1));
			g.page = 1;
		} else if (inp != null) g.inventory.setItem(10, inp);
		
		ItemStack i = g.inventory.getItem(10);
		if (ItemBuilder.isUIElement(i))
		{
			blank();
			return;
		}
		String pa = i.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"bedrock");
		if (!RecipeManager.i.recipeBrowser_byIngredient.containsKey(pa))
		{
			blank();
			return;
		}
		
		Recipe recipe = RecipeManager.i.recipeBrowser_byIngredient.get(pa).get(g.page-1);
		g.inventory.setItem(16, recipe.product);
		
		for (int j = 0; j < 9; j++) 
		{
			if (recipe.types[j] != null) g.inventory.setItem(slots[j], ItemBuilder.i.build(recipe.types[j], recipe.amounts[j]));
			else g.inventory.setItem(slots[j],GUICreator.pane(Material.YELLOW_STAINED_GLASS_PANE));
		}
		
		if (g.page > 1) g.inventory.setItem(30, GUICreator.item(Material.ARROW, "Page " + (g.page-1), null, 1));
		else g.inventory.setItem(30, GUICreator.pane());
		if (g.page < RecipeManager.i.recipeBrowser_byIngredient.get(pa).size())
		{
			g.inventory.setItem(32, GUICreator.item(Material.ARROW, "Page " + (g.page+1), null, 1));
		}
		else g.inventory.setItem(32, GUICreator.pane());
	}
	
	private void blank()
	{
		for (int slot = 0; slot < 36; slot++)
		{
			if (slot % 9 > 2 && slot % 9 < 6 && slot < 27) 
			{
				g.inventory.setItem(slot, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
			}
		}
		g.inventory.setItem(16, GUICreator.item(Material.BARRIER,ChatColor.RED + "No recipes associated with this item",null,1));
		
		g.inventory.setItem(30, GUICreator.pane());
		g.inventory.setItem(32, GUICreator.pane());
	}
}
