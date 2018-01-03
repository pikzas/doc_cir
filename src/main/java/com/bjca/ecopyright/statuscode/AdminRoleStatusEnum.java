package com.bjca.ecopyright.statuscode;
/**
 * 角色
 * @className AdminRoleStatusEnum.java
 * @date 2016-5-18下午2:55:38
 * @mail humin@bjca.org.cn
 * @author humin
 *
 */
public enum AdminRoleStatusEnum {
		ADMIN("管理员", 1), //
		AUDITOR("审查员",2),
		OPERATOR("操作员",3),
		
		
		YONGHE("雍和",7),
		
		ACTIVATE("已激活",1),
		ABANDONED("已废弃",2),
		INVALID("已失效",3);
		
		
		private String	name;
		private Integer	value;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public Integer getValue()
		{
			return value;
		}

		public void setValue(Integer value)
		{
			this.value = value;
		}

		private AdminRoleStatusEnum(String name, int value)
		{
			this.name = name;
			this.value = value;
		}

		static public String getStatusName(String statusName) {
			for (AdminRoleStatusEnum statusEnum : AdminRoleStatusEnum.values()) {
				if (statusEnum.name().equals(statusName)) {
					return statusEnum.getName();
				}
			}
			return null;
		}
		static public Integer getStatusValue(String statusName) {
			for (AdminRoleStatusEnum statusEnum : AdminRoleStatusEnum.values()) {
				if (statusEnum.name().equals(statusName)) {
					return statusEnum.getValue();
				}
			}
			return null;
		}

		public static void main(String[] args)
		{
		}
}
