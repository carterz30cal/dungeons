package com.carterz30cal.items.abilities;

import java.util.HashMap;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.runes.*;
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
		abilities.put("leafset", AbilityLeafSet.class);
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
		abilities.put("fishingstrange", AbilitySpearFishingStrange.class);
		abilities.put("cryptkey", AbilityCryptKey.class);
		abilities.put("cryptkey2", AbilityCryptKey2.class);
		abilities.put("cryptkey3", AbilityCryptKey3.class);
		abilities.put("cryptraider", AbilityCryptRaiderSet.class);
		abilities.put("handsomereward", AbilityHandsomeReward.class);
		abilities.put("venomfang", AbilityVenomFang.class);
		abilities.put("venomhelm", AbilityVenomHelm.class);
		abilities.put("diggingshovel", AbilityDigging.class);
		abilities.put("totem2", AbilityTotem2.class);
		abilities.put("precursorcrown", AbilityPrecursorCrown.class);
		abilities.put("prejudice", AbilityPrejudice.class);
		abilities.put("cryptknight", AbilityCryptKnight.class);
		abilities.put("acidity", AbilityAcidity.class);
		abilities.put("golem", AbilityGolemSet.class);
		abilities.put("spores", AbilitySpores.class);
		abilities.put("gemstonecrown", AbilityGemstoneCrown.class);
		abilities.put("soulfire", AbilitySoulfire.class);
		abilities.put("scythe", AbilityScythe.class);
		abilities.put("stinger", AbilityStinger.class);
		abilities.put("wave", AbilityWave.class);
		abilities.put("cowdie", AbilityCowDie.class);
		abilities.put("undeadrod", AbilityUndeadFishRod.class);
		abilities.put("ghostboots", AbilityGhostBoots.class);
		abilities.put("spirittrousers", AbilitySpiritTrousers.class);
		abilities.put("spellconcentration", ModSpellConcentration.class);
		abilities.put("manaefficient", ModManaEfficient.class);
		abilities.put("frozen", ModFrozen.class);
		abilities.put("cryptgrimoire", ModCryptGrimoire.class);
		abilities.put("soulsaber", AbilitySoulSaber.class);
		abilities.put("healingwand1", AbilityHealingWand1.class);
		
		abilities.put("midasboots", AbilityMidasBoots.class);
		
		
		
		abilities.put("rune_blood",RuneBlood.class);
		abilities.put("rune_snake",RuneSnake.class);
		abilities.put("rune_rich" ,RuneRich.class);
		abilities.put("rune_acid" ,RuneAcid.class);
		abilities.put("rune_square",RuneSquare.class);
		abilities.put("rune_slayer",RuneSlayer.class);
		abilities.put("rune_hunt",RuneHunt.class);
		abilities.put("rune_tap",RuneTap.class);
		abilities.put("rune_sweat",RuneSweat.class);
		abilities.put("rune_snow", RuneSnow.class);
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
