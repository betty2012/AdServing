package net.mad.ads.db.model.type;

import net.mad.ads.db.definition.AdDefinition;


public abstract class AbstractAdType implements AdType {
	private int type = 0;
	private String name = "";
	public AbstractAdType(String name, int type) {
		this.type = type;
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public String getTypeAsString() {
		return String.valueOf(type);
	}
	public String getName () {
		return this.name;
	}
}
