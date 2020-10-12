package sugaryo.t4jboot.app.controller.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({
		"t4jb/view",
		"t4j-boot/view",
		"nyappi/view", })
public class ViewController {
	
	private static final Logger log = LoggerFactory.getLogger( ViewController.class );
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	
}
