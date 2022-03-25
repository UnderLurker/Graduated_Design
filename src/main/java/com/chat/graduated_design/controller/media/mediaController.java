package com.chat.graduated_design.controller.media;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.handler.NonStaticResourceHttpRequestHandler;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media/preview")
public class mediaController {
    @Autowired
    private fileDataServiceImpl fileDataService;
    @Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @GetMapping("/{fileStorageNo}")
    public void mediaPreview(@PathVariable("fileStorageNo") Integer fileStorageNo,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception{
        FileStorage fileStorage=fileDataService.getById(fileStorageNo);
        String root=null;
        try {
            root=ResourceUtils.getURL("classpath:static/image").getPath().replace("%20", " ").substring(1);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        root+="/"+fileStorage.getPath()+"/"+fileStorage.getUuid();

        Path filePath = Paths.get(root);
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!mimeType.isEmpty()) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

}
