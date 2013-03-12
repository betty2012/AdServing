package net.mad.ads.db.model;

public interface AdQuery<T> {

	public void addQuery (String name, String value);
	
	public void addRangeQuery (String lowerValue, String upperValue);
	
	public T getQuery ();
}
