package sugaryo.t4jboot.app.module;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.data.values.MediaTweet;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Component
public class TwitterApiCall {
	
	private static final Logger log = LoggerFactory.getLogger( TwitterApiCall.class );
	
	private Twitter twitter;
	
	@Deprecated // PostConstruct無くなるらしい。
	@PostConstruct
	private void ctor() {
		this.twitter = new TwitterFactory().getInstance();
	}
	
	
	public List<MediaTweet> medias( final long id ) {
		
		var tweet = this.getTweet( id );
		
		final String userName = tweet.getUser().getScreenName();
		final long userId = tweet.getUser().getId();
		final long tweetId = tweet.getId();
		
		List<MediaTweet> medias = new ArrayList<>();
		
		final MediaEntity[] mediaEntities = tweet.getMediaEntities();
		for ( MediaEntity entity : mediaEntities ) {
			final String url = entity.getMediaURLHttps();
			
			MediaTweet media = new MediaTweet( userName, userId, tweetId, url );
			
			medias.add( media );
			log.info( media.toString() );
		}
		return medias;
	}
	
	private Status getTweet( final long id ) {
		
		try {
			return twitter.showStatus( id );
		}
		// 検査例外はRuntimeでくるんでポイ。
		catch ( TwitterException ex ) {
			throw new RuntimeException( ex );
		}
	}

	public void tweet( String message ) {
		
		try {
			log.info( message );
			twitter.updateStatus( message );
		}
		// 検査例外はRuntimeでくるんでポイ。
		catch ( TwitterException ex ) {
			throw new RuntimeException( ex );
		}
		
	}
	
	public Status retweet( final long id ) {
		
		try {
			log.info( "▼リツイート▼" );
			// 既にリツイート済みの場合はいったん解除する。
			if ( this.getTweet( id ).isRetweetedByMe() ) {
				log.info( "◇リツイートの解除" );
				this.twitter.unRetweetStatus( id );
			}
			
			// 解除したうえでリツイートする。
			var rt = this.twitter.retweetStatus( id );
			log.info( "▲リツイート▲[{} > {} : {}]",
					id, 
					rt.getId(), 
					rt.getText() );
			
			return rt;
		}
		// 検査例外はRuntimeでくるんでポイ。 
		catch ( TwitterException ex ) {
			throw new RuntimeException( ex );
		}
	}

}
