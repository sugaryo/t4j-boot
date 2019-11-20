package sugaryo.t4jboot.app.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
import sugaryo.t4jboot.data.values.MediaTweet;

@Component
public class MediaTweetCrawller {
	
	private final TwitterApiCall twitter;
	
	public MediaTweetCrawller( @Autowired TwitterApiCall twitter ) {
		this.twitter = twitter;
	}
	
	public List<MediaTweet> crawlMediaTweets( final long id ) {
		return this.twitter.medias( id );
	}
	public String[] crawlMediaUrls( final long id ) {
		return this.twitter.medias( id ).stream().map( x -> x.url ).toArray( String[]::new );
	}
}
