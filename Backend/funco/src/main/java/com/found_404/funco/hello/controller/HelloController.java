package com.found_404.funco.hello.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.hello.dto.request.HelloRequest;
import com.found_404.funco.hello.dto.response.HelloResponse;
import com.found_404.funco.hello.service.HelloService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/v1/hello")
@RequiredArgsConstructor
@RestController
public class HelloController {

	private final HelloService helloService;

	@GetMapping
	public String hello() {
		return "이 메세지가 보인다면, 서버는 살아있다.";
	}

	@GetMapping("/{helloId}")
	public ResponseEntity<HelloResponse> getHello(@PathVariable final Long helloId) {
		return ResponseEntity.ok(helloService.readHello(helloId));
	}

	@PostMapping
	public ResponseEntity<Void> addHello(@RequestBody HelloRequest helloRequest) {
		helloService.createHello(helloRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{helloId}")
	public ResponseEntity<?> modifyHello(@RequestBody HelloRequest helloRequest, @PathVariable final Long helloId) {
		helloService.updateHello(helloRequest, helloId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{helloId}")
	public ResponseEntity<Void> removeHello(@PathVariable Long helloId) {
		helloService.deleteHello(helloId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
