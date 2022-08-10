package com.ssafy.web.model.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildResponse {
	String name;

	Date birth;

	int gender;

	String profileUrl;

}