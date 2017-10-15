// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICampaign2MongoService.java

package com.ailk.biapp.ci.dataservice.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICampaign2MongoService
{

	public abstract int queryPersonCampaignsCount()
		throws CIServiceException;

	public abstract List queryPersonCampaigns(int i, int j)
		throws CIServiceException;

	public abstract List queryCiCustomCampsegRelList()
		throws CIServiceException;
}
