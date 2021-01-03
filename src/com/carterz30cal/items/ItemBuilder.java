package com.carterz30cal.items;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.abilities.AbilityManager;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.items.magic.ItemSpell;
import com.carterz30cal.items.magic.ItemWand;
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
	public ChatColor[] rarityColours = {ChatColor.GRAY,ChatColor.BLUE,ChatColor.AQUA,ChatColor.RED,ChatColor.LIGHT_PURPLE,ChatColor.GOLD};
	public HashMap<String,String> attributeColours;
	public HashMap<String,Item> items;
	public HashMap<String,ItemSet> itemsets;
	public HashMap<String,ItemSharpener> sharps;
	public HashMap<String,String> sharpeners;
	public static final String w = ChatColor.WHITE.toString();
	public static ItemStack menuItem;
	public static String[] keys;
	
	public static int itemCount;
	
	private static String[] armour_pieces = {
			"_HELMET","_CHESTPLATE","_LEGGINGS","_BOOTS"
	};
	
	public static final String[] files = {
			"waterway/armour","waterway/ingredients","waterway/weapons","waterway/tools",
			"waterway/sharpeners","waterway/lootboxes","waterway/items_rainevent","waterway/items_spearfishing",
			"waterway/magic",
			"necropolis/ingredients","necropolis/armour","necropolis/weapons","necropolis/tools",
			"necropolis/sharpeners"
			};
	public static final String[] setFiles = {
			"waterway/sets","necropolis/sets"
	};
	
	
	public static void copyToFile(InputStream inputStream, File file) {
	    try(OutputStream outputStream = new FileOutputStream(file)) {
	        IOUtils.copy(inputStream, outputStream);
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	} 
	
	public ItemBuilder()
	{
		i = this;
		
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
		
		attributeColours.put("mana", ChatColor.LIGHT_PURPLE + "Mana: " + w);
		
		sharps = new HashMap<String,ItemSharpener>();
		sharpeners = new HashMap<String,String>();
		
		for (String f : setFiles)
		{
			File file = null;
			try
			{
				file = File.createTempFile("setfile." + f, null);
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			if (file == null) continue;
			copyToFile(Dungeons.instance.getResource(f + ".yml"),file);
			FileConfiguration sets = new YamlConfiguration();
			try 
			{
				sets.load(file);
		    } 
			catch (IOException | InvalidConfigurationException e) 
			{
		        e.printStackTrace();
		    }
			
			
			
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
				s.set_ability = sets.getString(set + ".set.ability","none");

				if (sets.contains(set + ".synergy.attributes"))
				{
					for (String attr : sets.getConfigurationSection(set + ".synergy.attributes").getKeys(false))
					{
						s.syn_attributes.put(attr, sets.getDouble(set + ".synergy.attributes." + attr));
					}
				}
				itemsets.put(set, s);
			}
		}
		
		
		for (String f : files)
		{
			File file = null;
			try
			{
				file = File.createTempFile("itemfile." + f, null);
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			if (file == null) continue;
			copyToFile(Dungeons.instance.getResource(f + ".yml"),file);
			FileConfiguration data = new YamlConfiguration();
			try 
			{
				data.load(file);
		    } 
			catch (IOException | InvalidConfigurationException e) 
			{
		        e.printStackTrace();
		    }
			
			
			
			for (String it : data.getKeys(false)) generate(data,it);
		}
		System.out.println(itemCount);
		keys = new String[itemCount];
		for (Entry<String,Item> i : items.entrySet()) keys[i.getValue().id] = i.getKey();
		
		
		
		menuItem = new ItemStack(Material.EMERALD,1);
		ItemMeta mm = menuItem.getItemMeta();
		mm.setDisplayName(ChatColor.GOLD + "Dungeons Menu");
		mm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		mm.addEnchant(Enchantment.DURABILITY, 1, true);
		menuItem.setItemMeta(mm);
	}

	
	public void generate(FileConfiguration data, String it)
	{
		System.out.println(it);
		String[] sp = it.split("-");
		if (sp.length == 1) generate_regular(data,it);
		else if (sp[0].equals("armour")) generate_armour(data,it);
	}
	public void generate_armour(FileConfiguration data,String it)
	{
		String mats = data.getString(it + ".item");
		Material[] material = new Material[4];
		if (mats.split(",").length == 4) for (int s = 0; s < 4; s++) material[s] = Material.valueOf(mats.split(",")[s]);
		else if (mats.split(",").length == 2)
		{
			for (int s = 0; s < 4; s++) 
			{	
				if (s == 0) material[0] = Material.valueOf(mats.split(",")[0]);
				else material[s] = Material.valueOf(mats.split(",")[1] + armour_pieces[s]);
			}
		}
		else for (int s = 0; s < 4; s++) material[s] = Material.valueOf(mats + armour_pieces[s]);
		for (int i = 0; i < 4;i++)
		{
			Item item = new Item();
			item.type = "armour";
			item.rarity = Rarity.valueOf(data.getString(it + ".rarity", "COMMON"));
			item.name = rarityColours[item.rarity.ordinal()] + data.getString(it + ".name", "null") + " " + data.getString(it + ".suffix", "null,null,null,null").split(",")[i];
			item.attributes = new HashMap<String,Double>();
			item.glow = data.getBoolean(it + ".glow", false);
			item.combatReq = data.getInt(it + ".req", 0);
			
			
			
			if (i == 0 && data.contains(it + ".skull"))
			{
				item.material = Material.PLAYER_HEAD;
				item.data.put("skull_data", data.getString(it + ".skull.data"));
				item.data.put("skull_sig", data.getString(it + ".skull.sig"));
			}
			else item.material = material[i];
			item.id = itemCount;
			
			for (String attr : data.getConfigurationSection(it + ".attributes").getKeys(false))
			{
				double d = Double.parseDouble(data.getString(it + ".attributes." + attr).split(",")[i]);
				if (d != 0) item.attributes.put(attr, d);
				
			}
			
			if (data.contains(it + ".colour") && !(i == 0 && data.contains(it + ".skull")))
			{
				item.data.put("r", Integer.parseInt(data.getString(it + ".colour").split(",")[0]));
				item.data.put("g", Integer.parseInt(data.getString(it + ".colour").split(",")[1]));
				item.data.put("b", Integer.parseInt(data.getString(it + ".colour").split(",")[2]));
			}
			if (data.contains(it + ".set")) item.set = itemsets.get(data.getString(it + ".set"));
			
			items.put("armour_" + data.getString(it + ".name", "null").toLowerCase() + armour_pieces[i].toLowerCase(), item);
			itemCount++;
		}
	}
	public void generate_regular(FileConfiguration data, String it)
	{
		String type = data.getString(it + ".type", "ingredient");
		Item item;
		if (type.equals("lootbox")) item = new ItemLootbox();
		else if (type.equals("wand")) item = new ItemWand();
		else if (type.equals("spell")) item = new ItemSpell();
		else item = new Item();

		item.type = type;
		item.rarity = Rarity.valueOf(data.getString(it + ".rarity", "COMMON"));
		item.name = rarityColours[item.rarity.ordinal()] + data.getString(it + ".name", "null");
		item.attributes = new HashMap<String,Double>();
		item.glow = data.getBoolean(it + ".glow", false);
		item.material = Material.valueOf(data.getString(it + ".item"));
		item.id = itemCount;
		item.combatReq = data.getInt(it + ".req", 0);
		
		
		if (type.equals("lootbox"))
		{
			ItemLootbox lootbox = (ItemLootbox)item;
			for (String loot : data.getConfigurationSection(it + ".loot").getKeys(false))
			{
				lootbox.items.add(loot.split(";")[0]);
				String[] amounts = data.getString(it + ".loot." + loot + ".amount", "1").split("-");
				Integer[] actualamounts = new Integer[2];
				if (amounts.length > 1) 
				{
					actualamounts[0] = Integer.parseInt(amounts[0]); actualamounts[1] = Integer.parseInt(amounts[1]);
				}
				else
				{
					int am = Integer.parseInt(amounts[0]);
					actualamounts[0] = am; actualamounts[1] = am;
				}
				lootbox.amounts.add(actualamounts);
				lootbox.chance.add(data.getInt(it + ".loot." + loot + ".chance", 1));
				lootbox.enchants.add(data.getString(it + ".loot." + loot + ".enchants", ""));
			}
		}
		if (item instanceof ItemWand)
		{
			ItemWand wand = (ItemWand)item;
			wand.mana = data.getInt(it + ".wand.mana",0);
		}
		if (item instanceof ItemSpell)
		{
			ItemSpell spell = (ItemSpell)item;
			String[] c = data.getString(it + ".spell.colour", "255,0,0").split(",");
			spell.colour = Color.fromRGB(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
			spell.damage = data.getInt(it + ".spell.damage",0);
			spell.speed = data.getDouble(it + ".spell.speed", 1);
			spell.mana = data.getInt(it + ".spell.mana",0);
			spell.pierces = data.getInt(it + ".spell.pierces",0);
		}
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
		if (data.contains(it + ".set")) item.set = itemsets.get(data.getString(it + ".set"));
		if (data.contains(it + ".ability")) item.data.put("ability", data.getString(it + ".ability"));
		if (item.material == Material.PLAYER_HEAD)
		{
			item.data.put("skull_data", data.getString(it + ".skull.data"));
			item.data.put("skull_sig", data.getString(it + ".skull.sig"));
		}
		
		if (data.contains(it + ".sharpener"))
		{
			ItemSharpener sha = new ItemSharpener();
			sha.plusColour = ChatColor.valueOf(data.getString(it + ".sharpener.plus").toUpperCase());
			sha.attributes = item.attributes;
			
			sharps.put(data.getString(it + ".sharpener.id"), sha);
			sharpeners.put(it, data.getString(it + ".sharpener.id"));
		}
		itemCount++;
		items.put(it, item);
	}
	public ItemStack build(String s,int amount)
	{
		ItemStack product = build(s,null,"","");
		product.setAmount(amount);
		return product;
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
        
		if (item.type.equals("armour") || item.type.equals("weapon") || item.type.equals("wand") || item.type.equals("spell")
				|| item.type.equals("modifier")) 
		{
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("zeroarmour",0,Operation.MULTIPLY_SCALAR_1));
		}
		if (item.material == Material.PLAYER_HEAD)
		{
			meta = generateSkullMeta(meta,(String)item.data.get("skull_data"),(String)item.data.get("skull_sig"));
		}
		ret.setItemMeta(updateMeta(meta,owner));
		
		return ret;
	}
	public static boolean isUIElement(ItemStack item)
	{
		if (item == null) return false;
		if (!item.hasItemMeta()) return false;
		
		String itemn = item.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, "not a book");
		if (itemn.equals("uielement")) return true;
		else return false;
	}
	public static ItemMeta generateSkullMeta(ItemMeta meta, String data, String sig)
	{
		SkullMeta smeta = (SkullMeta)meta;
		GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(data.getBytes()), null);
        profile.getProperties().put("textures", new Property("textures", data,sig));
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
		return smeta;
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
				ArrayList<AbsEnchant> enchants = EnchantManager.get(meta.getPersistentDataContainer());
				int rarity = 0;
				for (AbsEnchant e : enchants)
				{
					rarity = Math.max(rarity, e.rarity());
					if (enchants.size() > 4) lore.add(ChatColor.DARK_PURPLE + e.name());
					else
					{
						if (e.level < e.max()) lore.add(ChatColor.DARK_PURPLE + e.name());
						else if (e.level == e.max()) lore.add(ChatColor.BLUE + e.name());
						else lore.add(ChatColor.GOLD + e.name());
						lore.add(ChatColor.LIGHT_PURPLE + " " + e.description());
					}
				}
				meta.setDisplayName(rarityColours[rarity] + ChatColor.stripColor(meta.getDisplayName()));
				lore.add(0,ChatColor.DARK_GRAY + StringManipulator.capitalise(Rarity.values()[rarity].toString()) + " Enchanted Book");
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
		
		if (item.type.equals("armour"))
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
		else if (!item.type.equals("wand")) lore.add(ChatColor.DARK_GRAY + StringManipulator.capitalise(item.rarity.toString()) +" " + StringManipulator.capitalise(item.type));
		
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
		if (item instanceof ItemWand) 
		{
			ItemWand wand = (ItemWand)item;
			if (get(meta,ItemWand.kSpell) != null)
			{
				lore.add(ChatColor.GRAY + "Spell: " + ((ItemSpell)get(meta,ItemWand.kSpell)).name);
			}
			lore.add("");
			lore.add(ChatColor.GRAY + "Mana cost: " + ChatColor.AQUA + wand.cost(meta));
			if (get(meta,ItemWand.kModifier) != null)
			{
				Item mod = get(meta,ItemWand.kModifier);
				lore.addAll(addAbility((String)mod.data.get("ability"),""));
			}
		}
		if (!meta.getPersistentDataContainer().get(kEnchants, PersistentDataType.STRING).equals(""))
		{
			if (item.type != "armour") meta.addEnchant(Enchantment.DURABILITY, 1, true);
			lore.add("");
			ArrayList<AbsEnchant> enchants = EnchantManager.get(meta.getPersistentDataContainer());
			for (AbsEnchant e : enchants)
			{
				if (enchants.size() > 4) lore.add(ChatColor.DARK_PURPLE + e.name());
				else
				{
					if (e.level < e.max()) lore.add(ChatColor.DARK_PURPLE + e.name());
					else if (e.level == e.max()) lore.add(ChatColor.BLUE + e.name());
					else lore.add(ChatColor.GOLD + e.name());
					lore.add(ChatColor.LIGHT_PURPLE + " " + e.description());
				}
			}
		}
		else 
		{
			if (item.glow) meta.addEnchant(Enchantment.DIG_SPEED, 2, true);
			else meta.removeEnchant(Enchantment.DIG_SPEED);
		}
		if (item.material == Material.TRIDENT) meta.addEnchant(Enchantment.LOYALTY, 5, true);
		if (item.data.containsKey("ability"))
		{
			lore.add("");
			lore.addAll(addAbility((String)item.data.get("ability"),""));
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
					if (!item.set.set_ability.equals("none")) lore.addAll(addAbility(item.set.set_ability," "));
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
		if ((owner == null || item.combatReq > owner.skills.getSkillLevel("combat")) && item.combatReq > 0) lore.add(ChatColor.RED + "Requires Combat Level " + item.combatReq);
		if (item.type.equals("lootbox"))
		{
			lore.add("");
			lore.add(ChatColor.GOLD + "Click to open the lootbox");
			lore.add(ChatColor.GOLD + " and get awesome rewards!");
		}
		meta.setLore(lore);
		return meta;
	}
	
	public ArrayList<String> addAbility(String ab,String prefix)
	{
		ArrayList<String> n = new ArrayList<String>();
		AbsAbility abs = AbilityManager.get(ab);
		
		for (int i = 0; i < abs.description().size(); i++)
		{
			if (i == 0) n.add(prefix + abs.description().get(i));
			else n.add(prefix + ChatColor.GRAY + " " + abs.description().get(i));
		}
		return n;
	}
	
	
	public ItemStack maxStack(ItemStack i)
	{
		if (!i.hasItemMeta()) return i;
		PersistentDataContainer p = i.getItemMeta().getPersistentDataContainer();
		if (p == null) return i;
		Item item = items.get(p.getOrDefault(kItem, PersistentDataType.STRING, null));
		if (item != null && (item.type.equals("weapon") || item.type.equals("armour") || item.type.equals("tool")
				|| item.type.equals("wand") || item.type.equals("spell") || item.type.equals("modifier"))) i.setAmount(1);
		else i.setAmount(i.getMaxStackSize());
		return i;
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
	
	/*
	 * ITEM1 must be the item being enchanted.
	 * 0 = YES
	 * 1 = incompatible enchantment
	 * 2 = item is a book!
	 * 3 = catalyst is incorrect
	 */
	public static int canEnchant(ItemStack item1,ItemStack item2,ItemStack catalyst)
	{
		ArrayList<AbsEnchant> enchants = EnchantManager.get(item1.getItemMeta().getPersistentDataContainer());
		enchants.addAll(EnchantManager.get(item2.getItemMeta().getPersistentDataContainer()));
		String type = i.items.get(item1.getItemMeta().getPersistentDataContainer().get(kItem, PersistentDataType.STRING)).type;
		int c = Integer.parseInt(catalyst.getItemMeta().getPersistentDataContainer().get(kItem, PersistentDataType.STRING).split("=")[1]);
		//if (type.equals("book")) return 2;
		if (EnchantManager.catalyst(item2) != c) return 3;
		for (AbsEnchant e : enchants) if (!e.type().equals(type)) return 1;
		return 0;
	}
	
	public static Item get(ItemStack item)
	{
		if (item == null || !item.hasItemMeta()) return null;
		return i.items.get(item.getItemMeta().getPersistentDataContainer().get(kItem, PersistentDataType.STRING));
	}
	public static Item get(ItemMeta item)
	{
		return i.items.get(item.getPersistentDataContainer().get(kItem, PersistentDataType.STRING));
	}
	public static Item get(ItemStack item,NamespacedKey key)
	{
		return i.items.get(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
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
			else if (eLevel == eCurr && !isMax(eLevel,e)) enchantments.put(e, eLevel+1);
		}
		
		for (String enchant : item2.getItemMeta().getPersistentDataContainer().get(kEnchants, PersistentDataType.STRING).split(";"))
		{
			String e = enchant.split(",")[0];
			int eCurr = enchantments.getOrDefault(e, 0);
			int eLevel = Integer.parseInt(enchant.split(",")[1]);
			
			if (eLevel > eCurr) enchantments.put(e, eLevel);
			else if (eLevel == eCurr && !isMax(eLevel,e)) enchantments.put(e, eLevel+1);
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
	
	public static boolean isMax(int level, String enchant)
	{
		try {
			if (level < EnchantManager.enchantments.get(enchant).newInstance().max()) return false;
			else return true;
		} 
		catch (InstantiationException | IllegalAccessException e1) { e1.printStackTrace(); }
		return true;
	}

	public static Item get(ItemMeta item, NamespacedKey key) 
	{
		return i.items.get(item.getPersistentDataContainer().get(key, PersistentDataType.STRING));
	}
}
