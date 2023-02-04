package com.study.jdbc.error;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 예외 전환 - 리토지토리에서 체크 예외 발생하면 RuntimeException으로 전환하여 예외 처리
 */
@Slf4j
public class UncheckedAppTest {

    @Test
    void checked() {
        CheckedAppTest.Controller controller = new CheckedAppTest.Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            //e.printStackTrace();
            log.info("ex", e);
        }
    }

    static class Controller {
        CheckedAppTest.Service service = new CheckedAppTest.Service();
        public void request() throws SQLException, ConnectException {
            service.call();
        }
    }

    static class Service {
        CheckedAppTest.Repository repository = new CheckedAppTest.Repository();

        public void call() throws SQLException, ConnectException {
            repository.callSqlException();
            repository.callConnectException();
        }
    }

    static class Repository {
        public void callSqlException() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e); // 기존 예외(e) 포함해야 함!
            }
        }

        public void callConnectException() throws ConnectException {
            throw new RuntimeConnectionException("연결 실패-체크예외");
        }

        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {  // Throwable cuase 는 기존 예외 포함받음
            super(cause);
        }
    }

    static class RuntimeConnectionException extends RuntimeException {
        public RuntimeConnectionException(String message) {
            super(message);
        }
    }
}
