package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillVitality extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "vitality";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Vitality";
	}

	@Override
	public String description(int level) {
		
		return "+" + level*4 + " health.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "knight";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,5,3);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.health += level*4;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 10;
	}

}
