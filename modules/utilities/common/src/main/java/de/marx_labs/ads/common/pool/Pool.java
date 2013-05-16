package de.marx_labs.ads.common.pool;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class Pool<T> {

	private final BlockingQueue<T> objects;

	public Pool(Collection<? extends T> objects) {
		this.objects = new ArrayBlockingQueue<T>(objects.size(), false, objects);
	}

	public T borrow() throws InterruptedException {
		return this.objects.take();
	}

	public void giveBack(T object) throws InterruptedException {
		this.objects.put(object);
	}
}
