package sugaryo.t4jboot.app.module;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;

@Component
public class NyappiCall {
	
	private final RandomHolder random;
	
	private final TwitterApiCall twitter;
	
	public NyappiCall(
			@Autowired RandomHolder random,
			@Autowired TwitterApiCall twitter ) {
		this.random = random;
		this.twitter = twitter;
	}
	
	public void call() {
		
		final DateTimeFormatter MDH = DateTimeFormatter.ofPattern( "M月d日の HH時 " );
		final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime adjust = now.plusMinutes( 1 );
		
		String hour = adjust.format( MDH ); // cron誤差対応のため１分加算。
		String timestamp = now.format( YMDHMS );
		
		final boolean is04H = 4 == adjust.getHour();
		String message = (is04H ? "なーるーよーじーーーー！！"
				: "にゃっぴー。" + "\r\n" + hour + "ですよー。")
				+ "\r\n"
				+ "\r\n" + timestamp + " #にゃっぴこーる";
		this.twitter.tweet( message );
	}
	
}
