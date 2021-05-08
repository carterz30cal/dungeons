package com.carterz30cal.gui;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.StringManipulator;

import org.bukkit.ChatColor;

public class SkillsGUI extends GUI
{
	public SkillsGUI(Player p) {
		super(p);
		
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		
		int menuSize = 54;
		
		inventory = Bukkit.createInventory(null, menuSize, "Character Level");
		ItemStack[] contents = new ItemStack[menuSize];
		
		for (int i = 0; i < 54;i++)
		{
			if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == 5) contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
			else contents[i] = GUICreator.pane(Material.GRAY_STAINED_GLASS_PANE);
		}
		
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = ItemBuilder.generateSkullMeta(head.getItemMeta(), d.player);
		meta.setDisplayName(CharacterSkill.prettyText(d.level.level()) +" "+ d.player.getDisplayName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.BLUE + "Experience: " + ChatColor.WHITE + StringManipulator.truncateLess(d.level.experience)
				+ " / " + StringManipulator.truncateLess(CharacterSkill.requirement(d.level.level()+1))
				+ ChatColor.BLUE + " (" + d.level.prettyProgress() + "%)");
		lore.add("");
		lore.add("");
		lore.add(ChatColor.WHITE + "Points available: " + d.level.points);
		meta.setLore(lore);
		head.setItemMeta(meta);
		contents[4] = head;
		
		for (int pro = 11; pro < 16;pro++)
		{
			double poo = (pro - 10) * 0.2;
			if (poo <= d.level.progress()) contents[pro] = GUICreator.pane(Material.LIME_STAINED_GLASS_PANE);
			else if (poo - d.level.progress() <= 0.2 && poo - d.level.progress() > 0) contents[pro] = GUICreator.pane(Material.ORANGE_STAINED_GLASS_PANE);
			else contents[pro] = GUICreator.pane(Material.RED_STAINED_GLASS_PANE);
		}
		
		int level = d.level.get("health");
		boolean glow = d.level.points > 0;
		contents[29] = GUICreator.item(Material.APPLE          , ChatColor.RED  + "Vitality "  + level         ,"+" + (level*4) + " Health",glow);
		level = d.level.get("armour");
		contents[30] = GUICreator.item(Material.IRON_CHESTPLATE, ChatColor.BLUE + "Golem " + level          ,"+" + (level*3) + " Armour",glow);
		level = d.level.get("damage");
		contents[31] = GUICreator.item(Material.IRON_SWORD     , ChatColor.GRAY + "Knight " + level          ,"+" + (level*1) + " Damage",glow);
		level = d.level.get("mana");
		contents[32] = GUICreator.item(Material.BLAZE_POWDER   , ChatColor.LIGHT_PURPLE + "Wizardry " + level,"+" + (level*4) + " Mana",glow);
		level = d.level.get("bonuscoins");
		contents[33] = GUICreator.item(Material.GOLD_INGOT     , ChatColor.GOLD + "Wealth " + level          ,"+" + level + " coins per kill",glow && level < 20);
		
		level = d.level.get("luck");
		contents[38] = GUICreator.item(Material.FISHING_ROD   , ChatColor.AQUA + "Fisherman " + level,"+" + (level*2) + " Luck",glow);
		
		contents[39] = GUICreator.item(Material.BARRIER, ChatColor.RED + "Coming soon!", ChatColor.DARK_RED + "This skill will release soon!");
		contents[40] = GUICreator.item(Material.BARRIER, ChatColor.RED + "Coming soon!", ChatColor.DARK_RED + "This skill will release soon!");
		contents[41] = GUICreator.item(Material.BARRIER, ChatColor.RED + "Coming soon!", ChatColor.DARK_RED + "This skill will release soon!");
		contents[42] = GUICreator.item(Material.BARRIER, ChatColor.RED + "Coming soon!", ChatColor.DARK_RED + "This skill will release soon!");
		
		contents[49] = GUICreator.item(Material.PHANTOM_MEMBRANE, ChatColor.DARK_RED + "Scrub skills",
				new String[] {ChatColor.DARK_RED + "Reset your skills and get all points back",ChatColor.GOLD + "Costs 1000 coins"},1);
				
		inventory.setContents(contents);
		render(p);
	}

	@Override
	public boolean handleClick(InventoryClickEvent e, int position, Player p)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		if (position == 49 && d.coins >= 1000)
		{
			int pts = 0;
			for (int t : d.level.pointAllocation.values()) pts += t;
			d.level.pointAllocation.clear();
			d.level.points += pts;
			d.coins -= 1000;
			new SkillsGUI(p);
			return true;
		}
		String add = null;
		if (position == 29) add = "health";
		else if (position == 30) add = "armour";
		else if (position == 31) add = "damage";
		else if (position == 32) add = "mana";
		else if (position == 33 && d.level.get("bonuscoins") < 20) add = "bonuscoins";
		else if (position == 38) add = "luck";
		
		if (add != null && d.level.points > 0)
		{
			int lvl = d.level.get(add);
			d.level.points--;
			d.level.pointAllocation.put(add, ++lvl);
			
			new SkillsGUI(p);
		}
		return true;
	}
	@Override
	public boolean handleDrag(InventoryDragEvent e, Player p)
	{
		return true;
	}
}
