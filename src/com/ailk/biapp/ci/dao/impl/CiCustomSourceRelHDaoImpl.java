// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomSourceRelHDaoImpl.java

package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomSourceRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomSourceRel;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
@Repository
public class CiCustomSourceRelHDaoImpl extends HibernateBaseDao<CiCustomSourceRel,String> implements ICiCustomSourceRelHDao
{

	public CiCustomSourceRelHDaoImpl() {}

	public List selectByCustomGroupId(String customGroupId)
	{
		String hql = " from CiCustomSourceRel where customGroupId = ? order by sortNum asc";
		return find(hql, new Object[] {
			customGroupId
		});
	}

	public void insert(CiCustomSourceRel cicustomsourcerel)
	{
	}

	public void insertCiCustomSourceRelList(List ciCustomSourceRels)
	{
		CiCustomSourceRel rel;
		for (Iterator i$ = ciCustomSourceRels.iterator(); i$.hasNext(); save(rel))
			rel = (CiCustomSourceRel)i$.next();

	}

	public void deleteByCustomGroupId(String customGroupId)
	{
		String hql = " delete from CiCustomSourceRel where customGroupId = ? ";
		batchExecute(hql, new Object[] {
			customGroupId
		});
	}
}
