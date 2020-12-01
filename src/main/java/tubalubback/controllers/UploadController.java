package tubalubback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tubalubback.services.S3Service;

import javax.websocket.server.PathParam;

@RestController
@CrossOrigin
public class UploadController {

    @Autowired
    private S3Service s3;

    @GetMapping("/upload")
    public ResponseEntity<String> getPresignedUrl(@PathParam("filename") String filename) {
        return ResponseEntity.ok(s3.presignPutUrl(filename));
    }

}
