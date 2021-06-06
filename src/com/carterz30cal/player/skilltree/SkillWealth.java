package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillWealth extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "wealth";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Wealth";
	}

	@Override
	public String description(int level) {
		
		return "+" + level + " coins on kill.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "golem";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,2,3);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.bonuskillcoins += level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 5;
	}

}
