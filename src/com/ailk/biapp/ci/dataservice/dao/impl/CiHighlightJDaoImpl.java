// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiHighlightJDaoImpl.java

package com.ailk.biapp.ci.dataservice.dao.impl;

import com.ailk.biapp.ci.dataservice.dao.ICiHighlightJDao;
import com.ailk.biapp.ci.util.MongoDB;
import com.mongodb.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
@Repository
public class CiHighlightJDaoImpl
	implements ICiHighlightJDao
{

	DB db;

	public CiHighlightJDaoImpl()
	{
	}

	public List getHighLightList()
	{
		List list = (List)getHighLightObj().get("highlight");
		return list;
	}

	public DBObject getHighLightObj()
	{
		db = MongoDB.getInstance().getDB();
		DBCollection collection = db.getCollection("highlight");
		DBObject dbObject = collection.findOne();
		return dbObject;
	}

	public boolean needHighLight(String element_id)
	{
		boolean flag = false;
		List list = getHighLightList();
		if (list.contains(element_id))
			flag = true;
		return flag;
	}
}
