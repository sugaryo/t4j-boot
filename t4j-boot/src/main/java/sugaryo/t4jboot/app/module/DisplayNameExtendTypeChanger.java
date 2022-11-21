package sugaryo.t4jboot.app.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;
import sugaryo.t4jboot.common.utility.RandomSelector;

@Component
public class DisplayNameExtendTypeChanger {

	final TwitterApiCall twitter;
	
	public DisplayNameExtendTypeChanger( @Autowired TwitterApiCall twitter ) {
		this.twitter = twitter;
	}
	
	
	public enum ExtendsElementTypes {
		CLASS("extends"),
		INTERFACE("implements");
		
		public final String method;

		private ExtendsElementTypes( String method ) {
			this.method = method;
		}
	}
	
	public static class ExtendsElement {
		public final ExtendsElementTypes type;
		public final String name;
		
		public ExtendsElement( ExtendsElementTypes type, String name ) {
			this.type = type;
			this.name = name;
		}
		
		public String as(String username) {
			return username + " " + this.type.method + " " + this.name + ";";
		}
	}
	public static class ClassExtends extends ExtendsElement {
		public ClassExtends( String name ) {
			super( ExtendsElementTypes.CLASS, name );
		}
	}
	public static class InterfaceImplements extends ExtendsElement {
		public InterfaceImplements( String name ) {
			super( ExtendsElementTypes.INTERFACE, name );
		}
	} 
	
	private static final ExtendsElement[] elements = new ExtendsElement[] {
		new ClassExtends( "TestClass" ),
		new InterfaceImplements( "ITestable" ),
	};

	
	public String shuffle() {
		return this.update( RandomSelector.select( elements ) );
	}
	
	private String update(final ExtendsElement element) {
		return this.twitter.updateDisplayName( element.as( "える" ) ).getName();
	}
}
