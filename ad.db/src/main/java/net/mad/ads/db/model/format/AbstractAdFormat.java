package net.mad.ads.db.model.format;

public abstract class AbstractAdFormat implements AdFormat {
	private String name = null;
	private int width = -1;
	private int height = -1;
	
	public AbstractAdFormat (String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getCompoundName () {
		return width + "x" + height;
	}
}
