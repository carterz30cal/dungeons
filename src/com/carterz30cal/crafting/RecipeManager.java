package com.carterz30cal.crafting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;

public class RecipeManager
{
	public static RecipeManager i;
	
	public HashMap<String,Recipe> recipes;
	public HashMap<String,ArrayList<String>> recipeBrowser_byIngredient;
	private File recipeFile;
	public final FileConfiguration recipeConfig;
	
	public RecipeManager()
	{
		i = this;
		
		// init file
		recipeFile = new File(Dungeons.instance.getDataFolder(), "recipes.yml");
		if (!recipeFile.exists())
		{
			recipeFile.getParentFile().mkdirs();
			Dungeons.instance.saveResource("recipes.yml",false);
		}
		
		recipeConfig = new YamlConfiguration();
		try 
		{
			recipeConfig.load(recipeFile);
        } 
		catch (IOException | InvalidConfigurationException e) 
		{
            e.printStackTrace();
        }
		// fetch recipes and construct the objects
		recipes = new HashMap<String,Recipe>();
		recipeBrowser_byIngredient = new HashMap<String,ArrayList<String>>();
		for (String p : recipeConfig.getKeys(false))
		{
			String[] prod = recipeConfig.getString(p + ".product").split(",");
			String enchants = recipeConfig.getString(p + ".enchants","");
			ItemStack product;
			if (enchants.equals("")) product = ItemBuilder.i.build(prod[0],null);
			else product = ItemBuilder.i.build(prod[0],null,enchants);

			if (prod.length > 1) product.setAmount(Integer.parseInt(prod[1]));
			else product.setAmount(1);
			
			int xp = recipeConfig.getInt(p + ".xp", 1); 
			
			HashMap<String,String> materials = new HashMap<String,String>();
			for (String mat : recipeConfig.getConfigurationSection(p + ".materials").getKeys(false))
			{
				String ing = recipeConfig.getString(p + ".materials." + mat);
				materials.put(mat,ing);
				recipeBrowser_byIngredient.putIfAbsent(ing.split(",")[0], new ArrayList<String>());
				if (!recipeBrowser_byIngredient.get(ing.split(",")[0]).contains(p)) recipeBrowser_byIngredient.get(ing.split(",")[0]).add(p);
			}
			String recipe = "";
			int[] amounts = new int[9];
			for (int i = 0; i < 3; i++)
			{
				String[] r = recipeConfig.getString(p + ".recipe." + i,"0 0 0").split(" ");
				for (int j = 0; j < 3;j++)
				{
					if (r[j].equals("0")) 
					{
						recipe += "empty";
						amounts[(i*3)+j] = 0;
					}
					else 
					{
						recipe += r[j];
						amounts[(i*3)+j] = Integer.parseInt(materials.getOrDefault(r[j], "nothing,0").split(",")[1]);
					}
				}
				
			}
			recipes.put(create(recipe,materials),new Recipe(product,xp,amounts));
		}
	}
	public static Recipe getRecipe(ItemStack[] ingredients)
	{
		return i.recipes.getOrDefault(create(ingredients), null);
	}
	public static String create(String recipe,HashMap<String,String> materials)
	{
		String hash = recipe;
		for (String m : materials.keySet())
		{
			hash = hash.replaceAll(m, materials.get(m).split(",")[0]);
		}
		return hash;
	}
	public static String create(ItemStack[] ingredients)
	{
		String hash = "";
		for (ItemStack item : ingredients)
		{
			if (item == null || !item.hasItemMeta())
			{
				hash += "empty";
				continue;
			}
			PersistentDataContainer p = item.getItemMeta().getPersistentDataContainer();
			hash += p.getOrDefault(ItemBuilder.kItem,PersistentDataType.STRING,"minecraft");
		}
		return hash;
	}
}
