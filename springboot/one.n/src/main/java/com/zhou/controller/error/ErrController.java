package com.zhou.controller.error;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrController implements ErrorController{
	private static final Logger logger = LoggerFactory.getLogger(ErrController.class);
	@Override
	public String getErrorPath() {
		return "error/error";
	}

	@RequestMapping
	public String error()
	{
		logger.error("出错啦！进入自定义错误控制器");
		return getErrorPath();
	}
}
