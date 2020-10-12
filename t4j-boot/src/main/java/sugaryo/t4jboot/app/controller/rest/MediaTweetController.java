package sugaryo.t4jboot.app.controller.rest;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.module.MediaTweetCrawller;
import sugaryo.t4jboot.common.utility.StringUtil;
import sugaryo.t4jboot.data.values.MediaTweet;



@RestController
@RequestMapping("t4j-boot/api")
public class MediaTweetController {
	
	private static final Logger log = LoggerFactory.getLogger( MediaTweetController.class );

	@Autowired
	ConfigSet config;
	
	@Autowired
	MediaTweetCrawller mediatweets;
	
	
	
	// ■TweetID 指定でのメディアURL情報取得

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
	
	
	// ■ListID 指定でのメディアURL情報取得

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
	
	// ■ListID 指定 ＋ページング指定 でのメディアURL情報取得
	
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
}
