package hello.springadvanced.trace.logtrace;

import hello.springadvanced.trace.TraceStatus;

/**
 * 로그를 출력하는 모든 메서드에 TraceId 파라미터를 추가해야 하는 문제가 발생
 * 문제를 해결할 목적으로 새로운 로그 추적기
 * 다양한 구현제로 변경할 수 있도록 인터페이스 구성
 * 인터페이스에서는 최소한의 기능만 정의
 */
public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}
