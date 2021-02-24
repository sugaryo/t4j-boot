package sugaryo.t4jboot.data.pad;

import java.util.List;

import sugaryo.t4jboot.common.constants.pad.DropKind;
import sugaryo.t4jboot.data.values.DeciSecScore;

public class Roulette {
	
	
	public static class Parameter 
	{
		/** 基準ドロップ */
		public final DropKind drop;

		/** パズル操作時間 */
		public final DeciSecScore time;
		
		/** ルーレットの変化スパン */
		public final DeciSecScore span;
		
		
		public Parameter( String drop, double time, double span ) {
			this.drop = DropKind.from( drop );
			this.time = new DeciSecScore( time );
			this.span = new DeciSecScore( span );
		}
		public Parameter( DropKind drop, DeciSecScore time, DeciSecScore span ) {
			this.drop = drop;
			this.time = time;
			this.span = span;
		}
	}
	
	
	public static class RoulettePuzzle
	{
		/** ルーレットのトータルドロップ */
		private List<DropKind> drops;
		
		/** ルーレットの周回数 */
		private int rounds;
		
		/** 最後の進みドロップ数 */
		private int remDrops;
		
		/** 最後の余り時間 */
		private DeciSecScore remTime;
		
		
		
		public List<DropKind> getDrops() {
			return drops;
		}

		public void setDrops( List<DropKind> drops ) {
			this.drops = drops;
		}

		public int getRounds() {
			return rounds;
		}

		public void setRounds( int rounds ) {
			this.rounds = rounds;
		}

		public int getRemDrops() {
			return remDrops;
		}

		public void setRemDrops( int remDrops ) {
			this.remDrops = remDrops;
		}

		public DeciSecScore getRemTime() {
			return remTime;
		}

		public void setRemTime( DeciSecScore remTime ) {
			this.remTime = remTime;
		}
	}
}
