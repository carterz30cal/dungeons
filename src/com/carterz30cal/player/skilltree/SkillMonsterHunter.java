package com.carterz30cal.player.skilltree;

import com.carterz30cal.player.DungeonsPlayerStats;

public class SkillMonsterHunter extends AbsSkill {

	@Override
	public String id() {
		return "monsterhunterlvel";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Monster Hunter";
	}

	@Override
	public String description(int level) {
		
		return "Gain " + level + " extra xp per kill.";
	}
	
	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "none";
	}

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(-1,4,4);
	}

	public void stats(int level,DungeonsPlayerStats bank) 
	{
		bank.flatxp += level;
	}
	
	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public int levelreq() {
		// TODO Auto-generated method stub
		return 0;
	}

}
