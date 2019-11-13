package sugaryo.t4jboot.app.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sugaryo.t4jboot.app.module.TwitterApiCall;
import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.common.utility.ThreadUtil;

@RestController
@RequestMapping("t4j-boot/api")
public class WebApiController {
	
	private static final Logger log = LoggerFactory.getLogger( WebApiController.class );
	
	@Autowired
	TwitterApiCall twitter;
	
	@RequestMapping("images/tweet/{id}")
	public String imagesByTweetId( @PathVariable long id ) throws Exception {
		
		var medias = this.twitter.medias( id );
		
		var urls = medias.stream()
				.map( x -> x.url )
				.toArray( String[]::new );
		
		String json = JsonMapper.stringify( urls );
		return json;
	}
	
	@RequestMapping("images/tweet/{id}/detail")
	public String imagesByTweetIdDetail( @PathVariable long id ) throws Exception {
		
		var medias = this.twitter.medias( id );
		
		var details = medias.stream()
				.map( x -> x.toString() )
				.toArray( String[]::new );
		
		String json = JsonMapper.stringify( details );
		return json;
	}
	
  
	@Autowired 
	RandomHolder random;
	
	@GetMapping("test/random/{count}")
	public String testRandomHolder(@PathVariable int count) {
		
		var sb = new StringBuilder();
		
		String crlf = "";
		for ( int n = 0; n < count; n++ ) {
			
			if ( 0 == n % 10 ) {
				sb.append( crlf );
				crlf = "\r\n"; // 初回だけ無視したいので遅延代入。
			}
			sb.append( this.random.rand() ? "●" : "○" );
		}
		return sb.toString();
	}
	

	@Scheduled(cron = "0 0 * * * *") // 一瞬早い事があるので１秒補正。
	public void cron() {
		if ( random.rand() ) {
			this.retweet();
		} 
		else {
			this.nyappi();
		}
	}
	
	@RequestMapping("test")
	public void test() {
		if ( random.rand() ) {
			this.retweet();
		} 
		else {
			this.nyappi();
		}
	}
	

	@RequestMapping("nyappi") 
	private void nyappi() {
		
		final DateTimeFormatter MDH = DateTimeFormatter.ofPattern( "M月d日の HH時 " );
		final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime adjust = now.plusMinutes( 1 );
		
		String hour = adjust.format( MDH ); // cron誤差対応のため１分加算。
		String timestamp = now.format( YMDHMS );
		
		final boolean is04H = 4 == adjust.getHour();
		String message = 
				( is04H ? "なーるーよーじーーーー！！" 
						: "にゃっぴー。" + "\r\n" + hour + "ですよー。" )
				+ "\r\n"
				+ "\r\n" + timestamp + " #にゃっぴこーる";
		this.twitter.tweet( message );
	}
	
	private int counter = 0;
	
	@RequestMapping("retweet")
	private void retweet() {
		int s = ++counter % 3;
		switch ( s ) {
			case 0:
				rt_himiru();
				break;
				
			case 1:
				rt_note();
				break;
				
			case 2:
				rt_qiita();
				break;
				
				default:
		}
	}
	private void rt_himiru() {
		
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
	private void rt_note() {
		
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
	private void rt_qiita() {
		
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
	

	@Scheduled(cron = "0 0 9 * 1-5 *")
	public void selfrt_weekday() {
		// 平日は９時に。
		selfrt();
	}
	@Scheduled(cron = "0 0 12 * 6-7 *")
	public void selfrt_holiday() {
		// 土日は１２時に。
		selfrt();
	}
	@RequestMapping("self-rt") 
	private void selfrt() {
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
	
	@RequestMapping("self-rt/{id}") 
	public String selfrt(@PathVariable long id) {
		
		var rt = this.twitter.retweet( id );
		
		return JsonMapper.stringify( rt );
	}
	
	
	
	@RequestMapping("test/ex")
	public String testException() throws Exception {
		
		throw new RuntimeException( "エラー発生" );
	}
	
	@ExceptionHandler
	private ResponseEntity<String> onError( Exception ex ) {
		
		log.error( ex.getMessage(), ex );
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String json = JsonMapper.map()
				.put( "message", "API エラー" )
				.put( "detail", ex.getMessage() )
				.put( "status", status.value() )
				.stringify();
		
		return new ResponseEntity<String>( json, status );
	}
}
