package kr.co.shortenurlservice.domain;

public class NotFoundShortenUrlException extends RuntimeException {

    public NotFoundShortenUrlException() {
        super("단축 URL을 찾지 못했습니다.");
    }

    public NotFoundShortenUrlException(String message) {
        super(message);
    }
}
