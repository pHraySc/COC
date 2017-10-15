package com.ailk.biapp.ci.localization.shanxi.model;

import com.ailk.biapp.ci.localization.shanxi.dao.SXBIConnectionEx;
import com.ailk.biapp.ci.localization.shanxi.dao.SqlcaSX;
import com.asiainfo.biframe.privilege.IUser;
import java.io.Serializable;
import org.apache.log4j.Logger;

public class SXUser implements IUser, Serializable {
    private static final long serialVersionUID = 8851545541069541637L;
    private final Logger log = Logger.getLogger(SXUser.class);
    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SXUser(String userId) {
        try {
            this.userId = userId;
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public String getCityid() {
        String cityId = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CITY_ID FROM LKG_STAFF WHERE STAFF_ID=\'").append(this.userId).append("\'");
        SqlcaSX sqlCaSX = null;

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            sqlCaSX.execute(sql.toString());
            if(sqlCaSX.next()) {
                cityId = sqlCaSX.getString("CITY_ID");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        this.log.debug("getCityid sql:" + sql);
        this.log.debug("city id is:" + cityId);
        return cityId;
    }

    public String getCountryid() {
        String countryId = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNTRY_ID FROM LKG_STAFF WHERE STAFF_ID=\'").append(this.userId).append("\'");
        SqlcaSX sqlCaSX = null;

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            sqlCaSX.execute(sql.toString());
            if(sqlCaSX.next()) {
                countryId = sqlCaSX.getString("COUNTRY_ID");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        this.log.debug("getCountryId sql:" + sql);
        this.log.debug("country id is:" + countryId);
        return countryId;
    }

    public String getDomainType() {
        return null;
    }

    public String getEmail() {
        return null;
    }

    public String getGroupId() {
        String groupId = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT GROUP_ID FROM LKG_STAFF WHERE STAFF_ID=\'").append(this.userId).append("\'");
        SqlcaSX sqlCaSX = null;

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            sqlCaSX.execute(sql.toString());
            if(sqlCaSX.next()) {
                groupId = sqlCaSX.getString("GROUP_ID");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        return groupId;
    }

    public String getLoginId() {
        return this.userId;
    }

    public String getMobilePhone() {
        String phone = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT PHONE FROM LKG_STAFF WHERE STAFF_ID=\'").append(this.userId).append("\'");
        SqlcaSX sqlCaSX = null;

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            sqlCaSX.execute(sql.toString());
            if(sqlCaSX.next()) {
                phone = sqlCaSX.getString("PHONE");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        return phone;
    }

    public String getUserid() {
        return this.userId;
    }

    public String getUsername() {
        String userName = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT STAFF_NAME FROM LKG_STAFF WHERE STAFF_ID=\'").append(this.userId).append("\'");
        SqlcaSX sqlCaSX = null;

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            sqlCaSX.execute(sql.toString());
            if(sqlCaSX.next()) {
                userName = sqlCaSX.getString("STAFF_NAME");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        return userName;
    }

    public int getDepartmentid() {
        String deptId = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DEP_ID FROM LKG_STAFF WHERE STAFF_ID=\'").append(this.userId).append("\'");
        SqlcaSX sqlCaSX = null;

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            sqlCaSX.execute(sql.toString());
            if(sqlCaSX.next()) {
                deptId = sqlCaSX.getString("DEP_ID");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        return Integer.valueOf(deptId).intValue();
    }
}
