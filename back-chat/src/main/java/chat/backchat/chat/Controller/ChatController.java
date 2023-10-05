package chat.backchat.chat.Controller;

import chat.backchat.chat.Domain.ChatResponse;
import chat.backchat.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 생성 (회원가입과 동시에 uuid 생성(채팅방 & 컨테이너 아이디) 이므로 나중에 없애기)
//    @PostMapping("/room")
//    @ResponseBody
//    public ChatRoom createRoom(@RequestBody String name) {
//        return chatRoomRepository.createChatRoom(name);
//    }

    // 실제 코드 편집기에서 보낼 때 채팅방 생성
    @PostMapping("/chat/{uuid}/{questionId}")
    public ChatResponse createChatRoom(@PathVariable String uuid, @PathVariable String questionId) {
        return chatRoomRepository.findRoomId(uuid, questionId);
    }

}

