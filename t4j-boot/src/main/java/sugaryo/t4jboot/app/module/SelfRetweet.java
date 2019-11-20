package sugaryo.t4jboot.app.module;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
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

	
	//FIXME：これ系はenumストラテジに変更。
	public void rtHimiruIllust() {
		
		final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
		
		LocalDateTime now = LocalDateTime.now();
		String timestamp = now.format( YMDHMS );
		
		String message = "にゃっぴコールはキャンセルされました。"
				+ "\r\n" + "かわりに 固定ツイート を宣伝RTします。"
				+ "\r\n"
				+ "\r\n" + timestamp + " #にゃっぴこーる"
				+ "\r\n"
				+ "\r\n" + "https://twitter.com/ellnorePZDR297/status/920625245786013696";
		this.twitter.tweet( message );
	}
	
	public void rtCurryNote() {
		
		final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
		
		LocalDateTime now = LocalDateTime.now();
		String timestamp = now.format( YMDHMS );
		
		String message = "にゃっぴコールはキャンセルされました。"
				+ "\r\n" + "かわりに カレーnote を宣伝します!!"
				+ "\r\n"
				+ "\r\n" + timestamp + " #にゃっぴこーる"
				+ "\r\n"
				+ "\r\n" + "https://sugaryo1224.hatenablog.com/entry/2019/07/15/040443";
		this.twitter.tweet( message );
	}
	
	public void rtQiitaSpringBoot() {
		
		final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
		
		LocalDateTime now = LocalDateTime.now();
		String timestamp = now.format( YMDHMS );
		
		String message = "にゃっぴコールはキャンセルされました。"
				+ "\r\n" + "かわりに Qiita記事 を宣伝します。"
				+ "\r\n"
				+ "\r\n" + timestamp + " #にゃっぴこーる"
				+ "\r\n" + "SpringBootに入門する為の助走本（随時更新）"
				+ "\r\n" + "https://qiita.com/sugaryo/items/5695bfcc21365f429767 #Qiita";
		this.twitter.tweet( message );
	}
	
}
