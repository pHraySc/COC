// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICampaign2MongoJDao.java

package com.ailk.biapp.ci.dataservice.dao;

import java.util.List;

public interface ICampaign2MongoJDao
{

	public abstract int selectCiPersonCampaignsCount()
		throws Exception;

	public abstract List selectCiPersonCampaignsList(int i, int j)
		throws Exception;
}
