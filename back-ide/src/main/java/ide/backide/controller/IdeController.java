package ide.backide.controller;

import ide.backide.domain.RequestCodeDTO;
import ide.backide.service.FileIOService;
//import ide.backide.service.IdeService;
import ide.backide.service.JavaCompilerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IdeController {
    private final JavaCompilerService javaCompilerService;
    private final FileIOService fileIOService;
    @PostMapping("/{questionId}")
    public void getCompileResult(@PathVariable String questionId, @RequestBody RequestCodeDTO requestCode) throws Exception {
        fileIOService.saveFile(requestCode.getRequestCode(), questionId);
        javaCompilerService.compiler(questionId);
    }

    @GetMapping("/{questionId}")
    public String getSavedFile(@PathVariable String questionId) throws IOException {
        return fileIOService.findFileByName(questionId);
    }

}
