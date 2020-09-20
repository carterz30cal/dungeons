package com.carterz30cal.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerSkills;
import com.carterz30cal.utility.StringManipulator;


public class GUICreator 
{
	public static Inventory createBase(int size,MenuType type)
	{
		Inventory base;
		switch (type)
		{
		case BACKPACK:
			base = baseEmpty(size,type);
			break;
		default:
			base = baseFull(size,type);
		}
		
		return base;
	}
	private static String name(MenuType type)
	{
		switch (type)
		{
		case MAINMENU:
			return "Dungeons";
		case SKILLS:
			return "Skills";
		case PERKS:
			return "Perks";
		case EXPERIENCE:
			return "Experience";
		case BACKPACK:
			return "Backpack";
		case CRAFTING:
			return "Crafting";
		case SETTINGS:
			return "Settings";
		case RECIPES:
			return "Recipe Browser";
		case ENCHANTING:
			return "Enchanting";
		case REWARDS:
			return "Rewards";
		case ANVIL:
			return "Anvil";
		case ADMIN:
			return "Admin Menu";
		case ADMIN_ITEMBROWSER:
			return "Admin Item Browser";
		default:
			return "null";
		}
	}
	private static Inventory baseFull(int size,MenuType type)
	{
		Inventory b = Bukkit.createInventory(null, size,name(type));
		ItemStack[] contents = new ItemStack[size];
		for (int i = 0; i < size; i++)
		{
			contents[i] = pane();
		}
		b.setContents(contents);
		return b;
	}
	public static ItemStack pane()
	{
		final ItemStack frame = new ItemStack(Material.WHITE_STAINED_GLASS_PANE,1);
		final ItemMeta meta = frame.getItemMeta();
		meta.setDisplayName(" ");
		meta.getPersistentDataContainer().set(ItemBuilder.kItem, PersistentDataType.STRING, "uielement");
		frame.setItemMeta(meta);
		return frame;
	}
	public static ItemStack pane(Material material)
	{
		final ItemStack frame = new ItemStack(material,1);
		final ItemMeta meta = frame.getItemMeta();
		meta.setDisplayName(" ");
		meta.getPersistentDataContainer().set(ItemBuilder.kItem, PersistentDataType.STRING, "uielement");
		frame.setItemMeta(meta);
		return frame;
	}
	public static ItemStack pane(Material material,boolean glowing)
	{
		final ItemStack frame = new ItemStack(material,1);
		final ItemMeta meta = frame.getItemMeta();
		meta.setDisplayName(" ");
		meta.getPersistentDataContainer().set(ItemBuilder.kItem, PersistentDataType.STRING, "uielement");
		if (glowing)
		{
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
		}
		frame.setItemMeta(meta);
		return frame;
	}
	public static ItemStack item(Material mat,String name,String[] lore,int amount)
	{
		ItemStack item = new ItemStack(mat,amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + name);
		
		if (lore != null)
		{
			ArrayList<String> clore = new ArrayList<String>();
			for (String l : lore) 
			{
				clore.add(ChatColor.WHITE + l);
			}
			meta.setLore(clore);
		} 
		else meta.setLore(null);
		meta.getPersistentDataContainer().set(ItemBuilder.kItem, PersistentDataType.STRING, "uielement");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		return item;
	}
	public static int middle(int size) 
	{
		return size/2;
	}
	public static int top()
	{
		return 4;
	}
	public static int bottom(int size)
	{
		return size-5;
	}

	private static Inventory baseEmpty(int size,MenuType type)
	{
		return Bukkit.createInventory(null, size,name(type));
	}
	public static String[] skills(String skill,DungeonsPlayer dp,String bonusText)
	{
		DungeonsPlayerSkills skills = dp.skills;
		int xpForLevel = DungeonsPlayerSkills.getLevelRequirement(skills.getSkillLevel(skill))
				- DungeonsPlayerSkills.getLevelRequirement(skills.getSkillLevel(skill)-1);
		int xpProgress = skills.getSkill(skill) - DungeonsPlayerSkills.getLevelRequirement(skills.getSkillLevel(skill)-1);
		
		float progress = (float)xpProgress/(float)xpForLevel;
		String progressBar = StringManipulator.progressBar(progress,dp.settingSkillsDisplay,dp.colourblindMode);
		
		switch (skills.getSkillLevel(skill))
		{
		case 0:
			String[] lo = {" " + progressBar,ChatColor.DARK_GRAY + " " + (xpForLevel-xpProgress) + " xp till next level"};
			return lo;
		case 25:
			String[] lore = {" " + ChatColor.BOLD.toString() + StringManipulator.rainbow("MAX LEVEL"),"",
					" " + ChatColor.GOLD + ChatColor.BOLD.toString() + "LEVEL BONUS: " + ChatColor.RESET + ChatColor.WHITE + bonusText
			};
			return lore;
		default:
			String[] lor = {" " + progressBar,ChatColor.DARK_GRAY + " " + (xpForLevel-xpProgress) + " xp till next level","",
					" " + ChatColor.GOLD + ChatColor.BOLD.toString() + "LEVEL BONUS: " + ChatColor.RESET + ChatColor.WHITE + bonusText};
			return lor;
		}
	}
	public static ItemStack perk(String perk,DungeonsPlayer player)
	{
		FileConfiguration p = Dungeons.instance.fPerksC;
		ChatColor namecolour = ChatColor.valueOf(p.getString(perk + ".namecolour"));
		String name = namecolour + p.getString(perk + ".name") + " " + player.perks.getLevel(perk);
		Material icon = Material.valueOf(p.getString(perk + ".icon"));
		
		ItemStack item = new ItemStack(icon,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> lore = new ArrayList<String>();
		
		int level = player.perks.getLevel(perk);
		for (String effect : p.getConfigurationSection(perk + ".effects").getKeys(false))
		{
			
			if (effect.equals("damagep")) 
			{
				double effectAm = p.getDouble(perk + ".effects." + effect)*level;
				lore.add(" " + ItemBuilder.i.attributeColours.get(effect) + ChatColor.WHITE + effectAm);
			}
			else
			{
				int effectAm = p.getInt(perk + ".effects." + effect)*level;
				lore.add(" " + ItemBuilder.i.attributeColours.get(effect) + ChatColor.WHITE + effectAm);
			}
		}
		lore.add("");
		if (level < 10) lore.add(" " + ChatColor.GOLD + player.perks.getKills(perk) + "/" + player.perks.getKillsForLevel(level+1));
		else lore.add("" + ChatColor.GOLD + player.perks.getKills(perk));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public static int typeSize(MenuType type)
	{
		switch (type)
		{
		case PERKS:
		case ENCHANTING:
		case CRAFTING:
		case ADMIN_ITEMBROWSER:
		case BACKPACK:
			return 54;
		case RECIPES:
		case ADMIN:
		case MAINMENU:
			return 36;
		default:
			return 27;
		}
	}
}
