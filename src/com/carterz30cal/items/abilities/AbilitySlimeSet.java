package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.player.DungeonsPlayer;

public class AbilitySlimeSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> des = new ArrayList<String>();
		des.add(prefix + "Bouncy!");
		des.add("Gain Jump Boost II");
		return des;
	}

	@Override
	public void onTick  (DungeonsPlayer d)
	{
		d.player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,25,1,true));
	} 
}
