/**
 * Mad-Advertisement
 * Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.marx_labs.ads.db.db.store.impl.local;

import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

public class AdCollector extends Collector {

	private Scorer scorer;
	

	private BitSet hits = null;

	public AdCollector(int size) {
		hits = new BitSet(size);
	}

	// simply print docId and score of every matching document
	@Override
	public void collect(int doc) throws IOException {
		hits.set(doc);
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;
	}
	
	public BitSet getHits () {
		return hits;
	}

	@Override
	public void setNextReader(AtomicReaderContext reader) throws IOException {
		
	}
}
