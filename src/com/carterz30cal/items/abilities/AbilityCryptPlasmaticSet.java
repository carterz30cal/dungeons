package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class AbilityCryptPlasmaticSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Ghostly Invigoration");
		d.add("Whilst in a crypt:");
		d.add("- Double your regen and mana");
		d.add("- Triple your damage");
		d.add("- Gain 150 health and armour");
		d.add("Also activates the ghostly boots");
		d.add("ability if you are about to die.");
		return d;
	}

	public void stats(DungeonsPlayerStats s) 
	{
		if (AbilityGhostBoots.tasks.containsKey(s.o))
		{
			s.health = 100;
			s.armour = 0;
			s.mana = 0;
		}
		else if (s.o.inCrypt) 
		{
			s.armour += 150;
			s.health += 150;
			s.damage *= 3;
			s.regen *= 2;
			s.mana *= 2;
		}
	}
	public void onEnd(DungeonsPlayer d) 
	{
		GhostTask t = AbilityGhostBoots.tasks.get(d);
		if (t != null) t.end();
	}; // when server is closed.
	public void onLogOut(DungeonsPlayer d) 
	{
		GhostTask t = AbilityGhostBoots.tasks.get(d);
		if (t != null) t.end();
	}; 
	
	public boolean allowTarget(DungeonsPlayer d, DMob m) 
	{
		return AbilityGhostBoots.tasks.getOrDefault(d, null) == null;
	} 
	public void onTick  (DungeonsPlayer d) 
	{
		if (AbilityGhostBoots.cooldowns.getOrDefault(d, 0) == 0 && d.getHealthPercent() < 0.1)
		{
			d.player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You became a ghost!");
			
			AbilityGhostBoots.cooldowns.put(d, 2200);
			new GhostTask(d);
		}
		else if (AbilityGhostBoots.cooldowns.containsKey(d)) AbilityGhostBoots.cooldowns.put(d, Math.max(AbilityGhostBoots.cooldowns.getOrDefault(d, 0) - 1,0));
	}
	
		
		
		
}
