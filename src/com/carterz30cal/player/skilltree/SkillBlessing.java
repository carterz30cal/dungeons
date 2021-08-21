package com.carterz30cal.player.skilltree;

public class SkillBlessing extends AbsSkill {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return "blessing";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Blessing";
	}

	@Override
	protected String description(int level) {
		// TODO Auto-generated method stub
		return "Grants +" + level + "% luck and " + level*2 + " health.";
	}

	@Override
	public String skillreq() {
		// TODO Auto-generated method stub
		return "charisma";
	}

	@Override
	public int levelreq() 
	{
		return 10;
	}
	
	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return new Position(1,2,1);
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 5;
	}

}
