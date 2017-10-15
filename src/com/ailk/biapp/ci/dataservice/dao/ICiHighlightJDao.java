// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICiHighlightJDao.java

package com.ailk.biapp.ci.dataservice.dao;

import com.mongodb.DBObject;
import java.util.List;

public interface ICiHighlightJDao
{

	public abstract DBObject getHighLightObj();

	public abstract List getHighLightList();

	public abstract boolean needHighLight(String s);
}
