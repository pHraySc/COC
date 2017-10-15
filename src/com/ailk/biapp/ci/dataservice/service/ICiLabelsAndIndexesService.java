// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICiLabelsAndIndexesService.java

package com.ailk.biapp.ci.dataservice.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;
import org.bson.BSONObject;

public interface ICiLabelsAndIndexesService
{

	public abstract BSONObject getLabelsAndIndexesByMobile(String s)
		throws CIServiceException;

	public abstract List getCrmTypeModelByMobile(String s)
		throws CIServiceException;

	public abstract List getCrmTypeModelByMobile(String s, String s1, String s2, String s3, String s4)
		throws CIServiceException;

	public abstract List getCrmTypeModelByMobile4OtherProvince(String s)
		throws CIServiceException;

	public abstract Integer queryPhoneNum(String s);
}
