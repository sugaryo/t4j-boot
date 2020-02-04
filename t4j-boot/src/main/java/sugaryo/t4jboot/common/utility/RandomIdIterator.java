package sugaryo.t4jboot.common.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomIdIterator {

	private static final Logger log = LoggerFactory.getLogger( RandomIdIterator.class );

    private long[] ids;

    public RandomIdIterator(long[] ids) {
        this.ids = ids;
    }

    // 本当は C# の yield return みたいにしたいけど
    public long[] iterate( final int n ) {

        // ゼロ指定の場合は空配列を返す。
        // マイナスはゼロ指定と同じ扱い。
        if ( n <= 0 ) return new long[]{};
        
        // TODO：オリジナルのコレクションを返すのはどうかと思う（コピーを返すべき）けど、自作ライブラリなので取り敢えずこれで。
        if ( ids.length <= n ) return this.ids; 


        ////////////////////
        // ランダム抽出処理
        ////////////////////
        
        // 指定個数のインデクサを生成。
        int[] indexes = indexer( ids.length, n );

        log.debug("{} / {} : {}", n, ids.length, indexes );
        
        // 元配列から指定個数の要素を抜き出して新しい配列に詰めて返す。
        long[] extracted = new long[n];
        for ( int i = 0; i < n; i++ ) {
            extracted[i] = this.ids[indexes[i]];
        }
        return extracted;
    }

    /**
     * 重複しないランダムなインデクサを生成する。
     * @param s 元のコレクションの要素数
     * @param n 生成するインデクサの個数
     * @return 総数 {@code s} に於けるインデックス {@code [0～s-1]} から、重複せずランダムに選んだ {@code n} 個のインデックスを返す。
     */
    public static int[] indexer(final int s, final int n) { // 単体でテストもしたいのでpublicにしておく。
        
        final int half = s / 2;

        // 生成個数が半分以下の場合は、素直に生成した方が速い。
        // 生成個数が半分を超えている場合は、逆算した方が速い。
        // 例えば、総数１０個からランダムに７個選ぶ場合、選ばない３個を決めた方が速い。
        // TODO：inclusiveは順序保持しないが、exclusiveは順序保持するので、出来ればどっちかに寄せたい。
        return n <= half
                ? inclusive( s, n )
                : exclusive( s, n );
    }

    private static int[] inclusive( final int s, final int n ) {

        // 乱数生成
        var random = new Random( LocalDateTime.now().getNano() );

        // 重複判定するためのHashSet
        var distinct = new HashSet<>();

        // 指定した個数のインデクサを生成。
        int[] indexes = new int[n];
        for ( int i = 0; i < indexes.length; i++ ) {
            
            boolean add;
            int index;
            do {
                index = random.nextInt( s ); // s exclusive.
                add = distinct.add( index );
            } while( !add );
            indexes[i] = index;
        }
        return indexes;
    }

    private static final int IGNORE = -1;
    private static int[] exclusive( final int s, final int n ) {

        // 選ばない数 m を求める。
        final int m = s - n;
        
        // 選ばない m 個のインデクサを決める。
        // ※ ここはinclusiveのロジックをそのまま使える
        int[] exclude = inclusive( s, m );

        // 選ばない m 個を決めたら、それ以外のインデックスを求めて返す。
        // TODO：配列の補集合の求め方をもう少し工夫したい。
        int[] all = IntStream.range(0, s).toArray();
        for ( int ex : exclude ) {
            all[ex] = IGNORE;
        }
        
        int[] indexes = new int[n];
        int i = 0;
        for ( int index : all ) {

            if ( IGNORE == index ) continue;

            indexes[i++] = index;
        }
        return indexes;
    }
    

    // ショートカット
    public static long[] iterate( final long[] ids, final int n ) {
        return new RandomIdIterator(ids).iterate(n);
    }
}
