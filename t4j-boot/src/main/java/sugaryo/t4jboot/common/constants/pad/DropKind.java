package sugaryo.t4jboot.common.constants.pad;

public enum DropKind {

	/** 回復 */
	HEART("h", "回復", 0) {
		@Override
		public DropKind prev() {
			return DARK;
		}
		
		@Override
		public DropKind next() {
			return FIRE;
		}
	},
	/** 火 */
	FIRE("r", "火", 1) {
		@Override
		public DropKind prev() {
			return HEART;
		}
		
		@Override
		public DropKind next() {
			return WATER;
		}
	},
	/** 水 */
	WATER("b", "水", 2) {
		@Override
		public DropKind prev() {
			return FIRE;
		}
		
		@Override
		public DropKind next() {
			return WOOD;
		}
	},
	/** 木 */
	WOOD("g", "木", 3) {
		@Override
		public DropKind prev() {
			return WATER;
		}
		
		@Override
		public DropKind next() {
			return LIGHT;
		}
	},
	/** 光 */
	LIGHT("y", "光", 4) {
		@Override
		public DropKind prev() {
			return WOOD;
		}
		
		@Override
		public DropKind next() {
			return DARK;
		}
	},
	/** 闇 */
	DARK("p", "闇", 5) {
		@Override
		public DropKind prev() {
			return LIGHT;
		}
		
		@Override
		public DropKind next() {
			return HEART;
		}
	};
	
	public final String code;
	public final String name;
	public final int index;
	
	private DropKind( String code, String name, int index ) {
		this.code = code;
		this.name = name;
		this.index = index;
	}

	public abstract DropKind prev();
	public abstract DropKind next();


	public static DropKind from(final String code) {
		var kinds = DropKind.values();
		for ( DropKind kind : kinds ) {
			if ( kind.code.equals( code ) ) return kind;
		}
		return null;
	}
}
