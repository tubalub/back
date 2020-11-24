package tubalubback.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import tubalubback.TubalubBackApplication;

@RestController
@RequestMapping("/music")
public class MusicController {
	public static DataInputStream dataStream;
	public static int pos = -1;
	public static List<String> files = new ArrayList<>();
	public static List<ServletOutputStream> streams = new ArrayList<>();
	
	public void startStreaming() throws IOException {
		System.out.println("starting stream");
		if(files.isEmpty()) {
			files.add("D:\\Home\\Music\\2AM\\1집 Saint o'clock\\13 이 노래.mp3");
			files.add("D:\\Home\\Music\\Taylor Swift\\Love Story\\00 Love Story.mp3");
			files.add("D:\\Home\\Music\\Tablo\\열꽃_ Part 1\\02 나쁘다(feat_ 진실).mp3");
		}
		if (++pos >= files.size()) {
			pos=0;
		}
		dataStream = new DataInputStream(new FileInputStream(files.get(pos)));
	}
	
	@RequestMapping("/stream/next") 
	public ResponseEntity<Void> nextSong(){
		System.out.println("next song");
		streams.forEach(stream -> {
			try {
				stream.flush();
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return new ResponseEntity<>(HttpStatus.OK);
		
	}

	
	@GetMapping("/stream")
	public ResponseEntity<StreamingResponseBody> streamMusic(HttpServletResponse response) throws IOException {
		if(dataStream==null) {
			startStreaming();
		}
		StreamingResponseBody stream = out ->{
			System.out.println("transfering stream");
			if(dataStream.available()==0) {
				startStreaming();
			}
			IOUtils.copyLarge(dataStream, response.getOutputStream());
			streams.add(response.getOutputStream());
		};
		return new ResponseEntity<>(stream, HttpStatus.OK);
	}

}
