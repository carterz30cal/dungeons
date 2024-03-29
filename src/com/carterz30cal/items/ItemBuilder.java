package com.carterz30cal.items;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.enchants.AbsEnch;
import com.carterz30cal.enchants.AbsEnchTypes;
import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.ability.AbilityManager;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.items.magic.ItemSpell;
import com.carterz30cal.items.magic.ItemWand;
import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.potions.AbsPotion;
import com.carterz30cal.potions.PotionColour;
import com.carterz30cal.potions.PotionType;
import com.carterz30cal.utility.Pair;
import com.carterz30cal.utility.RandomFunctions;
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
	public static final NamespacedKey kExtras = new NamespacedKey(Dungeons.instance,"extra");
	public static final NamespacedKey kRunic = new NamespacedKey(Dungeons.instance,"runic");
	public static final NamespacedKey kFuel = new NamespacedKey(Dungeons.instance,"fuel");
	public ChatColor[] rarityColours = {ChatColor.GRAY,ChatColor.BLUE,ChatColor.AQUA,ChatColor.RED,ChatColor.LIGHT_PURPLE,ChatColor.GOLD,ChatColor.GREEN};
	public HashMap<String,String> attributeColours;
	public HashMap<String,Item> items;
	public HashMap<String,ItemSet> itemsets;
	public HashMap<String,ItemSharpener> sharps;
	public HashMap<String,String> sharpeners;
	
	public HashMap<String,ItemAppliable> appliables;
	public static final String w = ChatColor.WHITE.toString();
	public static ItemStack menuItem;
	public static String[] keys;
	
	public static int itemCount;
	public static int noteCount;
	
	private static String[] armour_pieces = {
			"_HELMET","_CHESTPLATE","_LEGGINGS","_BOOTS"
	};
	public static final String[] files = {
			"waterway2/items_weapons","waterway2/items_bows","waterway2/items_tools","waterway2/items_sharpeners","waterway2/items_quests",
			"waterway2/items_lootbox","waterway2/items_armour","waterway2/items_ingredients"
	};
	/*
	public static final String[] files = {
			"waterway2/items_weapons","waterway2/items_bows","waterway2/items_ingredients","waterway2/items_quests",
			"waterway2/items_armour","waterway/mining",
			"waterway/sharpeners","waterway/lootboxes","waterway/items_rainevent","waterway/items_spearfishing",
			"waterway/quest_rewards","waterway/items_titan","waterway/items_fishing",
			"waterway/items_sands",
			"necropolis/ingredients","necropolis/armour","necropolis/weapons","necropolis/tools",
			"necropolis/sharpeners","necropolis/specials","necropolis/cryptarmour","necropolis/magic",
			"necropolis/runes","necropolis/items_crypts","necropolis/crypts_items_ancient",
			"necropolis/mushroom_items","necropolis/items_boss","necropolis/items_digging",
			"necropolis/items_crypts_variant_corrupt","necropolis/items_crypts_variant_living",
			"unique/bestowed",
			"infested/ingredients","infested/swords","infested/bows","infested/pickaxes","infested/armours",
			"infested/magic","infested/items_temple","infested/items_luxium","infested/items_potions",
			"ruins/ingredients","ruins/mining","ruins/weapons","ruins/armour"
			};
	*/
	public static final String[] setFiles = {
			"waterway2/sets"
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
		
		appliables = new HashMap<>();
		attributeColours = new HashMap<String,String>();
		attributeColours.put("damage", ChatColor.GRAY + "Damage: " + w);
		attributeColours.put("damagep", ChatColor.GREEN + "Damage: " + w);
		
		attributeColours.put("health", ChatColor.RED + "Health: " + w);
		attributeColours.put("armour", ChatColor.BLUE + "Armour: " + w);
		attributeColours.put("regen", ChatColor.YELLOW + "Regen: " + w);
		
		attributeColours.put("bonusxp", ChatColor.AQUA + "Xp Boost: " + w);
		
		attributeColours.put("miningspeed", ChatColor.GRAY + "Speed: " + w);
		attributeColours.put("fortune", ChatColor.GOLD + "Fortune: " + w);
		
		attributeColours.put("killcoins", ChatColor.GOLD + "Bonus Coins: " + w);
		
		attributeColours.put("mana", ChatColor.LIGHT_PURPLE + "Mana: " + w);
		
		attributeColours.put("luck", ChatColor.GREEN + "Luck: " + w);
		attributeColours.put("fishingspeed",ChatColor.AQUA + "Fishing Speed: " + w);
		
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
				if (sets.contains(set + ".set_attributes"))
				{
					for (String attr : sets.getConfigurationSection(set + ".set_attributes").getKeys(false))
					{
						s.set_attributes.put(attr, sets.getDouble(set + ".set_attributes." + attr));
					}
				}
				s.set_ability = sets.getString(set + ".set_ability","none");
				s.syn_ability = sets.getString(set + ".synergy_ability","none");
				if (sets.contains(set + ".synergy_attributes"))
				{
					for (String attr : sets.getConfigurationSection(set + ".synergy_attributes").getKeys(false))
					{
						s.syn_attributes.put(attr, sets.getDouble(set + ".synergy_attributes." + attr));
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
			
			
			
			for (String it : data.getKeys(false)) generate(data,it,f);
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

	
	public void generate(FileConfiguration data, String it,String f)
	{
		//System.out.println(it);
		String[] sp = it.split("-");
		if (sp.length == 1) generate_regular(data,it,f);
		else if (sp[0].equals("armour")) generate_armour(data,it,f);
	}
	public void generate_armour(FileConfiguration data,String it,String f)
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
			item.areaReq = data.getString(it + ".areareq",null);
			
			
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
			else if (data.contains(it + ".colours") && !(i == 0 && data.contains(it + ".skull")))
			{
				item.data.put("r", Integer.parseInt(data.getString(it + ".colours." + i).split(",")[0]));
				item.data.put("g", Integer.parseInt(data.getString(it + ".colours." + i).split(",")[1]));
				item.data.put("b", Integer.parseInt(data.getString(it + ".colours." + i).split(",")[2]));
			}
			if (data.contains(it + ".set")) item.set = itemsets.get(data.getString(it + ".set"));
			if (data.contains(it + ".ability"))
			{
				String ability = data.getString(it + ".ability");
				if (ability.split(",").length > 1) item.data.put("ability", ability.split(",")[i]);
				else item.data.put("ability", ability);
			}
			item.area = f.split("/")[0];
			items.put("armour_" + it.split("-")[1] + armour_pieces[i].toLowerCase(), item);
			itemCount++;
		}
	}
	public void generate_regular(FileConfiguration data, String it,String f)
	{
		String type = data.getString(it + ".type", "ingredient");
		Item item;
		if (type.equals("lootbox")) item = new ItemLootbox();
		else if (type.equals("wand")) item = new ItemWand();
		else if (type.equals("spell")) item = new ItemSpell();
		else if (type.equals("appliable")) item = new ItemAppliable();
		else if (type.equals("engine")) item = new ItemEngine();
		else if (type.equals("potionmat")) item = new ItemPotionMat();
		else item = new Item();

		item.type = type;
		item.rarity = Rarity.valueOf(data.getString(it + ".rarity", "COMMON"));
		item.name = rarityColours[item.rarity.ordinal()] + data.getString(it + ".name", "null");
		if (data.contains(it + ".description")) item.description = data.getString(it + ".description",null).split(";");
		else item.description = null;
		item.attributes = new HashMap<String,Double>();
		item.glow = data.getBoolean(it + ".glow", false);
		item.material = Material.valueOf(data.getString(it + ".item"));
		item.id = itemCount;
		
		item.combatReq = data.getInt(it + ".req", 0);
		item.areaReq = data.getString(it + ".areareq",null);
		//item.nolore = data.getBoolean(item + ".nolore",false);
		item.noDesc = data.getBoolean(it + ".nodesc", false);
		item.noStack = data.getBoolean(it + ".nostack",false) || item.type.equals("engine");
		item.noRunic = data.getBoolean(it + ".norune",false);
		item.prefix = data.getString(it + ".prefix");
		
		if (type.equals("weapon"))
		{
			item.slots = data.getInt(it + ".slots", 1);
		}
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
		else if (item instanceof ItemPotionMat)
		{
			ItemPotionMat mat = (ItemPotionMat)item;
			String[] spl = data.getString(it + ".potion", "void,1").split(";");
			for (String s : spl)
			{
				PotionElement element = new PotionElement();
				element.type = ElementType.valueOf(s.split(",")[0].toUpperCase());
				element.level = Integer.parseInt(s.split(",")[1]);
				mat.elements.add(element);
			}
		}
		else if (item instanceof ItemEngine)
		{
			ItemEngine engine = (ItemEngine)item;
			engine.capacity = data.getInt(it + ".capacity",0);
		}
		else if (item instanceof ItemWand)
		{
			ItemWand wand = (ItemWand)item;
			wand.mana = data.getInt(it + ".wand.mana",0);
			wand.damage = data.getInt(it + ".wand.damage",0);
		}
		else if (item instanceof ItemSpell)
		{
			ItemSpell spell = (ItemSpell)item;
			String[] c = data.getString(it + ".spell.colour", "255,0,0").split(",");
			spell.colour = Color.fromRGB(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
			spell.damage = data.getInt(it + ".spell.damage",0);
			spell.speed = data.getDouble(it + ".spell.speed", 1);
			spell.mana = data.getInt(it + ".spell.mana",0);
			spell.pierces = data.getInt(it + ".spell.pierces",0);
		}
		else if (item instanceof ItemAppliable)
		{
			ItemAppliable appliable = (ItemAppliable)item;
			
			//appliable.noDesc = true;
			appliable.prefix = data.getString(it + ".modifier.prefix");
			appliable.order = data.getInt(it + ".modifier.order", 0);
			for (String material : data.getString(it + ".modifier.types").split(",")) appliable.suitable.add(Material.valueOf(material));
			for (String attr : data.getConfigurationSection(it + ".modifier.attributes").getKeys(false))
			{
				appliable.app_attributes.put(attr, data.getDouble(it + ".modifier.attributes." + attr));
			}
			
			appliables.put(it, appliable);
		}
		if (data.contains(it + ".attributes"))
		{
			for (String attr : data.getConfigurationSection(it + ".attributes").getKeys(false))
			{
				item.attributes.put(attr, data.getDouble(it + ".attributes." + attr));
			}
		}
		if ((item.material == Material.FIREWORK_STAR || item.material == Material.POTION) && data.contains(it + ".colour"))
		{
			item.data.put("colour", Color.fromRGB(Integer.parseInt(data.getString(it + ".colour").split(",")[0]),
					Integer.parseInt(data.getString(it + ".colour").split(",")[1]),
					Integer.parseInt(data.getString(it + ".colour").split(",")[2])));
		}
		if (item.type.equals("note"))
		{
			item.data.put("note", data.getString(it + ".note"));
			item.noDesc = true;
			if (data.getBoolean(it + ".included", true)) noteCount++;
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
			sha.plusColour = ChatColor.valueOf(data.getString(it + ".sharpener.colour").toUpperCase());
			sha.plus = data.getString(it + ".sharpener.plus","+");
			sha.attributes = item.attributes;
			sha.id = it;
			
			sharps.put(sha.id, sha);
			sharpeners.put(it, sha.id);
		}
		item.area = f.split("/")[0];
		itemCount++;
		items.put(it, item);
	}
	public ItemStack build(String s,int amount)
	{
		ItemStack product = build(s,null,"","");
		product.setAmount(amount);
		return product;
	}
	public ItemStack build(String s,String e,int amount)
	{
		ItemStack product = build(s,null,e,"");
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
		else if (s.equals("randomnote")) return build("alchemical_note" + RandomFunctions.random(1, noteCount),owner,enchants,sh);
		else if (s.equals("null")) return null;
		else if (s.equals("uielement")) return null;
		else if (s.equals("overflow_jar"))
		{
			ItemStack i = build("polish_jar",owner);
			ItemMeta meta = i.getItemMeta();
			meta.getPersistentDataContainer().set(kItem, PersistentDataType.STRING, "overflow_jar");
			meta.getPersistentDataContainer().set(kEnchants, PersistentDataType.STRING, enchants);
			
			meta.setDisplayName(ChatColor.YELLOW + "Overflow Jar");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.DARK_GRAY + "Assorted Potion Ingredients");
			lore.add("");
			lore.add(ChatColor.LIGHT_PURPLE + "Elements: ");
			for (String ele: enchants.split(";")) 
			{
				String[] elesp = ele.split(",");
				PotionElement element = new PotionElement(ElementType.valueOf(elesp[0]),Integer.parseInt(elesp[1]));
				if (element.type == ElementType.VOID) lore.add(ChatColor.LIGHT_PURPLE + "- " + element.type.display() + " " + element.type.pretty());
				else lore.add(ChatColor.LIGHT_PURPLE + "- " + element.type.display() + " " + element.type.pretty() + " " + element.level);
			}
			meta.setLore(lore);
			i.setItemMeta(meta);
			
			return i;
		}
		else if (s.equals("potion"))
		{
			ItemStack i = build("water_bottle",owner);
			ItemMeta meta = i.getItemMeta();
			meta.getPersistentDataContainer().set(kItem, PersistentDataType.STRING, "potion");
			meta.getPersistentDataContainer().set(kEnchants, PersistentDataType.STRING, enchants);
			
			List<PotionType> effects = new ArrayList<>();
			List<Integer> levels = new ArrayList<>();
			for (String pot : enchants.split(";"))
			{
				if (pot.equals("")) continue;
				effects.add(PotionType.valueOf(pot.split(",")[0]));
				levels.add(Integer.parseInt(pot.split(",")[1]));
			}
			List<PotionColour> colours = new ArrayList<>();
			List<String> lore = new ArrayList<>();
			
			meta.setDisplayName(ChatColor.YELLOW + "Brewed Potion");
			for (int poe = 0; poe < effects.size();poe++)
			{
				PotionType effect = effects.get(poe);
				colours.add(effect.colour);
				lore.add("");
				lore.add(ChatColor.RED + StringManipulator.capitalise(effect.name()) + " " + StringManipulator.romanNumerals[levels.get(poe)-1] + ChatColor.GRAY + " (20:00)");
				
				List<String> nl = new ArrayList<>();
				((AbsPotion)effect.create(levels.get(poe))).text(nl);
				for (int n = 0; n < nl.size();n++) nl.set(n, ChatColor.GRAY + nl.get(n));
				lore.addAll(nl);
			}

			meta.setLore(lore);
			PotionMeta metaP = (PotionMeta)meta;
			Color c = PotionColour.combine(colours).asColour();
			metaP.setColor(c);
			
			i.setItemMeta(meta);

			return i;
		}
		Item item = items.get(s);
		if (item == null) return new ItemStack(Material.valueOf(s.toUpperCase()));
		
		ItemStack ret = new ItemStack(item.material);
		ItemMeta meta = ret.getItemMeta();
		
		meta.getPersistentDataContainer().set(kItem, PersistentDataType.STRING, s);
		meta.getPersistentDataContainer().set(kCustomName, PersistentDataType.STRING, item.name);
		meta.getPersistentDataContainer().set(kEnchants, PersistentDataType.STRING, enchants);
		meta.getPersistentDataContainer().set(kSharps, PersistentDataType.STRING, sh);
		if (item.type.equals("engine")) setFuel(meta,0);
		//meta.getPersistentDataContainer().set(kExtras, PersistentDataType.STRING, "");
		meta.setUnbreakable(true);
		
		meta.addItemFlags(ItemFlag.HIDE_DYE);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		
		if (item.material == Material.FIREWORK_STAR)
		{
			FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
            FireworkEffect aa = FireworkEffect.builder().withColor((Color)item.data.get("colour")).build();
            metaFw.setEffect(aa);
		}
		else if (item.material == Material.POTION)
		{
			PotionMeta metaP = (PotionMeta)meta;
			Color c = (Color)item.data.get("colour");
			metaP.setColor(c);
		}
        
		if (item.type.equals("armour") || item.type.equals("weapon") || item.type.equals("wand") || item.type.equals("spell")
				|| item.type.equals("modifier") || item.type.equals("appliable") || item.noStack) 
		{
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID().toString(),0,Operation.MULTIPLY_SCALAR_1));
		}
		if (item.material == Material.PLAYER_HEAD)
		{
			meta = generateSkullMeta(meta,(String)item.data.get("skull_data"),(String)item.data.get("skull_sig"));
		}

		ret.setItemMeta(updateMeta(meta,owner));

		if (item.nolore) return noLore(ret);
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
	public static ItemMeta generateSkullMeta(ItemMeta meta, Player player)
	{
		SkullMeta smeta = (SkullMeta)meta;
		GameProfile profile = ((CraftPlayer)player).getProfile();

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
			if (t.equals("book"))
			{
				boolean hasSpecial = false;
				List<Pair<AbsEnchTypes,AbsEnch>> pairs = AbsEnchTypes.getPairs(meta.getPersistentDataContainer());
				
				for (Pair<AbsEnchTypes,AbsEnch> e : pairs) if (e.key.special) hasSpecial = true;
				if (hasSpecial) lore.add(ChatColor.DARK_GRAY + "An item can only have 1 special enchant!");
				
				int rarity = 0;
				lore.add("");
				for (Pair<AbsEnchTypes,AbsEnch> e : pairs)
				{
					rarity = Math.max(rarity, e.value.rarity());
					rarity = Math.min(rarity, Rarity.values().length-1);
					if (pairs.size() > 4) lore.add(ChatColor.DARK_PURPLE + e.key.name + " " + e.value.level);
					else
					{
						if (e.value.level < e.value.max()) lore.add(ChatColor.DARK_PURPLE + e.key.name + " " + e.value.level);
						else if (e.value.level == e.value.max()) lore.add(ChatColor.BLUE + e.key.name + " " + e.value.level);
						else if (e.value.max() == 0) lore.add(ChatColor.RED + e.key.name + " " + e.value.level);
						else lore.add(ChatColor.GOLD + e.key.name + " " + e.value.level);
						for (String l : e.value.description()) lore.add(ChatColor.GRAY + " " + l);
					}
				}
				meta.setDisplayName(rarityColours[rarity] + ChatColor.stripColor(meta.getDisplayName()));
				lore.add(0,ChatColor.DARK_GRAY + StringManipulator.capitalise(Rarity.values()[rarity].toString()) +
						" " + StringManipulator.capitalise(pairs.get(0).key.type) + " Book");
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
			plus = plus + sharp.plusColour + sharp.plus;
		}
		ArrayList<ItemAppliable> app = getAppliables(meta);
		String c = meta.getPersistentDataContainer().get(kCustomName, PersistentDataType.STRING);
		String cs = c;
		if (!plus.equals("")) cs += w + " [" + plus + w + "]";
		
		String colours = ChatColor.getLastColors(c);
		if (app != null && app.size() > 0)
		{
			cs = colours + StringManipulator.capitalise(app.get(app.size()-1).prefix) + " " + cs;
		}
		if (runePrefix(meta) != null) cs = colours + runePrefix(meta) + " " + cs;
		if (item.prefix != null)
		{
			cs = colours + StringManipulator.capitalise(item.prefix) + " " + cs;
		}
		meta.setDisplayName(cs);
		
		
		if (item.type.equals("note"))
		{
			String symbols = "";
			for (String sym : ((String)item.data.get("note")).split(","))
			{
				symbols += ElementType.valueOf(sym.toUpperCase()).display();
			}
			lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Scrawled on the note you");
			lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "see the symbols " + symbols);
		}
		
		
		if (!item.noDesc)
		{
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
			else if (item.type.equals("potionmat")) lore.add(ChatColor.DARK_GRAY + StringManipulator.capitalise(item.rarity.toString()) + " Potion Ingredient");
			else if (!item.type.equals("wand") && !item.type.equals("spell")) 
			{
				String description = ChatColor.DARK_GRAY + StringManipulator.capitalise(item.rarity.toString()) +" " + StringManipulator.capitalise(item.type);
				if (item.slots > 0) 
				{
					int am = sh.length;
					if (sh[0].equals("")) am = 0;
					description = description + " [" + am + "/" + item.slots + "]";
				}
				lore.add(description);
			}
		}
		if (item.description != null)
		{
			if (item.description[0].length() > 0 && item.description[0].charAt(0) != ';') lore.add("");
			for (String desc : item.description) lore.add(ChatColor.GRAY + desc);
		}
		if (item instanceof ItemEngine)
		{
			lore.add("");
			lore.add(ChatColor.GOLD + "Luxium: " + ChatColor.YELLOW + getFuel(meta) + "/" + ((ItemEngine)item).capacity + "✦");
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
				if (app != null && app.size() > 0)
				{
					for (ItemAppliable a : app)
					{
						for (Entry<String,Double> at : a.app_attributes.entrySet())
						{
							double atv = attributes.getOrDefault(at.getKey(), 0.0);
							attributes.put(at.getKey(), atv+at.getValue());
						}
					}
				}
				if (hasAttribute(attributes,"damage")) lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + getIntAttribute(attributes,"damage"));
				if (hasAttribute(attributes,"damagep")) lore.add(ChatColor.GRAY + "Power: " + ChatColor.RED + getDoubleAttribute(attributes,"damagep"));
				if (hasAttribute(attributes,"mana")) lore.add(ChatColor.GRAY + "Mana: " + ChatColor.LIGHT_PURPLE + getIntAttribute(attributes,"mana"));
				if (hasAttribute(attributes,"attackspeed")) lore.add(ChatColor.GRAY + "Attack Speed: " + ChatColor.YELLOW + getIntAttribute(attributes,"attackspeed"));
				if (hasAttribute(attributes,"health")) lore.add(ChatColor.GRAY + "Health: " + ChatColor.GREEN + getIntAttribute(attributes,"health"));
				if (hasAttribute(attributes,"armour")) lore.add(ChatColor.GRAY + "Armour: " + ChatColor.GREEN + getIntAttribute(attributes,"armour"));
				if (hasAttribute(attributes,"regen")) lore.add(ChatColor.GRAY + "Regen: " + ChatColor.GREEN + getIntAttribute(attributes,"regen"));
				if (hasAttribute(attributes,"miningspeed")) lore.add(ChatColor.GRAY + "Mining Speed: " + ChatColor.BLUE + getIntAttribute(attributes,"miningspeed"));
				if (hasAttribute(attributes,"fishingspeed")) lore.add(ChatColor.GRAY + "Fishing Power: " + ChatColor.BLUE + getIntAttribute(attributes,"fishingspeed"));
				if (hasAttribute(attributes,"fortune")) lore.add(ChatColor.GRAY + "Mining Fortune: " + ChatColor.BLUE + getIntAttribute(attributes,"fortune"));
				if (hasAttribute(attributes,"bonusxp")) lore.add(ChatColor.GRAY + "Bonus Xp: " + ChatColor.AQUA + getDoubleAttribute(attributes,"bonusxp"));
				if (hasAttribute(attributes,"luck")) lore.add(ChatColor.GRAY + "Luck: " + ChatColor.AQUA + getIntAttribute(attributes,"luck") + "%");
				
				if (hasAttribute(attributes,"killcoins"))
				{
					if (attributes.size() > 1) lore.add("");
					lore.add(ChatColor.GRAY + "Grants " + ChatColor.GOLD + getIntAttribute(attributes,"killcoins") + ChatColor.GRAY + " coins on kill.");
				}
			}
			lore.add("");
			lore.add(ChatColor.GRAY + "Engines are active when in the");
			lore.add(ChatColor.GRAY + "slot directly below your boots.");
		}
		else if (item instanceof ItemPotionMat)
		{
			ItemPotionMat mat = (ItemPotionMat)item;
			lore.add("");
			lore.add(ChatColor.LIGHT_PURPLE + "Elements: ");
			for (PotionElement element : mat.elements) 
			{
				if (element.type == ElementType.VOID) lore.add(ChatColor.LIGHT_PURPLE + "- " + element.type.display() + " " + element.type.pretty());
				else lore.add(ChatColor.LIGHT_PURPLE + "- " + element.type.display() + " " + element.type.pretty() + " " + element.level);
			}
		}
		if (item.attributes.size() > 0 && !(item instanceof ItemEngine))
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
			if (app != null && app.size() > 0)
			{
				for (ItemAppliable a : app)
				{
					for (Entry<String,Double> at : a.app_attributes.entrySet())
					{
						double atv = attributes.getOrDefault(at.getKey(), 0.0);
						attributes.put(at.getKey(), atv+at.getValue());
					}
				}
			}
			for (AbsEnch enchants : AbsEnchTypes.get(meta.getPersistentDataContainer())) enchants.stats(owner,attributes);
			
			lore.add("");
			if (hasAttribute(attributes,"damage")) lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + getIntAttribute(attributes,"damage"));
			if (hasAttribute(attributes,"damagep")) lore.add(ChatColor.GRAY + "Power: " + ChatColor.RED + getDoubleAttribute(attributes,"damagep"));
			if (hasAttribute(attributes,"mana")) lore.add(ChatColor.GRAY + "Mana: " + ChatColor.LIGHT_PURPLE + getIntAttribute(attributes,"mana"));
			if (hasAttribute(attributes,"attackspeed")) lore.add(ChatColor.GRAY + "Attack Speed: " + ChatColor.YELLOW + getIntAttribute(attributes,"attackspeed"));
			if (hasAttribute(attributes,"health")) lore.add(ChatColor.GRAY + "Health: " + ChatColor.GREEN + getIntAttribute(attributes,"health"));
			if (hasAttribute(attributes,"armour")) lore.add(ChatColor.GRAY + "Armour: " + ChatColor.GREEN + getIntAttribute(attributes,"armour"));
			if (hasAttribute(attributes,"regen")) lore.add(ChatColor.GRAY + "Regen: " + ChatColor.GREEN + getIntAttribute(attributes,"regen"));
			if (hasAttribute(attributes,"miningspeed")) lore.add(ChatColor.GRAY + "Mining Speed: " + ChatColor.BLUE + getIntAttribute(attributes,"miningspeed"));
			if (hasAttribute(attributes,"fishingspeed")) lore.add(ChatColor.GRAY + "Fishing Power: " + ChatColor.BLUE + getIntAttribute(attributes,"fishingspeed"));
			if (hasAttribute(attributes,"fortune")) lore.add(ChatColor.GRAY + "Mining Fortune: " + ChatColor.BLUE + getIntAttribute(attributes,"fortune"));
			if (hasAttribute(attributes,"bonusxp")) lore.add(ChatColor.GRAY + "Bonus Xp: " + ChatColor.AQUA + getDoubleAttribute(attributes,"bonusxp"));
			if (hasAttribute(attributes,"luck")) lore.add(ChatColor.GRAY + "Luck: " + ChatColor.AQUA + getIntAttribute(attributes,"luck") + "%");
			
			if (hasAttribute(attributes,"killcoins"))
			{
				if (attributes.size() > 1) lore.add("");
				lore.add(ChatColor.GRAY + "Grants " + ChatColor.GOLD + getIntAttribute(attributes,"killcoins") + ChatColor.GRAY + " coins on kill.");
			}
			
			/*
			for (Entry<String,Double> attribute : attributes.entrySet())
			{
				String d = Math.round(attribute.getValue()*100) + "%";
				if (attribute.getValue().intValue() == attribute.getValue()) d = Integer.toString(attribute.getValue().intValue());
				lore.add(attributeColours.get(attribute.getKey()) + d);
			}
			*/
		}
		if (item instanceof ItemWand) 
		{
			ItemWand wand = (ItemWand)item;
			ItemSpell spell = null;
			if (get(meta,ItemWand.kSpell) != null) spell = (ItemSpell)get(meta,ItemWand.kSpell);
			if (spell != null)
			{
				lore.add(ChatColor.GRAY + "Spell: " + ((ItemSpell)get(meta,ItemWand.kSpell)).name);
				lore.add("");
			}
			if (wand.damage != 0) 
			{
				if (spell != null) lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + (wand.damage+spell.damage));
				else lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + wand.damage);
			}
			lore.add(ChatColor.GRAY + "Mana cost: " + ChatColor.AQUA + wand.cost(meta));
			
			if (get(meta,ItemWand.kModifier) != null)
			{
				Item mod = get(meta,ItemWand.kModifier);
				lore.add("");
				lore.addAll(addAbility((String)mod.data.get("ability"),""));
			}
		}
		
		else if (item instanceof ItemSpell)
		{
			ItemSpell spell = (ItemSpell)item;
			
			lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + spell.damage);
			if (spell.mana != 0) lore.add(ChatColor.GRAY + "Mana Cost: " + ChatColor.AQUA + spell.mana);
			if (spell.pierces > 0) lore.add(ChatColor.GRAY + "Pierces: " + ChatColor.GREEN + spell.pierces);
		}
		if (!meta.getPersistentDataContainer().get(kEnchants, PersistentDataType.STRING).equals(""))
		{
			lore.add("");
			
			List<Pair<AbsEnchTypes,AbsEnch>> pairs = AbsEnchTypes.getPairs(meta.getPersistentDataContainer());
			if (pairs.size() > 8)
			{
				boolean next = false;
				String lor = "";
				for (Pair<AbsEnchTypes,AbsEnch> e : pairs)
				{
					int level = e.value.level;
					int max = e.value.max();
					String name = e.key.name + " " + level;
					
					if (!next)
					{
						if (e.key.special) lor = ChatColor.RED + e.key.name;
						else if (level < max) lor = ChatColor.DARK_PURPLE + name;
						else if (level == max) lor = ChatColor.BLUE + name;
						else lor = ChatColor.GOLD + name;
					}
					else
					{
						if (e.key.special) lor = ChatColor.RED + e.key.name;
						else if (level < max) lor = lor + ", " + ChatColor.DARK_PURPLE + name;
						else if (level == max) lor = lor + ", " + ChatColor.BLUE + name;
						else lor = lor + ", " + ChatColor.GOLD + name;
						lore.add(lor);
					}
					next = !next;
				}
				if (next) lore.add(lor);
			}
			else
			{
				for (Pair<AbsEnchTypes,AbsEnch> e : pairs)
				{
					int level = e.value.level;
					int max = e.value.max();
					String name = e.key.name + " " + level;
					
					if (e.key.special) lore.add(ChatColor.RED + e.key.name);
					else if (level < max) lore.add(ChatColor.DARK_PURPLE + name);
					else if (level == max) lore.add(ChatColor.BLUE + name);
					else lore.add(ChatColor.GOLD + name);
					if (pairs.size() <= 4) for (String l : e.value.description()) lore.add(ChatColor.GRAY + " " + l);
				}
			}
		}
		else if (item.glow) meta.addEnchant(Enchantment.DIG_SPEED, 2, true);
		else meta.removeEnchant(Enchantment.DIG_SPEED);
		if (item.material == Material.TRIDENT) meta.addEnchant(Enchantment.LOYALTY, 5, true);
		
		if (item.data.containsKey("ability"))
		{
			lore.add("");
			lore.addAll(addAbility((String)item.data.get("ability"),""));
		}
		if (item.type.equals("ritual"))
		{
			lore.add("");
			lore.add(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "Rituals severely damage the soul.");
			lore.add(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "Tamper too much, you die.");
		}
		if (meta.getPersistentDataContainer().has(kRunic, PersistentDataType.STRING))
		{
			lore.add("");
			lore.addAll(addAbility(meta.getPersistentDataContainer().get(kRunic, PersistentDataType.STRING),""));
		}
		else if (item.type.equals("rune") || item.type.equals("ritual"))
		{
			lore.add("");
			lore.addAll(addAbility(meta.getPersistentDataContainer().get(kItem, PersistentDataType.STRING),""));
		}
		
		if (owner != null && item.set != null)
		{	
			if ((item.type.equals("armour") && owner.stats.set) || (item.type.equals("weapon") && owner.stats.synergy))
			{
				lore.add("");
				Map<String,Double> attributes;
				String ability;
				if (item.type.equals("armour")) 
				{
					lore.add(ChatColor.GOLD + ChatColor.BOLD.toString() + "SET BONUS");

					attributes = item.set.set_attributes;
					ability = item.set.set_ability;
				}
				else 
				{
					lore.add(ChatColor.GOLD + ChatColor.BOLD.toString() + "SYNERGY BONUS");
					
					attributes = item.set.syn_attributes;
					ability = item.set.syn_ability;
				}
				
				if (hasAttribute(attributes,"damage")) lore.add(ChatColor.GRAY + " Damage: " + ChatColor.RED + getIntAttribute(attributes,"damage"));
				if (hasAttribute(attributes,"damagep")) lore.add(ChatColor.GRAY + " Power: " + ChatColor.RED + getDoubleAttribute(attributes,"damagep"));
				if (hasAttribute(attributes,"mana")) lore.add(ChatColor.GRAY + " Mana: " + ChatColor.LIGHT_PURPLE + getIntAttribute(attributes,"mana"));
				if (hasAttribute(attributes,"attackspeed")) lore.add(ChatColor.GRAY + " Attack Speed: " + ChatColor.YELLOW + getIntAttribute(attributes,"attackspeed"));
				if (hasAttribute(attributes,"health")) lore.add(ChatColor.GRAY + " Health: " + ChatColor.GREEN + getIntAttribute(attributes,"health"));
				if (hasAttribute(attributes,"armour")) lore.add(ChatColor.GRAY + " Armour: " + ChatColor.GREEN + getIntAttribute(attributes,"armour"));
				if (hasAttribute(attributes,"regen")) lore.add(ChatColor.GRAY + " Regen: " + ChatColor.GREEN + getIntAttribute(attributes,"regen"));
				if (hasAttribute(attributes,"miningspeed")) lore.add(ChatColor.GRAY + " Mining Speed: " + ChatColor.BLUE + getIntAttribute(attributes,"miningspeed"));
				if (hasAttribute(attributes,"fishingspeed")) lore.add(ChatColor.GRAY + " Fishing Power: " + ChatColor.BLUE + getIntAttribute(attributes,"fishingspeed"));
				if (hasAttribute(attributes,"fortune")) lore.add(ChatColor.GRAY + " Mining Fortune: " + ChatColor.BLUE + getIntAttribute(attributes,"fortune"));
				if (hasAttribute(attributes,"bonusxp")) lore.add(ChatColor.GRAY + " Bonus Xp: " + ChatColor.AQUA + getDoubleAttribute(attributes,"bonusxp"));
				if (hasAttribute(attributes,"luck")) lore.add(ChatColor.GRAY + " Luck: " + ChatColor.AQUA + getIntAttribute(attributes,"luck") + "%");
				if (!ability.equals("none")) lore.addAll(addAbility(ability," "));
			}
			
			if (!c.equals(item.name)) lore.add(ChatColor.DARK_GRAY + ChatColor.stripColor(item.name));
		}
		if ((owner == null || item.combatReq > owner.level.level) && item.combatReq > 0) 
		{
			lore.add(ChatColor.RED + "✖ Requires Level " + item.combatReq);
		}
		if (item.areaReq != null)
		{
			if (owner == null || !item.areaReq.equals(owner.area.id))
			{
				lore.add(ChatColor.RED + "✦ Only works in " + DungeonManager.i.warps.get(item.areaReq).name);
			}
		}
		if (item.type.equals("lootbox"))
		{
			lore.add("");
			lore.add(ChatColor.GOLD + "Click to open the lootbox");
			lore.add(ChatColor.GOLD + " and get awesome rewards!");
		}
		meta.setLore(lore);
		return meta;
	}
	private boolean hasAttribute(Map<String,Double> attributes,String attribute)
	{
		return attributes.getOrDefault(attribute,0d) != 0d;
	}
	private String getIntAttribute(Map<String,Double> attributes,String attribute)
	{
		int atr = attributes.getOrDefault(attribute, 0d).intValue();
		if (atr == 0) return null;
		else if (atr > 0) return "+" + Integer.toString(atr);
		else return Integer.toString(atr);
	}
	private String getDoubleAttribute(Map<String,Double> attributes,String attribute)
	{
		double atr = attributes.getOrDefault(attribute, 0d);
		if (atr == 0) return null;
		else if (atr > 0) return "+" + Math.round(atr*1000)/10 + "%";
		else return Math.round(atr*1000)/10 + "%";
	}
	public ArrayList<String> addAbility(String ab,String prefix)
	{
		ArrayList<String> n = new ArrayList<String>();
		AbsAbility abs = AbilityManager.get(ab);
		if (abs == null)
		{
			n.add(ChatColor.RED + "Ability not found!");
			return n;
		}
		for (int i = 0; i < abs.description().size(); i++)
		{
			if (i == 0) n.add(prefix + abs.description().get(i));
			else n.add(prefix + ChatColor.GRAY + " " + abs.description().get(i));
		}
		return n;
	}
	
	public static int stack(BackpackItem i)
	{
		if (i.itemType.equals("book")) return 1;
		Item item = ItemBuilder.i.items.get(i.itemType);
		
		if (item == null) return 64;
		if (item.noStack) return 1;
		switch (item.type)
		{
		case "weapon":
		case "armour":
		case "book":
		case "wand":
		case "spell":
		case "tool":
		case "modifier":
			return 1;
		default:
			return 64;
		}
	}
	public static int stack(Item item)
	{
		if (item == null) return 64;
		if (item.noStack) return 1;
		switch (item.type)
		{
		case "weapon":
		case "armour":
		case "book":
		case "wand":
		case "spell":
		case "tool":
		case "artifact":
		case "modifier":
			return 1;
		default:
			return 64;
		}
	}
	public ItemStack maxStack(ItemStack i)
	{
		if (!i.hasItemMeta()) return i;
		PersistentDataContainer p = i.getItemMeta().getPersistentDataContainer();
		if (p == null) return i;
		Item item = items.get(p.getOrDefault(kItem, PersistentDataType.STRING, null));
		i.setAmount(stack(item));
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
		if (sharps.length > item.slots-1 && (item.slots == 1 && !sharps[0].equals(""))) return false;
		return true;
	}
	public boolean canAddRune(ItemStack i)
	{
		ItemMeta moot = i.getItemMeta();
		PersistentDataContainer pdc = moot.getPersistentDataContainer();
		
		Item item = items.get(pdc.get(kItem, PersistentDataType.STRING));
		if (item == null) return false;
		if (item.noRunic) return false;
		if (!item.type.equals("weapon")) return false;
		
		return true;
	}
	public static boolean hasExtra(ItemStack item,ItemStack extra)
	{
		String ext = getItem(extra);
		for (String ex : item.getItemMeta().getPersistentDataContainer().get(kExtras, PersistentDataType.STRING).split(","))
		{
			if (ex.equals(ext)) return true;
		}
		return false;
	}
	public static ItemStack addExtra(ItemStack item,ItemStack extra)
	{
		ItemAppliable appliable = ItemBuilder.i.appliables.get(getItem(extra));
		if (appliable == null) return item;
		if (!appliable.suitable.contains(item.getType())) return item;
		ItemStack better = item.clone();
		String extras = item.getItemMeta().getPersistentDataContainer().getOrDefault(kExtras,PersistentDataType.STRING,"");
		//if (extras.equals("")) extras = getItem(extra);
		//else extras = extras + "," + getItem(extra);
		extras = getItem(extra);
		
		ItemMeta meta = better.getItemMeta();
		meta.getPersistentDataContainer().set(kExtras, PersistentDataType.STRING, extras);
		ItemBuilder.i.updateMeta(meta,null);
		better.setItemMeta(meta);
		return better;
	}
	public static ItemStack addExtras(ItemStack item,String extra)
	{
		if (extra.equals("")) return item;
		ItemStack better = item.clone();
		String extras = item.getItemMeta().getPersistentDataContainer().getOrDefault(kExtras,PersistentDataType.STRING,"");
		//if (extras.equals("")) extras = extra;
		//else extras = extras + "," + extra;
		extras = extra;
		
		ItemMeta meta = better.getItemMeta();
		meta.getPersistentDataContainer().set(kExtras, PersistentDataType.STRING, extras);
		ItemBuilder.i.updateMeta(meta,null);
		better.setItemMeta(meta);
		return better;
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
		else if (current.length < get(i).slots) ew += ";" + sharpener;
		
		pdc.set(kSharps, PersistentDataType.STRING, ew);
		
		moot = updateMeta(moot,null);
		item.setItemMeta(moot);
		return item;
	}
	
	public ItemStack addRune(ItemStack i, String rune)
	{
		ItemStack item = i.clone();
		ItemMeta moot = item.getItemMeta();
		PersistentDataContainer pdc = moot.getPersistentDataContainer();
		
		pdc.set(kRunic, PersistentDataType.STRING, rune);
		
		moot = updateMeta(moot,null);
		item.setItemMeta(moot);
		return item;
	}
	@SuppressWarnings("unchecked")
	public static int enchantable(ItemStack item1,ItemStack item2,ItemStack catalyst)
	{
		if (item1 == null || item2 == null || catalyst == null) return 4;
		List<AbsEnch> enchants = AbsEnchTypes.get(item1.getItemMeta().getPersistentDataContainer());
		List<AbsEnchTypes> types = AbsEnchTypes.getTypes(item1.getItemMeta().getPersistentDataContainer());
		enchants.addAll(AbsEnchTypes.get(item2.getItemMeta().getPersistentDataContainer()));
		types.addAll(AbsEnchTypes.getTypes(item2.getItemMeta().getPersistentDataContainer()));
		
		int c = Integer.parseInt(catalyst.getItemMeta().getPersistentDataContainer().get(kItem, PersistentDataType.STRING).split("=")[1]);
		String type = "book";
		if (ItemBuilder.get(item1) != null) type = ItemBuilder.get(item1).type;
		
		List<AbsEnchTypes> specialcheck = (List<AbsEnchTypes>) ((ArrayList<AbsEnchTypes>)types).clone();
		specialcheck.removeIf((AbsEnchTypes e) -> !e.special);
		if (specialcheck.size() > 1) return 6;
		//if (type.equals("book")) return 2;
		if (bothBooks(item1,item2))
		{
			for (AbsEnch e : enchants) if (e.level == e.max()) return 5;
			if (AbsEnchTypes.catalyst(item2) == c) return 0;
			else return 3;
		}
		if (AbsEnchTypes.catalyst(item2) != c) return 3;
		for (AbsEnchTypes e : types) if (!e.type.equals(type)) return 1;
		return 0;
	}
	/*
	 * ITEM1 must be the item being enchanted.
	 * 0 = YES
	 * 1 = incompatible enchantment
	 * 2 = item is a book!
	 * 3 = catalyst is incorrect
	 * 4 = item missing
	 * 5 = max level
	 * 6 = already has special enchantment
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public static int canEnchant(ItemStack item1,ItemStack item2,ItemStack catalyst)
	{
		if (item1 == null || item2 == null || catalyst == null) return 4;
		ArrayList<AbsEnchant> enchants = EnchantManager.get(item1.getItemMeta().getPersistentDataContainer());
		enchants.addAll(EnchantManager.get(item2.getItemMeta().getPersistentDataContainer()));
		
		int c = Integer.parseInt(catalyst.getItemMeta().getPersistentDataContainer().get(kItem, PersistentDataType.STRING).split("=")[1]);
		String type = "book";
		if (ItemBuilder.get(item1) != null) type = ItemBuilder.get(item1).type;

		ArrayList<AbsEnchant> specialcheck = (ArrayList<AbsEnchant>)enchants.clone();
		specialcheck.removeIf((AbsEnchant e) -> e.max() != 0);
		if (specialcheck.size() > 1) return 6;
		//if (type.equals("book")) return 2;
		if (bothBooks(item1,item2))
		{
			for (AbsEnchant e : enchants) if (e.level == e.max()) return 5;
			if (EnchantManager.catalyst(item2) == c) return 0;
			else return 3;
		}
		if (EnchantManager.catalyst(item2) != c) return 3;
		for (AbsEnchant e : enchants) if (!e.type().equals(type)) return 1;
		return 0;
	}
	public static boolean bothBooks(ItemStack item1,ItemStack item2)
	{
		if (getItem(item1).equals("book") && getItem(item2).equals("book")) return true;
		else return false;
	}
	
	public static ArrayList<ItemAppliable> getAppliables(ItemStack item)
	{
		PersistentDataContainer p = item.getItemMeta().getPersistentDataContainer();
		if (!p.has(kExtras, PersistentDataType.STRING)) return null;
		
		ArrayList<ItemAppliable> applist = new ArrayList<>();
		for (String s : p.get(kExtras, PersistentDataType.STRING).split(","))
		{
			ItemAppliable app = ItemBuilder.i.appliables.get(s);
			if (app != null) applist.add(app);
		}
		return applist;
	}
	public static ArrayList<ItemAppliable> getAppliables(ItemMeta item)
	{
		PersistentDataContainer p = item.getPersistentDataContainer();
		if (!p.has(kExtras, PersistentDataType.STRING)) return null;
		
		ArrayList<ItemAppliable> applist = new ArrayList<>();
		for (String s : p.get(kExtras, PersistentDataType.STRING).split(","))
		{
			ItemAppliable app = ItemBuilder.i.appliables.get(s);
			if (app != null) applist.add(app);
		}
		return applist;
	}
	public static int getFuel(ItemMeta item)
	{
		if (item == null) return 0;
		PersistentDataContainer p = item.getPersistentDataContainer();
		if (!p.has(kFuel, PersistentDataType.INTEGER)) return 0;
		
		return p.get(kFuel, PersistentDataType.INTEGER);
	}
	public static void setFuel(ItemMeta item,int fuel)
	{
		if (item == null) return;
		PersistentDataContainer p = item.getPersistentDataContainer();
		
		
		p.set(kFuel, PersistentDataType.INTEGER,fuel);
	}
	public static void addFuel(ItemMeta item,int fuel)
	{
		if (item == null) return;
		ItemEngine engine = (ItemEngine)get(item);
		if (engine == null) return;
		PersistentDataContainer p = item.getPersistentDataContainer();
		
		int amount = Math.min(engine.capacity, getFuel(item) + fuel);
		p.set(kFuel, PersistentDataType.INTEGER,amount);
	}
	
	public static String getItem(ItemStack item)
	{
		if (!item.hasItemMeta()) return null;
		return item.getItemMeta().getPersistentDataContainer().get(kItem, PersistentDataType.STRING);
	}
	public static String getEnchants(ItemStack item)
	{
		if (!item.hasItemMeta()) return null;
		return item.getItemMeta().getPersistentDataContainer().get(kEnchants, PersistentDataType.STRING);
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
	public static ItemStack noLore(ItemStack item)
	{
		ItemMeta me = item.getItemMeta();
		me.setLore(null);
		item.setItemMeta(me);
		
		return item;
	}
	
	public String runePrefix(ItemMeta meta)
	{
		if (!meta.getPersistentDataContainer().has(kRunic, PersistentDataType.STRING)) return null;
		Item rune = items.get(meta.getPersistentDataContainer().getOrDefault(kRunic, PersistentDataType.STRING, "none"));
		switch (rune.type)
		{
		case "rune":
			return "Runic";
		case "ritual":
			return "Aligned";
		default: return null;
		}
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
		if (level < AbsEnchTypes.get(enchant).max()) return false;
		else return true;
	}

	public static Item get(ItemMeta item, NamespacedKey key) 
	{
		return i.items.get(item.getPersistentDataContainer().get(key, PersistentDataType.STRING));
	}
}
