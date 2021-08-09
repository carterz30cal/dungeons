package com.carterz30cal.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.items.ElementType;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemPotionMat;
import com.carterz30cal.items.PotionElement;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.potions.PotionType;
import com.carterz30cal.utility.InventoryHandler;

import org.bukkit.ChatColor;

public class BrewingGUI extends GUI 
{
	public boolean active;
	public boolean warned;
	public List<PotionElement> elements = new ArrayList<>();

	public BrewingGUI(Player p) {
		super(p);
	
		inventory = Bukkit.createInventory(null, 54, "Brewing");
		
		refresh();
		
		render(p);
	}

	public void refresh()
	{
		ItemStack[] contents = new ItemStack[54];
		
		for (PotionElement element : elements) if (element.type == ElementType.VOID && element.level > 1) element.level = 1;
		
		for (int i = 0; i < 54;i++)
		{
			if (i / 9 == 1 && i % 9 != 0 && i % 9 < 8)
			{
				if (active) 
				{
					if (elements.size() >= i % 9) 
					{
						PotionElement element = elements.get((i % 9) - 1);
						contents[i] = element.type.gui(element.level);
					}
					else contents[i] = GUICreator.item(Material.GLASS, ChatColor.GRAY + "Empty Element Slot", null);
				}
				else contents[i] = GUICreator.item(Material.BARRIER, ChatColor.RED + "Insert a bottle to begin!", null);
			}
			else contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
		}
		
		contents[22] = GUICreator.pane(Material.PINK_STAINED_GLASS_PANE);
		
		contents[29] = GUICreator.pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		contents[30] = GUICreator.pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		contents[31] = active ? GUICreator.item(Material.CAULDRON,ChatColor.GREEN + "Brew!",null) : GUICreator.item(Material.BARRIER, ChatColor.RED + "Insert a bottle to begin!", null);
		contents[32] = GUICreator.pane(Material.LIME_STAINED_GLASS_PANE);
		contents[33] = GUICreator.pane(Material.LIME_STAINED_GLASS_PANE);
		
		contents[38] = active && elements.size() > 0 ? createPotion(new ArrayList<>(elements)) : null;
		contents[42] = elements.size() > 7 ? createOverflow(new ArrayList<>(elements)) : null;
		
		contents[53] = ItemBuilder.i.build("universal_element", 1);
		inventory.setContents(contents);
	}
	public static ItemStack createOverflow(List<PotionElement> elements)
	{
		String squished = "";
		elements = elements.subList(7, elements.size());
		for (PotionElement element : elements)
		{
			squished += element.type.name() + "," + element.level + ";";
		}
		squished = squished.substring(0,squished.length()-1);
		return ItemBuilder.i.build("overflow_jar",null,squished);
	}
	public static ItemStack createPotion(List<PotionElement> elements)
	{
		String elementstable = "";
		int lev = 1;
		if (elements.size() > 7) elements = elements.subList(0, 7);
		elements.add(new PotionElement(ElementType.VOID,1));
		for (PotionElement element : elements) 
		{
			if (element.type == ElementType.VOID && !elementstable.equals("")) 
			{
				elementstable = elementstable.substring(0,elementstable.length()-1) + ":" + lev + ";";
				lev = 1;
			}
			else 
			{
				elementstable += element.type.name() + ",";
				lev = element.level > lev ? element.level : lev;
			}
		}
		if (elementstable.endsWith(",")) elementstable = elementstable.substring(0, elementstable.length()-1);
		String potions = "";
		for (String combined : elementstable.split(";"))
		{
			int level = Integer.parseInt(combined.split(":")[1]);
			String com = combined.split(":")[0];
			for (PotionType potion : PotionType.values())
			{
				if (potion.recipe.equals(com))
				{
					potions = potions + potion.name() + "," + level + ";";
					break;
				}
			}
		}
		if (potions.equals("")) return ItemBuilder.i.build("sludge", elements.size() / 2);
		return ItemBuilder.i.build("potion", null,potions);
	}
	
	
	
	@Override
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		if (position >= 54)
		{
			ItemStack c = e.getCurrentItem();
			if (c == null) return true;
			String item = ItemBuilder.getItem(c);
			
			if (item.equals("empty_bottle") && !active) active = true;
			else if (!active) return true;
			else if (item.equals("extraction_catalyst") && elements.size() > 0)
			{
				elements.remove(0);
				if (elements.size() > 0) elements.remove(elements.size()-1);
			}
			else if (item.equals("conversion_catalyst") && elements.size() > 0)
			{
				PotionElement x = elements.get(0);
				x.type = x.type.inverse();
				if (elements.size() > 1)
				{
					PotionElement y = elements.get(1);
					y.type = y.type.inverse();
				}
			}
			else if (item.equals("extinguishing_element") && elements.size() > 0)
			{
				elements.removeIf((PotionElement el) -> el.type == ElementType.FIRE);
			}
			else if (item.equals("elemental_shift") && elements.size() > 0)
			{
				for (PotionElement element : elements)
				{
					if (element.type == ElementType.VOID) continue;
					else element.type = element.type.shift();
				}
			}
			else if (item.equals("reversal_element") && elements.size() > 0)
			{
				Collections.reverse(elements);
			}
			else if (item.equals("overflow_jar") && elements.size() < 7)
			{
				String[] spl = c.getItemMeta().getPersistentDataContainer().get(ItemBuilder.kEnchants, PersistentDataType.STRING).split(";");
				for (String s : spl)
				{
					PotionElement element = new PotionElement();
					element.type = ElementType.valueOf(s.split(",")[0].toUpperCase());
					element.level = Integer.parseInt(s.split(",")[1]);
					elements.add(element);
				}
			}
			else if (ItemBuilder.get(c) instanceof ItemPotionMat && elements.size() < 7)
			{
				ItemPotionMat m = (ItemPotionMat)ItemBuilder.get(c);
				List<PotionElement> copied = new ArrayList<>();
				for (PotionElement element : m.elements)
				{
					copied.add(new PotionElement(element));
				}
				elements.addAll(copied);
			}
			else return true;
			c.setAmount(c.getAmount()-1);
		}
		else if (position == 31 && active && inventory.getItem(38) != null)
		{
			DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
			InventoryHandler.addItem(d, inventory.getItem(38), false);
			if (inventory.getItem(42) != null) InventoryHandler.addItem(d, inventory.getItem(42), false);
			
			p.playSound(p.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
			active = false;
			warned = false;
			elements.clear();
		}
		refresh();
		return true;
	}
	
	@Override
	public void handleClose(InventoryCloseEvent e)
	{
		if (!warned && active)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "If you close the brewing stand now, you'll lose the mixture!");
			warned = true;
			refresh();
			render((Player) e.getPlayer());
		}
	}
}
