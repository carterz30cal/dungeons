package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.StringManipulator;

public class PotionEffectJumpBoost extends AbsPotion {

	@Override
	public void text(List<String> lore) {
		lore.add("Grants Jump Boost " + StringManipulator.romanNumerals[level-1]);
	}

	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onTick  (DungeonsPlayer d)
	{
		d.player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,30,Math.min(3, level-1),true));
	}

}
