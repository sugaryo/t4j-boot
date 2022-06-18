package sugaryo.t4jboot.app.controller.rest.strategy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.common.utility.JsonMapper;

@Component
public class ResponseParameterStrategy {
	
	@Autowired
	HttpServletRequest request; // thread-local
	
	public boolean pretty() {
		
		final var pretty = this.anyParameter( "pretty", "p", "-p" );
		
		// 指定されてない場合は false
		if ( null == pretty ) return false;
		
		// 明示的に false が指定された場合も false
		if ( pretty.equalsIgnoreCase( "false" ) ) return false;
		
		// 上記以外は全て true  
		return true;
	}
	
	public boolean verbose() {
		
		final var verbose = this.anyParameter( "verbose", "v", "-v" );
		
		// 指定されてない場合は false
		if ( null == verbose ) return false;
		
		// 明示的に false が指定された場合も false
		if ( verbose.equalsIgnoreCase( "false" ) ) return false;
		
		// 上記以外は全て true  
		return true;
	}
	
	private String anyParameter(String... params) {
		
		for ( String param : params ) {
			final String value = this.request.getParameter( param );
			if ( null != value ) return value;
		}
		
		return null;
	}
	
	public String stringify( Object obj ) {
		return JsonMapper.stringify( obj, this.pretty() );
	}
}
