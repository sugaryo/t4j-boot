package sugaryo.t4jboot.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import sugaryo.t4jboot.common.utility.RandomIdIterator;
import sugaryo.t4jboot.common.utility.StringUtil;
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

	

	// TweetID 指定でのメディアURL情報取得

	@GetMapping("images/tweet/{id}")
	public List<MediaTweet> imgByTweet( @PathVariable long id ) throws Exception {
		
		List<MediaTweet> medias = this.mediatweets.byTweet( id );
		return medias;
	}
	@GetMapping("images-url/tweet/{id}")
	public String[] imgUrlByTweet( @PathVariable long id ) throws Exception {
		
		String[] urls = this.mediatweets.byTweet( id )
				.stream()
				.map( x -> x.url )
				.toArray( String[]::new );
		
		return urls;
	}
	@GetMapping("images-metadata/tweet/{id}")
	public String imgMetadataByTweet( @PathVariable long id ) throws Exception {
		
		String[] metadata = this.mediatweets.byTweet( id )
				.stream()
				.map( x -> x.metadata() )
				.toArray( String[]::new );

		String planetext = StringUtil.join( "\n", metadata );
		log.debug( planetext );
		return planetext;
	}
	
	
	// ListID 指定でのメディアURL情報取得

	@GetMapping("images/list/{id}")
	public List<MediaTweet> imgByList( @PathVariable long id ) {
		
		List<MediaTweet> medias = this.mediatweets.byList( id );
		return medias;
	}
	@GetMapping("images-url/list/{id}")
	public String[] imgUrlByList( @PathVariable long id ) {
		
		String[] urls = this.mediatweets.byList( id )
				.stream()
				.map( x -> x.url )
				.toArray( String[]::new );

		return urls;
	}
	@GetMapping("images-metadata/list/{id}")
	public String imgMetadataByList( @PathVariable long id ) {
		
		String[] metadata = this.mediatweets.byList( id )
				.stream()
				.map( x -> x.metadata() )
				.toArray( String[]::new );
		
		String planetext = StringUtil.join( "\n", metadata );
		log.debug( planetext );
		return planetext;
	}
	
	// ListID 指定 ＋ページング指定 でのメディアURL情報取得
	
	@GetMapping({
			"images/list/{id}/page/{p}",
			"images/list/{id}/page/{p}/{n}"
	})
	public List<MediaTweet> imgByPagingList( 
			@PathVariable final long id, 
			@PathVariable final int p, 
			@PathVariable(required = false) Optional<Integer> n ) {

		List<MediaTweet> medias = n.isPresent()
				? this.mediatweets.byList( id, p, n.get() )
				: this.mediatweets.byList( id, p );
		
		return medias;
	}
	@GetMapping({
		"images-url/list/{id}/page/{p}",
		"images-url/list/{id}/page/{p}/{n}"
	})
	public String[] imgUrlByPagingList( 
			@PathVariable final long id, 
			@PathVariable final int p, 
			@PathVariable(required = false) Optional<Integer> n ) {
		
		List<MediaTweet> medias = n.isPresent()
				? this.mediatweets.byList( id, p, n.get() )
				: this.mediatweets.byList( id, p );

		String[] urls = medias
				.stream()
				.map( x -> x.url )
				.toArray( String[]::new );

		return urls;
	}
	@GetMapping({
		"images-metadata/list/{id}/page/{p}",
		"images-metadata/list/{id}/page/{p}/{n}"
	})
	public String imgMetadataByPagingList( 
			@PathVariable final long id, 
			@PathVariable final int p, 
			@PathVariable(required = false) Optional<Integer> n ) {

		List<MediaTweet> medias = n.isPresent()
				? this.mediatweets.byList( id, p, n.get() )
				: this.mediatweets.byList( id, p );
		
		String[] metadata = medias
				.stream()
				.map( x -> x.metadata() )
				.toArray( String[]::new );
		
		String planetext = StringUtil.join( "\n", metadata );
		log.debug( planetext );
		return planetext;
	}
	

	@GetMapping("nyappi") 
	public void nyappi() {
		this.nyappi.call();
	}
	
	@GetMapping("retweets")
	public void selfrts() {
		
		// category/all と同じ。
		this.self.retweets();
	}
	
	@GetMapping("retweets/category/{category}")
	public void selfrts( @PathVariable String category ) {

		log.info( "★ /api/retweets/category/{}", category );

		// category/all は別名定義。
		if ( "all".equals( category ) ) {
			this.self.retweets();
		} 
		// category を指定してリツイート。
		else {
			this.self.retweets( category );
		}
	}
	@GetMapping("retweets/category/{category}/{size}")
	public void selfrts( @PathVariable String category, @PathVariable int size ) {

		log.info( "★ /api/retweets/category/{}/{}", category, size );

		// category/all は別名定義。
		if ( "all".equals( category ) ) {
			this.self.retweets( size );
		} 
		// category,size を指定してリツイート。
		else {
			this.self.retweets( category, size );
		}
	}
	
	@RequestMapping("retweet/{id}")
	public String selfrt( @PathVariable long id ) {
		
		var rt = this.self.retweet( id );
		
		String json = JsonMapper.map()
				.put( "id", rt.getId() )
				.put( "name", rt.getUser().getName() )
				.put( "text", rt.getText() )
				.nest( "retweeted" )
						.put( "fv-count", rt.getRetweetedStatus().getFavoriteCount() )
						.put( "rt-count", rt.getRetweetedStatus().getRetweetCount() )
				.peel()
				.stringify();
		return json;
	}
	@RequestMapping({
		"retweet-verbose/{id}",
		"retweet-v/{id}",
	})
	public String selfrt_verbose( @PathVariable long id ) {
		
		var rt = this.self.retweet( id );
		
		return JsonMapper.stringify( rt );
	}
	
	
	
	@GetMapping(path = "test/random-nyappi")
	public String test_random_nyappi() {
		var kind = NyappiCall.NyappiTweetKind.random();
		return this.nyappi.messageOf( kind );
	}
	
	
	@GetMapping(path = "test/json")
	public String test_json_n() {
		
		return test_json( false );
	}
	@GetMapping(path = "test/json", params = "pretty")
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
	
	@GetMapping("test/random-id-iterator/{s}/{n}")
	String testRandomIdIterator(@PathVariable int s, @PathVariable int n) {

		int[] index = RandomIdIterator.indexer( s, n );

		String json = JsonMapper.stringify( index );
		log.info( "総数 {} - 抽出 {} : {}", s, n, json );
		return json;
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
