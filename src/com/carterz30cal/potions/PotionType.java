package com.carterz30cal.potions;


import com.carterz30cal.items.abilities.AbsAbility;

public enum PotionType
{
	STRENGTH(PotionEffectStrength.class,"FIRE,FIRE,EARTH",new PotionColour(255,80,80)),
	JUMP(PotionEffectJumpBoost.class,"AIR,AIR,AIR,EARTH,AIR",new PotionColour(200,240,200)),
	VENOMOUS(PotionEffectVenomous.class,"VENOM,WATER,WATER",new PotionColour(0,200,0)),
	IRONSKIN(PotionEffectIronskin.class,"EARTH,EARTH,EARTH,WATER",new PotionColour(100,180,50)),
	REGENERATION(PotionEffectRegeneration.class,"EARTH,VENOM",new PotionColour(255,255,0)),
	MIDAS(PotionEffectMidasFlask.class,"MIDAS,MIDAS,WATER",new PotionColour(190,190,0)),
	RAGE(PotionEffectRage.class,"MIDAS,FIRE,FIRE,MIDAS",new PotionColour(140,0,20)),
	RAIDER(PotionEffectRaider.class,"FIRE,EARTH,EARTH,AIR,FIRE",new PotionColour(102, 51, 153)),
	WISDOM(PotionEffectWisdom.class,"WATER,WATER,AIR,WATER",new PotionColour(0,100,200));
	
	public Class<? extends AbsPotion> ability;
	public String recipe;
	public PotionColour colour;
	
	private PotionType(Class<? extends AbsPotion> a,String r,PotionColour c)
	{
		ability = a;
		recipe = r;
		colour = c;
	}
	
	@SuppressWarnings("deprecation")
	public AbsAbility create(int level)
	{
		AbsPotion ab = null;
		try
		{
			ab = ability.newInstance();
			ab.level = level;
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return ab;
	}
}
