package com.carterz30cal.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.EnchantHandler;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.tasks.TaskAGUIBrowser;
import com.carterz30cal.tasks.TaskGUI;
import com.carterz30cal.tasks.TaskGUIEnchanting;
import com.carterz30cal.tasks.TaskGUIRecipeBrowser;
import com.carterz30cal.utility.StringManipulator;


public class GUI
{
	public final MenuType type;
	public final HashMap<Integer,MenuType> actions;
	public Inventory inventory;
	public int page;
	public boolean drop;
	private final int[] positions = {
			10,11,12,13,14,15,16,
			19,20,21,22,23,24,25,
			28,29,30,31,32,33,34,
			37,38,39,40,41,42,43
	};
	public static final Material[] perkBackgrounds = 
		{
				Material.BLACK_STAINED_GLASS_PANE,
				Material.PURPLE_STAINED_GLASS_PANE,
				Material.BLUE_STAINED_GLASS_PANE,
				Material.ORANGE_STAINED_GLASS_PANE,
				Material.BEDROCK
		};
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
			if (p.isOp())
			{
				lo = new String[] {
						ChatColor.RED + " Max Health: " + ChatColor.WHITE + Math.round(player.stats.health),
						ChatColor.BLUE + " Armour: " + ChatColor.WHITE + player.stats.armour,
						ChatColor.YELLOW + " Regen: " + ChatColor.WHITE + player.stats.regen,
						ChatColor.GRAY + " Damage: " + ChatColor.WHITE + player.stats.damage,
						"",
						ChatColor.BOLD + "" + ChatColor.GOLD + "Click for the Admin Menu"
						};
			}
			else
			{
				lo = new String[] {
						ChatColor.RED + " Max Health: " + ChatColor.WHITE + Math.round(player.stats.health),
						ChatColor.BLUE + " Armour: " + ChatColor.WHITE + player.stats.armour,
						ChatColor.YELLOW + " Regen: " + ChatColor.WHITE + player.stats.regen,
						ChatColor.GRAY + " Damage: " + ChatColor.WHITE + player.stats.damage
						};
			}
			contents[GUICreator.top()] = GUICreator.item(Material.PAPER, p.getDisplayName(), lo, 1);
			
			contents[10] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
			contents[11] = GUICreator.item(Material.STONE_SWORD, "Skills", null, 1);
			contents[12] = GUICreator.item(Material.COMPASS, ChatColor.GREEN + "Dungeon Explorer", null, 1);
			contents[13] = GUICreator.item(Material.PLAYER_HEAD, "Friends", null, 1);
			contents[14] = GUICreator.item(Material.BOOK, "Calendar", null, 1);
			contents[15] = GUICreator.item(Material.GOLD_INGOT, ChatColor.GOLD + "Rewards", null, 1);
			contents[16] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
			
