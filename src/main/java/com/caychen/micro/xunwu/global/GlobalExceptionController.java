package com.caychen.micro.xunwu.global;

import com.caychen.micro.xunwu.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Author:       Caychen
 * Date:         2019/9/7
 * Desc:
 */
@Controller
public class GlobalExceptionController implements ErrorController {

	@Autowired
	private ErrorAttributes errorAttributes;

	@Override
	public String getErrorPath() {
		return Constants.ERROR_PATH;
	}

	/**
	 * web页错误
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = Constants.ERROR_PATH, produces = "text/html")
	public String errorPageHandler(HttpServletRequest request, HttpServletResponse response) {
		int status = response.getStatus();

		if (status == 403 || status == 404 || status == 500) {
			return status + "";
		}

		return "index";
	}

	/**
	 * 除了web页面所有的api错误
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = Constants.ERROR_PATH)
	@ResponseBody
	public BaseResponse errorApiHandler(HttpServletRequest request) {

		ServletWebRequest requestAttributes = new ServletWebRequest(request);
		Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(requestAttributes, false);

		int status = getStatus(request);
		return BaseResponse.error(status, String.valueOf(attributes.getOrDefault("message", "error")));
	}

	private int getStatus(HttpServletRequest request) {
		Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
		return status != null ? status : 500;
	}
}
