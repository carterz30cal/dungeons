package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillTraining extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "training";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Training";
	}

	@Override
	public String description(int level) {
		
		return "+" + level*5 + "% damage.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "heavystrike";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,4,1);
	}

	@Override
	public int levelreq() 
	{
		return 15;
	}
	
	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.damagemod += level*0.05;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 1;
	}


}
