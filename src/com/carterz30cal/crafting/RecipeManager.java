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
	public HashMap<String,ArrayList<Recipe>> recipeBrowser_byIngredient;
	
	public static String[] files = {
			"waterway/recipes","waterway/recipes_magic","waterway/recipes_titan","waterway/recipes_fishing",
			"waterway/recipes_mining","waterway/recipes_sands",
			"necropolis/recipes","necropolis/recipes_digging","necropolis/crypts_recipes"
			};
	public static HashMap<String,String[]> patterns;
	
	
	
	public RecipeManager()
	{
		i = this;
		// fetch recipes and construct the objects
		recipes = new HashMap<String,Recipe>();
		recipeBrowser_byIngredient = new HashMap<String,ArrayList<Recipe>>();
		
		patterns = new HashMap<String,String[]>();
		
		patterns.put("helmet", new String[] {"X X X","X 0 X","0 0 0"});
		patterns.put("chestplate", new String[] {"X 0 X","X X X","X X X"});
		patterns.put("leggings", new String[] {"X X X","X 0 X","X 0 X"});
		patterns.put("boots", new String[] {"0 0 0","X 0 X","X 0 X"});
		
		patterns.put("sword", new String[] {"0 X 0","0 X 0","0 S 0"});
		patterns.put("stick", new String[] {"0 X 0","0 X 0","0 X 0"});
		
		patterns.put("block", new String[] {"X X X","X X X","X X X"});
		patterns.put("upgrade", new String[] {"X X X","X U X","X X X"});
		
		for (String f : files)
		{
			File file = null;
			try
			{
				file = File.createTempFile("recipefile." + f, null);
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			if (file == null) continue;
			ItemBuilder.copyToFile(Dungeons.instance.getResource(f + ".yml"),file);
			FileConfiguration data = new YamlConfiguration();
			try 
			{
				data.load(file);
		    } 
			catch (IOException | InvalidConfigurationException e) 
			{
		        e.printStackTrace();
		    }
			generate(data);
		}
	}
	private void generate (FileConfiguration recipeConfig)
	{
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
			}
			String recipe = "";
			int[] amounts = new int[9];
			String[] types = new String[9];
			String[] pattern = null;
			if (recipeConfig.contains(p + ".pattern"))
			{
				pattern = patterns.getOrDefault(recipeConfig.getString(p + ".pattern"),null);
			}
			for (int i = 0; i < 3; i++)
			{
				String[] r;
				if (pattern == null) r = recipeConfig.getString(p + ".recipe." + i,"0 0 0").split(" ");
				else r = pattern[i].split(" ");
				
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
						String[] pah = materials.getOrDefault(r[j], "nothing,0").split(",");
						amounts[(i*3)+j] = Integer.parseInt(pah[1]);
						if (pah[0].equals("nothing")) types[(i*3)+j] = null;
						else types[(i*3)+j] = pah[0];
					}
				}
				
			}
			Recipe fin = new Recipe(product,xp,amounts,types);
			recipes.put(create(recipe,materials),fin);
			
			for (String m : materials.values()) 
			{
				String mat = m.split(",")[0];
				recipeBrowser_byIngredient.putIfAbsent(mat, new ArrayList<Recipe>());
				if (!recipeBrowser_byIngredient.get(mat).contains(fin)) recipeBrowser_byIngredient.get(mat).add(fin);
			}
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
