package sugaryo.t4jboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

// DIコンテナ管理Bean定義を纏める。
@Configuration
public class SpringBeans {

	
	// 標準のViewTemplate用のエンジンを用意しておかないとViewControllerが処理できない
	@Primary
	@Bean
	public SpringTemplateEngine pageTemplateEngine() {
		var resolver = new ClassLoaderTemplateResolver();
		resolver.setTemplateMode( TemplateMode.HTML );
		resolver.setPrefix( "templates/" ); // src/mail/resources/templates
		resolver.setSuffix( ".html" );
		resolver.setCharacterEncoding( "UTF-8" );
		resolver.setCacheable( true );
		
		var engine = new SpringTemplateEngine();
		engine.setTemplateResolver( resolver );
		
		return engine;
	}
	
    @Bean("messageTemplateEngine")
    public SpringTemplateEngine messageTemplateEngine() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode( TemplateMode.TEXT );
        resolver.setPrefix( "templates/messages/" ); // src/mail/resources/templates/messages
        resolver.setSuffix( ".message" );
        resolver.setCharacterEncoding( "UTF-8" );
        resolver.setCacheable( true );

        var engine = new SpringTemplateEngine();
        engine.setTemplateResolver( resolver );

        return engine;
    }
}
