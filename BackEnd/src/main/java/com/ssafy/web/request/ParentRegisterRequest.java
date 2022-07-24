package com.ssafy.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 부모 회원가입할 떄 요청 보내는 데이터 
 * "/user/parent" post 방식에서 사용 */
@Getter
@Setter

@ApiModel("ParentRegisterRequest")
public class ParentRegisterRequest {
	// 부모정보 
	@ApiModelProperty(name="아이디")
	String id ;
	@ApiModelProperty(name="비밀번호")
	String password;
	@ApiModelProperty(name="이름")
	String name;
	@ApiModelProperty(name="이메일")
	String email;
	@ApiModelProperty(name="휴대폰")
	String phone;
	@ApiModelProperty(name="주소")
	String address;
	
	//아동정보 를 여기에다가 넣어야 할지 ? 
	//아동 추가를 고려하여 ChildRegisterRequest 를 따로 만들어서 아동 정보를 따로 담아야할지 
}
