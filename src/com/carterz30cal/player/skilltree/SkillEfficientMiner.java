package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillEfficientMiner extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "efficient";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Strong Picks";
	}

	@Override
	public String description(int level) {
		
		return "+" + level*4 + " mining speed.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "training";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,5,1);
	}

	@Override
	public int levelreq() 
	{
		return 10;
	}
	
	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.miningspeed += 4*level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 25;
	}


}
