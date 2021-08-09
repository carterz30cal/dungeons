package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

import org.bukkit.ChatColor;

public class RitualTwistedVitality extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(ritual + "Twisted Vitality");
		d.add(ChatColor.RED + "+ Double health on this item.");
		d.add(ChatColor.RED + "- Attacks against you deal double damage.");
		return d;
	}
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) { return 2; }
	public void statbank(DungeonsPlayerStatBank s) 
	{
		s.health *= 2;
	}
	public boolean isRitual() {return true;}
}
