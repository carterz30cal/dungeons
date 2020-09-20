package com.carterz30cal.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.EnchantHandler;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;

import net.md_5.bungee.api.ChatColor;

public class AnvilGUI extends GUI
{
	/*
	 * This class is a full replacement for the old enchanting 
	 *  screen and adds a bunch of new features.
	 * It also means we have a new free slot in the main menu
	 * 
	 * PAGE 1 - ENCHANTING
	 * PAGE 2 - SHARPENING
	 * 
	 */
	private static int maxpage = 2;
	public AnvilGUI(Player p,int pa) 
	{
		super(p);
		page = pa;
		inventory = Bukkit.createInventory(null, 45,"Anvil");
		ItemStack[] contents = new ItemStack[45];
		
		for (int i = 0; i < 45; i++)
		{
			if (i < 9)
			{
				switch (i)
				{
				case 0:
					if (page == 1) contents[i] = GUICreator.item(Material.EXPERIENCE_BOTTLE, ChatColor.BLUE + "Enchanting", null, 1);
					else contents[i] = GUICreator.item(Material.GLASS_BOTTLE, ChatColor.BLUE + "Enchanting", null, 1);
					break;
				case 1:
					if (page == 2) contents[i] = GUICreator.item(Material.DIAMOND_SWORD, ChatColor.BLUE + "Sharpening", null, 1);
					else contents[i] = GUICreator.item(Material.IRON_SWORD, ChatColor.BLUE + "Sharpening", null, 1);
					break;
				default:
					contents[i] = GUICreator.pane(Material.BROWN_STAINED_GLASS_PANE);
				}
			}
			else if (Math.floorDiv(i, 9) == 4) contents[i] = GUICreator.pane(Material.RED_STAINED_GLASS_PANE);
			else 
			{
				int port = i % 9;
				switch (page)
				{
				case 1:
				case 2:
					if (port > 2 && port < 6) contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
					else contents[i] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE,false);
					break;
				default:
					contents[i] = GUICreator.pane();
				}
			}
		}
		
		switch (page)
		{
		case 1:
			contents[19] = null;
			contents[22] = null;
			contents[25] = null;
			break;
		case 2:
			contents[19] = null;
			contents[22] = GUICreator.pane(Material.BARRIER);
			contents[25] = null;
			break;
		}
		
		inventory.setContents(contents);
		render(p);
	}
	
	@Override
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		// MENU SWITCHER
		if (position < 9)
		{
			int sel = (position % 9)+1;
			if (sel <= maxpage && sel != page) new AnvilGUI(p,sel);
			return true;
		}
		ItemStack click = e.getCurrentItem();
		if (click == null) return true;
		String it = click.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem,
				PersistentDataType.STRING, "");
		Item cli = ItemBuilder.i.items.get(it);
		switch (page)
		{
		case 1:
			// ENCHANTING LOGIC HERE
			if (cli == null && !it.equals("book")) return true;
			
			if (position == 19 || position == 22 || position == 25)
			{
				p.getInventory().addItem(click.clone());
				inventory.getItem(position).setAmount(0);
				
				for (int i = 36; i < 45; i++) inventory.setItem(i, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
				return true;
			}
			else if (position == 40)
			{
				p.getInventory().addItem(click.clone());
				click.setAmount(0);
				inventory.setItem(19, null);
				inventory.getItem(22).setAmount(inventory.getItem(22).getAmount()-1);
				inventory.setItem(25, null);
				
				for (int i = 36; i < 45; i++) inventory.setItem(i, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
				return true;
			}
			ItemStack mov = click.clone();
			mov.setAmount(Math.min(mov.getAmount(), mov.getMaxStackSize()));
			if (it.equals("book")) 
			{
				if (inventory.getItem(25) == null) inventory.setItem(25, mov);
				else if (inventory.getItem(19) == null) inventory.setItem(19, mov);
				else return true;
			}
			else if (cli.type.equals("catalyst") && inventory.getItem(22) == null) inventory.setItem(22, mov);
			else if (inventory.getItem(19) == null) inventory.setItem(19, mov);
			else return true;
			
			click.setAmount(click.getAmount()-Math.min(click.getAmount(), click.getMaxStackSize()));
			
			if (inventory.getItem(19) != null
					&& inventory.getItem(22) != null
					&& inventory.getItem(25) != null)
			{
				ItemStack item = inventory.getItem(19);
				ItemStack book = inventory.getItem(25);
				String catalyst = inventory.getItem(22).getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING);
				if (catalyst.equals(EnchantHandler.eh.getRequiredCatalyst(item, book)))
				{
					ItemStack product = inventory.getItem(19).clone();
					product = ItemBuilder.i.enchantItem(product, inventory.getItem(25));
					for (int i = 36; i < 45; i++) inventory.setItem(i, GUICreator.pane(Material.BLUE_STAINED_GLASS_PANE));
					inventory.setItem(40, product);
				}
				else
				{
					String[] explan = new String[]
					{
						" " + ChatColor.RED + "Requires " + ItemBuilder.i.build(EnchantHandler.eh.getRequiredCatalyst(item, book), null).getItemMeta().getDisplayName()
					};
					ItemStack barrier = GUICreator.item(Material.BARRIER, ChatColor.RED + "Incorrect Catalyst!", explan, 1);
					
					inventory.setItem(40, barrier);
				}
			}
			else for (int i = 36; i < 45; i++) inventory.setItem(i, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
			break;
		case 2:
			if (cli == null) return true;
			if (position == 19 || position == 25)
			{
				p.getInventory().addItem(click.clone());
				inventory.getItem(position).setAmount(0);
				
				for (int i = 36; i < 45; i++) inventory.setItem(i, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(22, GUICreator.pane(Material.BARRIER));
				return true;
			}
			else if (position == 22)
			{
				p.getInventory().addItem(click.clone());
				
				inventory.setItem(19, null);
				inventory.getItem(22).setAmount(0);
				inventory.setItem(25, null);
				
				for (int i = 36; i < 45; i++) inventory.setItem(i, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(22, GUICreator.pane(Material.BARRIER));
				return true;
			}
			ItemStack pop = click.clone();
			pop.setAmount(1);
			if (cli.type.equals("sharpener") && inventory.getItem(25) == null) inventory.setItem(25,pop);
			else if (inventory.getItem(19) == null) inventory.setItem(19, pop);
			else return true;
			click.setAmount(click.getAmount()-1);
			
			ItemStack weapon = inventory.getItem(19);
			ItemStack sharp = inventory.getItem(25);
			if (weapon != null && sharp != null && ItemBuilder.i.canSharpen(weapon))
			{
				for (int i = 36; i < 45; i++) inventory.setItem(i, GUICreator.pane(Material.BLUE_STAINED_GLASS_PANE));
				inventory.setItem(22, ItemBuilder.i.sharpenItem(weapon, sharp));
			}
			else
			{
				for (int i = 36; i < 45; i++) inventory.setItem(i, GUICreator.pane(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(22, GUICreator.pane(Material.BARRIER));
			}
			break;
		}
		return true;
	}
}
