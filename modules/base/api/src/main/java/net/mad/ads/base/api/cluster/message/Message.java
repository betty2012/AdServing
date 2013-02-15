package net.mad.ads.base.api.cluster.message;

import java.io.Serializable;

import net.mad.ads.base.api.cluster.Member;

public abstract class Message implements Serializable {

	private Member member;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
