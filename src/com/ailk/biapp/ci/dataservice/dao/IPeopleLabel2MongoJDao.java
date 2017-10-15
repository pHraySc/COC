// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IPeopleLabel2MongoJDao.java

package com.ailk.biapp.ci.dataservice.dao;

import java.util.List;

public interface IPeopleLabel2MongoJDao
{

	public abstract List selectPeopleLabelList()
		throws Exception;

	public abstract boolean getUnionTableOfLebelsAndIndex(String s, String s1);
}
