package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillFortune extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "fortune";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Fortune Cookie";
	}

	@Override
	public String description(int level) {
		
		return "+" + level*5 + " mining fortune.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "efficient";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,6,1);
	}

	@Override
	public int levelreq() 
	{
		return 10;
	}
	
	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.fortune += 5*level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 10;
	}


}