			contents[19] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
			contents[20] = GUICreator.item(Material.REDSTONE, "Perks", null, 1);
			contents[21] = GUICreator.item(Material.LEATHER, "Backpack", null, 1);
			contents[22] = GUICreator.item(Material.CREEPER_SPAWN_EGG, ChatColor.BLUE + "Beastiary", null, 1);
			contents[23] = GUICreator.item(Material.APPLE, "Recipe Browser", null, 1);
			contents[24] = GUICreator.item(Material.ANVIL, "Anvil", null, 1);
			contents[25] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
			break;
		case SKILLS:
			String[] summary = {
					ChatColor.GOLD + " Combat "   + ChatColor.WHITE + player.skills.getSkillLevel("combat"),
					ChatColor.GOLD + " Crafting " + ChatColor.WHITE + player.skills.getSkillLevel("crafting"),
					ChatColor.GOLD + " Mining "   + ChatColor.WHITE + player.skills.getSkillLevel("mining")
					};
			contents[GUICreator.top()] = GUICreator.item(Material.PAPER, p.getDisplayName() + "'s skills", summary, 1);
			String[] combatL = GUICreator.skills("combat", player, "+" + player.skills.getSkillLevel("combat")*4 + "% damage\n" + "+" 
			+ player.skills.getSkillLevel("combat") + " fist damage");
			String[] craftingL = GUICreator.skills("crafting", player, "no bonus yet");
			String[] miningL = GUICreator.skills("mining", player, "+" + player.skills.getSkillLevel("mining") + "% ore chance");
			contents[GUICreator.middle(size)-1] = GUICreator.item(Material.GOLDEN_SWORD,
					ChatColor.GOLD + "Combat " + ChatColor.WHITE + player.skills.getSkillLevel("combat"),combatL, 1);
			contents[GUICreator.middle(size)] = GUICreator.item(Material.STICK,
					ChatColor.GOLD + "Crafting " + ChatColor.WHITE + player.skills.getSkillLevel("crafting"), craftingL, 1);
			contents[GUICreator.middle(size)+1] = GUICreator.item(Material.GOLDEN_PICKAXE,
					ChatColor.GOLD + "Mining " + ChatColor.WHITE + player.skills.getSkillLevel("mining"), miningL, 1);
			contents[GUICreator.bottom(size)] = GUICreator.item(Material.RED_STAINED_GLASS_PANE, "Back", null, 1);
			actions.put(GUICreator.bottom(size),MenuType.MAINMENU);
			break;
		case PERKS:
			new GUI(1,p);
			doRender = false;
			break;
		case BACKPACK:
			/*
			for (BackpackItem item : player.backpack)
			{
				if (item == null) continue;
				contents[item.slot] = item.create(player.highlightRenamed);
			}
			*/
			new BackpackGUI(player.player);
			break;
		case SETTINGS:
			ChatColor pbc = ChatColor.GREEN;
			if (player.colourblindMode) pbc = ChatColor.BLUE;
			
			contents[GUICreator.middle(size)-2] = GUICreator.item(Material.GOLD_INGOT,
					ChatColor.RED + "Progress Bar Character: " + pbc +  StringManipulator.progressChars[player.settingSkillsDisplay], new String[]{" Click to change"}, 1);
			actions.put(GUICreator.middle(size)-2,MenuType.MAINMENU);
			
			ItemStack colourblind = GUICreator.item(Material.IRON_INGOT,"Colourblind Mode: " 
					+ StringManipulator.capitalise(Boolean.toString(player.colourblindMode)),null,1);
			if (player.colourblindMode) colourblind.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			contents[GUICreator.middle(size)-1] = colourblind;
			actions.put(GUICreator.middle(size)-1,MenuType.MAINMENU);
			
			contents[GUICreator.middle(size)] = GUICreator.item(perkBackgrounds[player.perkBackground], "Perk Menu Background", new String[]{" Click to change"}, 1);
			actions.put(GUICreator.middle(size),MenuType.MAINMENU);
			
			if (player.highlightRenamed) contents[GUICreator.middle(size)+1] = GUICreator.item(Material.GLOWSTONE_DUST, "Highlight Renamed Items: True", null, 1);
			else contents[GUICreator.middle(size)+1] = GUICreator.item(Material.GUNPOWDER, "Highlight Renamed Items: False", null, 1);
			actions.put(GUICreator.middle(size)+1, MenuType.MAINMENU);
			
