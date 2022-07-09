package sugaryo.t4jboot.app.controller.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.controller.rest.strategy.ResponseParameterStrategy;
import sugaryo.t4jboot.app.module.MediaTweetCrawller;
import sugaryo.t4jboot.common.utility.StringUtil;
import sugaryo.t4jboot.data.values.MediaTweet;


@RestController
@RequestMapping({
	"api/media/tweet",
	"api/image/tweet",
})
public class MediaTweetController {
	
	private static final Logger log = LoggerFactory.getLogger( MediaTweetController.class );

	@Autowired
	ConfigSet config;
	
	@Autowired
	MediaTweetCrawller mediatweets;
	
	@Autowired
	ResponseParameterStrategy response;
	
	
	// ■TweetID 指定でのメディアURL情報取得

	@GetMapping("{id}")
	public String imgByTweet( @PathVariable long id ) throws Exception {
		
		List<MediaTweet> medias = this.mediatweets.byTweet( id );
		return this.response.stringify( medias );
	}	
	@GetMapping("{id}/url")
	public String imgUrlByTweet( @PathVariable long id ) throws Exception {
		
		String[] urls = this.mediatweets.byTweet( id )
				.stream()
				.map( x -> x.url )
				.toArray( String[]::new );
		
		return this.response.stringify( urls );
	}
	@GetMapping("{id}/plain-text")
	public String imgMetadataByTweet( @PathVariable long id ) throws Exception {
		
		String[] metadata = this.mediatweets.byTweet( id )
				.stream()
				.map( x -> x.metadata() )
				.toArray( String[]::new );

		String plaintext = StringUtil.join( "\n", metadata );
		log.debug( plaintext );
		return plaintext;
	}
	
}
