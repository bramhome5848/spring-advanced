package hello.springadvanced.trace.logtrace;

import hello.springadvanced.trace.TraceId;
import hello.springadvanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * HelloTraceV2 와 거의 같은 기능
 * TraceId 를 동기화 하는 부분만 파라미터가 아닌 TraceId traceIdHolder 필드를 사용
 *
 * 동시성 문제
 * FiledLogTrace -> 싱글톤 스프링 빈으로 LogTraceConfig 에서 등록되어 있음
 * 해당 인스턴스의 필드를 여러 쓰레드가 동시에 접근하기 때문에 출력시 log 가 엉키는 동시성 문제가 발생
 */
@Slf4j
public class FieldLogTrace implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private TraceId traceIdHolder;  //traceId 동기화, 동시성 이슈 발생

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();  //로그를 시작할 때
        TraceId traceId = traceIdHolder;
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        Long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if(e == null) {
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}",
                    traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
                    e.toString());
        }

        releaseTraceId();   //로그를 종료할 때
    }

    /**
     * TraceId 를 새로 만들거나 앞선 로그의 TraceId 를 참고해서 도익화하고 level 증가
     */
    private void syncTraceId() {
        if(traceIdHolder == null) {
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNextId();
        }
    }

    /**
     * level 을 하나 감소 시키거나 최초 호출의 경우 traceId 제거
     */
    private void releaseTraceId() {
        if(traceIdHolder.isFirstLevel()) {
            traceIdHolder = null;   //destroy
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i=0 ; i<level ; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
