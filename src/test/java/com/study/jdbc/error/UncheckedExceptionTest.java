package com.study.jdbc.error;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 언체크 예외 테스트: 예외를 잡아서 처리하지 않아도 됨 (생략 가능- 자동으로 던저주기 때문)
 */
@Slf4j
public class UncheckedExceptionTest {

    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUnCheckedException.class);
    }

    static class MyUnCheckedException extends RuntimeException {
        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();

        // 필요한 경우 예외를 잡아서 처리
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUnCheckedException e) {
                log.info("예외 처리, message={}", e.getMessage(), e);

            }
        }

        // 예외를 잡지 않아도 됨. 자연스레 위로 올라감
        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        public void call() {
            throw new MyUnCheckedException("ex!");
        }
    }
}
