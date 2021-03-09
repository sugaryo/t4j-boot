package sugaryo.t4jboot.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sugaryo.t4jboot.common.utility.JsonMapper;

@RestControllerAdvice
public class RestHandler {
	
	private static final Logger log = LoggerFactory.getLogger( RestHandler.class );
	
	/**
	 * {@link Exception} 例外ハンドラ
	 * 
	 * @param ex 例外
	 * @return エラーレスポンス
	 */
	@ExceptionHandler(Exception.class)
	private ResponseEntity<String> onUnknownError( Exception ex ) {
		
		final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		log.error( status.getReasonPhrase(), ex );
		
		// 返却するエラー情報を構築。
		final String json = JsonMapper.map()
				.put( "code", status.value() )
				.put( "message", status.getReasonPhrase() )
				.stringify();
		return new ResponseEntity<String>( json, status );
	}
}
