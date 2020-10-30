package com.carterz30cal.tasks;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.crafting.RecipeManager;
import com.carterz30cal.dungeons.EnchantHandler;
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
		if (EnchantHandler.eh.isUIElement(inp))
		{
			g.inventory.setItem(10, GUICreator.item(Material.ORANGE_STAINED_GLASS_PANE, "Item Slot", null, 1));
			g.page = 1;
		} else if (inp != null) g.inventory.setItem(10, inp);
		
		ItemStack i = g.inventory.getItem(10);
		if (EnchantHandler.eh.isUIElement(i))
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
		String r = RecipeManager.i.recipeBrowser_byIngredient.get(pa).get(g.page-1);
		FileConfiguration rc = RecipeManager.i.recipeConfig;
		
		String[] prod = rc.getString(r + ".product").split(",");
		ItemStack product;
		if (rc.getString(r + ".enchants","").equals("")) product = ItemBuilder.i.build(prod[0],null);
		else product = ItemBuilder.i.build(prod[0],null,rc.getString(r + ".enchants",""));
		
		if (prod.length > 1) product.setAmount(Integer.parseInt(prod[1]));
		else product.setAmount(1);
		
		g.inventory.setItem(16, product);
		
		HashMap<String,String> materials = new HashMap<String,String>();
		for (String mat : rc.getConfigurationSection(r + ".materials").getKeys(false))
		{
			String ing = rc.getString(r + ".materials." + mat);
			materials.put(mat,ing);
		}
		for (int k = 0; k < 3; k++)
		{
			String[] rec = rc.getString(r + ".recipe." + k,"0 0 0").split(" ");
			for (int j = 0; j < 3;j++)
			{
				if (rec[j].equals("0")) 
				{
					g.inventory.setItem(slots[(k*3)+j], GUICreator.pane(Material.YELLOW_STAINED_GLASS_PANE));
				}
				else 
				{
					String mat = materials.get(rec[j]).split(",")[0];
					ItemStack vis;
					if (pa.equals(mat))
					{
						vis = i.clone();
						vis.setAmount(Integer.parseInt(materials.get(rec[j]).split(",")[1]));
					}
					else
					{
						vis = ItemBuilder.i.build(mat,null);
						vis.setAmount(Integer.parseInt(materials.get(rec[j]).split(",")[1]));
					}
					g.inventory.setItem(slots[(k*3)+j], vis);
				}
			}
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
