package sugaryo.t4jboot.app.controller.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.config.TweetData;
import sugaryo.t4jboot.app.module.MediaTweetCrawller;
import sugaryo.t4jboot.app.module.NyappiCall;
import sugaryo.t4jboot.app.module.SelfRetweet;
import sugaryo.t4jboot.common.utility.JsonMapper;


@RestController
@RequestMapping({
	"api/retweet",
	"api/rt", 
})
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
	

	
	@GetMapping("category")
	public String categories(
			@RequestParam(required = false) Optional<String> verbose,
			@RequestParam(required = false) Optional<String> v) {
		
		// -verbose
		if( verbose.isPresent() || v.isPresent() )
		{
			final var map = JsonMapper.map();
			final var categories = map.nest( "categories" );
			TweetData.each()
					.forEach( ( x ) ->
					{
						categories.put( x.name, x.ids.length );
					} );
			return map.stringify();
		}
		// normal
		else {
			return JsonMapper.stringify( TweetData.names() );
		}		
	}
	
	@PostMapping("category/{category}")
	public void rt_category( @PathVariable String category ) {

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
	@PostMapping("category/{category}/{size}")
	public void rt_category( @PathVariable String category, @PathVariable int size ) {

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
	
	@PostMapping("id/{id}")
	public String rt_id( @PathVariable long id,
			@RequestParam(required = false) Optional<String> verbose,
			@RequestParam(required = false) Optional<String> v ) {
		
		var rt = this.self.retweet( id );
		
		// -verbose 
		if ( verbose.isPresent() || v.isPresent() ) {
			
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
		// normal
		else {
			return JsonMapper.stringify( rt );
		}	
	}
}
