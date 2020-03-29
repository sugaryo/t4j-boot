package sugaryo.t4jboot.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:t4jboot.properties", encoding = "UTF-8")
public class ScheduleConfig {
    
	public static class AutoRetweet {

        public static class Counts {
            public final int weekday;
            public final int holiday;
    
            private Counts( int weekday, int holiday ) {
                this.weekday = weekday;
                this.holiday = holiday;
            }
        }

        public final Counts counts;

        private AutoRetweet(Counts counts) {
            this.counts = counts;
        }
    }

    public final AutoRetweet autoRt;

	public ScheduleConfig(
            @Value("${schedule.autort.weekday.count}") int autoRtCountWeekday,
            @Value("${schedule.autort.holiday.count}") int autoRtCountHoliday
			) {

        this.autoRt = new AutoRetweet( new AutoRetweet.Counts(
                autoRtCountWeekday, 
                autoRtCountHoliday ));
	}
}
