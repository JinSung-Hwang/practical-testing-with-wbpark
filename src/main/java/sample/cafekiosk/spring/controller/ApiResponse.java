package sample.cafekiosk.spring.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
  private int code;
  private HttpStatus status;
  private String message;
  private T data;

  public ApiResponse(HttpStatus status, String message, T data) {
    this.code = status.value();
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
    return new ApiResponse<>(httpStatus, message, data);
  }

  public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
    return of(httpStatus, httpStatus.name(), data);
  }

  public static <T> ApiResponse<T> ok(T data) { // note: 자주 쓰일만한 패턴의 함수를 스태틱 팩토리 메서드로 만듬
    return of(HttpStatus.OK, HttpStatus.OK.name(), data);
  }

}