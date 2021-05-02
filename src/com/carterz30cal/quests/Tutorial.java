package com.carterz30cal.quests;

import java.util.ArrayList;
import java.util.List;

public class Tutorial
{
	public String name;
	public List<String> messages = new ArrayList<>();
	public List<Trigger> triggers = new ArrayList<>();
	
	public Tutorial()
	{
		TutorialManager.tutorials.add(this);
	}
	public void add(TutorialTrigger type,String what)
	{
		Trigger t = new Trigger();
		t.trigger = type;
		t.what = what;
		triggers.add(t);
	}
	public void msg(String message)
	{
		messages.add(message);
	}
	
	public boolean triggered(TutorialTrigger event,String data)
	{
		for (Trigger trigger : triggers) if (event == trigger.trigger && data.equals(trigger.what)) return true;
		return false;
	}
}

class Trigger
{
	public TutorialTrigger trigger;
	public String what;
}
