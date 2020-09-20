package com.carterz30cal.items;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.EnchantHandler;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.StringManipulator;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ItemBuilder
{
	public static ItemBuilder i;
	
	public static final NamespacedKey kItem = new NamespacedKey(Dungeons.instance,"item");
	public static final NamespacedKey kCustomName = new NamespacedKey(Dungeons.instance,"name");
	public static final NamespacedKey kEnchants = new NamespacedKey(Dungeons.instance,"enchants");
	public static final NamespacedKey kSharps = new NamespacedKey(Dungeons.instance,"sharps");
	public ChatColor[] rarityColours = {ChatColor.GRAY,ChatColor.BLUE,ChatColor.RED,ChatColor.LIGHT_PURPLE,ChatColor.GOLD};
	public HashMap<String,String> attributeColours;
	public HashMap<String,Item> items;
	public HashMap<String,ItemSet> itemsets;
	public HashMap<String,ItemSharpener> sharps;
	public HashMap<String,String> sharpeners;
	public static final String w = ChatColor.WHITE.toString();
	public static ItemStack menuItem;
	public static String[] keys;
	
	public ItemBuilder()
	{
		i = this;
		
		File dataFile = new File(Dungeons.instance.getDataFolder(), "items.yml");
		File setFile = new File(Dungeons.instance.getDataFolder(),"sets.yml");
		if (!dataFile.exists())
		{
			dataFile.getParentFile().mkdirs();
			Dungeons.instance.saveResource("items.yml",false);
		}
		if (!setFile.exists())
		{
			setFile.getParentFile().mkdirs();
			Dungeons.instance.saveResource("sets.yml",false);
		}
		
		FileConfiguration data = new YamlConfiguration();
		FileConfiguration sets = new YamlConfiguration();
		try 
		{
			data.load(dataFile);
	    } 
		catch (IOException | InvalidConfigurationException e) 
		{
	        e.printStackTrace();
	    }
		try 
		{
			sets.load(setFile);
	    } 
		catch (IOException | InvalidConfigurationException e) 
		{
	        e.printStackTrace();
	    }
		items = new HashMap<String,Item>();
		itemsets = new HashMap<String,ItemSet>();
		
		attributeColours = new HashMap<String,String>();
		attributeColours.put("damage", ChatColor.GRAY + "Damage: " + w);
		attributeColours.put("damagep", ChatColor.GREEN + "Damage: " + w);
		
		attributeColours.put("health", ChatColor.RED + "Health: " + w);
		attributeColours.put("armour", ChatColor.BLUE + "Armour: " + w);
		attributeColours.put("regen", ChatColor.YELLOW + "Regen: " + w);
		
		attributeColours.put("orechance", w + "Ore Chance: ");
		attributeColours.put("bonusxp", ChatColor.DARK_RED + "Bonus Mining Xp: " + w);
		
		attributeColours.put("killcoins", ChatColor.GOLD + "Bonus Coins: " + w);
		
		sharps = new HashMap<String,ItemSharpener>();
		sharpeners = new HashMap<String,String>();
		
		for (String set : sets.getKeys(false))
		{
			ItemSet s = new ItemSet();
			s.set_lore = sets.getString(set + ".set.lore", "this is supposed to be lore");
			s.syn_lore = sets.getString(set + ".synergy.lore", "this is supposed to be lore");
			if (sets.contains(set + ".set.attributes"))
			{
				for (String attr : sets.getConfigurationSection(set + ".set.attributes").getKeys(false))
				{
					s.set_attributes.put(attr, sets.getDouble(set + ".set.attributes." + attr));
				}
			}
			if (sets.contains(set + ".synergy.attributes"))
			{
				for (String attr : sets.getConfigurationSection(set + ".synergy.attributes").getKeys(false))
				{
					s.syn_attributes.put(attr, sets.getDouble(set + ".synergy.attributes." + attr));
				}
			}
			itemsets.put(set, s);
		}
		keys = new String[data.getKeys(false).size()];
		int kin = 0;
		for (String it : data.getKeys(false))
		{
			keys[kin] = it;
			kin++;
			Item item = new Item();
			item.type = data.getString(it + ".type", "ingredient");
			item.rarity = Rarity.valueOf(data.getString(it + ".rarity", "COMMON"));
			item.name = rarityColours[item.rarity.ordinal()] + data.getString(it + ".name", "null");
			item.attributes = new HashMap<String,Double>();
			item.glow = data.getBoolean(it + ".glow", false);
			item.material = Material.valueOf(data.getString(it + ".item"));
			
			if (data.contains(it + ".attributes"))
			{
				for (String attr : data.getConfigurationSection(it + ".attributes").getKeys(false))
				{
					item.attributes.put(attr, data.getDouble(it + ".attributes." + attr));
				}
			}
			if (data.contains(it + ".colour"))
			{
				item.data.put("r", Integer.parseInt(data.getString(it + ".colour").split(",")[0]));
				item.data.put("g", Integer.parseInt(data.getString(it + ".colour").split(",")[1]));
				item.data.put("b", Integer.parseInt(data.getString(it + ".colour").split(",")[2]));
			}
			if (item.material == Material.PLAYER_HEAD)
			{
				item.data.put("skull_data", data.getString(it + ".skull.data"));
				item.data.put("skull_sig", data.getString(it + ".skull.sig"));
			}
			if (data.contains(it + ".set"))
			{
				item.set = itemsets.get(data.getString(it + ".set"));
			}
			if (data.contains(it + ".sharpener"))
			{
				ItemSharpener sha = new ItemSharpener();
				sha.plusColour = ChatColor.valueOf(data.getString(it + ".sharpener.plus").toUpperCase());
				sha.attributes = item.attributes;
				
				sharps.put(data.getString(it + ".sharpener.id"), sha);
				sharpeners.put(it, data.getString(it + ".sharpener.id"));
			}
			
			items.put(it, item);
		}
		
		menuItem = new ItemStack(Material.EMERALD,1);
		ItemMeta mm = menuItem.getItemMeta();
		mm.setDisplayName(ChatColor.GOLD + "Dungeons Menu");
		mm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		mm.addEnchant(Enchantment.DURABILITY, 1, true);
		menuItem.setItemMeta(mm);
	}

	public ItemStack build(String s,DungeonsPlayer owner)
	{
		return build(s,owner,"","");
	}
	public ItemStack build(String s,DungeonsPlayer owner,String enchants)
	{
		return build(s,owner,enchants,"");
	}
	public ItemStack build(String s,DungeonsPlayer owner,String enchants,String sh)
	{
		if (s.equals("book")) return book(owner,enchants);
		else if (s.equals("null")) return null;
		Item item = items.get(s);
		if (item == null) return new ItemStack(Material.valueOf(s.toUpperCase()));
		
		ItemStack ret = new ItemStack(item.material);
		ItemMeta meta = ret.getItemMeta();
		
		meta.getPersistentDataContainer().set(kItem, PersistentDataType.STRING, s);
		meta.getPersistentDataContainer().set(kCustomName, PersistentDataType.STRING, item.name);
		meta.getPersistentDataContainer().set(kEnchants, PersistentDataType.STRING, enchants);
		meta.getPersistentDataContainer().set(kSharps, PersistentDataType.STRING, sh);
		meta.setUnbreakable(true);
		
		meta.addItemFlags(ItemFlag.HIDE_DYE);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        
		if (item.type.equals("armour") || item.type.equals("weapon")) 
		{
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("zeroarmour",0,Operation.MULTIPLY_SCALAR_1));
		}
		if (item.material == Material.PLAYER_HEAD)
		{
			SkullMeta smeta = (SkullMeta)meta;
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	        profile.getProperties().put("textures", new Property("textures", (String)item.data.get("skull_data"),(String)item.data.get("skull_sig")));
	        Field profileField = null;
	        try {
	            profileField = smeta.getClass().getDeclaredField("profile");
	        } catch (NoSuchFieldException | SecurityException e) {
	            e.printStackTrace();
	        }
	        profileField.setAccessible(true);
	        try {
	            profileField.set(smeta, profile);
	        } catch (IllegalArgumentException | IllegalAccessException e) {
	            e.printStackTrace();
	        }
		}
		ret.setItemMeta(updateMeta(meta,owner));
		
		return ret;
	}
	private ItemStack book(DungeonsPlayer owner,String enchants)
	{
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta meta = item.getItemMeta();
		
		meta.getPersistentDataContainer().set(kItem, PersistentDataType.STRING, "book");
		meta.getPersistentDataContainer().set(kCustomName, PersistentDataType.STRING, "Enchanted Book");
		meta.getPersistentDataContainer().set(kEnchants, PersistentDataType.STRING, enchants);
		meta.setDisplayName("Enchanted Book");
		
		
		meta.addItemFlags(ItemFlag.HIDE_DYE);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(updateMeta(meta,owner));
		return item;
	}
	
	public ItemMeta updateMeta(ItemMeta meta,DungeonsPlayer owner)
	{
		if (meta == null) return null;
		String t = meta.getPersistentDataContainer().getOrDefault(kItem, PersistentDataType.STRING,"minecraft");
		Item item = items.get(t);
		ArrayList<String> lore = new ArrayList<String>();
		if (item == null) 
		{
			if (t == "book") 
			{
				lore.add("");
				String[] i_enchants = meta.getPersistentDataContainer().get(kEnchants, PersistentDataType.STRING).split(";");
				Rarity highest = Rarity.COMMON;
				for (String e : i_enchants)
				{
					if (highest.ordinal() < EnchantHandler.eh.getRarity(e)) highest = Rarity.values()[EnchantHandler.eh.getRarity(e)];
					if (i_enchants.length < 5)
					{
						
						lore.add(ChatColor.DARK_PURPLE + EnchantHandler.eh.getName(e) + " " + EnchantHandler.getLevel(e));
						lore.add(" " + ChatColor.LIGHT_PURPLE + EnchantHandler.eh.getDescription(e));
					}
					else lore.add(ChatColor.DARK_PURPLE + EnchantHandler.eh.getName(e) + " " + EnchantHandler.getLevel(e));
				}
				meta.setDisplayName(rarityColours[highest.ordinal()] + ChatColor.stripColor(meta.getDisplayName()));
				lore.add(0,ChatColor.DARK_GRAY + StringManipulator.capitalise(highest.toString()) + " Enchanted Book");
				meta.setLore(lore);
			}
			return meta;
		}
		String[] sh = meta.getPersistentDataContainer().getOrDefault(kSharps, PersistentDataType.STRING,"").split(";");
		String plus = "";
		for (String sha : sh)
		{
			ItemSharpener sharp = sharps.get(sha);
			if (sharp == null) continue;
			plus = plus + sharp.plusColour + "+";
		}
		String c = meta.getPersistentDataContainer().get(kCustomName, PersistentDataType.STRING);
		String cs = c;
		if (!plus.equals("")) cs += w + " [" + plus + w + "]";
		if (owner != null && owner.highlightRenamed && !c.equals(item.name)) meta.setDisplayName(cs + w + " (" + item.name + w + ")");
		else meta.setDisplayName(cs);
		
		
		
		if (!item.type.equals("armour")) lore.add(ChatColor.DARK_GRAY + StringManipulator.capitalise(item.rarity.toString()) +" " + StringManipulator.capitalise(item.type));
		else 
		{
			String[] m = item.material.toString().split("_");
			if (m[0].equals("LEATHER"))
			{
				((LeatherArmorMeta)meta).setColor(Color.fromRGB((int)item.data.get("r"), (int)item.data.get("g"), (int)item.data.get("b")));
			}
			if (m[0].equals("PLAYER"))
			{
				lore.add(ChatColor.DARK_GRAY + StringManipulator.capitalise(item.rarity.toString()) + " Helmet");
			}
			else lore.add(ChatColor.DARK_GRAY + StringManipulator.capitalise(item.rarity.toString()) + " " + StringManipulator.capitalise(m[1]));
			
		}
		
		if (item.attributes.size() > 0)
		{
			@SuppressWarnings("unchecked")
			HashMap<String,Double> attributes = (HashMap<String, Double>) item.attributes.clone();
			for (String sha : sh)
			{
				ItemSharpener sharp = sharps.get(sha);
				if (sharp == null) continue;
				for (Entry<String,Double> at : sharp.attributes.entrySet())
				{
					double atv = attributes.getOrDefault(at.getKey(), 0.0);
					attributes.put(at.getKey(), atv+at.getValue());
				}
			}
			lore.add("");
			for (Entry<String,Double> attribute : attributes.entrySet())
			{
				String d = Math.round(attribute.getValue()*100) + "%";
				if (attribute.getValue().intValue() == attribute.getValue()) d = Integer.toString(attribute.getValue().intValue());
				lore.add(attributeColours.get(attribute.getKey()) + d);
			}
		}
		if (!meta.getPersistentDataContainer().get(kEnchants, PersistentDataType.STRING).equals(""))
		{
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			lore.add("");
			String[] i_enchants = meta.getPersistentDataContainer().get(kEnchants, PersistentDataType.STRING).split(";");
			for (String e : i_enchants)
			{
				if (i_enchants.length < 5)
				{
					lore.add(ChatColor.DARK_PURPLE + EnchantHandler.eh.getName(e) + " " + EnchantHandler.getLevel(e));
					lore.add(" " + ChatColor.LIGHT_PURPLE + EnchantHandler.eh.getDescription(e));
				}
				else lore.add(ChatColor.DARK_PURPLE + EnchantHandler.eh.getName(e) + " " + EnchantHandler.getLevel(e));
			}
		}
		else 
		{
			if (item.glow) meta.addEnchant(Enchantment.DURABILITY, 1, true);
			else meta.removeEnchant(Enchantment.DURABILITY);
		}

		if (owner != null && item.set != null)
		{	
			if ((item.type.equals("armour") && owner.stats.set) || (item.type.equals("weapon") && owner.stats.synergy))
			{
				lore.add("");
				if (item.type.equals("armour")) 
				{
					lore.add(ChatColor.GOLD + ChatColor.BOLD.toString() + "SET BONUS");
					lore.add(ChatColor.DARK_GRAY + " " + item.set.set_lore);
					
					for (Entry<String,Double> attribute : item.set.set_attributes.entrySet())
					{
						String d = Math.round(attribute.getValue()*100) + "%";
						if (attribute.getValue().intValue() == attribute.getValue()) d = Integer.toString(attribute.getValue().intValue());
						lore.add(" " + attributeColours.get(attribute.getKey()) + d);
					}
				}
				else 
				{
					lore.add(ChatColor.GOLD + ChatColor.BOLD.toString() + "SYNERGY BONUS");
					lore.add(ChatColor.DARK_GRAY + " " + item.set.syn_lore);
					
					for (Entry<String,Double> attribute : item.set.syn_attributes.entrySet())
					{
						String d = Math.round(attribute.getValue()*100) + "%";
						if (attribute.getValue().intValue() == attribute.getValue()) d = Integer.toString(attribute.getValue().intValue());
						lore.add(" " + attributeColours.get(attribute.getKey()) + d);
					}
				}
			}
			
			if (!owner.highlightRenamed && !c.equals(item.name)) lore.add(ChatColor.DARK_GRAY + ChatColor.stripColor(item.name));
		}
		
		meta.setLore(lore);
		return meta;
	}
	public boolean canSharpen(ItemStack i)
	{
		ItemMeta moot = i.getItemMeta();
		PersistentDataContainer pdc = moot.getPersistentDataContainer();
		
		Item item = items.get(pdc.get(kItem, PersistentDataType.STRING));
		if (item == null) return false;
		if (!item.type.equals("weapon") && !item.type.equals("tool")) return false;
		
		String[] sharps = pdc.getOrDefault(kSharps, PersistentDataType.STRING, "").split(";");
		if (sharps.length > 3) return false;
		return true;
	}
	public ItemStack sharpenItem(ItemStack i, ItemStack sharpener)
	{
		String sh = sharpeners.getOrDefault(sharpener.getItemMeta().getPersistentDataContainer().getOrDefault(kItem, PersistentDataType.STRING, ""), "");
		return sharpenItem(i,sh);
	}
	public ItemStack sharpenItem(ItemStack i, String sharpener)
	{
		ItemStack item = i.clone();
		ItemMeta moot = item.getItemMeta();
		PersistentDataContainer pdc = moot.getPersistentDataContainer();
		String ew = pdc.getOrDefault(kSharps, PersistentDataType.STRING, "");
		String[] current = ew.split(";");
		
		if (!sharpener.equals("") && current[0].equals("")) ew = sharpener;
		else if (current.length < 4) ew += ";" + sharpener;
		
		pdc.set(kSharps, PersistentDataType.STRING, ew);
		
		moot = updateMeta(moot,null);
		item.setItemMeta(moot);
		return item;
	}
	public ItemStack enchantItem(ItemStack item1,ItemStack item2)
	{
		ItemStack ret = item1.clone();
		ItemMeta meta = ret.getItemMeta();
		HashMap<String,Integer> enchantments = new HashMap<String,Integer>();
		
		for (String enchant : item1.getItemMeta().getPersistentDataContainer().getOrDefault(kEnchants, PersistentDataType.STRING,"").split(";"))
		{
			if (enchant.split(",").length == 1) break;
			String e = enchant.split(",")[0];
			int eCurr = enchantments.getOrDefault(e, 0);
			int eLevel = Integer.parseInt(enchant.split(",")[1]);
			
			if (eLevel > eCurr) enchantments.put(e, eLevel);
			else if (eLevel == eCurr && eLevel < EnchantHandler.eh.enchants.get(e).maxLevel) enchantments.put(e, eLevel+1);
		}
		
		for (String enchant : item2.getItemMeta().getPersistentDataContainer().get(kEnchants, PersistentDataType.STRING).split(";"))
		{
			String e = enchant.split(",")[0];
			int eCurr = enchantments.getOrDefault(e, 0);
			int eLevel = Integer.parseInt(enchant.split(",")[1]);
			
			if (eLevel > eCurr) enchantments.put(e, eLevel);
			else if (eLevel == eCurr && eLevel < EnchantHandler.eh.enchants.get(e).maxLevel) enchantments.put(e, eLevel+1);
		}
		
		String enchants = "";
		Set<String> keys = enchantments.keySet();
		for (String enchant : keys)
		{
			if (keys.size() == 1) enchants = enchant + "," + enchantments.getOrDefault(enchant,1);
			else
			{
				enchants += enchant + "," + enchantments.get(enchant) + ";";
			}
		}
		if (keys.size() > 1) enchants = enchants.substring(0, enchants.length()-1);
		meta.getPersistentDataContainer().set(kEnchants, PersistentDataType.STRING, enchants);
		meta = updateMeta(meta,null);
		ret.setItemMeta(meta);
		return ret;
	}
}