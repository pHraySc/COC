// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICiCampaignsJDao.java

package com.ailk.biapp.ci.dataservice.dao;

import java.util.List;
import org.bson.BSONObject;

public interface ICiCampaignsJDao
{

	public abstract BSONObject getCampaignsByMobile(String s);

	public abstract String getCampaigns(String s);

	public abstract List getAllCampaigns(List list);
}
