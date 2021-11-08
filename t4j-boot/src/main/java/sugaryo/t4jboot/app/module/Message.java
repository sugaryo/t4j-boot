package sugaryo.t4jboot.app.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class Message {
	
	// TODO：このクラスもうちょっと何とかならんか？
	// FIXME：マジで何とかしたいのだが、、、RestTemplateのexchangeみたいな形にするしかないか？？
	
	/** Thymeleafのテンプレートエンジン（プレーンテキスト設定） */
	private final SpringTemplateEngine template;
	
	public Message( 
			@Qualifier("messageTemplateEngine")
			@Autowired SpringTemplateEngine template ) {
		this.template = template;
	}
	

	public String ofNyappiCall( String timestamp, String batteri, String hour ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		context.setVariable( "batteri", batteri );
		context.setVariable( "hour", hour );
		return this.template.process( "nyappi_call", context );
	}
	public String ofDai3JiCall( String timestamp, String batteri ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		context.setVariable( "batteri", batteri );
		return this.template.process( "nyappi_03", context );
	}
	public String ofNaru4JiCall( String timestamp, String batteri ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		context.setVariable( "batteri", batteri );
		return this.template.process( "nyappi_04", context );
	}
	
	
	public String ofAdvertiseFireMilleIllust( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "ad_ill_himiru", context );
	}

	public String ofAdvertiseKorone563KiroIllust( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "ad_ill_korone_563k", context );
	}
	
	@Deprecated
	public String ofAdvertiseQiitaSpringBoot( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "ad_qiita_spring_boot", context );
	}
	
	public String ofAdvertiseZennSpringBoot( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "ad_zenn_spring_boot", context );
	}
	
	public String ofAdvertiseCurryNote( String timestamp ) {
		var context = new Context();
		context.setVariable( "timestamp", timestamp );
		return this.template.process( "ad_curry_note", context );
	}
	
}
