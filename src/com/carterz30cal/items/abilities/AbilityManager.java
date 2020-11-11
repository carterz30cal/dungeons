package com.carterz30cal.items.abilities;

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
		abilities.put("cloudboots", AbilityCloudBoots.class);
		abilities.put("silkbonus", AbilitySilkSet.class);
		abilities.put("titanplate", AbilityTitanPlate.class);
	}
	
	public static AbsAbility get(String code)
	{
		try {
			return abilities.get(code).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
