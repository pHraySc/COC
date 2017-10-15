package com.ailk.biapp.ci.feedback.dao;

import com.ailk.biapp.ci.feedback.entity.DimDeclareDealStatus;
import java.util.List;

public interface IDimDeclareDealStatusHDao {
    List<DimDeclareDealStatus> selectAllStatus();
}
