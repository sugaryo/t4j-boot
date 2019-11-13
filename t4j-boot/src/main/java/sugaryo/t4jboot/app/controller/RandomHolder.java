package sugaryo.t4jboot.app.controller;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RandomHolder {
	
	private static final Logger log = LoggerFactory.getLogger( RandomHolder.class );

	private int prevent = 0;
	
	private Random random = new Random( new Date().getTime() );
	
	public boolean rand() {
		
		if ( 0 < this.prevent ) {
			// 抑止カウンタが残ってる場合はデクリメントしておわり。
			this.prevent--;
			return false;
		}
		
		int r = this.random.nextInt( 100 );
		boolean hit = r % 10 == 0;
		
		if ( hit ) {
			// 抑止カウンタ（取り敢えず３回待機させる）
			this.prevent = 3;
		}
		return hit;
	}
	
}
