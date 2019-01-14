package com.zhou.util;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具
 * @author  
 *
 */
public class RandomUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RandomUtils.class);
	
	/**
	 * 在 0（包括）和指定值（不包括）之间均匀分布的 int 值
	 * @param max
	 * @return
	 */
	public static int random(int max){
		return ThreadLocalRandom.current().nextInt(max);
	}
	
	/**
	 * 包含最大最小值
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max){
		if(max - min <= 0) return min;
		return min + ThreadLocalRandom.current().nextInt(max - min + 1);
	}
	
	/**
	 * 下一个高斯数
	 * @return
	 */
	public static double nextGaussian() {
		return ThreadLocalRandom.current().nextGaussian();
	}
	
	/**
     * 根据几率  计算是否生成 
     * @param probability	取值范围
     * @param ratio			比例（如万分比10000，百分比100）
     * @return true 生成, false 没生成
     */
    public  static boolean isGenerate(int probability,int ratio)
    {
    	if(ratio==0) {
    		ratio=1000;
		}
    	
    	int random_seed = ThreadLocalRandom.current().nextInt(ratio+1);
    	
    	return probability >= random_seed;
    }
    
    /**
     * 
     * gailv/probability 比率形式
     * @param ratio				比例（如万分比10000，百分比100）
     * @param probability		取值范围
     * @return true：生成， false：没生成
     */
    public  static boolean isGenerate2(int ratio,int probability)
    {
    	if (ratio == probability) {
    		return true;
    	}
    	if (probability==0) {
    		return false;
    	}
    	
    	int random_seed = ThreadLocalRandom.current().nextInt(ratio);
    	return random_seed + 1 <= probability;
    }
    
    /**
     * 根据几率  计算是否生成 
     * @param probability
     * @return
     */
    public  static boolean defaultIsGenerate(int probability)
    {
    	int globalIntValue = 10000;
		int random_seed=ThreadLocalRandom.current().nextInt(globalIntValue);
    	 return probability>=random_seed;
    }

    /**
     * 返回在0-maxcout之间产生的随机数时候小于num
     * @param num
     * @return
     */
   public static boolean isGenerateToBoolean(float num,int maxcout){
   	double count=Math.random()*maxcout;

   	if(count<num){
   		return true;	
   	}
   	return false;
   }
    
     /**
      * 返回在0-maxcout之间产生的随机数时候小于num
      * @param num
      * @return
      */
    public static boolean isGenerateToBoolean(int num,int maxcout){
    	double count=Math.random()*maxcout;

//    	System.out.println("计算========"+ count);
//    	System.out.println("传入========"+ num);
//    	System.out.println("计算<传入");
//    	System.out.println(count<num);
    	if(count<num){
    		return true;	
    	}
    	return false;
    }
   /**
    * 随机产生min到max之间的整数值 包括min max
    * @param min
    * @param max
    * @return
    */
   public static int randomIntValue(int min,int max){
	   return (int)(Math.random() * (double)(max - min + 1)) + min;  
   }
   
   
   public static float randomFloatValue(float min,float max){
	   return (float)(Math.random() * (double)(max-min)) + min;  
   }
   
   public static <T> T randomItem(Collection<T> collection){
	   if(collection==null||collection.size()==0){
		   return null;
	   }
	   int t=(int) (collection.size()*Math.random());
	   int i=0;
	   for(Iterator<T> item=collection.iterator();i<=t&&item.hasNext();){
		   T next=item.next();
		   if(i==t){
			   return next;
		   }
		   i++;
	   }
	   return null;
   }

	public static <T> List<T> randomItems(Collection<T> elements,int length){
		int size = elements.size();
		if(length> size){
			length = size;
		}
		Object [] newArray = new Object[size];
		newArray = elements.toArray(newArray);
		for (int i = 0; i < size; i++) {
			int random = RandomUtils.random(size);
			Object swapPtr = newArray[i];
			newArray[i] = newArray[random];
			newArray[random] = swapPtr;
		}
		List<T> result = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			result.add((T)newArray[i]);
		}
		return result;

	}
   
   /**
    * 
    * @param probs 根据总机率返回序号
    * @return
    */
	public static int randomIndexByProb(List<Integer> probs) {
		if (probs == null || probs.isEmpty()) {
			return -1;
		}
		try{
			int total = 0;
			for (Integer prob : probs) {
				//真的有人会add一个null进去...
				if (prob != null) {
					total += prob;
				}
			}

			int random = random(total);
			int index = 0;
			for (Integer prob : probs) {
				if (prob != null) {
					if(prob > random){
						return index;
					}else{
						random -= prob;
					}
				}
				index++;
			}
		}catch (Exception e) {
			logger.error("计算机率错误"+probs.toString(),e);
		}
		return -1;
	}
	
	/**
	 * 随机出某个范围内的x个不同的值出来
	 * （适用于大范围的数值，只取其中几个的情况，如果像1W个数值要随机出9K个值的那么密度性的情况，强烈不建议调用这函数）
	 * @param min		随机的最小值（0的话不会随机出0来，从1以上开始吧）
	 * @param max		随机的最大值
	 * @param count	随机出几个值呢
	 * @return {@link int[]} 随机出来的某个范围内的x个不同的值
	 */
	public static int[] ramdomNums(int min, int max, int count) {
		if (min <= 0) {
			min = 1;
		}
		
		// 如果范围内的值 < 需要随机出的总数，则全部返回范围内的值（还随机个蛋啊！！）
		int gad = max - min + 1;
		if (gad <= count) {
			int index = 0;
			int randomNums[] = new int[gad]; 
			for (int i = min; i <= max; i++) {
				randomNums[index] = i;
				index ++;
			}
			
			return randomNums;
		}
		
		int randomNums[] = new int[count]; 
		
		int index = 0;
		boolean flag = true;
		
		while (index < count) {
			flag = true;
			int random = random(min, max);
			
			//判断random是否存在指定的数组中 
			for (int i = 0 ; i < randomNums.length ; i++){
				if(random == randomNums[i]){
					flag = false;
					break;
				}
			}
			
			if (flag) {
				randomNums[index++] = random;
			}
		}
		
		return randomNums;
	}

	public static void main(String[] args) {
//		Integer[] ss = {4000, 10000};
//		
//		int su = 0;
//		int fail = 0;
//		for (int i = 0; i < 100; i++) {
//			boolean is = isGenerate2(ss[1], ss[0]);
//			
//			if (is) {
//				su ++;
//			}
//			else {
//				fail ++;
//			}
//		}
//		
//		System.out.println(su  + "||" + fail);
		
//		for (int i = 0; i < 10; i++) {
//			System.out.println(isGenerate(ss[0], ss[1]));
//		}
	}
	
}


