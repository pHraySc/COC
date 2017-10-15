package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.model.TargetCustomersAttr;
import java.util.Date;
import java.util.List;

public class TargetCustomersModel {
	private String target_customers_id;
	private String target_customers_name;
	private String target_customers_num;
	private String target_customers_base_month;
	private String target_customers_cycle;
	private String target_customers_tab_name;
	private String target_customers_source_name;
	private List<TargetCustomersAttr> target_customers_attr;
	private String customers_desc;
	private Date create_time;
	private String rule_desc;
	private String create_user_id;
	private String create_user_name;
	private Integer is_private;
	private String flag;

	public TargetCustomersModel() {
	}

	public String getTarget_customers_id() {
		return this.target_customers_id;
	}

	public void setTarget_customers_id(String targetCustomersId) {
		this.target_customers_id = targetCustomersId;
	}

	public String getTarget_customers_name() {
		return this.target_customers_name;
	}

	public void setTarget_customers_name(String targetCustomersName) {
		this.target_customers_name = targetCustomersName;
	}

	public String getTarget_customers_num() {
		return this.target_customers_num;
	}

	public void setTarget_customers_num(String targetCustomersNum) {
		this.target_customers_num = targetCustomersNum;
	}

	public String getTarget_customers_base_month() {
		return this.target_customers_base_month;
	}

	public void setTarget_customers_base_month(String targetCustomersBaseMonth) {
		this.target_customers_base_month = targetCustomersBaseMonth;
	}

	public String getTarget_customers_cycle() {
		return this.target_customers_cycle;
	}

	public void setTarget_customers_cycle(String targetCustomersCycle) {
		this.target_customers_cycle = targetCustomersCycle;
	}

	public String getTarget_customers_tab_name() {
		return this.target_customers_tab_name;
	}

	public void setTarget_customers_tab_name(String targetCustomersTabName) {
		this.target_customers_tab_name = targetCustomersTabName;
	}

	public List<TargetCustomersAttr> getTarget_customers_attr() {
		return this.target_customers_attr;
	}

	public void setTarget_customers_attr(List<TargetCustomersAttr> targetCustomersAttr) {
		this.target_customers_attr = targetCustomersAttr;
	}

	public String getTarget_customers_source_name() {
		return this.target_customers_source_name;
	}

	public void setTarget_customers_source_name(String targetCustomersSourceName) {
		this.target_customers_source_name = targetCustomersSourceName;
	}

	public String getCustomers_desc() {
		return this.customers_desc;
	}

	public void setCustomers_desc(String customersDesc) {
		this.customers_desc = customersDesc;
	}

	public Date getCreate_time() {
		return this.create_time;
	}

	public void setCreate_time(Date createTime) {
		this.create_time = createTime;
	}

	public String getRule_desc() {
		return this.rule_desc;
	}

	public void setRule_desc(String ruleDesc) {
		this.rule_desc = ruleDesc;
	}

	public String getCreate_user_id() {
		return this.create_user_id;
	}

	public void setCreate_user_id(String create_user_id) {
		this.create_user_id = create_user_id;
	}

	public String getCreate_user_name() {
		return this.create_user_name;
	}

	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}

	public Integer getIs_private() {
		return this.is_private;
	}

	public void setIs_private(Integer is_private) {
		this.is_private = is_private;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
