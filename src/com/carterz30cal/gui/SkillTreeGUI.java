package com.carterz30cal.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.skilltree.AbsSkill;
import com.carterz30cal.utility.StringManipulator;
 
import net.md_5.bungee.api.ChatColor;

public class SkillTreeGUI extends GUI 
{
	private Player owner;
	private AbsSkill[] clickables;
	private int top;
	
	public SkillTreeGUI(Player p)
	{
		super(p);
		
		owner = p;
		page = 1;
		draw();
	}
	
	private void draw()
	{
		DungeonsPlayer player = DungeonsPlayerManager.i.get(owner);
		inventory = Bukkit.createInventory(null, 45, "Skill Tree - Page " + page);
		
		ItemStack[] contents = new ItemStack[45];
		clickables = new AbsSkill[45];
		for (int i = 0; i < 45; i++)
		{
			if (i % 9 == 0 || i % 9 == 8) contents[i] = GUICreator.pane();
			else contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
		}
		
		for (AbsSkill skill : AbsSkill.skills.values())
		{
			top = Math.max(top, skill.position().page);
			if (skill.position().page != page) continue;
			int pos = (skill.position().y * 9) + skill.position().x;
			
			Material visual = Material.DIAMOND;
			ChatColor v = ChatColor.AQUA;
			if (!skill.skillreq().equals("none") && !player.skills.containsKey(skill.skillreq()))
			{
				visual = Material.REDSTONE;
				v = ChatColor.RED;
			}
			else if (skill.levelreq() > player.level.level)
			{
				visual = Material.REDSTONE;
				v = ChatColor.RED;
			}
			else if (!player.skills.containsKey(skill.id())) 
			{
				visual = Material.GOLD_INGOT;
				v = ChatColor.GOLD;
			}
			else if (player.skills.get(skill.id()) < skill.max()) 
			{
				visual = Material.EMERALD;
				v = ChatColor.GREEN;
			}
			
			ItemStack item = new ItemStack(visual,1);
			ItemMeta meta = item.getItemMeta();
			
			meta.setDisplayName(v + skill.name());
			List<String> lore = skill.template(player, player.skills.getOrDefault(skill.id(), 0));
			lore.replaceAll((String l) -> ChatColor.GRAY + l);
			meta.setLore(lore);
			item.setItemMeta(meta);
			contents[pos] = item;
			clickables[pos] = skill;
		}
		
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = ItemBuilder.generateSkullMeta(head.getItemMeta(), player.player);
		meta.setDisplayName(CharacterSkill.prettyText(player.level.level) + " " + player.player.getDisplayName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.BLUE + "Experience: " + ChatColor.WHITE + StringManipulator.truncateLess(player.level.experience)
				+ " / " + StringManipulator.truncateLess(CharacterSkill.tonextlevel(player.level.level))
				+ ChatColor.BLUE + " (" + player.level.prettyProgress() + "%)");
		lore.add("");
		lore.add("");
		lore.add(ChatColor.WHITE + "Points available: " + player.level.points());
		meta.setLore(lore);
		head.setItemMeta(meta);
		contents[18] = head;
		
		
		if (top != page) contents[0] = GUICreator.item(Material.ARROW, ChatColor.GREEN + "Page " + (page+1), null);
		if (page > 1) contents[36] = GUICreator.item(Material.ARROW, ChatColor.GREEN + "Page " + (page-1), null);
		
		contents[44] = GUICreator.item(Material.PHANTOM_MEMBRANE, ChatColor.RED + "Wipe skills - 750 coins", null);
		
		inventory.setContents(contents);
		render(owner);
	}
	
	@Override
	public boolean handleClick(InventoryClickEvent e, int position, Player p)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		if (position == 44 && d.coins >= 750 && d.skills.size() > 0)
		{
			d.coins -= 750;
			d.skills.clear();
			
			draw();
		}
		if (position == 0 && page != top) 
		{
			page++;
			draw();
		}
		else if (position == 36 && page > 1)
		{
			page--;
			draw();
		}
		else if (clickables[position] != null && d.level.points() > 0)
		{
			AbsSkill skill = clickables[position];
			if (skill.max() == d.skills.getOrDefault(skill.id(),0)) return true;
			else if (!skill.skillreq().equals("none") && !d.skills.containsKey(skill.skillreq())) return true;
			else if (skill.levelreq() > d.level.level) return true;
			
			if (e.isRightClick()) while (skill.max() > d.skills.getOrDefault(skill.id(),0) && d.level.points() > 0) d.skills.put(skill.id(), d.skills.getOrDefault(skill.id(), 0) + 1);
			else d.skills.put(skill.id(), d.skills.getOrDefault(skill.id(), 0) + 1);
			draw();
		}
		
		return true;
	}
}
