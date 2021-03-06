package com.carterz30cal.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.items.ItemBuilder;


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
	public static ItemStack item(Material mat,String name,String lore)
	{
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + name);
		
		ArrayList<String> l = new ArrayList<String>();
		if (lore != null)
		{
			l.add(ChatColor.WHITE + lore);
			meta.setLore(l);
		}
		else meta.setLore(null);
		meta.getPersistentDataContainer().set(ItemBuilder.kItem, PersistentDataType.STRING, "uielement");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		return item;
	}
	public static ItemStack item(Material mat,String name,String lore,boolean glow)
	{
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + name);
		
		ArrayList<String> l = new ArrayList<String>();
		if (lore != null)
		{
			l.add(ChatColor.WHITE + lore);
			meta.setLore(l);
		}
		else meta.setLore(null);
		meta.getPersistentDataContainer().set(ItemBuilder.kItem, PersistentDataType.STRING, "uielement");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		if (glow) meta.addEnchant(Enchantment.DURABILITY, 1, true);
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
