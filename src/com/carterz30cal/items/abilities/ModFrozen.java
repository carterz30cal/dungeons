package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class ModFrozen extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(modifier + "Frozen");
		d.add("Your spells inflict" + ChatColor.BLUE + " Slowness II");
		return d;
	}

	public void onMagicHit(DungeonsPlayer d,DMob hit) 
	{
		((LivingEntity)hit.entities.get(0)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,80,1,true));
	}
}
