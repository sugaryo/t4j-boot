package sugaryo.t4jboot.app.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.controller.rest.strategy.ResponseParameterStrategy;
import sugaryo.t4jboot.app.module.NyappiCall;
import sugaryo.t4jboot.app.module.RandomHolder;
import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.common.utility.RandomIdIterator;



@RestController
@RequestMapping({
	"api/test",
	"test",
})
public class TestApiController {
	
	private static final Logger log = LoggerFactory.getLogger( TestApiController.class );
	
	@Autowired
	ConfigSet config;
	
	@Autowired
	NyappiCall nyappi;
	
	@Autowired
	ResponseParameterStrategy response;
	
	
	@GetMapping("random-nyappi")
	public String test_random_nyappi() {
		var kind = NyappiCall.NyappiTweetKind.random();
		return this.nyappi.messageOf( kind );
	}
	
	@GetMapping("json")
	public String test_json() {

		return JsonMapper.map()
				.put( "id", UUID.randomUUID() )
				.put( "name", "testdata" )
				.put( "value", 123 )
				.stringify( this.response.pretty() );
	}
	
	
	@GetMapping("random/{count}")
	public String testRandomHolder( @PathVariable int count ) {
		
		// ここではDIコンテナ管理しているRandomHolderとは別にテスト実行したいので普通にnewする。
		var random = new RandomHolder( this.config );
		
		List<String> lines = new ArrayList<>();
		{			
			var buff = new StringBuilder();
			for ( int n = 1; n <= count; n++ ) {
				
				buff.append( random.rand() ? "●" : "○" );
				
				if ( 0 == n % 10 ) {
					// 一行確定
					lines.add( buff.toString() );
					
					// バッファリセット
					buff.setLength( 0 );
				}
			}
			// バッファに残りがあれば確定。
			if ( 0 < buff.length() ) {
				lines.add( buff.toString() );
			}
		}
		
		return this.response.stringify( lines ); 
	}
	
	@GetMapping("random-id-iterator/{s}/{n}")
	public String testRandomIdIterator( 
			@PathVariable int s, 
			@PathVariable int n ) {
		
		int[] index = RandomIdIterator.indexer( s, n );
		
		log.info( "総数 {} - 抽出 {} : {}", s, n, JsonMapper.stringify( index ) );
		
		return JsonMapper.map()
				.nest( "conditions" )
					.put( "s", s )
					.put( "n", n )
				.peel()
				.put( "index", index )
				.stringify( this.response.pretty() );
	}
	
	
	@RequestMapping("ex")
	String testException() throws Exception {
		throw new RuntimeException( "エラー発生" );
	}
}
