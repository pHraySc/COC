// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICiCampaignsService.java

package com.ailk.biapp.ci.dataservice.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiCampaignsService
{

	public abstract String getCampaigns(String s)
		throws CIServiceException;

	public abstract List getCampaignsList(String s)
		throws CIServiceException;
}
