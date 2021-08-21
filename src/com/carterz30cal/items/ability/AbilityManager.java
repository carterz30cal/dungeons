package com.carterz30cal.items.ability;

import java.util.HashMap;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.tasks.TaskTickAbilities;

public class AbilityManager
{
	public static HashMap<String,Class<? extends AbsAbility>> abilities;
	
	
	public AbilityManager ()
	{
		new TaskTickAbilities().runTaskTimer(Dungeons.instance, 20, 1);
		
		abilities = new HashMap<String,Class<? extends AbsAbility>>();
		abilities.put("rabbit", AbaRabbitSet.class);
	}
	
	@SuppressWarnings("deprecation")
	public static AbsAbility get(String code)
	{
		if (!abilities.containsKey(code)) return null;
		try {
			return abilities.get(code).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
