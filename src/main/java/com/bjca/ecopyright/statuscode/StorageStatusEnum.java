package com.bjca.ecopyright.statuscode;
/**
 * 仓库状态
 * @className StorageStatusEnum.java
 * @date 2016-5-30下午3:31:38
 * @mail humin@bjca.org.cn
 * @author humin
 *
 */
public enum StorageStatusEnum {
		
		IS_USE("正常",0),
		IS_ISABANDON("废弃",1);
		
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

		private StorageStatusEnum(String name, int value)
		{
			this.name = name;
			this.value = value;
		}

		static public String getStatusName(String statusName) {
			for (StorageStatusEnum statusEnum : StorageStatusEnum.values()) {
				if (statusEnum.name().equals(statusName)) {
					return statusEnum.getName();
				}
			}
			return null;
		}
		static public Integer getStatusValue(String statusName) {
			for (StorageStatusEnum statusEnum : StorageStatusEnum.values()) {
				if (statusEnum.name().equals(statusName)) {
					return statusEnum.getValue();
				}
			}
			return null;
		}
}
