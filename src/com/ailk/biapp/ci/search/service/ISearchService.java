package com.ailk.biapp.ci.search.service;

import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;

public interface ISearchService
{

	public abstract List queryAllLabelInfo()
		throws CIServiceException;

	public abstract List queryAllCustomGroupInfo()
		throws CIServiceException;

	public abstract void createAllCustomerGroupIndex()
		throws CIServiceException;

	public abstract void createAllLabelIndex()
		throws Exception;

	public abstract List searchLabelForPage(Pager pager, String s, String s1, CiLabelInfo cilabelinfo)
		throws CIServiceException;

	public abstract long searchLabelCount(String s, String s1, CiLabelInfo cilabelinfo)
		throws CIServiceException;

	public abstract void deleteLabelIndexByLabelId(Integer integer)
		throws CIServiceException;

	public abstract void addLabelIndexByLabelId(Integer integer)
		throws CIServiceException;
}
