package com.carterz30cal.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.DungeonsPlayer;

public class InventoryHandler
{
	public InventoryHandler i;
	
	public InventoryHandler()
	{
		i = this;
	}
	
	public static void addItem(DungeonsPlayer p, ItemStack item)
	{
		addItem(p,item,false);
	}
	public static void addItem(DungeonsPlayer p, ItemStack item,boolean commonItem)
	{
		boolean bypass = commonItem;
		if (!bypass && p.player.getInventory().firstEmpty() != -1) p.player.getInventory().addItem(item);
		else if (ItemBuilder.get(item) != null && ItemBuilder.get(item).type.equals("ingredient"))
		{
			p.sack.put(ItemBuilder.getItem(item), p.sack.getOrDefault(ItemBuilder.getItem(item), 0) + item.getAmount());
		}
		else insertBackpack(p,item);
	}
	private static void sort_valuables_cluster(DungeonsPlayer p)
	{
		Map<Integer,List<BackpackItem>> priorities = new HashMap<>();
		/*
		 * PRIORITIES
		 * 0 = weapons, armour,tools
		 * 1 = books, runes, sharpeners, appliables
		 * 2 = everything else
		 */
		for (int i = 0; i < 3; i++) priorities.put(i, new ArrayList<>());
		
		for (BackpackItem[] page : p.backpackb)
		{
			for (int i = 0; i < page.length;i++)
			{
				if (page[i] == null) continue;
				if (page[i].itemType.equals("book")) 
				{
					priorities.get(1).add(page[i]);
					continue;
				}
				
				Item item = ItemBuilder.i.items.get(page[i].itemType);
				if (item == null) 
				{
					priorities.get(2).add(page[i]);
					continue;
				}
				switch (item.type)
				{
				case "weapon":
				case "armour":
				case "tool":
				case "wand":
					priorities.get(0).add(page[i]);
					break;
				case "rune":
				case "sharpener":
				case "appliable":
				case "modifier":
				case "spell":
					priorities.get(1).add(page[i]);
					break;
				default:
					priorities.get(2).add(page[i]);
				}
			}
		}
		for (List<BackpackItem> l : priorities.values()) l.sort((a,b) -> a.itemType.hashCode() <= b.itemType.hashCode() ? -1 : 1);
		// add stuff in each priority tier first
		p.backpackb = new ArrayList<>();
		p.backpackb.add(new BackpackItem[45]);
		for (int i = 0; i < priorities.size();i++)
		{
			for (BackpackItem it : priorities.get(i)) insertBackpack(p,it);
		}
	}
	private static void sort_ingredients(DungeonsPlayer p)
	{
		Map<Integer,List<BackpackItem>> priorities = new HashMap<>();
		/*
		 * PRIORITIES
		 * 0 = weapons, armour,tools
		 * 1 = books, runes, sharpeners, appliables
		 * 2 = everything else
		 */
		for (int i = 0; i < 3; i++) priorities.put(i, new ArrayList<>());
		
		for (BackpackItem[] page : p.backpackb)
		{
			for (int i = 0; i < page.length;i++)
			{
				if (page[i] == null) continue;
				if (page[i].itemType.equals("book")) 
				{
					priorities.get(1).add(page[i]);
					continue;
				}
				
				Item item = ItemBuilder.i.items.get(page[i].itemType);
				if (item == null) 
				{
					priorities.get(2).add(page[i]);
					continue;
				}
				switch (item.type)
				{
				case "weapon":
				case "armour":
				case "tool":
				case "wand":
					priorities.get(0).add(page[i]);
					break;
				case "rune":
				case "sharpener":
				case "appliable":
				case "modifier":
				case "spell":
					priorities.get(1).add(page[i]);
					break;
				default:
					priorities.get(2).add(page[i]);
				}
			}
		}
		for (List<BackpackItem> l : priorities.values()) l.sort((a,b) -> a.itemType.hashCode() < b.itemType.hashCode() ? -1 : 1);
		// add stuff in each priority tier first
		p.backpackb = new ArrayList<>();
		p.backpackb.add(new BackpackItem[45]);
		for (int i = priorities.size()-1; i > -1;i--)
		{
			for (BackpackItem it : priorities.get(i)) insertBackpack(p,it);
		}
	}
	private static void sort_valuables(DungeonsPlayer p)
	{
		Map<Integer,List<BackpackItem>> priorities = new HashMap<>();
		/*
		 * PRIORITIES
		 * 0 = weapons, armour,tools
		 * 1 = books, runes, sharpeners, appliables
		 * 2 = everything else
		 */
		for (int i = 0; i < 3; i++) priorities.put(i, new ArrayList<>());
		
		for (BackpackItem[] page : p.backpackb)
		{
			for (int i = 0; i < page.length;i++)
			{
				if (page[i] == null) continue;
				if (page[i].itemType.equals("book")) 
				{
					priorities.get(1).add(page[i]);
					continue;
				}
				
				Item item = ItemBuilder.i.items.get(page[i].itemType);
				if (item == null) 
				{
					priorities.get(2).add(page[i]);
					continue;
				}
				switch (item.type)
				{
				case "weapon":
				case "armour":
				case "tool":
				case "wand":
					priorities.get(0).add(page[i]);
					break;
				case "rune":
				case "sharpener":
				case "appliable":
				case "modifier":
				case "spell":
					priorities.get(1).add(page[i]);
					break;
				default:
					priorities.get(2).add(page[i]);
				}
			}
		}
		
		// add stuff in each priority tier first
		p.backpackb = new ArrayList<>();
		p.backpackb.add(new BackpackItem[45]);
		for (int i = 0; i < priorities.size();i++)
		{
			for (BackpackItem it : priorities.get(i)) insertBackpack(p,it);
		}
	}
	public static void sortBackpack(DungeonsPlayer p,BackpackSort method)
	{
		if (method == BackpackSort.VALUABLES_FIRST) sort_valuables(p);
		else if (method == BackpackSort.VALUABLES_CLUSTER) sort_valuables_cluster(p);
		else if (method == BackpackSort.INGREDIENTS_FIRST) sort_ingredients(p);
	}
	
