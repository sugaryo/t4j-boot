package sugaryo.t4jboot.app.controller.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sugaryo.t4jboot.common.constants.pad.DropKind;
import sugaryo.t4jboot.data.pad.Roulette;
import sugaryo.t4jboot.data.values.DeciSecScore;

@Controller
@RequestMapping("nyappi/view/pad")
public class PadPageController {
	
	private static final Logger log = LoggerFactory.getLogger( PadPageController.class );
	
	@RequestMapping()
	public String page( Model model ) {
		
		// 初期表示
		boolean[] select = { false, false, false, false, false, false };
		select[DropKind.FIRE.index] = true; // 初期選択：火
		model.addAttribute( "drop", select );
		model.addAttribute( "time", 12 );
		model.addAttribute( "span", 1 );
		model.addAttribute( "calc", false );
		
		return "pad-tool";
	}
	
	@PostMapping("roulette")
	public String roulette( Model model
			, @RequestParam("drop") String drop
			, @RequestParam("puzzle_time") double time
			, @RequestParam("roulette_span" ) double span ) {

		var kind = DropKind.from( drop );

		// 取り敢えずパラメータ伝搬
		boolean[] select = { false, false, false, false, false, false };
		select[kind.index] = true;
		model.addAttribute( "drop", select );
		model.addAttribute( "time", time );
		model.addAttribute( "span", span );
		
		// 計算処理
		var parameter = new Roulette.Parameter( drop, time, span );
		var puzzle = this.calc( parameter );
		String[] drops = serialize( puzzle.getDrops() );
		
		// 計算結果を設定。
		model.addAttribute( "calc", true );
		model.addAttribute( "round", puzzle.getRounds() );
		model.addAttribute( "remtm", puzzle.getRemTime().seconds() );
		model.addAttribute( "drops", drops );
		
		return "pad-tool";
	}
	private Roulette.RoulettePuzzle calc( Roulette.Parameter parameter ) {
		
		final int time = parameter.time.score();
		final int span = parameter.span.score();
		
		final int turn = 6 * span; // ６色が一周するのに掛かる時間
		
		final int rounds = (int)(time / turn); // ルーレットの回転数 
		final int last = time % turn; // ルーレットの残り時間 
		
		final int remDrop = (int)(last / span); // 最終ターンで進ドロップ数
		final int remTime = last % span; // 最後に残った時間
				
		var drops = build( parameter.drop, rounds, remDrop, 0 != remTime );
		
		// 計算結果を返す。
		var result = new Roulette.RoulettePuzzle();
		result.setRounds( rounds );
		result.setRemDrops( remDrop );
		result.setRemTime( DeciSecScore.of( remTime ) );
		result.setDrops( drops );
		
		log.debug( "roulette-puzzle:【{}】", stringify( drops ) );
		
		return result;
	}
	
	private static List<DropKind> build( DropKind drop, int n, int x, boolean rem ) 
	{		
		var drops = new ArrayList<DropKind>();
		
		// 回転数掛けドロップ数
		// 最終ターンで進むドロップ数
		// あまり時間があればプラス１
		int total = (6 * n) + x + (rem ? 1 : 0);
		
		// 基準ドロップを設定。
		DropKind d = drop;
		
		// 基準ドロップから逆順でルーレット遷移させる。
		for ( int i = 0; i < total; i++ ) {
			drops.add( d );
			d = d.prev();
		}
		
		// 最終的な目的ドロップから逆順で詰めたので、リストをリバースして返す。
		Collections.reverse( drops );
		return drops;
	}
	
	private static String stringify( List<DropKind> drops ) {
		var sb = new StringBuilder();
		
		int i = 0;
		String separator = "";
		for ( DropKind drop : drops ) {
			if ( i++ % 6 == 0 ) {
				sb.append( separator );
				separator = " ";
			}
			sb.append( drop.name );
		}
		
		return sb.toString();
	}
	
	private static String[] serialize( List<DropKind> drops ) {
		return drops.stream()
				.map( x -> x.code )
				.toArray( String[]::new );
	}
}
