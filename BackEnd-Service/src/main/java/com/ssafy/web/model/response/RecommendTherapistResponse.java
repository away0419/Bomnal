package com.ssafy.web.model.response;

import lombok.Data;

@Data
public class RecommendTherapistResponse {
	private String thera_id;
	private String name;
	private String email;
	private String profile_url;
	private String thera_intro;
}
