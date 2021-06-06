package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillCharismatic extends AbsSkill {

	@Override
	public String id() {
		return "charisma";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Charismatic";
	}

	@Override
	public String description(int level) {
		
		return "Shop items cost " + ((double)level/2) + "% less.";
	}
	
	

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "wealth";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,2,2);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.shopDiscount *= 1 - (0.005*level);
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int levelreq() {
		// TODO Auto-generated method stub
		return 0;
	}

}
