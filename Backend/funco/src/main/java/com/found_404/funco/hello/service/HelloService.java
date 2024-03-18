package com.found_404.funco.hello.service;

import org.springframework.stereotype.Service;

import com.found_404.funco.hello.domain.Hello;
import com.found_404.funco.hello.domain.repository.HelloRepository;
import com.found_404.funco.hello.dto.request.HelloRequest;
import com.found_404.funco.hello.dto.response.HelloResponse;
import com.found_404.funco.hello.exception.HelloErrorCode;
import com.found_404.funco.hello.exception.HelloException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelloService {

	private final HelloRepository helloRepository;

	public HelloResponse readHello(Long helloId) {
		return HelloResponse.builder().message(findHelloById(helloId).getMessage()).build();
	}

	public void createHello(HelloRequest helloRequest) {
		helloRepository.save(Hello.builder().message(helloRequest.message()).build());
	}

	private Hello findHelloById(Long helloId) {
		return helloRepository.findById(helloId)
			.orElseThrow(() -> new HelloException(HelloErrorCode.HELLO_NOT_FOUND));
	}

	@Transactional
	public void updateHello(HelloRequest helloRequest, Long helloId) {
		Hello hello = findHelloById(helloId);
		hello.updateMessage(helloRequest.message());
	}

	public void deleteHello(Long helloId) {
		helloRepository.delete(findHelloById(helloId));
	}

}
