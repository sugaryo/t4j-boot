package sugaryo.t4jboot.app.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sugaryo.t4jboot.app.module.TagTweet;
import sugaryo.t4jboot.common.utility.JsonMapper;

@Controller
@RequestMapping("nyappi/view/tag-tweet")
public class TagTweetPageController {

	@Autowired
	TagTweet tagtweet;
	
	@RequestMapping()
	public String page(Model model) {

		model.addAttribute( "tags", "" );
		model.addAttribute( "content", "" );
		model.addAttribute( "result", "" );
		return "tag-tweet";
	}
	@PostMapping("preview")
	public String preview(Model model
			, @RequestParam(required = false, defaultValue = "") String tags
			, @RequestParam(required = false, defaultValue = "") String content ) {
		
		// ■入力なし
		if ( content.isEmpty() || tags.isEmpty() ) {
			
			model.addAttribute( "tags", tags );
			model.addAttribute( "content", "" );
			model.addAttribute( "result", "" );
		}
		// ■ぷれびゅー機能でメッセージ編集結果を受け取る。
		else {
			String message = tagtweet.preview( tags, content );
			
			model.addAttribute( "tags", tags );
			model.addAttribute( "content", content ); // ぷれびゅーの場合は入力内容をそのまま残す。
			model.addAttribute( "result", message );
		}
		
		return "tag-tweet";
	}
	@PostMapping("tweet")
	public String tweet(Model model
			, @RequestParam(required = false, defaultValue = "") String tags
			, @RequestParam(required = false, defaultValue = "") String content ) {
		
		// ■入力なし
		if ( content.isEmpty() || tags.isEmpty() ) {
			
			model.addAttribute( "tags", tags );
			model.addAttribute( "content", "" );
			model.addAttribute( "result", "" );
		}
		// ■Twitterに送信
		else {
			var tweet = tagtweet.post( tags, content );
			
			String result = JsonMapper.map()
					.put( "mode", "post" )
					.put( "id", tweet.getId() )
					.nest( "tweet" )
						.put( "text", tweet.getText() )
						.put( "created_at", tweet.getCreatedAt() )
					.peel()
					.stringify( true );
			
			model.addAttribute( "tags", tags );
			model.addAttribute( "content", "" ); // 送信したら次の処理のためにクリアして返す。
			model.addAttribute( "result", result );
		}
		
		return "tag-tweet";
	}
	
}
