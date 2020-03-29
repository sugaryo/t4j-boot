package sugaryo.t4jboot.app.api;

import java.text.MessageFormat;
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
	
	public List<Status> list( final long listId, final int begin, final int pages ) {
		
		// begin のガード句
		if ( begin < 1 ) throw new IllegalArgumentException( MessageFormat.format( 
				"開始ページの指定（{0}）が少なすぎます。"
				+ "開始ページ（ page : 1-based-number）は１以上を指定して下さい。"
				, begin )
		);
		
		// pages のガード句
		if ( 10 < pages ) throw new IllegalArgumentException( MessageFormat.format( 
				"ページ数の指定（{0}）が多すぎます。"
				+ "API制限対策のため、１０ページ以上のリクエストは分割して、時間をあけて実行して下さい。"
				, pages )
		);
		
		
		List<Status> tweets = new ArrayList<>();
		
		for ( int i = 0; i < pages; i++ ) {
			
			final int page = begin + i;

			log.info( "page {} of list[{}]", page, listId );
			
			var paging = new Paging( page );
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
