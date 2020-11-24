package tubalubback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tubalubback.services.S3Service;

import javax.websocket.server.PathParam;

@RestController
public class UploadController {

    @Autowired
    private S3Service s3;

    @GetMapping("/upload")
    public ResponseEntity<String> getPresignedUrl(@PathParam("filename") String filename) {
        if (filename.length() < 1) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(s3.presignPutUrl(filename));
    }

}
