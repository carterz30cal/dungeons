package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillWarrior extends AbsSkill {

	@Override
	public String id() {
		return "warrior";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Warrior";
	}

	@Override
	public String description(int level) {
		
		return "+" + level*2 + "% damage";
	}
	
	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "bloodlust";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(2,4,4);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.damagemod += 0.02 * level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int levelreq() {
		// TODO Auto-generated method stub
		return 20;
	}

}
