// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCampaignsJDaoImpl.java

package com.ailk.biapp.ci.dataservice.dao.impl;

import com.ailk.biapp.ci.dataservice.dao.ICiCampaignsJDao;
import com.ailk.biapp.ci.util.MongoDB;
import com.mongodb.*;
import java.util.ArrayList;
import java.util.List;
import org.bson.BSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class CiCampaignsJDaoImpl
	implements ICiCampaignsJDao
{

	DB db;

	public CiCampaignsJDaoImpl()
	{
	}

	public String getCampaigns(String mobile)
	{
		String result = "";
		result = getCampaignsByMobile(mobile).toString();
		return result;
	}

	public BSONObject getCampaignsByMobile(String mobile)
	{
		return MongoDB.getInstance().findOne("campaigns", "mobile", mobile);
	}

	public List getAllCampaigns(List sub)
	{
		db = MongoDB.getInstance().getDB();
		DBCollection collection = db.getCollection("allCampaigns");
		List list = new ArrayList();
		BasicDBList dbList = new BasicDBList();
		dbList.addAll(sub);
		DBObject inObj = new BasicDBObject("$in", dbList);
		DBCursor cursor = collection.find(new BasicDBObject("campaign_id", inObj));
		DBObject dbObj = null;
		for (; cursor.hasNext(); list.add(dbObj))
			dbObj = cursor.next();

		return list;
	}
}
