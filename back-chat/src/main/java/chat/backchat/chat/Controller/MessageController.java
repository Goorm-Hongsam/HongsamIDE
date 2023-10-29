package chat.backchat.chat.Controller;

import chat.backchat.chat.Domain.ChatMessage;
import chat.backchat.chat.Service.ChatService;
import chat.backchat.chat.pubsub.RedisPublisher;
import chat.backchat.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId(),message.getUuid());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");

            log.info("입장 메세지 전송");
            redisPublisher.publish(chatRoomRepository.getTopic(message.getUuid()), message);
        } else {
            log.info("메세지 전송");

            StopWatch sw = new StopWatch("메세지 전송 속도 측정");
            sw.start();
            redisPublisher.publish(chatRoomRepository.getTopic(message.getUuid()), message);
            sw.stop();
            System.out.println(sw.prettyPrint());

            chatService.saveMessage(message);
        }

    }

//     메세지 불러오기
    @GetMapping("/chat/message/{roomId}")
    public List<ChatMessage> loadMessage(@PathVariable String roomId) {
        return chatService.loadMessage(roomId);
    }


    /**
     * stomp 버전
     */
//    private final SimpMessageSendingOperations sendingOperations;
//
//    @MessageMapping("/chat/message") // pub/chat/message가 됨
//    public void enter(ChatMessage message) {
//        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
//            message.setMessage(message.getSender() + "님이 들어옴.");
//        }
//        sendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
//    }

}

