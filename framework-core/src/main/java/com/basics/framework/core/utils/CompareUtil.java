package com.basics.framework.core.utils;

import java.math.BigDecimal;

/**
 * 数字比较器工具类
 * @author 	 zc.ding
 * @since 	 2017年5月2日
 * @version  1.1
 */
public interface CompareUtil {

	/**
	 *  num > 0
	 *  @param num  数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean gtZero(BigDecimal num){
		return num != null && num.compareTo(BigDecimal.ZERO) > 0;
	}
	
	/**
	 *  num >= 0
	 *  @param num  数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean gteZero(BigDecimal num){
		return num != null && num.compareTo(BigDecimal.ZERO) >= 0;
	}
	
	/**
	 *  num < 0
	 *  @param num  数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean ltZero(BigDecimal num){
		return num != null && num.compareTo(BigDecimal.ZERO) < 0;
	}
	
	/**
	 *  num <= 0
	 *  @param num  数字
	 *  @return         : boolean 
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean lteZero(BigDecimal num){
		return num != null && num.compareTo(BigDecimal.ZERO) <= 0;
	}
	
	/**
	 *  num = 0
	 *  @param num  数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean eZero(BigDecimal num){
		return num != null && num.compareTo(BigDecimal.ZERO) <= 0;
	}
	
	/**
	 *  比较first 是否与 second 相等
	 *  @param first    数字
	 *  @param second   数字
	 *  @return         : boolean 
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean eq(BigDecimal first, double second){
		return first != null && first.compareTo(BigDecimal.valueOf(second)) == 0;
 	}
	
	/**
	 *  比较first 是否与 second 相等
	 *  @param first    数字
	 *  @param second   数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean eq(BigDecimal first, BigDecimal second){
		return first != null && second != null && first.compareTo(second) == 0;
	}
	
	/**
	 *  first > second
	 *  @param first    数字
	 *  @param second   数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean gt(BigDecimal first, BigDecimal second){
		return first != null && second != null && first.compareTo(second) > 0;
	}
	
	/**
	 *  first > second
	 *  @param first    数字
	 *  @param second   数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean gt(BigDecimal first, double second){
		return first != null && first.compareTo(BigDecimal.valueOf(second)) > 0;
	}
	
	/**
	 *  first > second
	 *  @param first    数字
	 *  @param second   数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean gt(double first, BigDecimal second){
		return second != null && BigDecimal.valueOf(first).compareTo(second) > 0;
	}
	
	/**
	 *  first >= second
	 *  @param first    数字
	 *  @param second   数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean gte(BigDecimal first, BigDecimal second){
		return first != null && second != null && first.compareTo(second) >= 0;
	}
	
	/**
	 *  first >= second
	 *  @param first    数字
	 *  @param second   数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean gte(BigDecimal first, double second){
		return first != null && first.compareTo(BigDecimal.valueOf(second)) >= 0;
	}
	
	/**
	 *  first >= second
	 *  @param first    数字
	 *  @param second   数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean gte(double first, BigDecimal second){
		return second != null && BigDecimal.valueOf(first).compareTo(second) >= 0;
	}
	
	
	/**
	 *  判断num是否是100的整数倍
	 *  @param num  数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean residue(BigDecimal num){
		return num != null && eZero(num.remainder(new BigDecimal(100))) && gtZero(num);
	}
	
	/**
	 *  判断num是否是100的整数倍
	 *  @param num  数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean residue(int num){
		return (num % 100) == 0 && num > 0;
	}
	
	/**
	 *  判断num是否是100的整数倍
	 *  @param num  数字
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean residue(double num){
		return (num % 100) == 0 && num > 0;
	}
	
	/**
	 *  判断num % divisor是否整除,且num > 0
	 *  @param num  数字
	 *  @param divisor  被除数
	 *  @return         : boolean 
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean residue(BigDecimal num, BigDecimal divisor){
		return num != null && divisor != null && eZero(num.remainder(divisor)) && gtZero(num);
	}
	
	/**
	 *  判断num % divisor是否整除,且num > 0
	 *  @param num      数字
	 *  @param divisor  被除数
	 *  @return         : boolean
	 *  @author         : zc.ding@foxmail.com
	 */
	static boolean residue(double num, double divisor){
		return eZero(BigDecimal.valueOf(num).remainder(BigDecimal.valueOf(divisor))) && gtZero(BigDecimal.valueOf(num));
	}
	
	
	public static void main(String[] args) {
		BigDecimal first = new BigDecimal(1);
		BigDecimal second = new BigDecimal(2);
		System.out.println(gtZero(first));
		System.out.println(gtZero(first.multiply(BigDecimal.valueOf(-1))));
		System.out.println(gte(first, second));
		System.out.println("-100 % 20 是否整除" + residue(new BigDecimal("-100")));
		System.out.println("100 % 20 是否整除" + residue(new BigDecimal("100"), new BigDecimal("20")));
		System.out.println(BigDecimal.valueOf(100).negate());
	}
}
