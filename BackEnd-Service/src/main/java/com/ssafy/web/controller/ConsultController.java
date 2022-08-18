package com.ssafy.web.controller;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.web.model.response.ConsultTotalResponse;
import com.ssafy.web.request.ConsultRequest;
import com.ssafy.web.service.ConsultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consult")
public class ConsultController {
	private final ConsultService conService;

	/* 상담방 생성 */
	@PostMapping("/room")
	public int creatRoom(@RequestBody ConsultRequest conreq) {
		int consultNo = conService.createRoom(conreq);
		return consultNo;
	}
	
	@Caching(evict= {
			@CacheEvict(value="consultTherapist", allEntries=true),
			@CacheEvict(value="consultTherapistAndChildId", allEntries=true),
			@CacheEvict(value="consultParentAndChildId", allEntries=true),
			@CacheEvict(value="consultParent", allEntries=true)
	})
	/*메모 수정*/
	@PutMapping("/memo")
	public String updateMemo(@RequestBody ConsultRequest conreq) {
		conService.updateMemo(conreq);
		return "success";
	}
	
	@Caching(evict= {
			@CacheEvict(value="consultTherapist", allEntries=true),
			@CacheEvict(value="consultTherapistAndChildId", allEntries=true),
			@CacheEvict(value="consultParentAndChildId", allEntries=true),
			@CacheEvict(value="consultParent", allEntries=true)
	})
	/*일지 수정*/
	@PutMapping("/record")
	public String updateRecord(@RequestBody ConsultRequest conreq) {
		conService.updateRecord(conreq);
		return "success";
	}
	
	/* 해당 치료사가 자신이 맡은 모든 아동의 기록을 보고 싶은 경우 */
	@Cacheable(value="consultTherapist",key="#theraId + #page + #size", cacheManager = "cacheManager")
	@GetMapping("/thearpist/{theraId}/{page}/{size}")
	public List<ConsultTotalResponse> findByTherapist(@PathVariable("theraId") String theraId,
			@PathVariable("page") int page, @PathVariable("size") int size){
		PageRequest pr = PageRequest.of(page-1, size);
		List<ConsultTotalResponse> list = conService.findByTheraId(theraId, pr);
		return list;
	}
	
	/* 해당 치료사가 원하는 아이의 상담 기록만 보고 싶은 경우 */
	@Cacheable(value="consultTherapistAndChildId",key="#theraId + #childId + #page + #size", cacheManager = "cacheManager")
	@GetMapping("/thearpist/{theraId}/{childId}/{page}/{size}")
	public List<ConsultTotalResponse> findByTherapistAndChildId(
			@PathVariable("theraId") String theraId, @PathVariable("childId") String childId,
			@PathVariable("page") int page, @PathVariable("size") int size){
		PageRequest pr = PageRequest.of(page-1, size);
		List<ConsultTotalResponse> list = conService.findByTheraIdAndChildId(theraId,childId, pr);
		return list;
	}

	/* 해당 부모가 해당 아이의 상담 기록만 보고 싶은 경우 */
	@Cacheable(value="consultParentAndChildId",key="#parnetId + #childId + #page + #size", cacheManager = "cacheManager")
	@GetMapping("/parent/{parentId}/{childId}/{page}/{size}")
	public List<ConsultTotalResponse> findConsultByChildId(
			@PathVariable("parentId") String parentId ,@PathVariable("childId") String childId,
			@PathVariable("page") int page, @PathVariable("size") int size){
		PageRequest pr = PageRequest.of(page-1, size);
		List<ConsultTotalResponse> list = conService.findByParentIdAndChildId(parentId,childId, pr);
		return list;
	}
	
	/* 해당 부모가 모든 아이의 상담 기록 보고 싶은 경우 */
	@GetMapping("/parent/{parentId}/{page}/{size}")
	@Cacheable(value="consultParent",key="#parentId + #page + #size", cacheManager = "cacheManager")
	public List<ConsultTotalResponse> findByParentId(
			@PathVariable("parentId") String parentId,
			@PathVariable("page") int page, @PathVariable("size") int size){
		PageRequest pr = PageRequest.of(page-1, size);
		List<ConsultTotalResponse> list = conService.findByParentId(parentId, pr);
		return list;
	}
	
	@GetMapping("/therapistcount/{theraId}/{childId}")
	@Cacheable(value="consultTherapistByChildCount",key="#theraId + #childId", cacheManager = "cacheManager")
	public int countByTherapistAndChild(@PathVariable("theraId") String theraId, @PathVariable("childId") String childId){
		return conService.countByTheraIdBychildId(theraId, childId);
	}
	
	@GetMapping("/parentcount/{parentId}/{childId}")
	@Cacheable(value="consultParentByChildCount",key="#parentId + #childId", cacheManager = "cacheManager")
	public int countByParentAndChild(@PathVariable("parentId") String parentId ,@PathVariable("childId") String childId){
		return conService.countByParentIdBychildId(parentId, childId);
	}
	
	
}
