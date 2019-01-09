package com.zhou.controller.shiro;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

//@RestControllerAdvice
@ControllerAdvice
public class ShiroErrorController {
	/**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {}
    
    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "Magical Sam");
    }
    
    /**
     * 全局异常捕捉处理
     * 优先级问题，同一种异常处理在本类的优先级要高，会优先使用本类的其次是全局的。
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String,Object> errorHandler(Exception ex) {
        Map<String,Object> map = new HashMap<>();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }
    
  /*  @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String,Object> errorHandler(Exception ex) {
        Map<String,Object> map = new HashMap<>();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }*/
}
