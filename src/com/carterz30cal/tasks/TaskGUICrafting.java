package com.carterz30cal.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.crafting.Recipe;
import com.carterz30cal.crafting.RecipeManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.EnchantHandler;
import com.carterz30cal.gui.GUI;
import com.carterz30cal.gui.GUICreator;
import com.carterz30cal.gui.MenuType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskGUICrafting extends BukkitRunnable
{
	public ItemStack item;
	public int pos;
	public Player p;
	
	public static int[] slots = {
			3,4,5,
			12,13,14,
			21,22,23
	};
	public TaskGUICrafting (ItemStack i, int po,Player player)
	{
		item = i;
		pos = po;
		p = player;
	}
	@Override
	public void run()
	{
		DungeonsPlayer dp = DungeonsPlayerManager.i.get(p);
		GUI g = dp.gui;
		
		if (item == null)
		{
			// lol everything is here lol 
			ItemStack[] ingredients = new ItemStack[9];
			for (int i = 0; i < 9; i++)
			{
				ItemStack item = g.inventory.getItem(slots[i]);
				if (EnchantHandler.eh.isUIElement(item)) ingredients[i] = null;
				else ingredients[i] = item;
			}
			
			Recipe recipe = RecipeManager.getRecipe(ingredients);
			int amount = 1;
			// we use pos as the type of click.
			if (pos == 1)
			{
				amount = 0;
				while (recipe.isCraftable(ingredients))
				{
					ingredients = recipe.craft(ingredients);
					amount++;
				}
			}
			else
			{
				ingredients = recipe.craft(ingredients);
			}
			ItemStack product = recipe.product.clone();
			product.setAmount(amount*recipe.amount);
			
			DungeonsPlayerManager.i.get(p).skills.add("crafting", recipe.xp*amount);
			if (p.getInventory().firstEmpty() == -1) p.getWorld().dropItem(p.getLocation(), product);
			else p.getInventory().addItem(product);
			
			for (int j = 0; j < 9; j++)
			{
				if (ingredients[j] == null || ingredients[j].getAmount() <= 0) 
				{
					g.inventory.setItem(slots[j], GUICreator.item(Material.WHITE_STAINED_GLASS_PANE, "Ingredient Slot", null, 1));
				}
				else g.inventory.setItem(slots[j], ingredients[j]);
			}
			
			recipe = RecipeManager.getRecipe(ingredients);
			if (recipe == null || !recipe.isCraftable(ingredients))
			{
				g.inventory.setItem(GUICreator.bottom(GUICreator.typeSize(MenuType.CRAFTING))-9, 
						GUICreator.item(Material.BARRIER, ChatColor.RED + "Not a valid recipe", null, 1));
				for (int k = 0; k < 6;k++)
				{
					int r = k*9;
					g.inventory.setItem(r, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
					g.inventory.setItem(r+1, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
					g.inventory.setItem(r+7, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
					g.inventory.setItem(r+8, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
				}
			} 
			else 
			{
				g.inventory.setItem(GUICreator.bottom(GUICreator.typeSize(MenuType.CRAFTING))-9, recipe.product);
				for (int k = 0; k < 6;k++)
				{
					int r = k*9;
					Material m = Material.GREEN_STAINED_GLASS_PANE;
					if (DungeonsPlayerManager.i.get(p).colourblindMode) m = Material.BLUE_STAINED_GLASS_PANE;
					g.inventory.setItem(r, GUICreator.pane(m));
					g.inventory.setItem(r+1, GUICreator.pane(m));
					g.inventory.setItem(r+7, GUICreator.pane(m));
					g.inventory.setItem(r+8, GUICreator.pane(m));
				}
			}
		}
		else
		{
			if (EnchantHandler.eh.isUIElement(item)) g.inventory.setItem(pos, GUICreator.item(Material.WHITE_STAINED_GLASS_PANE, "Ingredient Slot", null, 1));
			else g.inventory.setItem(pos, item);
			
			ItemStack[] ingredients = new ItemStack[9];
			for (int i = 0; i < 9; i++)
			{
				ItemStack item = g.inventory.getItem(slots[i]);
				if (EnchantHandler.eh.isUIElement(item)) ingredients[i] = null;
				else ingredients[i] = item;
			}
			
			Recipe recipe = RecipeManager.getRecipe(ingredients);

			if (recipe == null || !recipe.isCraftable(ingredients))
			{
				g.inventory.setItem(GUICreator.bottom(GUICreator.typeSize(MenuType.CRAFTING))-9, 
						GUICreator.item(Material.BARRIER, ChatColor.RED + "Not a valid recipe", null, 1));
				for (int k = 0; k < 6;k++)
				{
					int r = k*9;
					g.inventory.setItem(r, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
					g.inventory.setItem(r+1, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
					g.inventory.setItem(r+7, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
					g.inventory.setItem(r+8, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
				}
			} 
			else 
			{
				g.inventory.setItem(GUICreator.bottom(GUICreator.typeSize(MenuType.CRAFTING))-9, recipe.product);
				for (int k = 0; k < 6;k++)
				{
					int r = k*9;
					Material m = Material.GREEN_STAINED_GLASS_PANE;
					if (DungeonsPlayerManager.i.get(p).colourblindMode) m = Material.BLUE_STAINED_GLASS_PANE;
					g.inventory.setItem(r, GUICreator.pane(m));
					g.inventory.setItem(r+1, GUICreator.pane(m));
					g.inventory.setItem(r+7, GUICreator.pane(m));
					g.inventory.setItem(r+8, GUICreator.pane(m));
				}
			}
		}
		
		
		new TaskGUI(g,p).runTaskLater(Dungeons.instance, 1);
	}

}
