package net.mad.ads.db.model.format;

import java.io.Serializable;

public interface AdFormat extends Serializable {
	public String getName();
	
	public int getWidth();
	
	public int getHeight();
	
	public String getCompoundName ();
}
