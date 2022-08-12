package com.ssafy.web.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 서버에 대한 응답 
 * 메세지 , 응답 코드*/
@Getter
@Setter
@ApiModel("BaseResponseBody")
public class UserLoginPostRes {
	@ApiModelProperty(name="응답 메시지", example="정상")
	String message =null;
	@ApiModelProperty(name="사용자고유아이디", example="p/t+12")
	String userid= null;
	String accessToken; // JWT 
	String refreshToken; // 재발급을 위한 refresh 토큰 추가 

	public UserLoginPostRes() {}
	
	public UserLoginPostRes(String message, String userid, String accessToken ,String refreshToken) {
		this.message = message;
		this.userid = userid;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	public static UserLoginPostRes offail(String userid, String message) {
		UserLoginPostRes res= new UserLoginPostRes() ;
		res.setUserid(userid);
		res.setMessage(message);
		return res;
	}
//	public static UserLoginPostRes ofsuccess(String userid, String message, String accessToken, String refreshToken) {
//		UserLoginPostRes res= new UserLoginPostRes() ;
//		res.setUserid(userid);
//		res.setAccessToken(accessToken);
//		res.setMessage(message);
//		res.setRefreshToken(refreshToken);
//		return res;
//	}
	
	// 재발급 할 때 줄 정보 
	public static UserLoginPostRes ofrefresh(String accessToken, String refreshToken) {
		UserLoginPostRes res= new UserLoginPostRes();
		res.setAccessToken(accessToken);
		res.setRefreshToken(refreshToken);
		return res; 
	}

}
