package com.ssafy.web.service;

import java.util.List;

import com.ssafy.web.model.response.RecommendTherapistResponse;

public interface BExpertiseTherapistService {
	RecommendTherapistResponse[] recommendTherapistList(int expertise_no);
}
