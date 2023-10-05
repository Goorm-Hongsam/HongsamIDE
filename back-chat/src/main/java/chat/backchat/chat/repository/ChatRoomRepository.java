package chat.backchat.chat.repository;

import chat.backchat.chat.Domain.ChatResponse;
import chat.backchat.chat.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ChatRoomRepository {
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;

    // Redis
//    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, String> opsHashChatRoom;
    private SetOperations<String, Object> topicAndRoom; // <topic(uuid),roomId>
    private ValueOperations<String,Object> topicStore;

    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        topicAndRoom = redisTemplate.opsForSet();
//        topics = new HashMap<>();
    }

    // topic 생성 확인
    public ChatResponse findRoomId(String uuid, String questionId) {

        String roomId = uuid + questionId;

        Optional<Long> roomsNum = Optional.ofNullable(topicAndRoom.size(uuid));
        Optional<Boolean> isRoom = Optional.ofNullable(topicAndRoom.isMember(uuid, roomId));

        if (roomsNum.isEmpty() || roomsNum.get() == 0) { // topic 없음
            // topic 생성하고 저장
            // roomID 생성하고 저장
            ChannelTopic topic = new ChannelTopic(uuid);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topicAndRoom.add(uuid, roomId);
            topicStore.set(uuid,topic);
        } else { // topic 있음
            // roomId 있는지 확인
            if (isRoom.isEmpty() || !isRoom.get()) { // roomId 존재 안함
                // roomId 생성해서 저장하고 반환
                topicAndRoom.add(uuid, roomId);
            }
        }
        return new ChatResponse(200, roomId);
    }

    public ChannelTopic getTopic(String uuid) {
        return (ChannelTopic) topicStore.get(uuid);
    }


    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
//    public ChatRoom createChatRoom(String name) {
//        ChatRoom chatRoom = ChatRoom.create(name);
//        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//        return chatRoom;
//    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
//    public void enterChatRoom(String roomId) {
//        ChannelTopic topic = topics.get(roomId);
//        if (topic == null) {
//            topic = new ChannelTopic(roomId);
//            redisMessageListener.addMessageListener(redisSubscriber, topic);
//            topics.put(roomId, topic);
//        }
//    }

//    public ChannelTopic getTopic(String roomId) {
//        return topics.get(roomId);
//    }

    // 테스트
//    public void redisTest() {
//        Map<String, ChatRoom> roomsMap = opsHashChatRoom.entries("CHAT_ROOM");
//        roomsMap.forEach((key, value) -> {
//            System.out.println("Key: " + key);
//            System.out.println("Value: " + value.toString());
//        });
//    }
}
