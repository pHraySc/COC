// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomFileRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiCustomFileRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String fileId;
	private String customGroupId;
	private String fileName;
	private String fileUrl;
	private String fileType;
	private Timestamp fileStartTime;
	private Timestamp fileEndTime;

	public CiCustomFileRel()
	{
	}

	public CiCustomFileRel(String customGroupId, String fileName, String fileUrl, String fileType, Timestamp fileStartTime, Timestamp fileEndTime)
	{
		this.customGroupId = customGroupId;
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.fileType = fileType;
		this.fileStartTime = fileStartTime;
		this.fileEndTime = fileEndTime;
	}

	public String getFileId()
	{
		return fileId;
	}

	public void setFileId(String fileId)
	{
		this.fileId = fileId;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileUrl()
	{
		return fileUrl;
	}

	public void setFileUrl(String fileUrl)
	{
		this.fileUrl = fileUrl;
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public Timestamp getFileStartTime()
	{
		return fileStartTime;
	}

	public void setFileStartTime(Timestamp fileStartTime)
	{
		this.fileStartTime = fileStartTime;
	}

	public Timestamp getFileEndTime()
	{
		return fileEndTime;
	}

	public void setFileEndTime(Timestamp fileEndTime)
	{
		this.fileEndTime = fileEndTime;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiCustomFileRel [fileId=").append(fileId).append(", customGroupId=").append(customGroupId).append(", fileName=").append(fileName).append(", fileUrl=").append(fileUrl).append(", fileType=").append(fileType).append(", fileStartTime=").append(fileStartTime).append(", fileEndTime=").append(fileEndTime).append("]").toString();
	}
}
