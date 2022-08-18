package com.ssafy.web.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.persistence.Cacheable;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ssafy.web.common.PathUtil;
import com.ssafy.web.db.entity.play.FeelingCard;
import com.ssafy.web.db.entity.play.ObjectCard;
import com.ssafy.web.db.entity.play.Play;
import com.ssafy.web.db.repository.FeelingCardRepository;
import com.ssafy.web.db.repository.ObjectCardRepository;
import com.ssafy.web.db.repository.PlayRepository;
import com.ssafy.web.dto.FeelingDto;
import com.ssafy.web.dto.ObjectDto;
import com.ssafy.web.model.response.PlayResponse;
import com.ssafy.web.request.PlayRequest;

@Service
public class PlayServiceImpl implements PlayService {

	@Autowired
	PlayRepository playRepository;

	@Autowired
	ObjectCardRepository objectCardRepository;

	@Autowired
	FeelingCardRepository feelingCardRepository;

	@Autowired
	WebClient webClient;
	
	@Autowired
	RedisService redisService;

	/** 보호자 -> 아동의 놀이 기록 조회 */
	@Override
	public List<PlayResponse> getChildPlaylist(String childId) {
		List<Play> list = playRepository.findByChildId(childId);
		List<PlayResponse> playList = new ArrayList<PlayResponse>();

		for (Play play : list) {
			String childName = webClient.get().uri("/info/child/" + childId).retrieve().bodyToMono(String.class)
					.block();

			PlayResponse pResponse = new PlayResponse();

			pResponse.setChildId(childId);
			pResponse.setChildName(childName);
			pResponse.setPlayTime(play.getCreateTime());
			pResponse.setTotalTime(play.getTotalTime());
			pResponse.setScore(play.getScore());

			playList.add(pResponse);
		}

		return playList;
	}

	/** 보호자 -> 아동의 놀이 기록 저장 */
	@Override
	public void savePlayResult(PlayRequest playRequest) {
		Play play = new Play();

		play.setChildId(playRequest.getChildId());
		play.setCreateTime(playRequest.getCreateTime());
		play.setScore(playRequest.getScore());
		play.setTotalTime(playRequest.getTotalTime());

		playRepository.save(play);
	}

	/** 사물 카드 놀이 -> 3장씩 카드 보내기 */
	@Override
	public List<ObjectDto> objectCardPlay() {
//		int totalCard = objectCardRepository.findAll().size();
		//레디스에 카드 넣기
//		redisService.setCards();
		//		if(redisService.getCards(1) == null) {
//			redisService.setCards();
//		}
		for(int i=1; i<=21; i++)
			System.out.println(redisService.getCards(i));
//		List<String> cards = redisService.getCards();
////		System.out.println(cards.get(0));
		int totalCard = objectCardRepository.countAll();
		int arr[] = new int[3]; // 카드 아이디 3개 저장

		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			arr[i] = random.nextInt(totalCard) + 1;
			for (int j = 0; j < i; j++) {
				if (arr[i] == arr[j]) {
					i--;
				}
			}
		}

		System.out.printf("카드 아이디: %d %d %d ", arr[0], arr[1], arr[2]);
	
		List<ObjectDto> objectList = new ArrayList<ObjectDto>();
		for (int i = 0; i < 3; i++) {
//			ObjectCard card = objectCardRepository.findByCardId(arr[i]);
			ObjectDto objectCard = new ObjectDto();
			String selectCard = redisService.getCards(arr[i]); 
			System.out.println(selectCard);
			//apple.jpg,사과,사과를 골라주세요!
			StringTokenizer st = new StringTokenizer(selectCard, ",");


			String url = PathUtil.OBJECT_CARD_PATH + st.nextToken();
			objectCard.setName(st.nextToken());
			objectCard.setQuestion(st.nextToken());
			byte[] imageByteArray;
			try {
				InputStream imageIS = new FileInputStream(url);
				imageByteArray = IOUtils.toByteArray(imageIS);
				objectCard.setImage(imageByteArray);
				imageIS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			objectList.add(objectCard);
		}

		return objectList;

	}

	/** 감정 카드 놀이 -> 3장씩 카드 보내기 */
	@Override
	public List<FeelingDto> feelingCardPlay() {
		int totalCardCnt = objectCardRepository.countAll();
		int arr[] = new int[3]; // 카드 아이디 3개 저장

		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			arr[i] = random.nextInt(totalCardCnt) + 1;
			for (int j = 0; j < i; j++) {
				if (arr[i] == arr[j]) {
					i--;
				}
			}
		}

		List<FeelingDto> feelingList = new ArrayList<FeelingDto>();
		for (int i = 0; i < 3; i++) {
			FeelingCard card = feelingCardRepository.findByCardId(arr[i]);
			FeelingDto feelingCard = new FeelingDto();

			feelingCard.setImage(card.getImage());
			feelingCard.setName(card.getName());
			feelingCard.setQuestion(card.getQuestion());

			feelingList.add(feelingCard);
		}
		return feelingList;
	}

}
