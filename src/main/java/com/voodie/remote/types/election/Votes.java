package com.voodie.remote.types.election;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.List;

@XmlSeeAlso(value = { Vote.class })
@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Votes implements Serializable {

	private List<Vote> votes = Lists.newArrayList();

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}
}