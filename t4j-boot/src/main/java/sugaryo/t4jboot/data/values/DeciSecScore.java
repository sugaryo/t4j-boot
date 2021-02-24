package sugaryo.t4jboot.data.values;

import java.math.BigDecimal;

// 秒数を 0.1 単位（デシ秒）で持ち、整数で扱うデータクラス。
public class DeciSecScore {
	
	/** デシ秒 */
	private int value;

	/**
	 * @param second 秒（0.1単位）
	 */
	public DeciSecScore( String second ) {
		// FIXME：ここはあとで要検証。
		this.value = new BigDecimal( second ).multiply( BigDecimal.TEN ).intValue();
	}
	/**
	 * @param second 秒（0.1単位）
	 */
	public DeciSecScore( double second ) {
		this.value = (int)(second * 10);
	}
	/**
	 * @param second 秒
	 */
	public DeciSecScore( int second ) {
		this.value = second * 10;
	}
	
	/** デフォコン */
	private DeciSecScore() {
		this.value = 0;
	}
	
	
	public int score() {
		return this.value;
	}
	public double seconds() {
		return this.value / 10.0;
	}
	
	public DeciSecScore score( int value ) {
		this.value = value;
		return this;
	}
	public DeciSecScore seconds(double second) {
		this.value = (int)(second * 10);
		return this;
	}
	
	public static DeciSecScore of( int value ) {
		return new DeciSecScore().score( value );
	}
}
