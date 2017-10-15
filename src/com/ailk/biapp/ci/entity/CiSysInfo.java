// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiSysInfo.java

package com.ailk.biapp.ci.entity;


public class CiSysInfo
{

	private String sysId;
	private String sysName;
	private String ftpServerIp;
	private String ftpUser;
	private String ftpPort;
	private String ftpPwd;
	private String ftpPath;
	private String localPath;
	private String webserviceWSDL;
	private String webserviceTargetNamespace;
	private String webserviceMethod;
	private String webserviceArgs;
	private Integer showInPage;
	private String descTxt;
	private Integer isNeedXml;
	private Integer isNeedDes;
	private String desKey;
	private Integer isNeedCycle;
	private String actionId;
	private String functionId;
	private String pushClassName;
	private String reqId;

	public CiSysInfo()
	{
	}

	public String getPushClassName()
	{
		return pushClassName;
	}

	public void setPushClassName(String pushClassName)
	{
		this.pushClassName = pushClassName;
	}

	public String getActionId()
	{
		return actionId;
	}

	public void setActionId(String actionId)
	{
		this.actionId = actionId;
	}

	public String getFunctionId()
	{
		return functionId;
	}

	public void setFunctionId(String functionId)
	{
		this.functionId = functionId;
	}

	public String getSysId()
	{
		return sysId;
	}

	public void setSysId(String sysId)
	{
		this.sysId = sysId;
	}

	public String getSysName()
	{
		return sysName;
	}

	public void setSysName(String sysName)
	{
		this.sysName = sysName;
	}

	public String getFtpServerIp()
	{
		return ftpServerIp;
	}

	public void setFtpServerIp(String ftpServerIp)
	{
		this.ftpServerIp = ftpServerIp;
	}

	public String getFtpUser()
	{
		return ftpUser;
	}

	public void setFtpUser(String ftpUser)
	{
		this.ftpUser = ftpUser;
	}

	public String getFtpPort()
	{
		return ftpPort;
	}

	public void setFtpPort(String ftpPort)
	{
		this.ftpPort = ftpPort;
	}

	public String getFtpPwd()
	{
		return ftpPwd;
	}

	public void setFtpPwd(String ftpPwd)
	{
		this.ftpPwd = ftpPwd;
	}

	public String getFtpPath()
	{
		return ftpPath;
	}

	public void setFtpPath(String ftpPath)
	{
		this.ftpPath = ftpPath;
	}

	public String getLocalPath()
	{
		return localPath;
	}

	public void setLocalPath(String localPath)
	{
		this.localPath = localPath;
	}

	public String getWebserviceWSDL()
	{
		return webserviceWSDL;
	}

	public void setWebserviceWSDL(String webserviceWSDL)
	{
		this.webserviceWSDL = webserviceWSDL;
	}

	public String getWebserviceTargetNamespace()
	{
		return webserviceTargetNamespace;
	}

	public void setWebserviceTargetNamespace(String webserviceTargetNamespace)
	{
		this.webserviceTargetNamespace = webserviceTargetNamespace;
	}

	public String getWebserviceMethod()
	{
		return webserviceMethod;
	}

	public void setWebserviceMethod(String webserviceMethod)
	{
		this.webserviceMethod = webserviceMethod;
	}

	public String getWebserviceArgs()
	{
		return webserviceArgs;
	}

	public void setWebserviceArgs(String webserviceArgs)
	{
		this.webserviceArgs = webserviceArgs;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}

	public Integer getShowInPage()
	{
		return showInPage;
	}

	public void setShowInPage(Integer showInPage)
	{
		this.showInPage = showInPage;
	}

	public Integer getIsNeedXml()
	{
		return isNeedXml;
	}

	public void setIsNeedXml(Integer isNeedXml)
	{
		this.isNeedXml = isNeedXml;
	}

	public Integer getIsNeedDes()
	{
		return isNeedDes;
	}

	public void setIsNeedDes(Integer isNeedDes)
	{
		this.isNeedDes = isNeedDes;
	}

	public String getDesKey()
	{
		return desKey;
	}

	public void setDesKey(String desKey)
	{
		this.desKey = desKey;
	}

	public Integer getIsNeedCycle()
	{
		return isNeedCycle;
	}

	public void setIsNeedCycle(Integer isNeedCycle)
	{
		this.isNeedCycle = isNeedCycle;
	}

	public String getReqId()
	{
		return reqId;
	}

	public void setReqId(String reqId)
	{
		this.reqId = reqId;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiSysInfo [sysId=").append(sysId).append(", sysName=").append(sysName).append(", ftpServerIp=").append(ftpServerIp).append(", ftpUser=").append(ftpUser).append(", ftpPort=").append(ftpPort).append(", ftpPwd=").append(ftpPwd).append(", ftpPath=").append(ftpPath).append(", localPath=").append(localPath).append(", webserviceWSDL=").append(webserviceWSDL).append(", webserviceTargetNamespace=").append(webserviceTargetNamespace).append(", webserviceMethod=").append(webserviceMethod).append(", webserviceArgs=").append(webserviceArgs).append(", showInPage=").append(showInPage).append(", descTxt=").append(descTxt).append(", isNeedXml=").append(isNeedXml).append(", isNeedDes=").append(isNeedDes).append(", desKey=").append(desKey).append(", isNeedCycle=").append(isNeedCycle).append(", actionId=").append(actionId).append(", functionId=").append(functionId).append(", pushClassName=").append(pushClassName).append("]").toString();
	}
}
