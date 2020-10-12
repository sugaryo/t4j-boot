package sugaryo.t4jboot.app.controller.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.module.NyappiCall;
import sugaryo.t4jboot.app.module.RandomHolder;
import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.common.utility.RandomIdIterator;



@RestController
@RequestMapping({
	"t4jb/api",
	"t4j-boot/api",
	"nyappi/api", })
public class TestApiController {
	
	private static final Logger log = LoggerFactory.getLogger( TestApiController.class );
	
	@Autowired
	ConfigSet config;
	
	@Autowired
	NyappiCall nyappi;
	
	
	
	@GetMapping(path = "random-nyappi")
	public String test_random_nyappi() {
		var kind = NyappiCall.NyappiTweetKind.random();
		return this.nyappi.messageOf( kind );
	}
	
	@GetMapping(path = "json")
	public String test_json_n() {
		
		return test_json( false );
	}
	
	@GetMapping(path = "json", params = "pretty")
	public String test_json_p() {
		
		return test_json( true );
	}
	
	private String test_json( boolean pretty ) {
		
		@SuppressWarnings("serial")
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put( "id", UUID.randomUUID() );
				put( "name", "testdata" );
				put( "value", 123 );
			}
		};
		
		return JsonMapper.stringify( map, pretty );
	}
	
	
	
	private static final String CRLF = System.getProperty( "line.separator" );
	
	@GetMapping("random/{count}")
	String testRandomHolder( @PathVariable int count ) {
		
		// ここではDIコンテナ管理しているRandomHolderとは別にテスト実行したいので普通にnewする。
		var random = new RandomHolder( this.config );
		
		var sb = new StringBuilder();
		String crlf = "";
		for ( int n = 0; n < count; n++ ) {
			
			if ( 0 == n % 10 ) {
				sb.append( crlf );
				crlf = CRLF; // 初回だけ無視したいので遅延代入。
			}
			sb.append( random.rand() ? "●" : "○" );
		}
		sb.append( crlf ); // 最後に改行入れておかないとcurlの結果が変になるので末尾改行しておく。
		
		return sb.toString();
	}
	
	@GetMapping("random-id-iterator/{s}/{n}")
	String testRandomIdIterator( 
			@PathVariable int s, 
			@PathVariable int n ) {
		
		int[] index = RandomIdIterator.indexer( s, n );
		
		String json = JsonMapper.stringify( index );
		log.info( "総数 {} - 抽出 {} : {}", s, n, json );
		return json;
	}
	
	
	@RequestMapping("ex")
	String testException() throws Exception {
		throw new RuntimeException( "エラー発生" );
	}
}
