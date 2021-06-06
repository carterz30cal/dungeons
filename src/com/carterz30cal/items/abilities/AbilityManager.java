package com.carterz30cal.items.abilities;

import java.util.HashMap;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.runes.*;
import com.carterz30cal.items.abilities.infested.*;
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
		abilities.put("holyknight", AbilityCryptHolyKnight.class);
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
		abilities.put("healingwand2", AbilityHealingWand2.class);
		abilities.put("healingwand3", AbilityHealingWand3.class);
		abilities.put("cspirit", AbilityCryptSpirit.class);
		abilities.put("watertotem", AbilityWaterTotem.class);
		abilities.put("sand", AbilitySandSword.class);
		abilities.put("hydraset", AbilityHydraSet.class);
		abilities.put("hydrastar", AbilityHydraStar.class);
		abilities.put("mushroomtotem", AbilityMushroomTotem.class);
		abilities.put("unholyshroom", AbilityUnholyShroom.class);
		abilities.put("diggingsight", AbilityDiggingSight.class);
		abilities.put("dagger", AbilityDagger.class);
		abilities.put("cryptlord", AbilityCryptLordSet.class);
		abilities.put("digginghunter", AbilityDiggingHunter.class);
		abilities.put("digginglord", AbilityDiggingLord.class);
		abilities.put("devastation", AbilityDevastation.class);
		abilities.put("phantomboots", AbilityPhantom.class);
		
		abilities.put("healerblue", AbilityHealerBlue.class);
		abilities.put("healergreen", AbilityHealerGreen.class);
		abilities.put("healerred", AbilityHealerRed.class);
		
		abilities.put("reaperblade", AbilityReaperBlade.class);
		abilities.put("reaperhelmet", AbilityReaperHelmet.class);
		abilities.put("reaperchestplate", AbilityReaperChestplate.class);
		abilities.put("reaperleggings", AbilityReaperLeggings.class);
		abilities.put("reaperboots", AbilityReaperBoots.class);
		abilities.put("reaperset",AbilityReaperSet.class);
		
		abilities.put("venombite", AbilityVenom.class);
		abilities.put("tiki", AbilityTikiSet.class);
		abilities.put("fangedaxe", AbilityFangedAxe.class);
		abilities.put("webset", AbilityWebSet.class);
		abilities.put("eggbuster", AbilityEggBuster.class);
		abilities.put("egghat", AbilityEggHat.class);
		abilities.put("sniperset", AbilitySniperSet.class);
		abilities.put("hunter", AbilityHunter.class);
		abilities.put("hunter2", AbilityHunter2.class);
		abilities.put("hunter3", AbilityHunter3.class);
		abilities.put("hunter4", AbilityHunter4.class);
		abilities.put("tarantulahelmet", AbilityTarantulaHelmet.class);
		
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
		abilities.put("rune_space", RuneSpace.class);
		abilities.put("rune_holy", RuneHoly.class);
		abilities.put("rune_steel", RuneSteel.class);
		abilities.put("rune_void", RuneVoid.class);
	}
	
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
