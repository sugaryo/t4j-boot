package sugaryo.t4jboot.app.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
	
	// ■GET
	
	public Status tweet( final long id ) {
		
		Status tweet = this.get( id );
		return tweet;
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
	
	public List<Status> list( final long listId ) {
		
		final int pBegin = 1; //FIXME：後でパラメータ化。
		final int pages = 10; //FIXME：後でパラメータ化。
		
		
		List<Status> tweets = new ArrayList<>();
		
		for ( int i = 0; i < pages; i++ ) {
			
			final int page = pBegin + i;

			log.info( "page {} of list[{}]", page, listId );
			
			var paging = new Paging( page ); //FIXME：ページング操作もあとでパラメータ化
			tweets.addAll( this.lst( listId, paging ) );
		}
		return tweets;
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
	

	
	// ■POST
	
	public Status tweet( String message ) {
		
		try {
			log.debug( "▼tweet▼" );
			log.debug( "message : {}", message );
			var t = twitter.updateStatus( message );
			log.debug( "▲tweet▲[{}]", t.getId() );
			
			return t;
		}
		// 検査例外はRuntimeでくるんでポイ。
		catch ( TwitterException ex ) {
			throw new RuntimeException( ex );
		}
	}
	
	public Status retweet( final long id ) {
		
		try {
			log.debug( "▼retweet▼[]", id );
			// 既にリツイート済みの場合はいったん解除する。
			if ( this.get( id ).isRetweetedByMe() ) {
				log.debug( "◇リツイートの解除" );
				this.twitter.unRetweetStatus( id );
			}
			
			// 解除したうえでリツイートする。
			var rt = this.twitter.retweetStatus( id );
			log.debug( "message : {}", rt.getText() );
			log.debug( "▲retweet▲[{}]", rt.getId() );
			
			return rt;
		}
		// 検査例外はRuntimeでくるんでポイ。
		catch ( TwitterException ex ) {
			throw new RuntimeException( ex );
		}
	}
	
}
