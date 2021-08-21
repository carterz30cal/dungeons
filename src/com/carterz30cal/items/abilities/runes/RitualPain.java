package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

import net.md_5.bungee.api.ChatColor;

public class RitualPain extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(ritual + "Pain");
		d.add(ChatColor.RED + "+ 35 damage stat");
		d.add(ChatColor.RED + "- Take 12 true damage every hit");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		d.damage(12, true);
		return damage;
	}
	public void statbank(DungeonsPlayerStatBank s) 
	{
		s.damage += 35;
	}
	public boolean isRitual() {return true;}
}
