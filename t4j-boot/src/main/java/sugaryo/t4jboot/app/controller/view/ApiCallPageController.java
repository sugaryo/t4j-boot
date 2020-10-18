package sugaryo.t4jboot.app.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("nyappi/view/api-call")
public class ApiCallPageController {
	
	@RequestMapping()
	public String page() {
		return "api-call";
	}
	
	
}
