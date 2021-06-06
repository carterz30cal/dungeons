package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillKnight extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "knight";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Knight";
	}

	@Override
	public String description(int level) {
		
		return "+" + level + " damage.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "xpboost";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,4,3);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.damage += level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public int levelreq() {
		// TODO Auto-generated method stub
		return 0;
	}

}
