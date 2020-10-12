package sugaryo.t4jboot.app.controller.rest;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.config.TweetData;
import sugaryo.t4jboot.app.module.MediaTweetCrawller;
import sugaryo.t4jboot.app.module.NyappiCall;
import sugaryo.t4jboot.app.module.SelfRetweet;
import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.common.utility.StringUtil;
import sugaryo.t4jboot.data.values.MediaTweet;

@RestController
@RequestMapping("t4j-boot/api")
public class WebApiController {
	
	// TODO： -verbose オプション持ちのエンドポイントには、ついでに ?pretty オプションも付けたい。
	// TODO：というか共通化できそうなら全部 ?pretty オプション欲しいよね？
	
	private static final Logger log = LoggerFactory.getLogger( WebApiController.class );

	@Autowired
	ConfigSet config;
	
	@Autowired
	MediaTweetCrawller mediatweets;
	
	@Autowired
	NyappiCall nyappi;
	
	@Autowired
	SelfRetweet self;
	

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
	

	@RequestMapping("nyappi") 
	public void nyappi() {
		this.nyappi.call();
	}
	
	
	@GetMapping("retweets/category")
	public String categories() {
		
		return JsonMapper.stringify( TweetData.names() );
	}
	
	@GetMapping({
		"retweets/category-v",
		"retweets/category-verbose",
	})
	public String categories_verbose() {
		
		final var map = JsonMapper.map();
		final var categories = map.nest( "categories" );
		TweetData.each()
				.forEach( ( x ) ->
				{
					categories.put( x.name, x.ids.length );
				} );
		return map.stringify();
	}
	
	
	@PostMapping("retweets")
	public void selfrts() {
		
		// category/all と同じ。
		this.self.retweets();
	}
	
	@PostMapping("retweets/category/{category}")
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
	@PostMapping("retweets/category/{category}/{size}")
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
	
	@PostMapping("retweet/{id}")
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
	@PostMapping({
		"retweet-verbose/{id}",
		"retweet-v/{id}",
	})
	public String selfrt_verbose( @PathVariable long id ) {
		
		var rt = this.self.retweet( id );
		
		return JsonMapper.stringify( rt );
	}
	

	
	@RequestMapping("ex")
	String testException() throws Exception {
		throw new RuntimeException( "エラー発生" );
	}
}
