package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillGolem extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "golem";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Golem";
	}

	@Override
	public String description(int level) {
		
		return "+" + level*3 + " armour.";
	}


	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "knight";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,3,3);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.armour += level*3;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 10;
	}

}
