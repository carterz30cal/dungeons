package com.carterz30cal.potions;

import java.util.ArrayList;
import java.util.List;

import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

public class PotionEffectWisdom extends AbsPotion {

	@Override
	public void text(List<String> lore)
	{
		lore.add("Gain " + (level*15) + " mana.");
		lore.add("Boss mobs drop " + (level*6) + " more xp.");
	}
	public void stats(DungeonsPlayerStats s) 
	{
		s.mana += 15*level;
	}
	@Override
	public ArrayList<String> description() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onKill (DungeonsPlayer d,DMobType mob) 
	{
		if (mob.boss) d.level.giveFlat(level*6);
	}

}
