package sugaryo.t4jboot.data.values;

import java.text.MessageFormat;

public class MediaTweet {
	public final String userName;
	public final String userId;
	public final String tweetId;
	public final String url;
	
	public MediaTweet( String userName, String userId, String tweetId, String url ) {
		this.userName = userName;
		this.userId = userId;
		this.tweetId = tweetId;
		this.url = url;
	}
	
	public MediaTweet( String userName, long userId, long tweetId, String url ) {
		this.userName = userName;
		this.userId = String.valueOf( userId );
		this.tweetId = String.valueOf( tweetId );
		this.url = url;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format( "{0}\t{1}\t{2}\t{3}",
				this.userName,
				this.userId,
				this.tweetId,
				this.url );
	}
}
