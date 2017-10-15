// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserAttentionCustomId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiUserAttentionLabelId

public class CiUserAttentionCustomId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String customId;

	public CiUserAttentionCustomId()
	{
	}

	public CiUserAttentionCustomId(String userId, String customId)
	{
		this.userId = userId;
		this.customId = customId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getCustomId()
	{
		return customId;
	}

	public void setCustomId(String customId)
	{
		this.customId = customId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiUserAttentionLabelId))
		{
			return false;
		} else
		{
			CiUserAttentionCustomId castOther = (CiUserAttentionCustomId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getCustomId() == castOther.getCustomId() || getCustomId() != null && castOther.getCustomId() != null && getCustomId().equals(castOther.getCustomId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getCustomId() != null ? getCustomId().hashCode() : 0);
		return result;
	}
}
