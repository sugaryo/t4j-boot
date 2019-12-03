package sugaryo.t4jboot.app.module;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class RandomHolder {
	
	private static final Logger log = LoggerFactory.getLogger( RandomHolder.class );
	
	// FIXME：あとでプロパティにしておく。
	private static final int PREVENT_LEVEL = 3;
	private static final int HIT_RATIO = 10;
	
	private AtomicInteger prevent = new AtomicInteger(0);
		
	private Random random = new Random( new Date().getTime() );
	
	public boolean rand() {

		log.debug( "★ prevent-counter : {}", this.prevent.get() );
		
		if ( 0 < this.prevent.get() ) {
			// 抑止中の場合は抑止カウンタをデクリメントしておわり。
			log.debug( "◇連続ヒットの抑止中。" );
			this.prevent.decrementAndGet();
			return false;
		}
		
		int r = this.random.nextInt( 100 );

		log.debug( "  - random : {}", r );

		boolean hit = r % HIT_RATIO == 0;
		if ( hit ) {
			// 抑止カウンタ
			log.debug( "◇連続ヒットの抑止を設定。" );
			this.prevent.set( PREVENT_LEVEL );
		}
		return hit;
	}
}
