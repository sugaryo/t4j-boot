package sugaryo.t4jboot.app.controller;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


// FIXME：controllerからmoduleに移動。
@Component
public class RandomHolder {
	
	private static final Logger log = LoggerFactory.getLogger( RandomHolder.class );
	
	// FIXME：あとでプロパティにしておく。
	private static final int PREVENT_LEVEL = 3;
	private static final int HIT_RATIO = 10;
	
	private int prevent = 0; //TODO：一応AtomicIntegerにするべきじゃない？
	
	private Random random = new Random( new Date().getTime() );
	
	public boolean rand() {
		
		if ( 0 < this.prevent ) {
			// 抑止中の場合は抑止カウンタをデクリメントしておわり。
			log.debug( "◇連続ヒットの抑止中。" );
			this.prevent--;
			return false;
		}
		
		int r = this.random.nextInt( 100 );
		boolean hit = r % HIT_RATIO == 0;
		
		if ( hit ) {
			// 抑止カウンタ
			log.debug( "◇抑止を設定。" );
			this.prevent = PREVENT_LEVEL;
		}
		return hit;
	}
}
