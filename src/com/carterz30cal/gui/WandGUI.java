package com.carterz30cal.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.magic.ItemWand;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.InventoryHandler;

import net.md_5.bungee.api.ChatColor;


public class WandGUI extends GUI {

	public WandGUI(Player p) 
	{
		super(p);
		
		ItemStack[] contents = new ItemStack[45];
		inventory = Bukkit.createInventory(null, 45,"Edit Wand");
		for (int i = 0; i < 45; i++)
		{
			if (i / 9 == 0 || i / 9 == 4 || i % 9 == 0 || i % 9 == 8) contents[i] = GUICreator.pane(Material.GRAY_STAINED_GLASS_PANE);
			else contents[i] = GUICreator.pane();
		}
		ItemStack wand = p.getInventory().getItemInMainHand();
		if (wand.getAmount() > 1)
		{
			ItemStack wc = wand.clone();
			wc.setAmount(wand.getAmount()-1);
			wand.setAmount(1);
			
			InventoryHandler.addItem(DungeonsPlayerManager.i.get(p), wc);
		}
		String w = wand.getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING);
		String sp = wand.getItemMeta().getPersistentDataContainer().get(ItemWand.kSpell, PersistentDataType.STRING);
		String m = wand.getItemMeta().getPersistentDataContainer().get(ItemWand.kModifier, PersistentDataType.STRING);
		 
		if (sp != null && !sp.equals("uielement") && m != null && !m.equals("uielement")) return;
		
		
		ItemStack wandb = ItemBuilder.i.build(w, 1);
		ItemStack spell = null;
		ItemStack modifier = null;
		if (sp != null && !sp.equals("uielement")) spell = ItemBuilder.i.build(sp, 1);
		else spell = GUICreator.item(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Insert spell", null);
		if (m != null && !m.equals("uielement")) modifier = ItemBuilder.i.build(m, 1);
		else modifier = GUICreator.item(Material.ORANGE_STAINED_GLASS_PANE, ChatColor.GOLD + "Insert modifier", null);

		contents[20] = wandb;
		contents[22] = spell;
		contents[24] = modifier;
		
		inventory.setContents(contents);
		render(p);
	}
	@Override
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		ItemStack c = e.getCurrentItem();
		if (c != null && position >= 45)
		{
			Item i = ItemBuilder.get(c);
			if (i == null) return true;
			if (i.type.equals("spell"))
			{
				ItemStack co = c.clone();
				co.setAmount(1);
				inventory.setItem(22, co);
				c.setAmount(c.getAmount()-1);
			}
			else if (i.type.equals("modifier"))
			{
				ItemStack co = c.clone();
				co.setAmount(1);
				inventory.setItem(24, co);
				c.setAmount(c.getAmount()-1);
			}
		}
		return true;
	}
	@Override
	public boolean handleDrag(InventoryDragEvent e,Player p)
	{
		return true;
	}
}
