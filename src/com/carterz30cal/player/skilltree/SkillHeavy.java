package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class SkillHeavy extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "heavystrike";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Heavy Strike";
	}

	@Override
	public String description(int level) {
		
		return "Gain a 5% chance to deal an extra " + level*25 + " damage.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "knight";
	}
	
	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,4,2);
	}

	@Override
	public int levelreq() 
	{
		return 10;
	}
	@Override
	public int onAttack(int level,DungeonsPlayer player,int damage) 
	{
		if (RandomFunctions.random(1, 20) == 5) return damage + (level*25);
		return damage;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 8;
	}

}
