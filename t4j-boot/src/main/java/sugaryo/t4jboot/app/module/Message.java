package sugaryo.t4jboot.app.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class Message {
	
	/** Thymeleafのテンプレートエンジン（プレーンテキスト設定） */
	private final SpringTemplateEngine template;
	
	public Message( @Autowired SpringTemplateEngine template ) {
		this.template = template;
	}
	

	public String ofNyappiCall( String timestamp, String hour ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		context.setVariable( "hour", hour );
		return this.template.process( "nyappi_call", context );
	}
	
	public String ofNaru4JiCall( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "na_ru_yo_ji", context );
	}
	
	
	public String ofAdvertiseFireMilleIllust( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "ad_himiru", context );
	}
	
	public String ofAdvertiseQiitaSpringBoot( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "ad_qiita_spring_boot", context );
	}
	
	public String ofAdvertiseCurryNote( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "ad_curry_note", context );
	}
	
}
