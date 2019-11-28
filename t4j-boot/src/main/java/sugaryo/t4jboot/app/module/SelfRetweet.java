package sugaryo.t4jboot.app.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
import sugaryo.t4jboot.common.utility.ThreadUtil;
import twitter4j.Status;

@Component
public class SelfRetweet {

	private final TwitterApiCall twitter;

	public SelfRetweet( 
			@Autowired TwitterApiCall twitter ) {
		this.twitter = twitter;
	}
	
	public Status retweet(final long id) {
		return this.twitter.retweet( id );
	}

	public void retweets() {

		// TODO：あとでこれはDBかプロパティか何かに変える。
		long[] ids = {
				920625245786013696L,
				1171421232203538432L,
				1164938246050070528L,
				
				1137625774620471296L,
				1155808360224002049L,
				1171850493502447616L,
				1129740738860867590L,
				
				1194181882650357760L,
				1194537527924772865L,
		
		};
		
		for ( long id : ids ) {
			this.twitter.retweet( id );
			ThreadUtil.sleep( 1000 );
		}
	}
}
