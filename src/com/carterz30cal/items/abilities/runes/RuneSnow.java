package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneSnow extends AbsAbility 
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(rune + "Snow");
		d.add("Inflict Slowness I");
		d.add("Deals +7 damage to slowed enemies");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		int p = 8;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.3, 0.3), RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.3, 0.3)), 3
					,new Particle.DustOptions(Color.WHITE,1));
			p--;
		}
		if (((LivingEntity)dMob.entities.get(0)).hasPotionEffect(PotionEffectType.SLOW)) return damage + 7;
		
		((LivingEntity)dMob.entities.get(0)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,0,true));
		return damage;
	} 
}
