package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilitySilkSet extends AbsAbility
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> des = new ArrayList<String>();
		des.add(prefix + "SPEEEEEEED");
		des.add("Gives you Speed 2!");
		return des;
	}
	
	@Override
	public void onTick (DungeonsPlayer d)
	{
		d.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,5,1,true));
	}
}
