package com.ailk.biapp.ci.model;

public class CiLogStatAnalysisModel implements Comparable<CiLogStatAnalysisModel> {
    private String beginDate;
    private String endDate;
    private String parentId;
    private String opTypeId;
    private String secondDeptId;
    private String thirdDeptId;
    private String dataDate;
    private String secondDeptName;
    private String thirdDeptName;
    private String opTimes;
    private String userName;
    private String hasChild;
    private String opTypeName;
    private boolean week;
    private String opTimes1;
    private String opTimes2;
    private String opTimes3;
    private String opTimes4;
    private String opTimes5;
    private String opTimes6;
    private String opTimes7;
    private String opTimes8;
    private String opTimes9;
    private String opTimes10;
    public static final int SUPPERT_OP_TYPE_NUM = 10;

    public CiLogStatAnalysisModel() {
    }

    public CiLogStatAnalysisModel(String beginDate, String endDate, String parentId, String opTypeId) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.parentId = parentId;
        this.opTypeId = opTypeId;
    }

    public CiLogStatAnalysisModel(String beginDate, String endDate, String parentId, String opTypeId, String dataDate, String secondDeptId, String thirdDeptId) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.parentId = parentId;
        this.opTypeId = opTypeId;
        this.dataDate = dataDate;
        this.secondDeptId = secondDeptId;
        this.thirdDeptId = thirdDeptId;
    }

    public String getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate.replace("-", "");
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate.replace("-", "");
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOpTypeId() {
        return this.opTypeId;
    }

    public void setOpTypeId(String opTypeId) {
        this.opTypeId = opTypeId;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getSecondDeptId() {
        return this.secondDeptId;
    }

    public void setSecondDeptId(String secondDeptId) {
        this.secondDeptId = secondDeptId;
    }

    public String getThirdDeptId() {
        return this.thirdDeptId;
    }

    public void setThirdDeptId(String thirdDeptId) {
        this.thirdDeptId = thirdDeptId;
    }

    public String getSecondDeptName() {
        return this.secondDeptName;
    }

    public void setSecondDeptName(String secondDeptName) {
        this.secondDeptName = secondDeptName;
    }

    public String getThirdDeptName() {
        return this.thirdDeptName;
    }

    public void setThirdDeptName(String thirdDeptName) {
        this.thirdDeptName = thirdDeptName;
    }

    public String getOpTimes() {
        return this.opTimes;
    }

    public void setOpTimes(String opTimes) {
        this.opTimes = opTimes;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOpTimes1() {
        return this.opTimes1;
    }

    public void setOpTimes1(String opTimes1) {
        this.opTimes1 = opTimes1;
    }

    public String getOpTimes2() {
        return this.opTimes2;
    }

    public void setOpTimes2(String opTimes2) {
        this.opTimes2 = opTimes2;
    }

    public String getOpTimes3() {
        return this.opTimes3;
    }

    public void setOpTimes3(String opTimes3) {
        this.opTimes3 = opTimes3;
    }

    public String getOpTimes4() {
        return this.opTimes4;
    }

    public void setOpTimes4(String opTimes4) {
        this.opTimes4 = opTimes4;
    }

    public String getOpTimes5() {
        return this.opTimes5;
    }

    public void setOpTimes5(String opTimes5) {
        this.opTimes5 = opTimes5;
    }

    public String getOpTimes6() {
        return this.opTimes6;
    }

    public void setOpTimes6(String opTimes6) {
        this.opTimes6 = opTimes6;
    }

    public String getOpTimes7() {
        return this.opTimes7;
    }

    public void setOpTimes7(String opTimes7) {
        this.opTimes7 = opTimes7;
    }

    public String getOpTimes8() {
        return this.opTimes8;
    }

    public void setOpTimes8(String opTimes8) {
        this.opTimes8 = opTimes8;
    }

    public String getOpTimes9() {
        return this.opTimes9;
    }

    public void setOpTimes9(String opTimes9) {
        this.opTimes9 = opTimes9;
    }

    public String getOpTimes10() {
        return this.opTimes10;
    }

    public void setOpTimes10(String opTimes10) {
        this.opTimes10 = opTimes10;
    }

    public int hashCode() {
        boolean prime = true;
        byte result = 1;
        int result1 = 31 * result + (this.beginDate == null?0:this.beginDate.hashCode());
        result1 = 31 * result1 + (this.dataDate == null?0:this.dataDate.hashCode());
        result1 = 31 * result1 + (this.endDate == null?0:this.endDate.hashCode());
        result1 = 31 * result1 + (this.opTimes == null?0:this.opTimes.hashCode());
        result1 = 31 * result1 + (this.secondDeptId == null?0:this.secondDeptId.hashCode());
        result1 = 31 * result1 + (this.secondDeptName == null?0:this.secondDeptName.hashCode());
        result1 = 31 * result1 + (this.thirdDeptId == null?0:this.thirdDeptId.hashCode());
        result1 = 31 * result1 + (this.thirdDeptName == null?0:this.thirdDeptName.hashCode());
        result1 = 31 * result1 + (this.userName == null?0:this.userName.hashCode());
        return result1;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            CiLogStatAnalysisModel other = (CiLogStatAnalysisModel)obj;
            if(this.beginDate == null) {
                if(other.beginDate != null) {
                    return false;
                }
            } else if(!this.beginDate.equals(other.beginDate)) {
                return false;
            }

            if(this.dataDate == null) {
                if(other.dataDate != null) {
                    return false;
                }
            } else if(!this.dataDate.equals(other.dataDate)) {
                return false;
            }

            if(this.endDate == null) {
                if(other.endDate != null) {
                    return false;
                }
            } else if(!this.endDate.equals(other.endDate)) {
                return false;
            }

            if(this.opTimes == null) {
                if(other.opTimes != null) {
                    return false;
                }
            } else if(!this.opTimes.equals(other.opTimes)) {
                return false;
            }

            if(this.secondDeptId == null) {
                if(other.secondDeptId != null) {
                    return false;
                }
            } else if(!this.secondDeptId.equals(other.secondDeptId)) {
                return false;
            }

            if(this.secondDeptName == null) {
                if(other.secondDeptName != null) {
                    return false;
                }
            } else if(!this.secondDeptName.equals(other.secondDeptName)) {
                return false;
            }

            if(this.thirdDeptId == null) {
                if(other.thirdDeptId != null) {
                    return false;
                }
            } else if(!this.thirdDeptId.equals(other.thirdDeptId)) {
                return false;
            }

            if(this.thirdDeptName == null) {
                if(other.thirdDeptName != null) {
                    return false;
                }
            } else if(!this.thirdDeptName.equals(other.thirdDeptName)) {
                return false;
            }

            if(this.userName == null) {
                if(other.userName != null) {
                    return false;
                }
            } else if(!this.userName.equals(other.userName)) {
                return false;
            }

            return true;
        }
    }

    public String getHasChild() {
        return this.hasChild;
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    public String getOpTypeName() {
        return this.opTypeName;
    }

    public void setOpTypeName(String opTypeName) {
        this.opTypeName = opTypeName;
    }

    public boolean isWeek() {
        return this.week;
    }

    public void setWeek(boolean week) {
        this.week = week;
    }

    public int compareTo(CiLogStatAnalysisModel model) {
        return this.getDataDate().compareTo(model.getDataDate());
    }
}
