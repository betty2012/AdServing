package net.mad.ads.db.model.impl.lucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.util.BytesRef;

import net.mad.ads.db.model.AdQuery;

public class LuceneAdQuery implements AdQuery<BooleanQuery> {

	private BooleanQuery query = null;

	public LuceneAdQuery() {
		this.query = new BooleanQuery();
	}

	@Override
	public void addQuery(String name, String value, AdQuery.Occur occur) {
		this.query.add(new TermQuery(new Term(name, value)),
				getLuceneOccur(occur));
	}

	@Override
	public void addRangeQuery(String name, String lowerValue,
			String upperValue, AdQuery.Occur occur) {
		this.query.add(new TermRangeQuery(name, new BytesRef(lowerValue),
				new BytesRef(upperValue), true, true), getLuceneOccur(occur));
	}

	@Override
	public BooleanQuery getQuery() {
		return this.query;
	}

	@Override
	public void addSubQuery(BooleanQuery subQuery, AdQuery.Occur occur) {
		this.query.add(subQuery, getLuceneOccur(occur));
	}

	private BooleanClause.Occur getLuceneOccur(AdQuery.Occur occur) {
		switch (occur) {
		case MUST:
			return BooleanClause.Occur.MUST;
		case MUST_NOT:
			return BooleanClause.Occur.MUST_NOT;
		default:
			return BooleanClause.Occur.SHOULD;
		}
	}
}
