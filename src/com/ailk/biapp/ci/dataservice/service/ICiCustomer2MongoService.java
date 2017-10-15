// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICiCustomer2MongoService.java

package com.ailk.biapp.ci.dataservice.service;

import com.ailk.biapp.ci.model.CiCustomCampsegRel;
import java.util.List;

public interface ICiCustomer2MongoService
{

	public abstract List queryNeedProcessCiCustomCampsegRel();

	public abstract boolean updateDeelStatus(CiCustomCampsegRel cicustomcampsegrel, int i);

	public abstract boolean process(CiCustomCampsegRel cicustomcampsegrel);

	public abstract boolean toWidthTable();
}
