package com.carterz30cal.tasks;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskTickAbilities extends BukkitRunnable {
	@Override
	public void run()
	{
		for (DungeonsPlayer d : DungeonsPlayerManager.i.players.values()) 
		{
			ArrayList<Class<? extends AbsAbility>> uniques = new ArrayList<>();
			for (AbsAbility a : d.stats.abilities) 
			{
				if (a.isUnique() && uniques.contains(a.getClass())) continue;
				a.onTick(d);
				
				uniques.add(a.getClass());
			}
			d.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,5205,-1,true));
			//d.player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,10000,0,true));
			d.regainMana(0.00175);
			if (d.afk != -1) d.afk++;
			d.playtime++;
			if (d.afk == 20*60*10) 
			{
				d.damage(99999999, true);
			}
		}
	}

}
