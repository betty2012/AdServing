package net.mad.ads.db.model.type;

import net.mad.ads.db.definition.AdDefinition;


public abstract class AbstractAdType implements AdType {
	private String type = "";
	private String name = "";
	public AbstractAdType(String name, String type) {
		this.type = type;
		this.name = name;
	}
	public String getType() {
		return type;
	}
	
	public String getName () {
		return this.name;
	}
	
	@Override
	public int compareTo(AdType comp) {
		return name.compareTo(comp.getName());
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
