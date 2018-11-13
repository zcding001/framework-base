package com.base.framework.core.enums;

/**
*  终端类型
*  @date                    ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
*/
public enum TerminalTypes {
	PC("PC", 1),
    IOS("IOS", 2),
    ANDROID("ANDROID", 3), 
    WAP("WAP", 4),
    WX("WX", 5);

	/**
	 * 标识
	 */
	private String name;
	/**
	 * 名称
	 */
	private int value;

	TerminalTypes(String name, int value) {
		this.name = name;
		this.value = value;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
    *  通过终端名称获取类型值
    *  @param name  终端名称
    *  @return int
    *  @date                    ：2018/11/13
    *  @author                  ：zc.ding@foxmail.com
    */
	public static int getValue(String name) {
		for (TerminalTypes terminalTypes : TerminalTypes.values()) {
			if (terminalTypes.getName().equals(name)) {
				return terminalTypes.getValue();
			}
		}
		return -1;
	}

	/**
	*  通过终端值获取终端名称
	*  @param value 终端值
	*  @return java.lang.String
	*  @date                    ：2018/11/13
	*  @author                  ：zc.ding@foxmail.com
	*/
	public static String getName(int value) {
		for (TerminalTypes terminalTypes : TerminalTypes.values()) {
			if (terminalTypes.getValue() == value) {
				return terminalTypes.getName();
			}
		}
		return null;
	}

	/**
	*  通过终端值获取对应的枚举类型
	*  @param value 终端值
	*  @return com.yirun.framework.core.enums.TerminalTypes
	*  @date                    ：2018/11/13
	*  @author                  ：zc.ding@foxmail.com
	*/
	public static TerminalTypes getTerminalTypes(int value) {
		for (TerminalTypes terminalTypes : TerminalTypes.values()) {
			if (terminalTypes.getValue() == value) {
				return terminalTypes;
			}
		}
		return null;
	}

	/**
	*  通过终端名称获取对应的枚举类型
	*  @param name  终端名称
	*  @return com.yirun.framework.core.enums.TerminalTypes
	*  @date                    ：2018/11/13
	*  @author                  ：zc.ding@foxmail.com
	*/
	public static TerminalTypes getTerminalTypes(String name) {
		for (TerminalTypes terminalTypes : TerminalTypes.values()) {
			if (terminalTypes.getName().equals(name)) {
				return terminalTypes;
			}
		}
		return null;
	}
}
