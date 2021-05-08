package com.carterz30cal.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.StringManipulator;

import org.bukkit.ChatColor;

public class DungeonExplorerGUI extends GUI
{
	public String[] warps;
	public DungeonExplorerGUI(Player p)
	{
		super(p);
		
		DungeonManager dm = DungeonManager.i;
		DungeonsPlayer dp = DungeonsPlayerManager.i.get(p);
		inventory = Bukkit.createInventory(null, 27 + (dm.dungeons.size()/9)*9, "Dungeon Explorer");
		ItemStack[] contents = new ItemStack[inventory.getSize()];
		warps = new String[inventory.getSize()];
		int currentdungeon = 0;
		for (int i = 0; i < inventory.getSize(); i++)
		{
			if (i / 9 == inventory.getSize()/9 - 1 && i % 9 == 4) contents[i] = GUICreator.item(Material.ARROW, ChatColor.RED + "Back", null, 1);
			else if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == inventory.getSize()/9 - 1) contents[i] = GUICreator.pane();
			else
			{
				if (currentdungeon < dm.dungeons.size())
				{
					Dungeon cd = dm.ordered.get(currentdungeon);
					ItemStack icon = new ItemStack(Material.PLAYER_HEAD);
					int arealevel = dp.explorer.getAreaLevel(cd.id);
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName(ChatColor.BLUE + cd.name);
					
					ArrayList<String> lore = new ArrayList<String>();
					for (String l : cd.expl_lore.split("/")) lore.add(" " + ChatColor.GRAY + l);
					lore.add("");
					if (cd.unfinished) lore.add(ChatColor.RED + "UNFINISHED");
					else
					{
						if (arealevel == 0)
						{
							lore.add(" " + ChatColor.GREEN + "Kill another " + dp.explorer.getKillsForNext(cd.id) + " mobs");
							lore.add(" " + ChatColor.GREEN + "to unlock area specific bonuses!");
						}
						else
						{
							lore.add(ChatColor.BOLD + "" +  ChatColor.GREEN + "Area Bonus " + StringManipulator.romanNumerals[arealevel-1]);
							
							lore.add(" " + ChatColor.BLUE + "+" + (int)(dp.explorer.bonusXp(cd.id)*100) + "% xp boost");
							int excoins = dp.explorer.bonusCoins(cd.id);
							if (excoins > 0) lore.add(" " + ChatColor.GOLD + "+" + excoins + " coins per kill");
							int req = dp.explorer.getKillsForNext(cd.id);
							if (req == -1) lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "MAXED");
							else lore.add(ChatColor.GREEN + "" + req + " kills to level up");
						}
						lore.add("");
						if (dp.canWarp(cd.id)) lore.add(ChatColor.GOLD + "Right click to warp");
						else lore.add(ChatColor.RED + "✖ Cannot warp, beat previous area first");
					}
					meta.setLore(lore);
					
					meta = ItemBuilder.generateSkullMeta(meta, cd.icon_data, cd.icon_sig);
					
					icon.setItemMeta(meta);
					contents[i] = icon;
					
					warps[i] = cd.id;
					currentdungeon++;
				}
				else contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
			}
		}
		inventory.setContents(contents);
		render(p);
	}

	
	@Override
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		if (position / 9 == inventory.getSize()/9 - 1 && position % 9 == 4) new GUI(MenuType.MAINMENU,p);
		if (e.getClick() == ClickType.RIGHT)
		{
			if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) DungeonsPlayerManager.i.get(p).warp(warps[position]);
		}
		return true;
	}
}
