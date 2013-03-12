package net.mad.ads.db.model.impl.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;

import net.mad.ads.db.model.AdDocument;

public class LuceneAdDocument implements AdDocument<Document> {

	private Document document = null;
	
	public LuceneAdDocument () {
		this.document = new Document();
	}
	
	@Override
	public Document getDocument() {
		return document;
	}

	@Override
	public void addField(String name, String value) {
		document.add(new StringField(name, value, Store.NO));
	}

}
