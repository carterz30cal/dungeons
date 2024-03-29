package com.carterz30cal.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.tasks.TaskAGUIBrowser;
import com.carterz30cal.tasks.TaskGUIRecipeBrowser;


public class GUI
{
	public final MenuType type;
	public final HashMap<Integer,MenuType> actions;
	public Inventory inventory;
	public int page;
	public boolean drop;
	public GUI(Player p)
	{
		type = MenuType.OTHER;
		page = -1;
		inventory = GUICreator.createBase(54, type);
		actions = new HashMap<Integer,MenuType>();
	}
	public GUI(MenuType t,Player p)
	{
		drop = true;
		page = -1;
		type = t;
		DungeonsPlayer player = DungeonsPlayerManager.i.get(p);
		int size = GUICreator.typeSize(type);
		inventory = GUICreator.createBase(size, type);
		actions = new HashMap<Integer,MenuType>();
		ItemStack[] contents = inventory.getContents();
		
		boolean doRender = true;
		switch (type)
		{
		case MAINMENU:
			String[] lo = null;
			
			int xpboosttotal = (int) Math.round((player.stats.miningXp-1)*100);
			if (p.isOp())
			{
				lo = new String[] {
						ChatColor.RED + " Max Health: " + ChatColor.WHITE + Math.round(player.stats.health),
						ChatColor.BLUE + " Armour: " + ChatColor.WHITE + player.stats.armour,
						ChatColor.YELLOW + " Regen: " + ChatColor.WHITE + player.stats.regen,
						ChatColor.GRAY + " Damage: " + ChatColor.WHITE + player.stats.damage,
						ChatColor.AQUA + "Total Xp Boost: " + ChatColor.WHITE + "+" + player.stats.flatxp + " & +" + xpboosttotal + "%",
						"",
						ChatColor.GOLD + "" + ChatColor.BOLD + "ADMIN MENU"
						};
			}
			else
			{
				lo = new String[] {
						ChatColor.RED + " Max Health: " + ChatColor.WHITE + Math.round(player.stats.health),
						ChatColor.BLUE + " Armour: " + ChatColor.WHITE + player.stats.armour,
						ChatColor.YELLOW + " Regen: " + ChatColor.WHITE + player.stats.regen,
						ChatColor.GRAY + " Damage: " + ChatColor.WHITE + player.stats.damage,
						ChatColor.AQUA + "Total Xp Boost: " + ChatColor.WHITE + "+" + player.stats.flatxp + " & +" + xpboosttotal + "%"
						};
			}
			contents[GUICreator.top()] = GUICreator.item(Material.PAPER, p.getDisplayName(), lo, 1);
			
			contents[10] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
			contents[11] = GUICreator.item(Material.STONE_SWORD, "Skill Tree", null, 1);
			contents[12] = GUICreator.item(Material.COMPASS, ChatColor.GREEN + "Dungeon Explorer", null, 1);
			contents[13] = GUICreator.item(Material.ACACIA_FENCE, "Bestiary", null, 1);
			contents[14] = GUICreator.item(Material.ENCHANTING_TABLE, "Enchantment Guide", null, 1);
			contents[15] = GUICreator.item(Material.BARRIER, "Coming soon!", null, 1);
			contents[16] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
			
			contents[19] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
			contents[20] = GUICreator.item(Material.BROWN_SHULKER_BOX, "Ingredient Sack", null, 1);
			contents[21] = GUICreator.item(Material.LEATHER, "Backpack", null, 1);
			contents[22] = GUICreator.item(Material.BARRIER, "Coming soon!", null, 1);
			contents[23] = GUICreator.item(Material.APPLE, "Recipe Browser", null, 1);
			contents[24] = GUICreator.item(Material.ANVIL, "Anvil", new String[] {"Enchanting, Applying and Crafting"}, 1);
			contents[25] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
			break;
		case ADMIN:
			for (int slot = 0; slot < size; slot++)
			{
				if (slot % 9 == 0 || slot % 9 == 8) contents[slot] = GUICreator.pane(Material.YELLOW_STAINED_GLASS_PANE);
				else if (slot / 9 == 0 || slot / 9 == 3) contents[slot] = GUICreator.pane(Material.LIME_STAINED_GLASS_PANE);
			}
			contents[10] = GUICreator.item(Material.DIAMOND_SWORD, "Item Browser", null, 1);
			contents[19] = GUICreator.item(Material.PAPER, "Online Players - " + Bukkit.getOnlinePlayers().size(), null, 1);
			break;
		case ADMIN_ITEMBROWSER:
			page = 1;
			for (int slot = 0; slot < size; slot++)
			{
				if (slot % 9 == 0 || slot % 9 == 8) contents[slot] = GUICreator.pane(Material.YELLOW_STAINED_GLASS_PANE);
				else if (slot / 9 == 0 || slot / 9 == 5) contents[slot] = GUICreator.pane(Material.LIME_STAINED_GLASS_PANE);
				else contents[slot] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
			}
			new TaskAGUIBrowser(this,p).runTaskLater(Dungeons.instance, 1);
			break;
		case RECIPES:
			page = 1;
			for (int slot = 0; slot < 27; slot++)
			{
				switch (slot % 9)
				{
				case 0:
				case 1:
				case 2:
					contents[slot] = GUICreator.item(Material.BLACK_STAINED_GLASS_PANE, "Filter by Ingredient", null, 1);
					break;
				case 3:
				case 4:
				case 5:
					contents[slot] = GUICreator.pane(Material.RED_STAINED_GLASS_PANE);
					break;
				default:
					contents[slot] = GUICreator.pane(Material.BLUE_STAINED_GLASS_PANE);
				}
			}
			contents[10] = GUICreator.item(Material.ORANGE_STAINED_GLASS_PANE, "Item Slot", null, 1);
			contents[16] = GUICreator.item(Material.BARRIER,ChatColor.RED + "No recipes associated with this item",null,1);
			contents[35] = GUICreator.item(Material.CRAFTING_TABLE, ChatColor.GOLD + "Crafting Shortcut", null, 1);
			break;
		case ENCHANTING:
			for (int slot = 0; slot < size; slot++)
			{
				if (slot < 27) contents[slot] = GUICreator.pane(Material.RED_STAINED_GLASS_PANE);
				else contents[slot] = GUICreator.pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
			}
			contents[GUICreator.top()] = GUICreator.item(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE + "Enchantable Item Slot", null, 1);
			contents[GUICreator.top()+9] = GUICreator.item(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.GOLD + "Book Slot", null, 1);
			contents[GUICreator.top()+18] = GUICreator.item(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GRAY + "Catalyst Slot", null, 1);
			contents[GUICreator.bottom(size)] = GUICreator.item(Material.RED_STAINED_GLASS_PANE, "Back", null, 1);
			actions.put(GUICreator.bottom(size),MenuType.MAINMENU);
			break;
		case CRAFTING:
			for (int slot = 0; slot < size; slot++)
			{
				if (slot % 9 > 2 && slot % 9 < 6) 
				{
					if (slot < 27) contents[slot] = GUICreator.item(Material.WHITE_STAINED_GLASS_PANE, "Ingredient Slot", null, 1);
					else contents[slot] = GUICreator.item(Material.ORANGE_STAINED_GLASS_PANE, "Output", null, 1);
				}
				else if (slot % 9 < 2 || slot % 9 > 6) contents[slot] = GUICreator.pane(Material.RED_STAINED_GLASS_PANE);
				else contents[slot] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
			}
			contents[GUICreator.bottom(size)-9] = GUICreator.item(Material.BARRIER, ChatColor.RED + "Not a valid recipe", null, 1);
			//contents[GUICreator.bottom(size)] = GUICreator.item(Material.RED_STAINED_GLASS_PANE, "Back", null, 1);
			//actions.put(GUICreator.bottom(size),MenuType.MAINMENU);
			break;
		default:
			doRender = false;
			break;
		}
		inventory.setContents(contents);
		if (doRender) render(p);
	}
	public void render(Player p)
	{
		p.openInventory(inventory);
		DungeonsPlayerManager.i.get(p).gui = this;
	}

	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		int mSize = GUICreator.typeSize(type);
		if (e.getClick() != ClickType.LEFT
				&& (type != MenuType.BACKPACK && type != MenuType.ENCHANTING && type != MenuType.CRAFTING)) return true;
		if (e.getClick() == ClickType.LEFT && position >= mSize) return false;
		if (type == MenuType.ADMIN && p.isOp())
		{
			switch (position)
			{
			case 10:
				new GUI(MenuType.ADMIN_ITEMBROWSER,p);
			}
		}
		else if (type == MenuType.ADMIN_ITEMBROWSER && p.isOp())
		{
			if (position == 47 && e.getCurrentItem().getType() == Material.ARROW)
			{
				page--;
				new TaskAGUIBrowser(this,p).runTaskLater(Dungeons.instance, 1);
			}
			else if (position == 51 && e.getCurrentItem().getType() == Material.ARROW)
			{
				page++;
				new TaskAGUIBrowser(this,p).runTaskLater(Dungeons.instance, 1);
			}
			else if (!ItemBuilder.isUIElement(e.getCurrentItem()) && e.getCurrentItem() != null)
			{
				ItemStack add = e.getCurrentItem().clone();
				add = ItemBuilder.i.maxStack(add);
				p.getInventory().addItem(add);
			}
				
		}
		if (type == MenuType.RECIPES)
		{
			if (position == 35) new AnvilGUI(p,3);
			if (position == 10)
			{
				ItemStack c = inventory.getItem(10);
				if (ItemBuilder.isUIElement(c))
				{
					if (e.getView().getCursor().getType() == Material.AIR) return true;
					new TaskGUIRecipeBrowser(this,e.getView().getCursor(),p).runTaskLater(Dungeons.instance, 1);
					e.getView().setCursor(null);
				}
				else
				{
					new TaskGUIRecipeBrowser(this,GUICreator.pane(),p).runTaskLater(Dungeons.instance, 1);
					
					if (p.getInventory().firstEmpty() == -1) p.getWorld().dropItem(p.getLocation(),c);
					else p.getInventory().addItem(c);
				}
				return true;
			}
			else if (position == 30 || position == 32)
			{
				ItemStack c = inventory.getItem(position);
				if (c.getType() == Material.ARROW)
				{
					if (position == 30) page -= 1;
					else page += 1;
				}
				new TaskGUIRecipeBrowser(this,null,p).runTaskLater(Dungeons.instance, 1);
			}
		}
		if (type == MenuType.MAINMENU)
		{
			switch (position)
			{
			case 4:
				if (p.isOp()) new GUI(MenuType.ADMIN,p);
				break;
			case 11:
				new SkillTreeGUI(p);
				break;
			case 12:
				new DungeonExplorerGUI(p);
				break;
			case 13:
				new BestiaryGUI(p);
				break;
			case 14:
				new EnchantGuideGUI(p);
				break;
			case 15:
				//new RewardsGUI(p);
				break;
			case 20:
				new SackGUI(p);
				break;
			case 21:
				new BackpackGUI(p);
				break;
			case 22:
				//new GUI(MenuType.SETTINGS,p);
				break;
			case 23:
				new GUI(MenuType.RECIPES,p);
				break;
			case 24:
				new AnvilGUI(p,1);
				break;
			}
			return true;
		}
		if (actions.containsKey(position))
		{
			new GUI(actions.get(position),p);
			return true;
		}
		if (type == MenuType.BACKPACK) return false;
		else return true;
	}
	public void handleClose(InventoryCloseEvent e)
	{
		
	}
	public boolean handleDrag(InventoryDragEvent e,Player p)
	{
		return true;
	}
}
