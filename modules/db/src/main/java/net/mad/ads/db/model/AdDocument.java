package net.mad.ads.db.model;

public interface AdDocument<T> {
	
	public T getDocument ();

	public void addField (String name, String value);
}
