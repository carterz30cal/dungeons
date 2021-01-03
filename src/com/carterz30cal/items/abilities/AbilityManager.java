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
		abilities.put("soulreap", AbilitySoulreap.class);
		abilities.put("soulstealer", AbilitySoulstealer.class);
		abilities.put("ghoulbone",AbilityGhoulBone.class);
		abilities.put("ghoulset",AbilityGhoulSet.class);
		abilities.put("boneset", AbilityBoneSet.class);
		abilities.put("stormbreaker", AbilityStormbreaker.class);
		abilities.put("stormfork", AbilityStormfork.class);
		abilities.put("stormset", AbilityStormSet.class);
		abilities.put("slimeset", AbilitySlimeSet.class);
		abilities.put("slimebow", AbilitySlimeBow.class);
		abilities.put("rainsummon", AbilityRainSummon.class);
		abilities.put("fishingt1", AbilitySpearFishingT1.class);
		abilities.put("fishingt2", AbilitySpearFishingT2.class);
		abilities.put("fishingt3", AbilitySpearFishingT3.class);
		abilities.put("fishingt4", AbilitySpearFishingT4.class);
		abilities.put("cryptkey", AbilityCryptKey.class);
		
		
		
		abilities.put("spellconcentration", ModSpellConcentration.class);
		abilities.put("manaefficient", ModManaEfficient.class);
		abilities.put("frozen", ModFrozen.class);
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
