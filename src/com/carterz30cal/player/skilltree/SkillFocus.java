package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillFocus extends AbsSkill {

	@Override
	public String id() {
		return "focus";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Focus";
	}

	@Override
	public String description(int level) {
		
		return "+" + (level*4) + " mana.";
	}
	
	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "warrior";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(2,3,4);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.mana += 4*level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 25;
	}

	@Override
	public int levelreq() {
		// TODO Auto-generated method stub
		return 20;
	}

}
