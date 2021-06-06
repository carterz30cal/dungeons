package com.carterz30cal.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.areas.InfestedHunter;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.mobs.abilities.DMobAbility;
import com.carterz30cal.mobs.abilities.MobOwned;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

import net.md_5.bungee.api.ChatColor;

public class MonsterHunterGUI extends GUI
{
	public DungeonsPlayer owner;
	
	private static String[] summon;
	public MonsterHunterGUI(Player p) 
	{
		super(p);
		
		inventory = Bukkit.createInventory(null, 36, "Tarantula Hunter");
		owner = DungeonsPlayerManager.i.get(p);
		int skill = level(owner.level.hunter);
		ItemStack[] contents = new ItemStack[36];
		summon = new String[36];
		
		for (int i = 0; i < 36; i++) contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
		
		
		contents[3] = GUICreator.item(Material.STRING, ChatColor.GREEN + "Tarantula Hunter", 
				new String[] {"Tarantulas give a bounty upon being slain, and","have a chance to drop many useful items","which can help you with your adventures!"},1);
		
		ItemStack head = GUICreator.item(Material.PLAYER_HEAD, ChatColor.GOLD + "Hunter " + skill,owner.level.hunter + " / " + requirement(skill));
		head.setItemMeta(ItemBuilder.generateSkullMeta(head.getItemMeta(), p));
		contents[5] = head;
		
		contents[19] = boss("hunter_tarantula1",Material.COAL,19);
		contents[20] = boss("hunter_tarantula2",Material.LAPIS_LAZULI,20);
		contents[21] = boss("hunter_tarantula3",Material.REDSTONE,21);
		contents[22] = boss("hunter_tarantula4",Material.IRON_INGOT,22);
		contents[23] = boss("hunter_tarantula5",Material.GOLD_INGOT,23);
		contents[24] = boss("hunter_tarantula6",Material.DIAMOND,24);
		contents[25] = boss("hunter_tarantula7",Material.BLAZE_POWDER,25);
		
		inventory.setContents(contents);
		render(p);
	}
	
	public static ItemStack boss(String type,Material i,int slot)
	{
		summon[slot] = type;
		DMobType ty = DMobManager.types.get(type);
		
		MobOwned owned = null;
		for (DMobAbility a : ty.abilities)
		{
			if (a instanceof MobOwned) owned = (MobOwned) a;
		}
		ItemStack icon = GUICreator.item(i, ChatColor.RED + ty.name, null);
		ItemMeta meta = icon.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Health: " + ChatColor.RED + ty.health + "❤");
		lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + ty.damage + "✦");
		lore.add("");
		lore.add(ChatColor.GOLD + "Costs " + owned.cost + " coins to start, with");
		lore.add(ChatColor.GOLD + "a reward of " + owned.bounty + " coins if");
		lore.add(ChatColor.GOLD + "you beat the tarantula.");
		lore.add(ChatColor.AQUA + "+" + owned.xp + " hunter xp on kill.");
		
		meta.setLore(lore);
		icon.setItemMeta(meta);
		return icon;
	}
	@Override
	public boolean handleClick(InventoryClickEvent e, int position, Player p)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		if (summon[position] != null && !InfestedHunter.active.containsKey(d)) InfestedHunter.summon(d,summon[position]);
		else if (summon[position] != null) p.sendMessage(ChatColor.RED + "You already have a hunt ongoing!");
		return true;
	}
	public static long requirement(int level)
	{
		return (long) (100 * Math.pow(3, level));
	}
	public static int level(long experience)
	{
		// each level requires 3x the last.
		int level = 0;
		while (experience >= requirement(level) && level < 7) level = level+1;
		return level;
	}
	
	

}
