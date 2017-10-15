// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICiLabelsAndIndexesJDao.java

package com.ailk.biapp.ci.dataservice.dao;

import java.util.List;
import java.util.Map;
import org.bson.BSONObject;

public interface ICiLabelsAndIndexesJDao
{

	public abstract BSONObject getLabelsAndIndexesByMobile(String s);

	public abstract String getLabelsAndIndexes(String s);

	public abstract List getLabels(List list);

	public abstract List getIndexes(List list);

	public abstract List selectAllLabelInfo();

	public abstract Map selectBySQL(String s);

	public abstract Integer selectPhoneNum(String s, String s1);

	public abstract List selectTableName(String s);

	public abstract List selectLabelTypeInfo();
}
