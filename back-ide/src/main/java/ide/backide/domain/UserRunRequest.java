package ide.backide.domain;

import lombok.Data;

@Data
public class UserRunRequest {
    private String questionId;
    private String uuid;
    private String requestCode;
    private String language;
}
