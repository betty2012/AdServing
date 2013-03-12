package net.mad.ads.db.model;

public interface AdQuery<T> {

	public void addQuery (String name, String value, AdQuery.Occur occur);
	
	public void addRangeQuery (String name, String lowerValue, String upperValue, AdQuery.Occur occur);
	
	public void addSubQuery (T subQuery, AdQuery.Occur occur);
	
	public T getQuery ();
	
	
	public enum Occur {
		MUST,
		MUST_NOT,
		SHOULD
	}
}
