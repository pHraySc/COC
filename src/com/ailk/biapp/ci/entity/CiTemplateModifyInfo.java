// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiTemplateModifyInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiTemplateModifyInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer historyId;
	private String templateId;
	private String modifyUserId;
	private Date modifyTime;
	private String templateName;
	private String templateDesc;
	private String labelOptRuleShow;
	private String sceneId;
	private Integer isPrivate;

	public CiTemplateModifyInfo()
	{
	}

	public CiTemplateModifyInfo(Integer historyId, String templateId, String modifyUserId, Date modifyTime, String templateName, String templateDesc, String labelOptRuleShow, 
			String sceneId, Integer isPrivate)
	{
		this.historyId = historyId;
		this.templateId = templateId;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
		this.templateName = templateName;
		this.templateDesc = templateDesc;
		this.labelOptRuleShow = labelOptRuleShow;
		this.sceneId = sceneId;
		this.isPrivate = isPrivate;
	}

	public Integer getHistoryId()
	{
		return historyId;
	}

	public void setHistoryId(Integer historyId)
	{
		this.historyId = historyId;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getModifyUserId()
	{
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId)
	{
		this.modifyUserId = modifyUserId;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}

	public String getTemplateDesc()
	{
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc)
	{
		this.templateDesc = templateDesc;
	}

	public String getLabelOptRuleShow()
	{
		return labelOptRuleShow;
	}

	public void setLabelOptRuleShow(String labelOptRuleShow)
	{
		this.labelOptRuleShow = labelOptRuleShow;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiTemplateModifyInfo [historyId=").append(historyId).append(", templateId=").append(templateId).append(", modifyUserId=").append(modifyUserId).append(", modifyTime=").append(modifyTime).append(", templateName=").append(templateName).append(", templateDesc=").append(templateDesc).append(", labelOptRuleShow=").append(labelOptRuleShow).append("]").toString();
	}

	public Integer getIsPrivate()
	{
		return isPrivate;
	}

	public void setIsPrivate(Integer isPrivate)
	{
		this.isPrivate = isPrivate;
	}

	public String getSceneId()
	{
		return sceneId;
	}

	public void setSceneId(String sceneId)
	{
		this.sceneId = sceneId;
	}
}
