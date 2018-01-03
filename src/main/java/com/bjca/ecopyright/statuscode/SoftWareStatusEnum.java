package com.bjca.ecopyright.statuscode;
/**
 * 软件状态枚举
 * <p/>
 * 
 * Created by hm on 15-12-25.
 */
public enum SoftWareStatusEnum {
	TO_BE_IN_STORAGE("待入库",0),//待入库
	PENDING_PAYMENT("待缴费", 1),//待缴费
	PENDING_TRIAL("待分审", 2),//待分审
	TRIAL_OK("已分审", 3),//已分审
	PENDING_CORRECT("待补正", 4),//待补正
	PENDING_CERTIFICATE("待制证", 5),//待制证
	CERTIFICATE_OK("已制证",6),//已制证
	REVOKE("已撤回",7),//撤回
	
	TO_NEW_CHECKIN("新入库",51),//新入库
	TO_QUICK_CHECKIN("快捷入库",52),//快捷入库：雍和
	TO_EXECPTION_CHECKIN("异常回库",53),//异常回库
	TO_MARKCARD_CHECKIN("制证回库",55),//制证回库
	TO_OVERDUE_CHECKIN("逾期回库",57);//逾期回库;
		
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

		private SoftWareStatusEnum(String name, int value)
		{
			this.name = name;
			this.value = value;
		}

		static public String getStatusName(String statusName) {
			for (SoftWareStatusEnum statusEnum : SoftWareStatusEnum.values()) {
				if (statusEnum.name().equals(statusName)) {
					return statusEnum.getName();
				}
			}
			return null;
		}
		static public Integer getStatusValue(String statusName) {
			for (SoftWareStatusEnum statusEnum : SoftWareStatusEnum.values()) {
				if (statusEnum.name().equals(statusName)) {
					return statusEnum.getValue();
				}
			}
			return null;
		}

}
