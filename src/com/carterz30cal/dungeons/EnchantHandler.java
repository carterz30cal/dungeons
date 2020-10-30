package com.carterz30cal.dungeons;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;

@Deprecated
public class EnchantHandler
{
	public static EnchantHandler eh;
	public HashMap<String,Enchant> enchants;
	
	private FileConfiguration config;
	public EnchantHandler()
	{
		eh = this;
		enchants = new HashMap<String,Enchant>();
		
		File fEnchants = new File(Dungeons.instance.getDataFolder(),"enchants.yml");
		if (!fEnchants.exists())
		{
			fEnchants.getParentFile().mkdirs();
			Dungeons.instance.saveResource("enchants.yml",false);
		}
		config = new YamlConfiguration();
		try 
		{
			config.load(fEnchants);
		} 
		catch (IOException | InvalidConfigurationException e)
		{
            e.printStackTrace();
        }
		
		for (String e : config.getConfigurationSection("enchants").getKeys(false))
		{
			String path = "enchants." + e;
			int maxL = config.getInt(path + ".max", 1);
			String nom = config.getString(path + ".name", "null");
			HashMap<String,Double> ef = new HashMap<String,Double>();
			for (String p : config.getConfigurationSection(path + ".effects").getKeys(false))
			{
				ef.put(p, config.getDouble(path + ".effects." + p));
			}
			enchants.put(e, new Enchant(nom,maxL,ef));
		}
	}
	public float getEffect(ItemMeta m,String effect)
	{
		PersistentDataContainer d = m.getPersistentDataContainer();
		String eee = d.getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING, "");
		if (eee.equals("")) return 0;
		
		float total = 0;
		for (String en : d.get(ItemBuilder.kEnchants, PersistentDataType.STRING).split(";"))
		{
			String[] ens = en.split(",");
			if (ens.length == 1) continue;
			if (!enchants.containsKey(ens[0])) continue;
			total += enchants.get(en.split(",")[0]).get(effect,Double.parseDouble(ens[1]));
		}
		return total;
	}
	public String getDescription(String enchantment)
	{
		String[] spl = enchantment.split(",");
		String raw = config.getString("enchants." + spl[0] + ".description");
		int level = Integer.parseInt(spl[1]);
		for (String effect : config.getConfigurationSection("enchants." + spl[0] + ".effects").getKeys(false))
		{
			double effectstr = config.getDouble("enchants." + spl[0] + ".effects." + effect);
			// for percents 
			String desc = Integer.toString((int) Math.round(effectstr*level*100)) + "%";
			//if (effectstr > 0) desc = "+" + desc;
			raw = raw.replaceAll(effect.toUpperCase() + "%", desc);
			// for ints
			raw = raw.replaceAll(effect.toUpperCase(),Integer.toString((int) Math.round(effectstr*level)));
		}
		return raw;
	}
	public int getRarity(String enchantment)
	{
		String[] spl = enchantment.split(",");
		return config.getInt("enchants." + spl[0] + ".book.rarities." + spl[1],0);
	}
	public String getName(String enchantment)
	{
		return enchants.get(enchantment.split(",")[0]).name;
	}
	public String getRequiredCatalyst(ItemStack item1,ItemStack item2)
	{
		int highestCatalyst = 0;
		for (String enchant : item1.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING, "").split(";"))
		{
			int cat = config.getInt("enchants." + enchant.split(",")[0] + ".book.catalyst",0);
			highestCatalyst = Math.max(highestCatalyst, cat);
		}
		for (String enchant : item2.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING, "").split(";"))
		{
			int cat = config.getInt("enchants." + enchant.split(",")[0] + ".book.catalyst",0);
			highestCatalyst = Math.max(highestCatalyst, cat);
		}
		return "catalyst=" + highestCatalyst;
	}
	public boolean enchantable(ItemStack item)
	{
		if (item == null) return false;
		if (!item.hasItemMeta()) return false;
		PersistentDataContainer p = item.getItemMeta().getPersistentDataContainer();
		
		Item i = ItemBuilder.i.items.get(p.getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, "bedrock"));
		if (i == null) return false;
		else
		{
			switch (i.type)
			{
			case "weapon":
			case "armour":
			case "book":
				return true;
			default:
				return false;
			}
		}
		
	}
	public boolean isCatalyst(ItemStack item)
	{
		if (item == null) return false;
		if (!item.hasItemMeta()) return false;
		
		String[] itemn = item.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, "not a catalyst").split("=");
		if (itemn[0].equals("catalyst")) return true;
		else return false;
	}
	public boolean isBook(ItemStack item)
	{
		if (item == null) return false;
		if (!item.hasItemMeta()) return false;
		
		String itemn = item.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, "not a book");
		if (itemn.equals("book")) return true;
		else return false;
	}
	public boolean isUIElement(ItemStack item)
	{
		if (item == null) return false;
		if (!item.hasItemMeta()) return false;
		
		String itemn = item.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, "not a book");
		if (itemn.equals("uielement")) return true;
		else return false;
	}
	public static int getLevel(String enchantment)
	{
		return Integer.parseInt(enchantment.split(",")[1]);
	}
}
