package com.carterz30cal.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.Rarity;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class SackGUI extends GUI
{
	public static final Rarity[] filters = {null,Rarity.COMMON,Rarity.RARE,Rarity.UNUSUAL,Rarity.EPIC,Rarity.INCREDIBLE,Rarity.LEGENDARY,Rarity.MYTHICAL};
	public static final String[] areas = {null,"waterway","necropolis","infested","ruins"};
	public int filter;
	public int area;
	
	public DungeonsPlayer player;
	
	private SackItem[] items;
	public SackGUI(Player p) 
	{
		super(p);
		player = DungeonsPlayerManager.i.get(p);
	
		inventory = Bukkit.createInventory(null, 54, "Ingredient Sack");
		page = 1;
		filter = 0;
		update();
		
		render(p);
	}
	
	@SuppressWarnings("unused")
	public void update()
	{
		ItemStack[] contents = new ItemStack[54];
		items = new SackItem[54];
		
		List<SackItem> filtered = new ArrayList<>();
		Rarity allowed = filters[filter];
		String selarea = areas[area];
		for (Entry<String,Integer> e : player.sack.entrySet())
		{
			if (selarea != null && !ItemBuilder.i.items.get(e.getKey()).area.equals(selarea)) continue;
			else if (allowed == null) filtered.add(new SackItem(e));
			else if (allowed == ItemBuilder.i.items.get(e.getKey()).rarity) filtered.add(new SackItem(e));
		}
		// rarity sorting
		if (allowed == null)
		{
			List<List<SackItem>> rarities = new ArrayList<>();
			for (Rarity r : Rarity.values()) rarities.add(new ArrayList<>());
			
			// pigeonhole sort the rarities (pretty much the most efficient way to do it)
			for (SackItem f : filtered) rarities.get(f.getRarity()).add(f);
			filtered.clear();
			Collections.reverse(rarities);
			for (List<SackItem> lsi : rarities) 
			{
				lsi.sort((a,b) -> (a.amount > b.amount) ? -1 : 1);
				filtered.addAll(lsi);
			}
		}
		else filtered.sort((a,b) -> (a.amount > b.amount) ? -1 : 1);
		
		if (filtered.size() <= 28 * (page-1)) page = 1;
		int s = 28 * (page-1);
		for (int i = 0; i < 54; i++)
		{
			if (i % 9 == 0 || i % 9 == 8 || i < 9 || i / 9 == 5) contents[i] = GUICreator.pane();
			else if (s < filtered.size())
			{
				SackItem sack = filtered.get(s);
				ItemStack rep = ItemBuilder.i.build(sack.id,Math.min(sack.amount, 64));
				rep = ItemBuilder.noLore(rep);
				ItemMeta meta = rep.getItemMeta();
				List<String> l = new ArrayList<>();
				l.add(ChatColor.GOLD + "Stored: " + ChatColor.GRAY + sack.amount);
				meta.setLore(l);
				rep.setItemMeta(meta);
				
				items[i] = sack;
				contents[i] = rep;
				
				s++;
			}
		}
		
		if (page > 1) contents[46] = GUICreator.item(Material.ARROW, "Previous Page", null);
		if (filtered.size() > 28 * page) contents[52] = GUICreator.item(Material.ARROW, "Next Page", null);
		
		if (selarea == null) contents[47] = GUICreator.item(Material.MAP, ChatColor.GREEN + "Area Filter", "Currently: All");
		else contents[47] = GUICreator.item(Material.MAP, ChatColor.GREEN + "Area Filter", "Currently: " + StringManipulator.capitalise(selarea));
		
		if (allowed == null) contents[51] = GUICreator.item(Material.DIAMOND, ChatColor.GREEN + "Rarity Filter", "Currently: All");
		else contents[51] = GUICreator.item(Material.DIAMOND, ChatColor.GREEN + "Rarity Filter", "Currently: " + StringManipulator.capitalise(allowed.toString()));
		
		contents[49] = GUICreator.item(Material.ENDER_EYE, "Insert Items From Inventory", null);
		inventory.setContents(contents);
	}
	

	@Override
	public boolean handleClick (InventoryClickEvent e, int position, Player p)
	{
		if (position != -999 && position < items.length && items[position] != null)
		{
			SackItem sack = items[position];
			int take = Math.min(sack.amount,ItemBuilder.stack(sack.item));
			if (e.getClick().isRightClick()) take = 1;
			
			if (p.getInventory().firstEmpty() == -1) return true;
			p.getInventory().addItem(ItemBuilder.i.build(sack.id,take));
			if (sack.amount - take < 1) player.sack.remove(sack.id);
			else player.sack.put(sack.id, sack.amount - take);
		}
		else if (position == 46 && e.getCurrentItem().getType() == Material.ARROW) page--;
		else if (position == 47 && e.getClick() == ClickType.LEFT) {area++; if (area == areas.length) area = 0;}
		else if (position == 49)
		{
			for (ItemStack it : p.getInventory().getContents())
			{
				if (it == null || it.getType() == Material.AIR) continue;
				Item i = ItemBuilder.get(it);
				if (i != null && i.type.equals("ingredient"))
				{
					InventoryHandler.addItem(player, it,true);
					it.setAmount(0);
				}
			}
		}
		else if (position == 51 && e.getClick() == ClickType.LEFT) {filter++; if (filter == filters.length) filter = 0;}
		else if (position == 52 && e.getCurrentItem().getType() == Material.ARROW) page++;
		else if (position >= 54 && e.getCurrentItem() != null)
		{
			Item i = ItemBuilder.get(e.getCurrentItem());
			if (i != null && i.type.equals("ingredient")) 
			{
				InventoryHandler.addItem(player, e.getCurrentItem(),true);
				e.getCurrentItem().setAmount(0);
			}
		}
		
		
		update();
		return true;
	}

}

class SackItem
{
	String id;
	int amount;
	Item item;
	
	public int getRarity()
	{
		return item.rarity.ordinal();
	}
	public SackItem(Entry<String,Integer> entry)
	{
		id = entry.getKey();
		amount = entry.getValue();
		item = ItemBuilder.i.items.get(id);
	}
}

