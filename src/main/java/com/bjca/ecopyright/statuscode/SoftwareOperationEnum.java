package com.bjca.ecopyright.statuscode;
/**
 * 软件操作日志枚举
 * @className SoftwareOperationEnum.java
 * @date 2016-8-15下午6:11:09
 * @mail humin@bjca.org.cn
 * @author humin
 *
 */
public enum SoftwareOperationEnum {
	NEW_IN_STORAGE("新入库",0),
	CARD_IN_STORAGE("制证回库", 1),
	EXCEPTION_IN_STORAGE("异常回库", 2),
	TRIAL_OUT_STORAGE("分审出库", 3),
	CARD_OUT_STORAGE("制证出库", 4),
	UPDATE_CERTIFICATE_DATE("修改出证日期", 5),
	VERIFICATION_FEE("批量核费",6),
	BATCH_CORRECTION("批量补正",7),
	EXCEPTION_CHECKOUT("异常出库",8),
	UPDATE_SOFTWARE_STATUS("修改软件状态", 9),
	DO_REVOKE("撤回",10),
	OVERDUE_IN_STORAGE("逾期回库",11),
	MODIFY_AUDITOR("修改审核人员",12),
	QUICK_IN_STORAGE("快捷入库",13);
	
	
	
		
		private String	name;
		private int	value;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public int getValue()
		{
			return value;
		}

		public void setValue(int value)
		{
			this.value = value;
		}

		private SoftwareOperationEnum(String name, int value)
		{
			this.name = name;
			this.value = value;
		}

		static public String getStatusName(String statusName) {
			for (SoftwareOperationEnum statusEnum : SoftwareOperationEnum.values()) {
				if (statusEnum.name().equals(statusName)) {
					return statusEnum.getName();
				}
			}
			return null;
		}
		static public Integer getStatusValue(String statusName) {
			for (SoftwareOperationEnum statusEnum : SoftwareOperationEnum.values()) {
				if (statusEnum.name().equals(statusName)) {
					return statusEnum.getValue();
				}
			}
			return null;
		}

}
