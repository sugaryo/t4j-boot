package sugaryo.t4jboot.app.controller;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sugaryo.t4jboot.app.module.MediaTweetCrawller;
import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.data.values.MediaTweet;

@Controller
@RequestMapping("t4j-boot/view")
public class ViewController {
	
	private static final Logger log = LoggerFactory.getLogger( ViewController.class );
	
	@Autowired
	MediaTweetCrawller mediatweet;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/images/tweet/id")
	public String imagesByTweet( Model model,
			@RequestParam(required = false, defaultValue = "0") long id ) {
		
		var sb = new StringBuilder();
		
		if ( 0 != id ) {
			
			var medias = this.mediatweet.byTweet( id );
			
			String ln = "";
			for ( MediaTweet media : medias ) {
				sb.append( ln );
				sb.append( media.metadata() );
				ln = "\n";
			}
		}
		
		String detail = sb.toString();
		model.addAttribute( "detail", detail );
		
		return "image-by-id";
	}
	
	@RequestMapping("/images/tweet/url")
	public String imagesByUrl( Model model,
			@RequestParam(required = false, defaultValue = "") String urls ) {
		
		// ■初期表示 or URL入力なし
		if ( urls.isEmpty() ) {
			model.addAttribute( "detail", "result text-area" );
		} 
		// ■URL入力あり
		else {
			// パターンチェックとかはしないで決め打ちで処理する。
			var lines = urls.split( "\r\n" );
			
			var ids = Stream.of( lines )
					.filter( x -> x.length() > 0 )
					.map( x ->
					{
						String[] token = x.split( "/" );
						String tail = token[token.length - 1];
						long id = Long.valueOf( tail );
						return id;
					} )
					.toArray( Long[]::new );
			
			var sb = new StringBuilder();
			String ln = "";
			for ( long id : ids ) {
				
				var medias = this.mediatweet.byTweet( id );
				for ( MediaTweet media : medias ) {

					sb.append( ln );
					sb.append( media.metadata() );
					ln = "\n";
				}
			}
			String detail = sb.toString();
			model.addAttribute( "detail", detail );
		}
		
		
		return "image-by-url";
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
