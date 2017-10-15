// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ILabelsAndIndexes2MongoJDao.java

package com.ailk.biapp.ci.dataservice.dao;

import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public interface ILabelsAndIndexes2MongoJDao
{

	public abstract int selectLabelsAndIndexesCount(String s)
		throws Exception;

	public abstract List selectLabelsAndIndexes(int i, int j, String s)
		throws Exception;

	public abstract void execBigSelectSql(String s, RowCallbackHandler rowcallbackhandler)
		throws Exception;
}
