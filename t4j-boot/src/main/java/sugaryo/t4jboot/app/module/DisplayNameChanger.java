package sugaryo.t4jboot.app.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
import sugaryo.t4jboot.common.utility.RandomSelector;

@Component
public class DisplayNameChanger {
	
	final TwitterApiCall twitter;
	
	public DisplayNameChanger( @Autowired TwitterApiCall api ) {
		this.twitter = api;
	}
	
	public String change( final int httpStatusCode ) {
		final HttpStatus status = HttpStatus.resolve( httpStatusCode );
		return this.change( status );
	}
	public String change( final HttpStatus status ) {
		return this.update( null == status ? HttpStatus.BAD_REQUEST : status );
	}
	
	public String shuffle() {
		return this.update( RandomSelector.select( HttpStatus.values() ) );
	}
	
	// twitter display-name を更新して、更新後の名前を返す。
	private String update( final HttpStatus status ) {
		
		return this.twitter
				.updateDisplayName( "える/tweet " + status.value() + ";" + status.name() )
				.getName();
	}
}
