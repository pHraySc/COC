// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiConfigJDaoImpl.java

package com.ailk.biapp.ci.dataservice.dao.impl;

import com.ailk.biapp.ci.dataservice.dao.ICiConfigJDao;
import com.ailk.biapp.ci.util.MongoDB;
import com.mongodb.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CiConfigJDaoImpl
	implements ICiConfigJDao
{

	DB db;

	public CiConfigJDaoImpl()
	{
	}

	public List getConifg()
	{
		db = MongoDB.getInstance().getDB();
		List list = new ArrayList();
		DBCollection collection = db.getCollection("configs");
		DBCursor cursor = collection.find().sort((new BasicDBObject()).append("id", Integer.valueOf(1)));
		DBObject dbObj = null;
		for (; cursor.hasNext(); list.add(dbObj))
			dbObj = cursor.next();

		return list;
	}
}
