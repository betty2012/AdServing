package net.mad.ads.db.definition;

import net.mad.ads.db.definition.condition.ClickExpirationConditionDefinition;
import net.mad.ads.db.definition.condition.ViewExpirationConditionDefinition;

public class Campaign {
	private String id;
	private ViewExpirationConditionDefinition viewExpiration;
	private ClickExpirationConditionDefinition clickExpiration;
	
	public Campaign() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ViewExpirationConditionDefinition getViewExpiration() {
		return viewExpiration;
	}

	public void setViewExpiration(ViewExpirationConditionDefinition viewExpiration) {
		this.viewExpiration = viewExpiration;
	}

	public ClickExpirationConditionDefinition getClickExpiration() {
		return clickExpiration;
	}

	public void setClickExpiration(
			ClickExpirationConditionDefinition clickExpiration) {
		this.clickExpiration = clickExpiration;
	}
	
	
}
