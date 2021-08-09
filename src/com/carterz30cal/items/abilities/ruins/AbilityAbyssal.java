package com.carterz30cal.items.abilities.ruins;

import java.util.ArrayList;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityAbyssal extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Power of the Ruins");
		d.add("Deals 2x damage to abyssal monsters");
		d.add("if you are in the dark.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		if (Dungeons.w.getBlockAt(d.player.getEyeLocation()).getLightLevel() <= 7 && dMob.type.tags.contains("ruins")) return damage * 2;
		return damage;
	} 

}
