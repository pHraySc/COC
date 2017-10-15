package com.ailk.biapp.ci.model;

import java.io.Serializable;
import java.util.Date;

public class DimCocLabelInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String labelCode;
	private String firstClassCode;
	private String firstClassDesc;
	private String secondClassCode;
	private String secondClassDesc;
	private String thirdClassCode;
	private String thirdClassDesc;
	private String fourthClassCode;
	private String fourthClassDesc;
	private String fifthClassCode;
	private String fifthClassDesc;
	private String sixthClassCode;
	private String sixthClassDesc;
	private String dataCycle;
	private String busiDesc;
	private String busiRulesDesc;
	private String busiUnderstanding;
	private String creator;
	private String appSuggest;
	private Date effectiveTime;
	private Date invalidTime;

	public DimCocLabelInfo()
	{
	}

	public String getLabelCode()
	{
		return labelCode;
	}

	public void setLabelCode(String labelCode)
	{
		this.labelCode = labelCode;
	}

	public String getFirstClassCode()
	{
		return firstClassCode;
	}

	public void setFirstClassCode(String firstClassCode)
	{
		this.firstClassCode = firstClassCode;
	}

	public String getFirstClassDesc()
	{
		return firstClassDesc;
	}

	public void setFirstClassDesc(String firstClassDesc)
	{
		this.firstClassDesc = firstClassDesc;
	}

	public String getSecondClassCode()
	{
		return secondClassCode;
	}

	public void setSecondClassCode(String secondClassCode)
	{
		this.secondClassCode = secondClassCode;
	}

	public String getSecondClassDesc()
	{
		return secondClassDesc;
	}

	public void setSecondClassDesc(String secondClassDesc)
	{
		this.secondClassDesc = secondClassDesc;
	}

	public String getThirdClassCode()
	{
		return thirdClassCode;
	}

	public void setThirdClassCode(String thirdClassCode)
	{
		this.thirdClassCode = thirdClassCode;
	}

	public String getThirdClassDesc()
	{
		return thirdClassDesc;
	}

	public void setThirdClassDesc(String thirdClassDesc)
	{
		this.thirdClassDesc = thirdClassDesc;
	}

	public String getFourthClassCode()
	{
		return fourthClassCode;
	}

	public void setFourthClassCode(String fourthClassCode)
	{
		this.fourthClassCode = fourthClassCode;
	}

	public String getFourthClassDesc()
	{
		return fourthClassDesc;
	}

	public void setFourthClassDesc(String fourthClassDesc)
	{
		this.fourthClassDesc = fourthClassDesc;
	}

	public String getFifthClassCode()
	{
		return fifthClassCode;
	}

	public void setFifthClassCode(String fifthClassCode)
	{
		this.fifthClassCode = fifthClassCode;
	}

	public String getFifthClassDesc()
	{
		return fifthClassDesc;
	}

	public void setFifthClassDesc(String fifthClassDesc)
	{
		this.fifthClassDesc = fifthClassDesc;
	}

	public String getDataCycle()
	{
		return dataCycle;
	}

	public void setDataCycle(String dataCycle)
	{
		this.dataCycle = dataCycle;
	}

	public String getBusiUnderstanding()
	{
		return busiUnderstanding;
	}

	public void setBusiUnderstanding(String busiUnderstanding)
	{
		this.busiUnderstanding = busiUnderstanding;
	}

	public Date getEffectiveTime()
	{
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime)
	{
		this.effectiveTime = effectiveTime;
	}

	public Date getInvalidTime()
	{
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime)
	{
		this.invalidTime = invalidTime;
	}

	public String getBusiDesc()
	{
		return busiDesc;
	}

	public void setBusiDesc(String busiDesc)
	{
		this.busiDesc = busiDesc;
	}

	public String getBusiRulesDesc()
	{
		return busiRulesDesc;
	}

	public void setBusiRulesDesc(String busiRulesDesc)
	{
		this.busiRulesDesc = busiRulesDesc;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public String getAppSuggest()
	{
		return appSuggest;
	}

	public void setAppSuggest(String appSuggest)
	{
		this.appSuggest = appSuggest;
	}

	public String getSixthClassCode()
	{
		return sixthClassCode;
	}

	public void setSixthClassCode(String sixthClassCode)
	{
		this.sixthClassCode = sixthClassCode;
	}

	public String getSixthClassDesc()
	{
		return sixthClassDesc;
	}

	public void setSixthClassDesc(String sixthClassDesc)
	{
		this.sixthClassDesc = sixthClassDesc;
	}
}
