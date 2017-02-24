package magma.com.anshan.dto;


import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Administrator
 * @version 1.0
 * @created 19-10��-2016 ���� 9:40:51
 */

public class CustomerDTO {

	private String address;
	private String mobile;
	private String password;
	private String uid;
	private String userName;
	private String token;
	private String lastLoginTime;
	private String tokenValidTime;


	public CustomerDTO(){

	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

	}
	public String getToken(){
		return token;
	}

	public String getLastLoginTime(){
		return lastLoginTime;
	}

	public String getTokenValidTime(){
		return tokenValidTime;
	}

	public String getAddress(){
		return address;
	}

	public String getMobile(){
		return mobile;
	}

	public String getPassword(){
		return password;
	}

	public String getUid(){
		return uid;
	}

	public String getUserName(){
		return userName;
	}

	/**
	 *
	 * @param address    address
	 */
	public void setAddress(String address){
		this.address = address ;

	}

	/**
	 *
	 * @param mobile    mobile
	 */
	public void setMobile(String mobile){
		this.mobile = mobile;

	}

	/**
	 *
	 * @param password    password
	 */
	public void setPassword(String password){
		this.password = password ;

	}

	/**
	 *
	 * @param  token  token
	 */

	public void setToken(String token){
		this.token=token;
	}

	/**
	 *
	 * @param  lastLoginTime  lastLoginTime
	 */

	public void setLastLoginTime(String lastLoginTime){
		this.lastLoginTime=lastLoginTime;
	}

	/**
	 *
	 * @param  tokenValidTime  tokenValidTime
	 */

	public void setTokenValidTime(String tokenValidTime){
		this.tokenValidTime = tokenValidTime;
	}

	/**
	 *
	 * @param uid    uid
	 */
/*	public void setUid(String uid){

	}*/

	/**
	 *
	 * @param  userName  userName
	 */
	public void setUsername(String userName){
		this.userName = userName;

	}

}