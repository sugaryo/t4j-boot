package sugaryo.t4jboot.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.module.MediaTweetCrawller;
import sugaryo.t4jboot.app.module.NyappiCall;
import sugaryo.t4jboot.app.module.RandomHolder;
import sugaryo.t4jboot.app.module.SelfRetweet;
import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.data.values.MediaTweet;


@RestController
@RequestMapping("t4j-boot/api")
public class WebApiController {
	
	private static final Logger log = LoggerFactory.getLogger( WebApiController.class );
	
	@Autowired
	MediaTweetCrawller mediatweets;

	@Autowired
	NyappiCall nyappi;
	
	@Autowired
	SelfRetweet self;
	
	@Autowired ConfigSet config;

	
	@GetMapping("images/tweet/{id}")
	public String imagesByTweetId( @PathVariable long id ) throws Exception {
		
		String[] urls = mediatweets.crawlMediaUrls( id );
		
		String json = JsonMapper.stringify( urls );
		return json;
	}
	
	@GetMapping("images/tweet/{id}/detail")
	public String imagesByTweetIdDetail( @PathVariable long id ) throws Exception {
		
		List<MediaTweet> medias = this.mediatweets.crawlMediaTweets( id );
		
		String json = JsonMapper.stringify( medias );
		return json;
	}

	@GetMapping("nyappi") 
	public void nyappi() {
		this.nyappi.call();
	}
	
	@GetMapping("self-rt") 
	public void selfrt() {
		this.self.retweets();
	}
	
	@RequestMapping("self-rt/{id}") 
	public String selfrt(@PathVariable long id) {
		
		var rt = this.self.retweet( id );
		
		return JsonMapper.stringify( rt );
	}
	

	private static final String CRLF = System.getProperty("line.separator");

	@GetMapping("test/random/{count}")
	String testRandomHolder(@PathVariable int count) {
	
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
	
	
	@RequestMapping("test/ex")
	String testException() throws Exception {
		
		throw new RuntimeException( "エラー発生" );
	}
	
	@ExceptionHandler
	private ResponseEntity<String> onError( Exception ex ) {
		
		log.error( ex.getMessage(), ex );
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String json = JsonMapper.map()
				.put( "message", "API エラー" )
				.put( "detail", ex.getMessage() )
				.put( "status", status.value() )
				.stringify();
		
		return new ResponseEntity<String>( json, status );
	}
}
