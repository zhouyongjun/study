package com.zyj.v1.ssh;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    private String strSource="";
    private String StrRe="";
    
    public String getStrSource() {
        return strSource;
    }
    public void setStrSource(String strSource) {
        this.strSource = strSource;
    }
    public String getStrRe() {
        return StrRe;
    }
    public void setStrRe(String strRe) {
        StrRe = strRe;
    }
    public String flixedReplace(String rep){
        return this.strSource.replaceAll(this.StrRe, rep);
    }
    /**
     * <li>Description:</li>
     * <li>Date:</li>
     * <li>Modify:</li>
     * <li>Version: 1.0</li>
     * @author Administrator
     * @param form
     * @param last
     * @return String
     */
    @SuppressWarnings("unchecked")
    public String splitReplace(String form,String last){
        String ss[]=this.getStrSource().split(this.getStrRe());
        Pattern p = Pattern.compile(this.getStrRe());
        Matcher m = p.matcher(this.getStrSource());
        LinkedHashMap<Integer, String> map=new LinkedHashMap<Integer,String>();
        int i=0;
        while(m.find()){
            map.put(i, form+m.group()+last);
            i++;
        }
        Iterator<?> iter=map.entrySet().iterator();
        StringBuffer tarStr=new StringBuffer();
        for(int j=0;j<ss.length;j++){
            if(!this.getStrSource().isEmpty()&&(!Character.isDigit(this.getStrSource().charAt(0))||j!=0)){
                tarStr.append(ss[j]);
            }
            if(iter.hasNext()){
                Map.Entry<Integer, String> entry=(Map.Entry<Integer, String>)iter.next();
                if(entry.getKey()==j){
                    tarStr.append(entry.getValue());
                }
            }
        }
        return tarStr.toString();
    }
    
    public static void main(String[] args) {
        RegexUtil re=new RegexUtil();
        re.setStrSource(" 2132 3213 adsd 12321 asdfsa dsdf 12 1313adfaf231321dfafda1141dfaffafdf");
        re.setStrRe("\\d+");
        System.out.println(" 2132 3213 adsd 12321 asdfsa dsdf 12 1313adfaf231321dfafda1141dfaffafdf");
        System.out.println(re.flixedReplace("hello"));
//        System.out.println(re.getStrSource().replaceAll("(\\w+)(\\d+)", "$1'$2"));
    }
}