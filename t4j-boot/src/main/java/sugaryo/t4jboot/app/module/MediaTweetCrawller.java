package sugaryo.t4jboot.app.module;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
import sugaryo.t4jboot.data.values.MediaTweet;
//import twitter4j.MediaEntity;
//import twitter4j.Status;
import sugaryo.t4jboot.app.api.Dummy.*;

@Component
public class MediaTweetCrawller {
	
	private final TwitterApiCall twitter;
	
	public MediaTweetCrawller( @Autowired TwitterApiCall twitter ) {
		this.twitter = twitter;
	}
	
	public List<MediaTweet> byTweet( final long tweetid ) {
		
		List<MediaTweet> medias = new ArrayList<>(); 
		
		Status tweet = this.twitter.tweet( tweetid );
		mediasUrlFrom( medias, tweet );
		
		return medias;
	}
	
	
	public static final class ListPagingDefault {
		public static final int BEGIN_PAGE = 1;
		public static final int PAGE_SIZE = 5;
	}
	
	public List<MediaTweet> byList( final long listId ) {
		return this.byList( listId, ListPagingDefault.BEGIN_PAGE );
	}
	public List<MediaTweet> byList( final long listId, final int begin ) {
		return this.byList( listId, begin, ListPagingDefault.PAGE_SIZE );
	}
	public List<MediaTweet> byList( final long listId, final int begin, final int pages ) {

		List<MediaTweet> medias = new ArrayList<>();
		
		List<Status> tweets = this.twitter.list( listId, begin, pages );
		for ( Status tweet : tweets ) {
			mediasUrlFrom( medias, tweet );
		}
		
		return medias;
	}
	
	// TODO：メソッド名がイケてないので後でリネーム。
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
		}
	}
}
