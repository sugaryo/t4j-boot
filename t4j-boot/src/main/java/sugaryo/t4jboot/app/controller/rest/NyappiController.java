package sugaryo.t4jboot.app.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.module.NyappiCall;



@RestController
@RequestMapping({
	"api/nyappi", })
public class NyappiController {
	
	private static final Logger log = LoggerFactory.getLogger( NyappiController.class );

	@Autowired
	ConfigSet config;
	
	@Autowired
	NyappiCall nyappi;
	
	
	@PostMapping("call") 
	public void nyappi() {
		this.nyappi.call();
	}
	
}
