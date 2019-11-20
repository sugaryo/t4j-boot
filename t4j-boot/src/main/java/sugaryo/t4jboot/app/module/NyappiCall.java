package sugaryo.t4jboot.app.module;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;

@Component
public class NyappiCall {
	
	private static final Logger log = LoggerFactory.getLogger( NyappiCall.class );
	
	private final RandomHolder random;
	
	private final TwitterApiCall twitter;
	
	public NyappiCall(
			@Autowired RandomHolder random,
			@Autowired TwitterApiCall twitter ) {
		this.random = random;
		this.twitter = twitter;
	}
	
	public enum NyappiTweetKind {
		
		QT_FIRE_MILLE_ILLUST {
			
			@Override
			protected String message() {
				final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
				
				LocalDateTime now = LocalDateTime.now();
				String timestamp = now.format( YMDHMS );
				
				String message = "にゃっぴコールはキャンセルされました。"
						+ "\r\n" + "かわりに 固定ツイート を宣伝RTします。"
						+ "\r\n"
						+ "\r\n" + timestamp + " #にゃっぴこーる"
						+ "\r\n"
						+ "\r\n" + "https://twitter.com/ellnorePZDR297/status/920625245786013696";
				return message;
			}
		},
		
		QT_CURRY_NOTE {
			
			@Override
			protected String message() {
	
				final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
				
				LocalDateTime now = LocalDateTime.now();
				String timestamp = now.format( YMDHMS );
				
				String message = "にゃっぴコールはキャンセルされました。"
						+ "\r\n" + "かわりに カレーnote を宣伝します!!"
						+ "\r\n"
						+ "\r\n" + timestamp + " #にゃっぴこーる"
						+ "\r\n"
						+ "\r\n" + "https://sugaryo1224.hatenablog.com/entry/2019/07/15/040443";
				return message;
			}
		},
		QT_QIITA_SPRING_BOOT {
			
			@Override
			protected String message() {
	
				final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
				
				LocalDateTime now = LocalDateTime.now();
				String timestamp = now.format( YMDHMS );
				
				String message = "にゃっぴコールはキャンセルされました。"
						+ "\r\n" + "かわりに Qiita記事 を宣伝します。"
						+ "\r\n"
						+ "\r\n" + timestamp + " #にゃっぴこーる"
						+ "\r\n" + "SpringBootに入門する為の助走本（随時更新）"
						+ "\r\n" + "https://qiita.com/sugaryo/items/5695bfcc21365f429767 #Qiita";
				return message;
			}
		},
		
		;
		protected abstract String message();
	}
	
	public void randomcall() {
		
		// 制御乱数でヒットしたら、にゃっぴキャンセルからの特殊コール。
		if ( this.random.rand() ) {
			log.info( "特殊にゃっぴこーる。" );
			
			NyappiTweetKind[] kinds = NyappiTweetKind.values();
			int nano13 = LocalDateTime.now().getNano() % 13;
			int i = nano13 % kinds.length;
			
			this.call( kinds[i] );
		} 
		// ヒットしなかった場合は、通常のにゃっぴこーる。
		else {
			log.info( "通常にゃっぴこーる。" );
			this.call();
		}
	}
	
	public void call( NyappiTweetKind kind ) {
		String message = kind.message();
		this.twitter.tweet( message );
	}
	
	public void call() {
		
		final DateTimeFormatter MDH = DateTimeFormatter.ofPattern( "M月d日の HH時 " );
		final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime adjust = now.plusMinutes( 1 );// cron誤差対応のため１分加算。
		
		String hour = adjust.format( MDH ); 
		String timestamp = now.format( YMDHMS );
		
		final boolean is04H = 4 == adjust.getHour();
		String message = (is04H 
						? "なーるーよーじーーーー！！"
						: "にゃっぴー。" + "\r\n" + hour + "ですよー。")
				+ "\r\n"
				+ "\r\n" + timestamp + " #にゃっぴこーる";
		this.twitter.tweet( message );
	}
}
