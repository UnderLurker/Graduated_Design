package com.chat.graduated_design.controller.audio;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class audioController {
    
    @RequestMapping("/audio/preview/{fileStorageNo}")
    public void audioPreview(@PathVariable("fileStorageNo") Integer fileStorageNo){
        
    }
}