			contents[GUICreator.bottom(size)] = GUICreator.item(Material.RED_STAINED_GLASS_PANE, "Back", null, 1);
			actions.put(GUICreator.bottom(size),MenuType.MAINMENU);
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
	// constructor specifically for perks
	// it could go in the normal gui but this is way easier
	public GUI (int pag,Player p)
	{
		drop = true;
		page = pag;
		type = MenuType.PERKS;
		DungeonsPlayer player = DungeonsPlayerManager.i.get(p);
		int size = GUICreator.typeSize(type);
		inventory = GUICreator.createBase(size, type);
		actions = new HashMap<Integer,MenuType>();
		ItemStack[] contents = inventory.getContents();
		
		int current = (pag-1)*28;
		int end = current+28;
		int ticker = 0;
		String[] perks = player.perks.perks.keySet().toArray(new String[0]);
		
		while (current < end)
		{
			if (current < player.perks.perks.size())
			{
				contents[positions[ticker]] = GUICreator.perk(perks[current], player);
			}
			else contents[positions[ticker]] = GUICreator.pane(perkBackgrounds[player.perkBackground]);
			
			ticker++;
			current++;
		}
		contents[GUICreator.bottom(size)] = GUICreator.item(Material.RED_STAINED_GLASS_PANE, "Back", null, 1);
		actions.put(GUICreator.bottom(size),MenuType.MAINMENU);
		if (pag > 1) 
		{
			contents[GUICreator.bottom(size)-2] = GUICreator.item(Material.ARROW, "Page " + (pag-1), null, 1);
			actions.put(GUICreator.bottom(size-2),MenuType.MAINMENU);
		}
		if (end < player.perks.perks.size()) 
		{	
			contents[GUICreator.bottom(size)+2] = GUICreator.item(Material.ARROW, "Page " + (pag+1), null, 1);
			actions.put(GUICreator.bottom(size+2),MenuType.MAINMENU);
		}
		inventory.setContents(contents);
		render(p);
	}
	public void render(Player p)
	{
		p.openInventory(inventory);
		DungeonsPlayerManager.i.get(p).gui = this;
	}

	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		DungeonsPlayer dp = DungeonsPlayerManager.i.get(p);
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
			else if (!EnchantHandler.eh.isUIElement(e.getCurrentItem()))
			{
				ItemStack add = e.getCurrentItem().clone();
				add = ItemBuilder.i.maxStack(add);
				p.getInventory().addItem(add);
			}
				
		}
		else if (type == MenuType.ENCHANTING)
		{
			ItemStack set = null;
			int pos = position;
			if (e.getClick() == ClickType.SHIFT_LEFT && position >= mSize)
			{
				ItemStack shiftItem = e.getView().getItem(position);
				if (EnchantHandler.eh.isBook(shiftItem) && EnchantHandler.eh.isUIElement(inventory.getItem(GUICreator.top()+9)))
				{
					set = shiftItem;
					pos = GUICreator.top()+9;
					e.getView().setItem(position, null);
				}
				else if (EnchantHandler.eh.enchantable(shiftItem) && EnchantHandler.eh.isUIElement(inventory.getItem(GUICreator.top())))
				{
					set = shiftItem;
					pos = GUICreator.top();
					e.getView().setItem(position, null);
				}
				else if (EnchantHandler.eh.isCatalyst(shiftItem) && EnchantHandler.eh.isUIElement(inventory.getItem(GUICreator.top()+18)))
				{
					set = shiftItem;
					pos = GUICreator.top()+18;
					e.getView().setItem(position, null);
				}
			}
			else if ((position == GUICreator.top()
					|| position == GUICreator.top()+9
					|| position == GUICreator.top()+18))
			{
				ItemStack purple = GUICreator.item(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE + "Enchantable Item Slot", null, 1);
				ItemStack yellow = GUICreator.item(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.GOLD + "Book Slot", null, 1);
				ItemStack black = GUICreator.item(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GRAY + "Catalyst Slot", null, 1);
				
				ItemStack clickItem = inventory.getItem(position);
				if (!EnchantHandler.eh.isUIElement(clickItem))
				{
					switch (position)
					{
					case 4:
						set = purple;
						e.getView().setCursor(null);
						break;
					case 13:
						set = yellow;
						e.getView().setCursor(null);
						break;
					case 22:
						set = black;
						e.getView().setCursor(null);
						break;
					default:
						set = null;
						e.getView().setCursor(null);
						break;
					}
					
					if (p.getInventory().firstEmpty() == -1) p.getWorld().dropItem(p.getLocation(), clickItem);
					else p.getInventory().addItem(clickItem);
					
					
				}
				else 
				{
					set = null;
					switch (position)
					{
					case 4:
						if (EnchantHandler.eh.enchantable(e.getCursor())) 
						{
							set = e.getCursor();
							e.getView().setCursor(null);
						}
						break;
					case 13:
						if (EnchantHandler.eh.isBook(e.getCursor()))
						{
							set = e.getCursor();
							e.getView().setCursor(null);
						}
						break;
					case 22:
						if (EnchantHandler.eh.isCatalyst(e.getCursor()))
						{
							set = e.getCursor();
							e.getView().setCursor(null);
						}
						break;
					default:
						break;
					}
					
				}
				
			}
			else if (position == 40 && !EnchantHandler.eh.isUIElement(inventory.getItem(40)))
			{
				p.getInventory().addItem(inventory.getItem(40));
				ItemStack catalyst = inventory.getItem(GUICreator.top()+18);
				if (catalyst.getAmount() > 1)
				{
					catalyst.setAmount(catalyst.getAmount()-1);
					p.getInventory().addItem(catalyst);
				}
				new SoundTask(p.getLocation(), p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2, 1).runTaskLater(Dungeons.instance, 4);
				drop = false;
				new TaskGUI(new GUI(MenuType.ENCHANTING,p),p).runTaskLater(Dungeons.instance, 1);
				return true;
			}
			if (set != null) 
			{
				new TaskGUIEnchanting(set,pos,p).runTaskLater(Dungeons.instance, 1);
				return true;
			}
			
		}
		if (type == MenuType.RECIPES)
		{
			if (position == 35) new AnvilGUI(p,3);
			if (position == 10)
			{
				ItemStack c = inventory.getItem(10);
				if (EnchantHandler.eh.isUIElement(c))
				{
					if (e.getView().getCursor().getType() == Material.AIR) return true;
					new TaskGUIRecipeBrowser(this,e.getView().getCursor()).runTaskLater(Dungeons.instance, 1);
					e.getView().setCursor(null);
				}
				else
				{
					new TaskGUIRecipeBrowser(this,GUICreator.pane()).runTaskLater(Dungeons.instance, 1);
					
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
				new TaskGUIRecipeBrowser(this,null).runTaskLater(Dungeons.instance, 1);
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
				new SkillsGUI(p);
				break;
			case 12:
				new DungeonExplorerGUI(p);
				break;
			case 13:
				//new GUI(MenuType.FRIENDS,p);
				break;
			case 14:
				//new GUI(MenuType.CRAFTING,p);
				break;
			case 15:
				new RewardsGUI(p);
				break;
			case 20:
				new GUI(MenuType.PERKS,p);
				break;
			case 21:
				new GUI(MenuType.BACKPACK,p);
				break;
			case 22:
				new GUI(MenuType.SETTINGS,p);
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
			if (type == MenuType.PERKS)
			{
				if (position == 47) new GUI(page-1,p);
				else if (position == 51) new GUI(page+1,p);
				else new GUI(actions.get(position),p);
			}
			else if (type == MenuType.SETTINGS)
			{
				
				if (position == GUICreator.middle(mSize)-2)
				{
					dp.settingSkillsDisplay = dp.settingSkillsDisplay + 1;
					if (dp.settingSkillsDisplay == StringManipulator.progressChars.length) dp.settingSkillsDisplay = 0;
				}
				else if (position == GUICreator.middle(mSize)-1)
				{
					dp.colourblindMode = !dp.colourblindMode;
				}
				else if (position == GUICreator.middle(mSize))
				{
					dp.perkBackground = dp.perkBackground + 1;
					if (dp.perkBackground == perkBackgrounds.length) dp.perkBackground = 0;
				}
				else if (position == GUICreator.middle(mSize) + 1)
				{
					dp.highlightRenamed = !dp.highlightRenamed;
				}
				else if (position == GUICreator.bottom(mSize))
				{
					new GUI(MenuType.MAINMENU,p);
					return true;
				}
				new GUI(MenuType.SETTINGS,p);
				return true;
			}
			else
			{
				new GUI(actions.get(position),p);
			}
			return true;
		}
		if (type == MenuType.BACKPACK) return false;
		else return true;
	}
	
	public boolean handleDrag(InventoryDragEvent e,Player p)
	{
		return true;
	}
}
