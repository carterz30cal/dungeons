package com.carterz30cal.player;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.EnchantHandler;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemSet;
import com.carterz30cal.items.ItemSharpener;

public class DungeonsPlayerStats
{
	public Player p;
	
	public int armour;
	public int health = 200;
	public int regen;
	public int damage;
	public int damageSweep;
	public int bonuskillcoins;
	public boolean set;
	public boolean synergy;
	public double oreChance;
	public double miningXp;
	public DungeonsPlayerStats(Player player)
	{
		p = player;
	}
	public void refresh()
	{
		DungeonsPlayer dp = DungeonsPlayerManager.i.get(p);
		
		// check for set
		ItemSet sett = null;
		for (ItemStack armour : p.getInventory().getArmorContents())
		{
			if (armour == null || armour.getType() == Material.AIR) 
			{
				sett = null;
				break;
			}
			Item a = ItemBuilder.i.items.get(armour.getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING));
			if (sett == null && a.set != null) sett = a.set;
			else if (sett == null) break;
			else if (sett != null && a.set == null)
			{
				sett = null;
				break;
			}
			else if (!sett.equals(a.set))
			{
				sett = null;
				break;
			}
			else continue;
		}
		ItemStack held = p.getInventory().getItemInMainHand();
		Item h = null;
		if (held != null && held.getType() != Material.AIR) 
		{
			h = ItemBuilder.i.items.get(held.getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING));
			if (held.getType() == Material.BOW && p.getInventory().getItem(8) != null && p.getInventory().getItem(8).getAmount() != 64) p.getInventory().setItem(8, new ItemStack(Material.ARROW,64));
			else if (held.getType() != Material.BOW && p.getGameMode() == GameMode.SURVIVAL) p.getInventory().setItem(8, ItemBuilder.menuItem.clone());
		}
		else
		{
			if (p.getGameMode() == GameMode.SURVIVAL) p.getInventory().setItem(8, ItemBuilder.menuItem.clone());
			else if (p.getInventory().getItem(8) != null && p.getInventory().getItem(8).isSimilar(ItemBuilder.menuItem)) p.getInventory().setItem(8, null);
		}
		set = sett != null;
		if (h != null) synergy = set && h.set == sett;
		else synergy = false;
		
		armour = 0;
		health = 200;
		regen = 1;
		damage = 0;
		damageSweep = 0;
		oreChance = 0;
		miningXp = 1;
		bonuskillcoins = 0;
		double damag = 0;
		double damageMod = 1;
		
		for (ItemStack item : p.getInventory().getArmorContents())
		{
			if (item == null || item.getType() == Material.AIR) continue;
			Item a = ItemBuilder.i.items.get(item.getItemMeta().getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING));
			ItemMeta m = item.getItemMeta();
			float globMod = 1+EnchantHandler.eh.getEffect(m, "modifier");
			if (set) globMod += EnchantHandler.eh.getEffect(m, "synergy");
			
			armour += Math.ceil(a.attributes.getOrDefault("armour",0.0)*(globMod+EnchantHandler.eh.getEffect(m, "armour")));
			health += (a.attributes.getOrDefault("health",0.0)*globMod)+EnchantHandler.eh.getEffect(m, "health");
			regen += (a.attributes.getOrDefault("regen",0.0)*globMod)+EnchantHandler.eh.getEffect(m, "regen");
			damag += (a.attributes.getOrDefault("damage",0.0)*globMod)+EnchantHandler.eh.getEffect(m, "damage");
			damageMod += (a.attributes.getOrDefault("damagep",0.0)*globMod)+EnchantHandler.eh.getEffect(m, "damagep");
			bonuskillcoins += a.attributes.getOrDefault("killcoins",0.0);
			item.setItemMeta(ItemBuilder.i.updateMeta(m, dp));
		}
		
		if (set)
		{
			armour += sett.set_attributes.getOrDefault("armour",(double)0);
			health += sett.set_attributes.getOrDefault("health",(double)0);
			regen += sett.set_attributes.getOrDefault("regen",(double)0);
			damag += sett.set_attributes.getOrDefault("damage",(double)0);
			damageMod += sett.set_attributes.getOrDefault("damagep",(double)0);
		}
		
		if (h != null && h.type.equals("weapon"))
		{
			ItemMeta hm = held.getItemMeta();
			@SuppressWarnings("unchecked")
			HashMap<String,Double> attributes = (HashMap<String, Double>) h.attributes.clone();
			String[] sharps = hm.getPersistentDataContainer().getOrDefault(ItemBuilder.kSharps, PersistentDataType.STRING, "").split(";");
			for (String s : sharps)
			{
				ItemSharpener sharpener = ItemBuilder.i.sharps.get(s);
				if (sharpener == null) continue;
				for (Entry<String,Double> at : sharpener.attributes.entrySet())
				{
					double atv = attributes.getOrDefault(at.getKey(), 0.0);
					attributes.put(at.getKey(), atv+at.getValue());
				}
			}
			
			float globMod = 1+EnchantHandler.eh.getEffect(hm, "modifier");
			if (set) globMod += EnchantHandler.eh.getEffect(hm, "synergy");
			armour += attributes.getOrDefault("armour",0.0)*(globMod+EnchantHandler.eh.getEffect(hm, "armour"));
			health += (attributes.getOrDefault("health",0.0)*globMod)+EnchantHandler.eh.getEffect(hm, "health");
			regen += (attributes.getOrDefault("regen",0.0)*globMod)+EnchantHandler.eh.getEffect(hm, "regen");
			damag += (attributes.getOrDefault("damage",0.0)*globMod)+EnchantHandler.eh.getEffect(hm, "damage");
			damageMod += (attributes.getOrDefault("damagep",0.0)*globMod)+EnchantHandler.eh.getEffect(hm, "damagep");
			bonuskillcoins += attributes.getOrDefault("killcoins",0.0)+EnchantHandler.eh.getEffect(hm, "killcoins");
			if (synergy)
			{
				armour += sett.syn_attributes.getOrDefault("armour",(double)0);
				health += sett.syn_attributes.getOrDefault("health",(double)0);
				regen += sett.syn_attributes.getOrDefault("regen",(double)0);
				damag += sett.syn_attributes.getOrDefault("damage",(double)0);
				damageMod += sett.syn_attributes.getOrDefault("damagep",(double)0);
			}
			held.setItemMeta(ItemBuilder.i.updateMeta(hm, dp));
			
			
		}
		else if (h != null && h.type.equals("tool"))
		{
			ItemMeta hm = held.getItemMeta();
			oreChance += h.attributes.getOrDefault("orechance", 0.0);
			miningXp += h.attributes.getOrDefault("bonusxp", 0.0);
			held.setItemMeta(ItemBuilder.i.updateMeta(hm, dp));
		}
		
		for (String perk : dp.perks.getPerks())
		{
			FileConfiguration per = Dungeons.instance.fPerksC;
			int level = dp.perks.getLevel(perk);
			armour += per.getInt(perk + ".effects.armour",0)*level;
			health += per.getInt(perk + ".effects.health",0)*level;
			regen += per.getInt(perk + ".effects.regen",0)*level;
			damag += per.getDouble(perk + ".effects.damage",0)*level;
		}
		
		
		if (dp != null)
		{
			damageMod += DungeonsPlayerManager.i.get(p).skills.getSkillLevel("combat")*0.04;
			oreChance += DungeonsPlayerManager.i.get(p).skills.getSkillLevel("mining")*0.01;
		}
		armour = Math.max(0, armour);
		damage = (int) Math.max(1 + DungeonsPlayerManager.i.get(p).skills.getSkillLevel("combat"), Math.round(damag * damageMod));
		if (p.hasPotionEffect(PotionEffectType.WITHER))
		{
			damage /= (p.getPotionEffect(PotionEffectType.WITHER).getAmplifier()+2);
		}
		p.setSaturation(20);
		p.setFoodLevel(20);
		if (held.hasItemMeta()) damageSweep = (int) Math.round((damag/3f)*(1+EnchantHandler.eh.getEffect(held.getItemMeta(), "sweep")));
		health = Math.max(1, health);
		regen = Math.max(1, regen);
	}


}
