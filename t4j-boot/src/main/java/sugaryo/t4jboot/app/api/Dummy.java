package sugaryo.t4jboot.app.api;

import java.util.List;

// FIXME : 取り敢えずダミーで片付ける。
/**
 * <b>取り敢えずコンパイル通しただけ。</b>
 * 
 * @author sugaryo
 */
public interface Dummy {
	
	public static class Twitter {
		
		public Status showStatus( long id ) {
			return null;
		}
		
		public List<Status> getUserListStatuses( long listId, Paging paging ) {
			return null;
		}
		
		public void unRetweetStatus( long id ) {
			
		}
		
		public Status retweetStatus( long id ) {
			return null;
		}
		
		public Status updateStatus( String message ) {
			return null;
		}
		
		public long getId() {
			return 0;
		}
		
		public Object getAccountSettings() {
			return null;
		}
		
		public User showUser( long id ) {
			return null;
		}
		
		public User updateProfile( String name, String url, String location, String description ) {
			return null;
		}
		
	};
	
	public static class TwitterFactory {
		
		public Twitter getInstance() {
			return null;
		}
		
	}
	
	public static class TwitterException extends RuntimeException {
		
	}
	
	public static class Status {
		
		public boolean isRetweetedByMe() {
			return false;
		}
		
		public String getText() {
			return null;
		}
		
		public long getId() {
			return 0;
		}
		
		public User getUser() {
			return null;
		}
		
		public Status getRetweetedStatus() {
			return null;
		}
		
		public int getFavoriteCount() {
			return 0;
		}
		
		public int getRetweetCount() {
			return 0;
		}
		
		public long getCreatedAt() {
			return 0;
		}
		
		public MediaEntity[] getMediaEntities() {
			return null;
		}
		
	}
	
	public static class Paging {
		
		public Paging( int page ) {
		}
	}
	
	public static class User {
		
		public String getName() {
			return null;
		}
		
		public String getScreenName() {
			return null;
		}
		
		public long getId() {
			return 0;
		}
		
	}
	
	
	public static class MediaEntity {
		
		public String getMediaURLHttps() {
			return null;
		}
		
	}
}
