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
@RequestMapping({
	"api/media/list",
	"api/image/list",
})
public class MediaListController {
	
	private static final Logger log = LoggerFactory.getLogger( MediaListController.class );

	@Autowired
	ConfigSet config;
	
	@Autowired
	MediaTweetCrawller mediatweets;
	
	
	
	// ■ListID 指定でのメディアURL情報取得

	@GetMapping("{id}")
	public List<MediaTweet> imgByList( @PathVariable long id ) {
		
		List<MediaTweet> medias = this.mediatweets.byList( id );
		return medias;
	}
	@GetMapping("{id}/url")
	public String[] imgUrlByList( @PathVariable long id ) {
		
		String[] urls = this.mediatweets.byList( id )
				.stream()
				.map( x -> x.url )
				.toArray( String[]::new );

		return urls;
	}
	@GetMapping("{id}/plain-text")
	public String imgMetadataByList( @PathVariable long id ) {
		
		String[] metadata = this.mediatweets.byList( id )
				.stream()
				.map( x -> x.metadata() )
				.toArray( String[]::new );
		
		String plaintext = StringUtil.join( "\n", metadata );
		log.debug( plaintext );
		return plaintext;
	}
	
	
	
	// ■ListID 指定 ＋ページング指定 でのメディアURL情報取得
	
	@GetMapping({
			"{id}/page/{p}",
			"{id}/page/{p}/{n}"
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
			"{id}/url/page/{p}",
			"{id}/url/page/{p}/{n}"
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
			"{id}/plain-text/page/{p}",
			"{id}/plain-text/page/{p}/{n}"
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
		
		String plaintext = StringUtil.join( "\n", metadata );
		log.debug( plaintext );
		return plaintext;
	}
}
