package sugaryo.t4jboot.app.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.data.values.MediaTweet;
import twitter4j.MediaEntity;
import twitter4j.Paging;
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
		
		List<MediaTweet> medias = new ArrayList<>();
		Status tweet = this.get( id );
		
		mediasUrlFrom( medias, tweet );

		return medias;
	}
	
	private Status get( final long id ) {
		
		try {
			return twitter.showStatus( id );
		}
		// 検査例外はRuntimeでくるんでポイ。
		catch ( TwitterException ex ) {
			throw new RuntimeException( ex );
		}
	}
	
	public List<MediaTweet> mediasOfList( final long listId ) {
		
		final int pBegin = 1; //FIXME：後でパラメータ化。
		final int pages = 10; //FIXME：後でパラメータ化。
		
		List<MediaTweet> medias = new ArrayList<>();
		
		for ( int i = 0; i < pages; i++ ) {
			
			final int page = pBegin + i;

			log.info( "page {} of list[{}]", page, listId );
			
			var paging = new Paging( page ); //FIXME：ページング操作もあとでパラメータ化
			var tweets = this.lst( listId, paging );
			for ( Status tweet : tweets ) {
				mediasUrlFrom( medias, tweet );
			}
		}
		return medias;
	}
	
	private List<Status> lst( final long listId, Paging paging ) {
		
		try {
			return this.twitter.getUserListStatuses( listId, paging );
		}
		// 検査例外はRuntimeでくるんでポイ。
		catch ( TwitterException ex ) {
			throw new RuntimeException( ex );
		}
	}
	
	private static void mediasUrlFrom( final List<MediaTweet> medias, final Status tweet ) {

		// ツイートに関するメタデータを取得。
		final String userName = tweet.getUser().getScreenName();
		final long userId     = tweet.getUser().getId();
		final long tweetId    = tweet.getId();

		// ツイートに含まれるメディアURLを取得。
		final MediaEntity[] mediaEntities = tweet.getMediaEntities();
		for ( MediaEntity entity : mediaEntities ) {
			final String url = entity.getMediaURLHttps();
			
			MediaTweet media = new MediaTweet( userName, userId, tweetId, url );
			
			medias.add( media );
			log.debug( "media : {}", media );
		}
	}


	
	public void tweet( String message ) {
		
		try {
			log.debug( "tweet:[{}]", message );
			twitter.updateStatus( message );
		}
		// 検査例外はRuntimeでくるんでポイ。
		catch ( TwitterException ex ) {
			throw new RuntimeException( ex );
		}
		
	}
	
	public Status retweet( final long id ) {
		
		try {
			log.debug( "▼リツイート▼" );
			// 既にリツイート済みの場合はいったん解除する。
			if ( this.get( id ).isRetweetedByMe() ) {
				log.debug( "◇リツイートの解除" );
				this.twitter.unRetweetStatus( id );
			}
			
			// 解除したうえでリツイートする。
			var rt = this.twitter.retweetStatus( id );
			log.debug( "▲リツイート▲[{} > {} : {}]",
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
