package com.carterz30cal.items;

public class PotionElement
{
	public PotionElement(ElementType void1, int i) {
		type = void1;
		level = i;
	}
	public PotionElement()
	{
		
	}
	public PotionElement(PotionElement element)
	{
		type = element.type;
		level = element.level;
	}
	public ElementType type;
	public int level;
}
