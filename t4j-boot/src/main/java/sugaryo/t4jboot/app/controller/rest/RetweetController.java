package sugaryo.t4jboot.app.controller.rest;

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

@RestController
@RequestMapping("t4j-boot/api")
public class RetweetController {
	
	private static final Logger log = LoggerFactory.getLogger( RetweetController.class );

	@Autowired
	ConfigSet config;
	
	@Autowired
	MediaTweetCrawller mediatweets;
	
	@Autowired
	NyappiCall nyappi;
	
	@Autowired
	SelfRetweet self;
	

	
	
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
	
	
	@Deprecated
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
}
