package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityHunter2 extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Hunter");
		d.add("Deal +70% damage to tarantulas.");
		d.add("Gives tarantulas Slowness II on hit.");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		if (dMob.type.tags.contains("hunter")) 
		{
			((LivingEntity)dMob.entities.get(0)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,1,true));
			return (int) (damage * 1.7);
		}
		return damage;
	}
}
