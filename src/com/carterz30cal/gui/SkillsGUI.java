package com.carterz30cal.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.DungeonsPlayerSkills;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class SkillsGUI extends GUI
{
	public static String[] skills = {"combat","mining","crafting","magic"};
	public static Material[] icons = {Material.GOLDEN_SWORD,Material.GOLDEN_PICKAXE,Material.STICK,Material.BLAZE_ROD};
	public SkillsGUI(Player p) {
		super(p);
		
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		DungeonsPlayerSkills s = d.skills;
		int menuSize = 18 + (9*skills.length);
		
		inventory = Bukkit.createInventory(null, menuSize, "Skills");
		ItemStack[] contents = new ItemStack[menuSize];
		
		for (int i = 0; i < menuSize; i++)
		{
			if (i / 9 == 0 || i / 9 == (menuSize / 9)-1) contents[i] = GUICreator.pane();
			else
			{
				String skill = skills[(i / 9) - 1];
				int skLevel = s.getSkillLevel(skill);
				int xpForLevel = DungeonsPlayerSkills.getLevelRequirement(s.getSkillLevel(skill))
						- DungeonsPlayerSkills.getLevelRequirement(s.getSkillLevel(skill)-1);
				int xpProgress = s.getSkill(skill) - DungeonsPlayerSkills.getLevelRequirement(s.getSkillLevel(skill)-1);
				
				float progress = (float)xpProgress/(float)xpForLevel;
				//float progress = d.skills.getSkill(skill) / DungeonsPlayerSkills.getLevelRequirement(d.skills.getSkillLevel(skill)+1);
				if (i % 9 == 1) 
				{
					ItemStack icon = GUICreator.item(icons[(i / 9) - 1], ChatColor.GOLD + StringManipulator.capitalise(skill) + " " + ChatColor.RED + s.getSkillLevel(skill), null, 1);
					ItemMeta meta = icon.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(" " + StringManipulator.progressBar(progress, 0, true) + " " + ChatColor.BLUE + Math.round(progress*100) + "%");
					lore.add(ChatColor.DARK_GRAY + " " + (xpForLevel-xpProgress) + " xp to go");
					lore.add(" ");
					if (s.getSkillLevel(skill) > 0)
					{
						lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Rewards");
						switch (skill)
						{
						case "combat":
							lore.add(ChatColor.GRAY + " +" + 4*skLevel + "% damage");
							lore.add(ChatColor.GRAY + " +" + skLevel + " punch damage");
							break;
						case "mining":
							lore.add(ChatColor.GRAY + " +" + skLevel + "% ore chance");
							break;
						case "crafting":
							break;
						case "magic":
							break;
						}
					}
					else lore.add(ChatColor.GOLD + "Level up " + StringManipulator.capitalise(skill) + " to unlock skill-specific bonuses!");
					
					meta.setLore(lore);
					icon.setItemMeta(meta);
					contents[i] = icon;
				}
				else if (i % 9 > 1 && i % 9 != 8)
				{
					if (progress > ((i % 9) - 1) / 6f) contents[i] = GUICreator.pane(Material.BLUE_STAINED_GLASS_PANE);
					else contents[i] = GUICreator.pane(Material.RED_STAINED_GLASS_PANE);
				}
				else contents[i] = GUICreator.pane();
			}
		}
		
		inventory.setContents(contents);
		render(p);
	}
	@Override
	public boolean handleClick(InventoryClickEvent e, int position, Player p)
	{
		return true;
	}
	@Override
	public boolean handleDrag(InventoryDragEvent e, Player p)
	{
		return true;
	}
}
