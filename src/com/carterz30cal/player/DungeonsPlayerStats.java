package com.carterz30cal.player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemAppliable;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemEngine;
import com.carterz30cal.items.ItemSet;
import com.carterz30cal.items.ItemSharpener;
import com.carterz30cal.items.abilities.AbilityManager;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.skilltree.AbsSkill;
import com.carterz30cal.potions.ActivePotion;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class DungeonsPlayerStats
{
	public Player p;
	public DungeonsPlayer o;
	public ArrayList<AbsAbility> abilities;
	
	
	public int armour;
	public int health = 200;
	public int mana;
	public int regen;
	public int damage;
	public int attackspeed;
	public double damagemod;
	public int damageSweep;
	public int bonuskillcoins;
	public boolean set;
	public boolean synergy;
	public int fortune;
	public double miningXp;
	public int flatxp;
	public double overkiller;
	
	public double shopDiscount;
	
	public int miningspeed;
	
	public int luck;
	public int fishingspeed;
	
	public int maxsouls;
	
	public List<AbsEnchant> ench;
	
	public List<String> persistentdata = new ArrayList<>();
	
	public Map<ActivePotion,Integer> potioneffects = new HashMap<>();
	
	
	public String heldtype;
	public ItemSet settype;
	
	public ItemStack engine;
	
	
	public DungeonsPlayerStats(Player player)
	{
		p = player;
		//o = DungeonsPlayerManager.i.get(p);
		abilities = new ArrayList<AbsAbility>();
	}
	public void refresh()
	{
		if (o == null) o = DungeonsPlayerManager.i.get(p);
		DungeonsPlayer dp = o;
		if (dp.questcooldown > 0) dp.questcooldown--;
		abilities = new ArrayList<AbsAbility>();
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
			else if ((sett != null && a.set == null) || !sett.equals(a.set) || a.combatReq > dp.level.level)
			{
				sett = null;
				break;
			}
			else continue;
		}
		settype = sett;
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
		mana = 0;
		regen = 1;
		damage = 0;
		damageSweep = 0;
		fortune = 0;
		miningXp = 1;
		bonuskillcoins = 0;
		damagemod = 1;
		overkiller = 1;
		flatxp = 1;
		shopDiscount = 1;
		maxsouls = 5;
		attackspeed = 0;
		
		miningspeed = 0;
		
		luck = 25;
		fishingspeed = 100;
		
		engine = null;
		
		heldtype = "none";
		DungeonsPlayerStatBank fbank = new DungeonsPlayerStatBank();
		fbank.d = dp;
		ench = new ArrayList<>();
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack item : p.getInventory().getArmorContents()) if (item != null && item.getType() != Material.AIR) items.add(item);
		Item enginei = ItemBuilder.get(p.getInventory().getItem(9));
		if (enginei != null && enginei instanceof ItemEngine) 
		{
			engine = p.getInventory().getItem(9);
			items.add(engine);
		}
		if (h != null && (h.type.equals("weapon") || h.type.equals("tool") || h.type.equals("artifact") || h.type.equals("rod"))) 
		{
			items.add(held);
			heldtype = h.type;
		}
		
		for (ItemStack item : items)
		{
			ItemMeta meta = item.getItemMeta();
			item.setItemMeta(ItemBuilder.i.updateMeta(meta, dp));
			
			Item i = ItemBuilder.i.items.get(meta.getPersistentDataContainer().get(ItemBuilder.kItem, PersistentDataType.STRING));
			if (i == null) continue;
			else if (i.combatReq > dp.level.level) continue;
			else if (i.areaReq != null && !i.areaReq.equals(dp.area.id)) continue;
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
			ArrayList<ItemAppliable> app = ItemBuilder.getAppliables(meta);
			if (app != null) for (ItemAppliable a : app) bank.add(a.app_attributes);
			if (meta.getPersistentDataContainer().has(ItemBuilder.kRunic, PersistentDataType.STRING))
			{
				AbsAbility r = AbilityManager.get(meta.getPersistentDataContainer().getOrDefault(ItemBuilder.kRunic, PersistentDataType.STRING, ""));
				abilities.add(r);
				if (r != null) r.statbank(bank);
			}
			if (i.data.containsKey("ability")) 
			{
				AbsAbility a = AbilityManager.get((String) i.data.get("ability"));
				abilities.add(a);
				if (a != null) a.statbank(bank);
			}
			ArrayList<AbsEnchant> enchants = EnchantManager.get(meta.getPersistentDataContainer());
			ench.addAll(enchants);
			AbsEnchant special = null;
			if (enchants != null && !enchants.isEmpty()) 
			{
				for (AbsEnchant e : enchants) 
				{
					if (e.max() == 0)
					{
						special = e;
						continue;
					}
					if (!e.type().equals(i.type)) continue;
					DungeonsPlayerStatBank b = e.onBank(bank);
					if (b != null) bank = b;
				}
			}
			if (special != null)
			{
				DungeonsPlayerStatBank b = special.onBank(bank);
				if (b != null) bank = b;
			}
			
			add(fbank,bank);
		}
		
		List<ActivePotion> removes = new ArrayList<>();
		for (Entry<ActivePotion,Integer> potion : potioneffects.entrySet())
		{
			if (potion.getValue() <= 0) removes.add(potion.getKey());
			else abilities.add(potion.getKey().ability);
		}
		for (ActivePotion r : removes) 
		{
			dp.player.sendMessage(ChatColor.RED + "Your " + StringManipulator.capitalise(r.type.name()) + " potion has ran out!");
			potioneffects.remove(r);
		}
		
		if (set)
		{
			DungeonsPlayerStatBank bank = new DungeonsPlayerStatBank();
			bank.add(sett.set_attributes);
			add(fbank,bank);
			
			if (!sett.set_ability.equals("none")) abilities.add(AbilityManager.get(sett.set_ability));
		}
		
		if (h != null && h.type.equals("weapon") && synergy)
		{
			DungeonsPlayerStatBank bank = new DungeonsPlayerStatBank();
			bank.add(sett.syn_attributes);
			add(fbank,bank);
		}
		
		for (AbsEnchant ench : ench) 
		{
			DungeonsPlayerStatBank sb = ench.onFinalBank(fbank);
			if (sb != null) fbank = sb;
		}
		add(fbank);
		
		if (dp != null && dp.area != null && dp.explorer.areas() > 0)
		{
			bonuskillcoins += dp.explorer.bonusCoins(dp.area.id);
			miningXp += dp.explorer.bonusXp(dp.area.id);
		}

		for (String sk : o.skills.keySet())
		{
			AbsSkill skill = AbsSkill.skills.get(sk);
			skill.stats(o.skills.get(sk), this);
		}
		int rituals = 0;
		for (AbsAbility a : abilities) 
		{
			a.stats(this);
			if (a.isRitual()) rituals++;
		}
		
		if (dp.voteBoost != null && dp.voteBoost.isAfter(Instant.now())) miningXp += 0.35;
		
		
		mana = Math.max(mana, 0);
		armour = Math.max(0, armour);
		damage = (int) Math.max(1, Math.round(damage * damagemod));
		if (p.hasPotionEffect(PotionEffectType.WITHER))
		{
			damage /= (p.getPotionEffect(PotionEffectType.WITHER).getAmplifier()+2);
		}
		
		damageSweep += Math.round(damage / 4d);
		p.setSaturation(20);
		p.setFoodLevel(20);
		
		if (rituals > 0)
		{
			armour *= Math.pow(0.8, rituals);
			regen -= rituals*3;
		}
		
		regen += Math.round(health * 0.01);
		
		for (AbsAbility a : abilities) a.finalStats(this);
		health = Math.max(10, health);
		regen = Math.max(-rituals, regen);
		if (p.getGameMode() == GameMode.CREATIVE) damage = Integer.MAX_VALUE;
	}
	public double get (HashMap<String,Double> map,String value)
	{
		return map.getOrDefault(value, 0d);
	}
	public void add(DungeonsPlayerStatBank bank,DungeonsPlayerStatBank addition)
	{
		bank.health    += addition.health;
		bank.mana      += addition.mana;
		bank.armour    += addition.armour;
		bank.regen     += addition.regen;
		
		bank.damage    += addition.damage;
		bank.damagemod += addition.damagemod;
		bank.sweep     += addition.sweep;
		bank.attackspeed += addition.attackspeed;
		bank.xpbonus   += addition.xpbonus;
		bank.fortune += addition.fortune;
		bank.killcoins += addition.killcoins;
		bank.miningspeed += addition.miningspeed;
		
		bank.luck += addition.luck;
		bank.fishingspeed += addition.fishingspeed;
	}
	public void add(DungeonsPlayerStatBank bank)
	{
		health         += bank.health;
		mana           += bank.mana;
		armour         += bank.armour;
		regen          += bank.regen;
		
		damage         += bank.damage;
		damagemod      += bank.damagemod;
		damageSweep    += bank.sweep;
		attackspeed    += bank.attackspeed;
		bonuskillcoins += bank.killcoins;
		
		fortune      += bank.fortune;
		miningXp       += bank.xpbonus;
		
		miningspeed += bank.miningspeed;
		
		luck += bank.luck;
		fishingspeed += bank.fishingspeed;
	}
}


