package com.study.jdbc.service;

import com.study.jdbc.domain.Member;
import com.study.jdbc.repository.MemberRepositoryV1;
import com.study.jdbc.repository.MemberRepositoryV2;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 커네셕 파라미터 연동, 풀을 고려한 종료
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection(); // 커넥션을 파라미터로 넘겨야하므로, 여기서 최초 연결
        try {
            con.setAutoCommit(false);
            executeAccountTransfer(con, fromId, toId, money);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException();
        } finally {
            release(con);
        }
    }

    private void executeAccountTransfer(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

    private static void validation(Member toMember) {
        if ("ex".equals(toMember.getMemberId())) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
