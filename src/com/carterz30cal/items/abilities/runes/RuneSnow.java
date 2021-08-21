package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneSnow extends AbsAbility 
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(rune + "Snow");
		d.add("Has a 40% chance to add or increase");
		d.add("Slowness on struck enemies. For each");
		d.add("level of Slowness, gain 5% damage.");
		d.add("Capped at Slowness X.");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		int p = 11;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.3, 0.3), RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.3, 0.3)), 3
					,new Particle.DustOptions(Color.WHITE,1));
			p--;
		}
		PotionEffect slow = ((LivingEntity)dMob.entities.get(0)).getPotionEffect(PotionEffectType.SLOW);
		int slowl = 0;
		if (slow != null) slowl = slow.getAmplifier()+1;
		
		if (RandomFunctions.random(1, 10) <= 4 && slowl < 10) ((LivingEntity)dMob.entities.get(0)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,slowl,true));
		
		return (int) (damage * (1 + (slowl*0.05d)));
	} 
}
