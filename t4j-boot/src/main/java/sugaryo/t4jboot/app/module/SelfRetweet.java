package sugaryo.t4jboot.app.module;

import static sugaryo.t4jboot.common.utility.ThreadUtil.sleep;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.config.TweetData;
import sugaryo.t4jboot.common.utility.RandomIdIterator;
import twitter4j.Status;

@Component
public class SelfRetweet {
	
	private static final Logger log = LoggerFactory.getLogger( SelfRetweet.class );
	
	private static final int RETWEET_LIMIT = 20;
	
	private final TwitterApiCall twitter;
	private final ConfigSet config;
	
	// ctor
	
	public SelfRetweet(
			@Autowired TwitterApiCall twitter,
			@Autowired ConfigSet config ) {
		this.twitter = twitter;
		this.config = config;
	}
	
	// retweet
	
	public Status retweet( final long id ) {
		
		log.info( "SelfRetweet retweet by id[{}].", id );
		
		return this.twitter.retweet( id );
	}
	
	
	// retweets
	
	public List<Status> retweets() {

		log.info( "SelfRetweet retweets preset all." );
		
		final long[] ids    = TweetData.union();
		final long interval = config.nyappi.selfrt.interval;
		return this.execute( suppress(ids), interval );
	}
	public List<Status> retweets(final int limit) {
		
		log.info( "SelfRetweet retweets preset all / limit[{}].", limit );
		
		final long[] ids    = TweetData.union();
		final long interval = config.nyappi.selfrt.interval;
		return this.execute( suppress(ids, limit), interval );
	}
	public List<Status> retweets(final String category) {

		log.info( "SelfRetweet retweets preset of category[{}].", category );
		
		final long[] ids    = TweetData.of( category );
		final long interval = config.nyappi.selfrt.interval;
		return this.execute( suppress(ids), interval );
	}
	public List<Status> retweets(final String category, final int limit) {

		log.info( "SelfRetweet retweets preset of category[{}] / limit[{}].", category, limit );
		
		final long[] ids    = TweetData.of( category );
		final long interval = config.nyappi.selfrt.interval;
		return this.execute( suppress(ids, limit), interval );
	}
	
	
	private List<Status> execute(
		final long[] ids,
		final long interval ) {
		
		List<Status> tweets = new ArrayList<>();
		
		log.info( "  - ids.length : {}", ids.length );
		for ( final long id : ids ) {
			var tweet = this.twitter.retweet( id );
			tweets.add( tweet );
			sleep( interval );
		}
		
		return tweets;
	}
	
	
	/**
	 * 要素数制限（ランダム抽出）
	 * 
	 * @param ids RTするツイートのID配列
	 * @return {@code ids} から デフォルト値 {@value #RETWEET_LIMIT} で制限したID配列  
	 * 
	 * @see sugaryo.t4jboot.common.utility.RandomIdIterator
	 */
	private static final long[] suppress(final long[] ids) {
		return suppress( ids, RETWEET_LIMIT );
	}
	/**
	 * 要素数制限（ランダム抽出）
	 * 
	 * @param ids  RTするツイートのID配列
	 * @param size 制限する要素数（デフォルト値 {@value #RETWEET_LIMIT}）
	 * @return {@code ids} から 指定の要素数 {@code size} で制限したID配列
	 * 
	 * @see sugaryo.t4jboot.common.utility.RandomIdIterator
	 */
	private static final long[] suppress(final long[] ids, final int size) {
		
		final int n = size < RETWEET_LIMIT ? size : RETWEET_LIMIT;
		
		final long[] suppressed = RandomIdIterator.iterate( ids, n );
		log.debug( "suppress ids." );
		log.debug( "    - before : {}", ids.length );
		log.debug( "    - after  : {} [{}]", suppressed.length, suppressed );
		return suppressed;
	}
}
