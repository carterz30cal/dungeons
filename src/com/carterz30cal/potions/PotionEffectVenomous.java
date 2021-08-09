package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

public class PotionEffectVenomous extends AbsPotion 
{

	@Override
	public void text(List<String> lore)
	{
		lore.add("Deals " + (3*level) + " true damage");
		lore.add("per second for 7 seconds.");
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		new BukkitRunnable()
		{
			int t = 0;
			@Override
			public void run() {
				if (t == 7) 
				{
					cancel();
					return;
				}
				dMob.damage(3*level, d, DamageType.TRUE,false);
				t++;
			}
			
		}.runTaskTimer(Dungeons.instance, 20, 20);
		return damage;
	} 
	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}

}
