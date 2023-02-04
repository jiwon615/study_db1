package com.study.jdbc.error;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 *  체크예외들은 무조건 예외를 잡아서 처리 / 던져하는데 여기서는 던지고 있음
 *  왜 throws SQLException 같이 던지는 코드 부분이 문제가 될까?
 *      - 서비스, 컨트롤러에서 java.sql.SQLException에 의존하기 때문
 *      - 만약 JDBC 기술 아닌 다른 기술(ex. JPA) 로 변경한다면? 의존된 코드를 JPA에 맞게 모두 고쳐야함
 */
@Slf4j
public class CheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new Service();
        public void request() throws SQLException, ConnectException {
            service.call();
        }
    }

    static class Service {
        Repository repository = new Repository();

        public void call() throws SQLException, ConnectException {
            repository.callSqlException();
            repository.callConnectException();
        }
    }

    static class Repository {
        public void callSqlException() throws SQLException {
            throw new SQLException("sql 예외-체크예외");
        }

        public void callConnectException() throws ConnectException {
            throw new ConnectException("연결 실패-체크예외");
        }
    }
}
