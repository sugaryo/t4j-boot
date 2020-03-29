package sugaryo.t4jboot.app.module;

import static sugaryo.t4jboot.common.utility.ThreadUtil.sleep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.common.utility.RandomIdIterator;
import twitter4j.Status;

@Component
public class SelfRetweet {
	
	private static final Logger log = LoggerFactory.getLogger( SelfRetweet.class );
	
	
	private final TwitterApiCall twitter;
	private final ConfigSet config;
	
	public SelfRetweet(
			@Autowired TwitterApiCall twitter,
			@Autowired ConfigSet config ) {
		this.twitter = twitter;
		this.config = config;
	}
	
	public Status retweet( final long id ) {
		
		log.info( "SelfRetweet retweet by id[{}].", id );
		
		return this.twitter.retweet( id );
	}
	

	public void retweets() {

		log.info( "SelfRetweet retweets preset all." );
		
		final long[] ids    = config.nyappi.selfrt.union();
		final long interval = config.nyappi.selfrt.interval;
		this.execute( ids, interval );
	}
	public void retweets(final int limit) {
		
		log.info( "SelfRetweet retweets preset all / limit[{}].", limit );
		
		final long[] ids    = config.nyappi.selfrt.union();
		final long interval = config.nyappi.selfrt.interval;
		this.execute( suppress(ids, limit), interval );
	}
	public void retweets(final String category) {

		log.info( "SelfRetweet retweets preset of category[{}].", category );
		
		final long[] ids    = config.nyappi.selfrt.of( category );
		final long interval = config.nyappi.selfrt.interval;
		this.execute( ids, interval );
	}
	public void retweets(final String category, final int limit) {

		log.info( "SelfRetweet retweets preset of category[{}] / limit[{}].", category, limit );
		
		final long[] ids    = config.nyappi.selfrt.of( category );
		final long interval = config.nyappi.selfrt.interval;
		this.execute( suppress(ids, limit), interval );
	}

	//TODO：APIのレスポンス用に戻り値を用意したい。
	private void execute(
		final long[] ids,
		final long interval ) {
		
		log.info( "  - ids.length : {}", ids.length );
		for ( final long id : ids ) {
			this.twitter.retweet( id );
			sleep( interval );
		}
	}

	private static final long[] suppress(final long[] ids, final int size) {

		final long[] suppressed = RandomIdIterator.iterate( ids, size );
		log.debug( "suppress(ids, {})", size );
		log.debug( "    - before : {}", ids );
		log.debug( "    - after  : {}", suppressed );
		return suppressed;
	}
}
