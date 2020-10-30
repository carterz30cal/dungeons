package com.carterz30cal.utility;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
		boolean bypass = commonItem && p.settings.getOrDefault("commondropsbackpack", "false").equals("true");
		if (!bypass && p.player.getInventory().firstEmpty() != -1) p.player.getInventory().addItem(item);
		else insertBackpack(p,item);
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
				else if (page[i].itemType == it && page[i].amount < item.getMaxStackSize()) 
				{
					page[i].amount += item.getAmount();
					return;
				}
			}
		}
		// if we get this far, we need a new backpack page
		BackpackItem[] npage = new BackpackItem[p.backpackb.get(0).length];
		npage[0] = new BackpackItem(item,0);
		p.backpackb.add(npage);
	}
}
