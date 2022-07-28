package sugaryo.t4jboot.app.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.controller.rest.strategy.ResponseParameterStrategy;
import sugaryo.t4jboot.app.module.DisplayNameChanger;
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
	
	@Autowired
	DisplayNameChanger displayname;
	
	//FIXME : NyyappiCall モジュールの方が戻り値対応出来てないので、まだ使えない。
	@Autowired
	ResponseParameterStrategy response;
	
	
	@PostMapping("call") 
	public void nyappi() {
		this.nyappi.call();
	}
	
	
	@PostMapping("display-name/shuffle")
	public String shuffleDisplayName() {
		return this.displayname.shuffle();
	}
	@PostMapping("display-name/change/{code}")
	public String changeDisplayName(@PathVariable int code) {
		return this.displayname.change(code);
	}
	
	
}
