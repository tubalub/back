package tubalubback.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/music")
public class MusicController {
	
	@GetMapping("/stream")
	public ResponseEntity<StreamingResponseBody> streamMusic(HttpServletResponse response) {
		final File testFile = new File("D:\\Home\\Music\\2AM\\1집 Saint o'clock\\13 이 노래.mp3");
		StreamingResponseBody stream = out ->{
			final InputStream output = new FileInputStream(testFile);
			output.transferTo(response.getOutputStream());
		};
		return new ResponseEntity<>(stream, HttpStatus.OK);
	}

}
