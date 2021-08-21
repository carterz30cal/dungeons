package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityPhantom extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Phantom");
		d.add("Heal 16❤ every hit");
		d.add("and gain Invisibility");
		d.add("for 4 seconds.");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		d.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,80,0,true));
		d.heal(16);
		return damage;
	}
	
	public boolean allowTarget(DungeonsPlayer d, DMob m)
	{
		return !d.player.hasPotionEffect(PotionEffectType.INVISIBILITY);
	}
}
