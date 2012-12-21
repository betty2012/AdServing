package net.mad.ads.base.api.service.adserver.model;

public abstract class LinkAdvertisement extends Advertisement {

	private String linkUrl;
	private String linkTarget;
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getLinkTarget() {
		return linkTarget;
	}
	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}
	
	
	
}
