package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilitySoulstealer extends AbsAbility
{
	public static HashMap<Integer,ArrayList<DMob>> affected;
	
	public AbilitySoulstealer()
	{
		if (affected == null) 
		{
			affected = new HashMap<Integer,ArrayList<DMob>> ();
			for (int i = 0; i < 220;i++) affected.put(i, new ArrayList<DMob>());
		}
	}
	
	
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Drain");
		desc.add("Struck mobs lose 12 health per second");
		desc.add(" for 10 seconds");
		desc.add(ChatColor.RED + "Ability cannot kill mobs");
		return desc;
	}

	
	@Override
	public int onAttack (DungeonsPlayer d, DMob mob,int damage)
	{
		if (mob.health > 0)
		{
			for (ArrayList<DMob> check : affected.values()) if (check.contains(mob)) return damage;
			affected.get(219).add(mob);
		}
		return damage;
	}
	
	@Override
	public void onTick (DungeonsPlayer d)
	{
		for (int i = 0; i < 220; i++)
		{
			ArrayList<DMob> af = affected.get(i);
			for (int p = 0; p < af.size();p++)
			{
				if (i != 0)
				{
					if (i % 20 == 0) af.get(p).damage(Math.min(12, af.get(p).health-1), null);
					affected.get(i-1).add(af.get(p));
				}
				af.remove(p);
			}
		}
	}
}
