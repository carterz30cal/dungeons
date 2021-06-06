package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillBloodlust extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "bloodlust";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Bloodlust";
	}

	@Override
	public String description(int level) {
		
		return "If below 35% health, gain " + level*5 + " damage.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "training";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,4,0);
	}

	@Override
	public int levelreq() 
	{
		return 15;
	}
	
	public void stats(int level,DungeonsPlayerStats bank) 
	{
		if (bank.o.getHealthPercent() < 0.35)
		{
			bank.damage += level*5;
		}
		
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 5;
	}


}
