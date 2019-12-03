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
	private static final int PREVENT_LEVEL = 2;
	private static final int HIT_RATIO = 50;
	private static final int MISS_LIMIT = 10;
	
	// FIXME：Atomicカウンタが二種類だと間でズレが生じる危険があるので後で修正。
	private AtomicInteger prevent = new AtomicInteger(0);
	private AtomicInteger missing = new AtomicInteger(0);
		
	private Random random = new Random( new Date().getTime() );
	
	public boolean rand() {
		
		log.debug( "----------------------------------------" );
		log.debug( "★ prevent-counter : {}", this.prevent.get() );
		
		// ■連続ヒット抑止中の場合：
		if ( 0 < this.prevent.get() ) {
			// 抑止中は抑止カウンタをデクリメントしておわり。
			// ※ 抑止中の強制falseも連続ミスにカウントする。
			log.info( "◇ 制御乱数[prevent] - 連続ヒットの抑止中。" );
			log.debug( "  - prevent : {}", this.prevent.decrementAndGet() ); // --prevent;
			log.debug( "  - missing : {}", this.missing.incrementAndGet() ); // ++missing;
			return false;
		}

		// 抑止中でない場合は乱数を引いてヒットテスト。
		final int r = this.random.nextInt( 100 );
		final boolean hit = r % HIT_RATIO == 0;
		log.debug( "  - random : {}", r );

		// ■ヒットした場合：
		if ( hit ) {
			// 抑止カウンタを設定・連続ミスカウンタをリセットしておわり。
			log.info( "◇ 制御乱数[hit]" );
			this.prevent.set( PREVENT_LEVEL );
			this.missing.set( 0 );
			return true;
		}

		// ヒットしなかった場合、連続ミスカウンタをインクリメントして連続上限判定。
		final int miss = this.missing.incrementAndGet();
		if ( MISS_LIMIT <= miss ) {
			// ミス限界を超えていた場合は強制ヒット。
			log.info( "◇ 制御乱数[force-hit]" );
			this.prevent.set( PREVENT_LEVEL );
			this.missing.set( 0 );
			return true;
		}
		// ヒットせず、連続ミスカウンタが限界に達していない場合は単なるミスヒット。
		else {
			log.info( "◇ 制御乱数[miss]" );
			log.debug( "  - missing : {}", miss );
			return false;
		}
	}
}
