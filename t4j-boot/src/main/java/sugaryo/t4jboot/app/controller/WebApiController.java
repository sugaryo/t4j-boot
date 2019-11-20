package sugaryo.t4jboot.app.controller;

import java.util.List;

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

import sugaryo.t4jboot.app.module.MediaTweetCrawller;
import sugaryo.t4jboot.app.module.NyappiCall;
import sugaryo.t4jboot.app.module.RandomHolder;
import sugaryo.t4jboot.app.module.SelfRetweet;
import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.common.utility.ThreadUtil;
import sugaryo.t4jboot.data.values.MediaTweet;


@RestController
@RequestMapping("t4j-boot/api")
public class WebApiController {
	
	private static final Logger log = LoggerFactory.getLogger( WebApiController.class );
	
	@Autowired
	MediaTweetCrawller mediatweets;
	
	@GetMapping("images/tweet/{id}")
	public String imagesByTweetId( @PathVariable long id ) throws Exception {
		
		String[] urls = mediatweets.crawlMediaUrls( id );
		
		String json = JsonMapper.stringify( urls );
		return json;
	}
	
	@GetMapping("images/tweet/{id}/detail")
	public String imagesByTweetIdDetail( @PathVariable long id ) throws Exception {
		
		List<MediaTweet> medias = this.mediatweets.crawlMediaTweets( id );
		
		String json = JsonMapper.stringify( medias );
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
	
	@Autowired
	NyappiCall nyappi;
	
	@Autowired
	SelfRetweet selfrt;

	@RequestMapping("nyappi") 
	private void nyappi() {
		this.nyappi.call();
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
		this.selfrt.rtHimiruIllust();
	}
	private void rt_note() {
		this.selfrt.rtCurryNote();
	}
	private void rt_qiita() {
		this.selfrt.rtQiitaSpringBoot();
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
			this.selfrt.retweet( id );
			ThreadUtil.sleep( 1000 );
		}
	}
	
	@RequestMapping("self-rt/{id}") 
	public String selfrt(@PathVariable long id) {
		
		var rt = this.selfrt.retweet( id );
		
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
