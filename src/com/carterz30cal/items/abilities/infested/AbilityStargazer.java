package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class AbilityStargazer extends AbsAbility 
{
	public static Map<DungeonsPlayer,Boolean> enabled = new HashMap<>();
	public static Map<DungeonsPlayer,Integer> cooldown = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Stargazing");
		d.add("While in combat, consume " + ChatColor.YELLOW + "1✦");
		d.add("every 5 seconds and gain 500 mana.");
		d.add("Shift to toggle.");
		return d;
	}
	
	@Override
	public void onSneak(DungeonsPlayer d)
	{
		boolean en = !enabled.getOrDefault(d, true);
		if (en && d.canUseLuxium(1)) d.player.sendMessage(ChatColor.GREEN + "Enabled Stargazing!");
		else if (en) d.player.sendMessage(ChatColor.RED + "Not enough Luxium!");
		else d.player.sendMessage(ChatColor.RED + "Disabled Stargazing!");
		enabled.put(d, en);
	}
	public void stats(DungeonsPlayerStats s) 
	{
		if (s.o.combatTicks == 0) return;
		else if (enabled.getOrDefault(s.o, true)) s.mana += 500;
	}
	@Override
	public void onTick (DungeonsPlayer d)
	{
		if (d.combatTicks == 0 || !enabled.getOrDefault(d, true)) return;
		if (!d.canUseLuxium(1)) enabled.put(d, false);
		if (cooldown.getOrDefault(d, 99) < 100) cooldown.put(d, cooldown.getOrDefault(d, 0)+1);
		else
		{
			cooldown.put(d, 0);
			if (!d.useLuxium(1)) enabled.put(d, false);
		}
	}

}
