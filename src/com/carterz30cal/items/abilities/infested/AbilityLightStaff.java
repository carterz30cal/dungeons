package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemEngine;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilityLightStaff extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Blinding Radiance");
		d.add("Deal 625 magic damage to up to 5 nearby enemies.");
		d.add("This damage is multiplied by your " + ChatColor.GREEN + "Damage %.");
		d.add("Also deals 2 true damage for every 75 capacity your engine has.");
		d.add("Consumes " + ChatColor.LIGHT_PURPLE + "225 mana" + ChatColor.GRAY + " and " + ChatColor.YELLOW + "4✦" + ChatColor.GRAY + " per right click.");
		return d;
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
		if (!d.canUseLuxium(4)) return false;
		if (!d.useMana(225)) return false;
		d.player.playSound(d.player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.6f, 1.3f);
		//d.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,0,true));
		d.useLuxium(4);
		Set<DMob> m = new HashSet<>();
		for (Entity en : Dungeons.w.getNearbyEntities(d.player.getLocation(), 7, 7, 7))
		{
			DMob t = DMobManager.get(en);
			if (t != null && !m.contains(t)) m.add(t);
		}
		
		int damage = (int) (625 * d.stats.damagemod);
		int truedamage = (((ItemEngine)ItemBuilder.get(d.stats.engine)).capacity / 75) * 2;
		
		int c = 0;
		for (DMob k : m) 
		{
			c++;
			if (c == 6) break;
			k.damage(damage, d, DamageType.MAGIC);
			k.damage(truedamage, d, DamageType.TRUE);
		}
		return true;
	}

}
