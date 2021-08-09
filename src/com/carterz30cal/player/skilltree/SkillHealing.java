package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillHealing extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "healing";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Mender";
	}

	@Override
	public String description(int level) {
		
		return "+" + level + " regen.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "vitality";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,6,3);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.regen += level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 6;
	}

}
