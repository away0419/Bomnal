package com.ssafy.web.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.web.RandomUserId;
import com.ssafy.web.db.entity.Parent;
import com.ssafy.web.db.entity.Therapist;
import com.ssafy.web.db.entity.User;
import com.ssafy.web.db.repository.ParentRepository;
import com.ssafy.web.db.repository.TheraRepository;
import com.ssafy.web.db.repository.UserRepository;
import com.ssafy.web.model.response.ParentResponse;
import com.ssafy.web.model.response.TherapistResponse;
import com.ssafy.web.request.ParentRegisterRequest;
import com.ssafy.web.request.TheraRegisterRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
 
	@Autowired
	TheraRepository theraRepository;
	@Autowired
	ParentRepository parentRepository;
	
	@Autowired
	UserRepository userRepository;
	
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	//치료사 회원가입 
	@Override
	public void theraRegist(TheraRegisterRequest theraInfo) {
		User user = new User();
		user.setUserId(RandomUserId.makeTheraId());
		user.setId(theraInfo.getId());
		user.setPassword(encoder.encode(theraInfo.getPassword()));
		
		Therapist thera = new Therapist();
		thera.setName(theraInfo.getName());
		thera.setEmail(theraInfo.getEmail());
		thera.setPhone(theraInfo.getPhone());
		thera.setAddress(theraInfo.getAddress());
		thera.setProfileUrl(theraInfo.getProfile_url());
		thera.setFileUrl(theraInfo.getFile_url());
		thera.setTheraIntro(theraInfo.getThera_intro());
	
		thera.setUser(user);
		theraRepository.save(thera);
		
	}

	//부모 회원가입 
	@Override
	public void parentRegist(ParentRegisterRequest parentInfo) {
		User user = new User();
		user.setUserId(RandomUserId.makeParentId());
		user.setId(parentInfo.getId());
		user.setPassword(encoder.encode(parentInfo.getPassword()));
		
		Parent parent= new Parent();
		parent.setName(parentInfo.getName());
		parent.setEmail(parentInfo.getEmail());
		parent.setPhone(parentInfo.getPhone());
		parent.setAddress(parentInfo.getAddress());
		parent.setUser(user);
		parentRepository.save(parent);

	}

	//아이디 중복검사 
	@Override
	public int checkId(String id) {
		User user= userRepository.findUserById(id).orElse(null);
		
		if(user == null) {
			//사용가능한 아이디
			return 1;
		}
		return 0; 
		
	}

	//이메일 중복검사 
	@Override
	public int checkEmail(String myemail) {
		Parent p = parentRepository.findByEmail(myemail).orElse(null);
		Therapist t = theraRepository.findByEmail(myemail).orElse(null);
		if( p== null && t == null ) { // 해당 이메일을 사용하는 유저가 없음 
			log.debug("사용가능한 이메일");
			return 1;
		}else {
			log.debug("중복 이메일");
			return 0;
		}
	}

	//부모 회원정보 조회 
	@Override
	public ParentResponse getParentInfo(String user_id) {
		User u = userRepository.findByUserId(user_id); 
		Parent p = parentRepository.findByUser(u);
		
		ParentResponse pr = new ParentResponse();
		pr.setId(u.getId());
		pr.setName(p.getName());
		pr.setPhone(p.getPhone());
		pr.setEmail(p.getEmail());
		pr.setAddress(p.getAddress());
		
		return pr;
	}

	//치료사 정보 조회
	@Override
	public TherapistResponse getTheraInfo(String user_id) {
		User u = userRepository.findByUserId(user_id); 
		Therapist t = theraRepository.findByUser(u);
		
		TherapistResponse tr= new TherapistResponse();
		tr.setId(u.getId());
		tr.setName(t.getName());
		tr.setEmail(t.getEmail());
		tr.setPhone(t.getPhone());
		tr.setAddress(t.getAddress());
		tr.setProfile_url(t.getProfileUrl());
		tr.setThera_intro(t.getTheraIntro());
		
		return tr;
		
	}



}
