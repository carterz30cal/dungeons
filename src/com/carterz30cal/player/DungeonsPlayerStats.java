package com.carterz30cal.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemSet;
import com.carterz30cal.items.ItemSharpener;
import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.mobs.MobAction;
import com.carterz30cal.tasks.TaskBlockReplace;

public class DungeonsPlayerStats
{
	public Player p;
	
	public int armour;
	public int health = 200;
	public int regen;
	public int damage;
	public double damagemod;
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
			else if ((sett != null && a.set == null) || !sett.equals(a.set))
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
		damagemod = 1;
		
		DungeonsPlayerStatBank fbank = new DungeonsPlayerStatBank();
		fbank.d = dp;
		ArrayList<AbsEnchant> allench = new ArrayList<AbsEnchant>();
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack item : p.getInventory().getArmorContents()) if (item != null && item.getType() != Material.AIR) items.add(item);
		if (h != null && (h.type.equals("weapon") || h.type.equals("tool"))) items.add(held);
		
		for (ItemStack item : items)
		{
			ItemMeta meta = item.getItemMeta();
			item.setItemMeta(ItemBuilder.i.updateMeta(meta, dp));
			
			Item i = ItemBuilder.i.items.get(meta.getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING));
			if (i == null) continue;
			DungeonsPlayerStatBank bank = new DungeonsPlayerStatBank();
			bank.d = dp;
			bank.add(i.attributes);
			if (i.type.equals("weapon")) 
			{
				String[] sharps = meta.getPersistentDataContainer().getOrDefault(ItemBuilder.kSharps, PersistentDataType.STRING, "").split(";");
				for (String s : sharps)
				{
					ItemSharpener sharpener = ItemBuilder.i.sharps.get(s);
					if (sharpener == null) continue;
					else bank.add(sharpener.attributes);
				}
			}
			ArrayList<AbsEnchant> enchants = EnchantManager.get(meta.getPersistentDataContainer());
			allench.addAll(enchants);
			if (enchants != null && !enchants.isEmpty()) for (AbsEnchant e : enchants) {
				DungeonsPlayerStatBank b = e.onBank(bank);
				if (b != null) bank = b;
			}
			
			if (i.data.containsKey("ability")) itemAction(DungeonMobCreator.i.actions.get(i.data.get("ability")));
			
			add(fbank,bank);
		}
		//
		
		//
		if (set)
		{
			DungeonsPlayerStatBank bank = new DungeonsPlayerStatBank();
			bank.add(sett.set_attributes);
			add(fbank,bank);
			
			if (!sett.set_ability.equals("none")) itemAction(DungeonMobCreator.i.actions.get(sett.set_ability));
		}
		
		if (h != null && h.type.equals("weapon") && synergy)
		{
			DungeonsPlayerStatBank bank = new DungeonsPlayerStatBank();
			bank.add(sett.syn_attributes);
			add(fbank,bank);
		}
		
		for (String perk : dp.perks.getPerks())
		{
			FileConfiguration per = Dungeons.instance.fPerksC;
			int level = dp.perks.getLevel(perk);
			armour += per.getInt(perk + ".effects.armour",0)*level;
			health += per.getInt(perk + ".effects.health",0)*level;
			regen += per.getInt(perk + ".effects.regen",0)*level;
			damage += per.getDouble(perk + ".effects.damage",0)*level;
		}
		
		for (AbsEnchant ench : allench) 
		{
			DungeonsPlayerStatBank sb = ench.onFinalBank(fbank);
			if (sb != null) fbank = sb;
		}
		add(fbank);
		
		if (dp != null)
		{
			damagemod += DungeonsPlayerManager.i.get(p).skills.getSkillLevel("combat")*0.04;
			oreChance += DungeonsPlayerManager.i.get(p).skills.getSkillLevel("mining")*0.01;
			if (dp.area != null && dp.explorer.areas() > 0)
			{
				bonuskillcoins += dp.explorer.bonusCoins(dp.area.id);
				miningXp += dp.explorer.bonusXp(dp.area.id);
			}
		}
		armour = Math.max(0, armour);
		damage = (int) Math.max(1 + DungeonsPlayerManager.i.get(p).skills.getSkillLevel("combat"), Math.round(damage * damagemod));
		if (p.hasPotionEffect(PotionEffectType.WITHER))
		{
			damage /= (p.getPotionEffect(PotionEffectType.WITHER).getAmplifier()+2);
		}
		p.setSaturation(20);
		p.setFoodLevel(20);
		if (held.hasItemMeta()) damageSweep += damage/3;
		health = Math.max(10, health);
		regen = Math.max(0, regen);
	}
	public double get (HashMap<String,Double> map,String value)
	{
		return map.getOrDefault(value, 0d);
	}
	public void add(DungeonsPlayerStatBank bank,DungeonsPlayerStatBank addition)
	{
		bank.health    += addition.health;
		bank.armour    += addition.armour;
		bank.regen     += addition.regen;
		
		bank.damage    += addition.damage;
		bank.damagemod += addition.damagemod;
		bank.sweep     += addition.sweep;
		bank.xpbonus   += addition.xpbonus;
		bank.orechance += addition.orechance;
	}
	public void add(DungeonsPlayerStatBank bank)
	{
		health         += bank.health;
		armour         += bank.armour;
		regen          += bank.regen;
		
		damage         += bank.damage;
		damagemod      += bank.damagemod;
		damageSweep    += bank.sweep;
		bonuskillcoins += bank.killcoins;
		
		oreChance      += bank.orechance;
		miningXp       += bank.xpbonus;
	}
	public void itemAction(MobAction a)
	{
		boolean stop = false;
		for (String action : a.effects)
		{
			if (stop) break;
			String[] data = action.split(":")[1].split(",");
			switch (action.split(":")[0])
			{
			case "reqblock":
				Block block = p.getLocation().add(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2])).getBlock();
				if (block.getType() != Material.valueOf(data[3])) stop = true;
				break;
			case "ylevel":
				int distance = p.getLocation().getBlockY();
				if ((data[0].equals("over") && distance < Integer.parseInt(data[1]))
						|| (data[0].equals("under") && distance >= Integer.parseInt(data[1]))) stop = true;
				break;
			case "block":
				Location rep = p.getLocation().add(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]));
				Block b = rep.getBlock();
				Material previous = b.getType();
				b.setType(Material.valueOf(data[3]));
				if (!Dungeons.instance.blocks.containsKey(b))
				{
					TaskBlockReplace tbr = new TaskBlockReplace(b,previous);
					tbr.runTaskLater(Dungeons.instance, Integer.parseInt(data[4]));
					Dungeons.instance.blocks.put(b, tbr);
				}
				break;
			case "potion":
				p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(data[0]),100,Integer.parseInt(data[1]),false,false));
			}
			
		}
	}
}
