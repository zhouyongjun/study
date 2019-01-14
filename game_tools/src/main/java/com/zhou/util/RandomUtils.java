package com.zhou.util;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ���������
 * @author  
 *
 */
public class RandomUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RandomUtils.class);
	
	/**
	 * �� 0����������ָ��ֵ����������֮����ȷֲ��� int ֵ
	 * @param max
	 * @return
	 */
	public static int random(int max){
		return ThreadLocalRandom.current().nextInt(max);
	}
	
	/**
	 * ���������Сֵ
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max){
		if(max - min <= 0) return min;
		return min + ThreadLocalRandom.current().nextInt(max - min + 1);
	}
	
	/**
	 * ��һ����˹��
	 * @return
	 */
	public static double nextGaussian() {
		return ThreadLocalRandom.current().nextGaussian();
	}
	
	/**
     * ���ݼ���  �����Ƿ����� 
     * @param probability	ȡֵ��Χ
     * @param ratio			����������ֱ�10000���ٷֱ�100��
     * @return true ����, false û����
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
     * gailv/probability ������ʽ
     * @param ratio				����������ֱ�10000���ٷֱ�100��
     * @param probability		ȡֵ��Χ
     * @return true�����ɣ� false��û����
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
     * ���ݼ���  �����Ƿ����� 
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
     * ������0-maxcout֮������������ʱ��С��num
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
      * ������0-maxcout֮������������ʱ��С��num
      * @param num
      * @return
      */
    public static boolean isGenerateToBoolean(int num,int maxcout){
    	double count=Math.random()*maxcout;

//    	System.out.println("����========"+ count);
//    	System.out.println("����========"+ num);
//    	System.out.println("����<����");
//    	System.out.println(count<num);
    	if(count<num){
    		return true;	
    	}
    	return false;
    }
   /**
    * �������min��max֮�������ֵ ����min max
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
    * @param probs �����ܻ��ʷ������
    * @return
    */
	public static int randomIndexByProb(List<Integer> probs) {
		if (probs == null || probs.isEmpty()) {
			return -1;
		}
		try{
			int total = 0;
			for (Integer prob : probs) {
				//������˻�addһ��null��ȥ...
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
			logger.error("������ʴ���"+probs.toString(),e);
		}
		return -1;
	}
	
	/**
	 * �����ĳ����Χ�ڵ�x����ͬ��ֵ����
	 * �������ڴ�Χ����ֵ��ֻȡ���м���������������1W����ֵҪ�����9K��ֵ����ô�ܶ��Ե������ǿ�Ҳ���������⺯����
	 * @param min		�������Сֵ��0�Ļ����������0������1���Ͽ�ʼ�ɣ�
	 * @param max		��������ֵ
	 * @param count	���������ֵ��
	 * @return {@link int[]} ���������ĳ����Χ�ڵ�x����ͬ��ֵ
	 */
	public static int[] ramdomNums(int min, int max, int count) {
		if (min <= 0) {
			min = 1;
		}
		
		// �����Χ�ڵ�ֵ < ��Ҫ���������������ȫ�����ط�Χ�ڵ�ֵ�������������������
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
			
			//�ж�random�Ƿ����ָ���������� 
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


