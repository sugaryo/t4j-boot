package sugaryo.t4jboot.app.controller.view;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sugaryo.t4jboot.app.module.MediaTweetCrawller;
import sugaryo.t4jboot.data.values.MediaTweet;

@Controller
@RequestMapping("nyappi/view/pad")
public class PadPageController {
	
	private static final Logger log = LoggerFactory.getLogger( PadPageController.class );
	
	private enum DropKind {
		
		/** 回復 */
		HEART("h", "回復", 0),
		/** 火 */
		FIRE("r", "火", 1),
		/** 水 */
		WATER("b", "水", 2),
		/** 木 */
		WOOD("g", "木", 3),
		/** 光 */
		LIGHT("y", "光", 4),
		/** 闇 */
		DARK("p", "闇", 5);
		
		private final String code;
		private final String name;
		private final int index;
		
		private DropKind( String code, String name, int index ) {
			this.code = code;
			this.name = name;
			this.index = index;
		}


		public static DropKind from(final String code) {
			var kinds = DropKind.values();
			for ( DropKind kind : kinds ) {
				if ( kind.code.equals( code ) ) return kind;
			}
			return null;
		}
	}
	
	
	@RequestMapping()
	public String page( Model model ) {
		
		// 初期表示
		boolean[] select = { false, false, false, false, false, false };
		select[DropKind.FIRE.index] = true; // 初期選択：火
		model.addAttribute( "drop", select );
		model.addAttribute( "time", 12 );
		model.addAttribute( "span", 1 );
		
		return "pad-tool";
	}
	
	@PostMapping("roulette")
	public String roulette( Model model
			, @RequestParam("drop") String drop
			, @RequestParam("puzzle_time") double time
			, @RequestParam("roulette_span" ) double span ) {

		var kind = DropKind.from( drop );
		log.info( "drop:{} ({})", drop, kind.name );
		log.info( "time:{}", time );
		log.info( "span:{}", span );

		// 取り敢えずパラメータ伝搬
		boolean[] select = { false, false, false, false, false, false };
		select[kind.index] = true;
		model.addAttribute( "drop", select );
		model.addAttribute( "time", time );
		model.addAttribute( "span", span );
		
		// TODO：計算処理
		return "pad-tool";
	}
}