	public static void insertBackpack (DungeonsPlayer p, BackpackItem item)
	{
		String it = item.itemType;
		for (BackpackItem[] page : p.backpackb)
		{
			for (int i = 0; i < page.length;i++)
			{
				if (page[i] == null) 
				{
					page[i] = item;
					return;
				}
				else if (page[i].itemType.equals(it) && (page[i].amount) < ItemBuilder.stack(item)) 
				{
					int diff = ItemBuilder.stack(item) - page[i].amount;
					if (diff >= item.amount) 
					{
						page[i].amount += item.amount;
						return;
					}
					else
					{
						item.amount -= diff;
						page[i].amount += diff;
					}
				}
			}
		}
		// if we get this far, we need a new backpack page
		BackpackItem[] npage = new BackpackItem[45];
		npage[0] = item;
		p.backpackb.add(npage);
	}
	public static void insertBackpack (DungeonsPlayer p, ItemStack item)
	{
		String it = item.getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, item.getType().toString());
		for (BackpackItem[] page : p.backpackb)
		{
			for (int i = 0; i < page.length;i++)
			{
				if (page[i] == null) 
				{
					page[i] = new BackpackItem(item,i);
					return;
				}
				else if (page[i].itemType.equals(it) && (page[i].amount+item.getAmount()) <= item.getMaxStackSize()) 
				{
					page[i].amount += item.getAmount();
					return;
				}
			}
		}
		// if we get this far, we need a new backpack page
		BackpackItem[] npage = new BackpackItem[45];
		npage[0] = new BackpackItem(item,0);
		p.backpackb.add(npage);
	}
}


