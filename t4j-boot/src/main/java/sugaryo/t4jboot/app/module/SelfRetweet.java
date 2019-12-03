package sugaryo.t4jboot.app.module;

import static sugaryo.t4jboot.common.utility.ThreadUtil.sleep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
import sugaryo.t4jboot.app.config.ConfigSet;
import twitter4j.Status;

@Component
public class SelfRetweet {
	
	private final TwitterApiCall twitter;
	private final ConfigSet config;
	
	public SelfRetweet(
			@Autowired TwitterApiCall twitter,
			@Autowired ConfigSet config ) {
		this.twitter = twitter;
		this.config = config;
	}
	
	public Status retweet( final long id ) {
		return this.twitter.retweet( id );
	}
	
	public void retweets() {
		
		final long[] ids    = config.nyappi.selfrt.ids;
		final long interval = config.nyappi.selfrt.interval;
		
		for ( final long id : ids ) {
			this.twitter.retweet( id );
			sleep( interval );
		}
	}
}
