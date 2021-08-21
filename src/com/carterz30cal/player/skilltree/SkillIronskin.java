package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillIronskin extends AbsSkill {

	@Override
	public String id() {
		return "ironskin";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Ironskin";
	}

	@Override
	public String description(int level) {
		
		return "+" + level + " armour.";
	}
	
	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "warrior";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(4,3,4);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.armour += level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public int levelreq() {
		// TODO Auto-generated method stub
		return 15;
	}

}
