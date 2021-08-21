package com.carterz30cal.gui;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.BestiaryEntry;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.mobs.MobDrop;
import com.carterz30cal.mobs.abilities.DMobAbility;
import com.carterz30cal.mobs.abilities.MobDamageRewarder;
import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class BestiaryGUI extends GUI
{
	public String entry = "none";
	public String[] areaFilter = {"all","waterway","necropolis","infested"};
	public int af = 0;
	
	public String[] clicks;
	
	public DungeonsPlayer owner;
	public BestiaryGUI(Player p) {
		super(p);
		
		owner = DungeonsPlayerManager.i.get(p);
		page = 1;
		
		refresh();
	}
	
	public void refresh()
	{
		ItemStack[] contents = new ItemStack[54];
		clicks = new String[54];
		if (entry.equals("none"))
		{
			inventory = Bukkit.createInventory(null, 54,"Bestiary - Page " + page);
			List<BestiaryEntry> entries = new ArrayList<>();
			for (BestiaryEntry e : DMobManager.bestiaryEntries.values())
			{
				if ((af == 0 || areaFilter[af].equals(e.area)) && owner.bestiary.getOrDefault(e.id, 0) != 0) entries.add(e);
			}
			int ee = (page-1)*28;
			for (int i = 0; i < 54; i++)
			{
				if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == 5) contents[i] = GUICreator.pane();
				else if (ee < entries.size())
				{
					BestiaryEntry ent = entries.get(ee);
					ItemStack item = ItemBuilder.i.build(ent.icon,1);
					ItemMeta meta = item.getItemMeta();
					
					meta.setDisplayName(ent.name);
					List<String> lore = new ArrayList<>();
					for (String d : ent.description) lore.add(ChatColor.GRAY + d);
					lore.add("");
					lore.add(ChatColor.GRAY + "Kills: " + ChatColor.RED + owner.bestiary.getOrDefault(ent.id, 0));
					
					meta.setLore(lore);
					item.setItemMeta(meta);
					
					contents[i] = item;
					clicks[i] = ent.id;
					ee++;
				}
			}
		}
		else
		{
			BestiaryEntry en = DMobManager.bestiaryEntries.get(entry);
			inventory = Bukkit.createInventory(null, 54,"Bestiary - " + ChatColor.stripColor(en.name));
			
			int m = 0;
			for (int i = 0; i < 54; i++)
			{
				if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == 5) contents[i] = GUICreator.pane();
				else if (m < en.mobs.size())
				{
					DMobType type = DMobManager.types.get(en.mobs.get(m));
					ItemStack item = ItemBuilder.i.build(en.icon,1);
					ItemMeta meta = item.getItemMeta();
					
					meta.setDisplayName(CharacterSkill.prettyText(type.level) + " " + type.name);
					List<String> lore = new ArrayList<>();
					//for (String d : ent.description) lore.add(ChatColor.GRAY + d);
					if (type.boss) lore.add(ChatColor.RED + "Boss");
					lore.add("");
					lore.add(ChatColor.GOLD + "Rewards");
					lore.add(ChatColor.GRAY + "- " + ChatColor.GOLD + (type.health / 25) + " coins");
					lore.add(ChatColor.GRAY + "- " + ChatColor.AQUA + Math.max(type.level+1, CharacterSkill.tonextlevel(type.level) / 100) + " xp");
					lore.add("");
					lore.add(ChatColor.GOLD + "Drops");
					
					@SuppressWarnings("unchecked")
					List<MobDrop> drops = (List<MobDrop>) type.drops.clone();
					
					for (DMobAbility a : type.abilities)
					{
						if (a instanceof MobDamageRewarder) 
						{
							MobDrop rewarder = new MobDrop();
							rewarder.item = ((MobDamageRewarder)a).reward;
							rewarder.chance = 1;
							rewarder.minAmount = 1;
							rewarder.maxAmount = 1;
							drops.add(rewarder);
						}
					}
					drops.sort((a,b) -> a.chance < b.chance ? 1 : -1);
					for (MobDrop drop : drops)
					{
						ItemStack dropi;
						if (drop.item.equals("book")) dropi = ItemBuilder.i.build("book",null,"BLADE,1");
						else dropi = ItemBuilder.i.build(drop.item,1);
						String chance = ChatColor.YELLOW + " (" + setSignificantDigits(drop.chance*100,2) + "%)";
						if (drop.chance >= 1) chance = "";
						String n = dropi.getItemMeta().getDisplayName();
						
						if (drop.minAmount == drop.maxAmount) 
						{
							if (drop.minAmount == 1) lore.add(" " + n + chance);
							else lore.add(ChatColor.GRAY + " " + drop.minAmount + " " + n + chance);
						}
						else lore.add(ChatColor.GRAY + " " + drop.minAmount + "-" + drop.maxAmount + " " + n + chance);
					}
					meta.setLore(lore);
					item.setItemMeta(meta);
					
					contents[i] = item;
					m++;
				}
			}
		}
		
		inventory.setContents(contents);
		render(owner.player);
	}
	public static String setSignificantDigits(double value, int significantDigits) {
		if (significantDigits < 0) throw new IllegalArgumentException();

	    // this is more precise than simply doing "new BigDecimal(value);"
	    BigDecimal bd = new BigDecimal(value, MathContext.DECIMAL64);
	    bd = bd.round(new MathContext(significantDigits, RoundingMode.HALF_UP));
	    final int precision = bd.precision();
	    if (precision < significantDigits)
	    bd = bd.setScale(bd.scale() + (significantDigits-precision));
	    
	    String s = bd.toPlainString();
	    if (bd.doubleValue() < 1d)
	    {
	    	int it = s.length()-1;
		    while (s.substring(it,it+1).equals("0")) 
		    {
		    	s = s.substring(0,it);
		    	it--;
		    }
	    }
	    
	    
	    return s;
	}  
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		if (position < 54 && entry.equals("none"))
		{
			entry = clicks[position];
			refresh();
		}
		return true;
	}
}
