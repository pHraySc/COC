// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ILabelAndIndex2MongoService.java

package com.ailk.biapp.ci.dataservice.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public interface ILabelAndIndex2MongoService
{

	public abstract int queryLabelsAndIndexesCount(String s)
		throws CIServiceException;

	public abstract List queryLabelsAndIndexes(int i, int j, String s)
		throws CIServiceException;

	public abstract List queryDimCrmLabelTypeList()
		throws CIServiceException;

	public abstract List queryPeopleLabelList()
		throws CIServiceException;

	public abstract List queryPersonIndexList()
		throws CIServiceException;

	public abstract List queryNewestLabelDate()
		throws CIServiceException;

	public abstract boolean collectAllTag(String s, String s1);

	public abstract void execBigSelectSql(String s, RowCallbackHandler rowcallbackhandler)
		throws Exception;
}
