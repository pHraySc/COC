package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class Campaigns
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String campaign_id;
	private String campaign_name;
	private String approve_date;

	public Campaigns(String campaign_id, String campaign_name, String approve_date)
	{
		this.campaign_id = campaign_id;
		this.campaign_name = campaign_name;
		this.approve_date = approve_date;
	}

	public String getCampaign_id()
	{
		return campaign_id;
	}

	public void setCampaign_id(String campaign_id)
	{
		this.campaign_id = campaign_id;
	}

	public String getCampaign_name()
	{
		return campaign_name;
	}

	public void setCampaign_name(String campaign_name)
	{
		this.campaign_name = campaign_name;
	}

	public String getApprove_date()
	{
		return approve_date;
	}

	public void setApprove_date(String approve_date)
	{
		this.approve_date = approve_date;
	}
}
