package hello.springadvanced.trace.logtrace;

import hello.springadvanced.trace.TraceId;
import hello.springadvanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * 쓰레드 로컬 - 주의사항
 * 쓰레드 로컬의 값을 사용 후 제거하지 않고 그냥 두면 WAS(톰캣)처럼 쓰레드 풀을 사용하는 경우 심각한 문제가 발생할 수 있음
 *
 * 사용자A 저장 요청
 * 1. 사용자A 가 저장 HTTP 를 요청
 * 2. WAS는 쓰레드 풀에서 쓰레드를 하나 조회
 * 3. 쓰레드 thread-A 가 할당
 * 4. thread-A 는 사용자A 의 데이터를 쓰레드 로컬에 저장
 * 5. 쓰레드 로컬의 thread-A 전용 보관소에 사용자A 데이터를 보관
 *
 * 사용자A 저장 요청 종료
 * 1. 사용자A 의 HTTP 응답이 끝남
 * 2. WAS 는 사용이 끝난 thread-A 를 쓰레드 풀에 반환, 쓰레드 생성 비용은 비싸기 때문에 쓰레드를 제거하지 않고, 보통 쓰레드 풀을 통해서 쓰레드를 재사용
 * 3. thread-A 는 쓰레드풀에 아직 살아있고 따라서 쓰레드 로컬의 thread-A 전용 보관소에 사용자A 데이터도 함께 살아있게 됨
 *
 * 사용자B 조회 요청
 * 1. 사용자B 가 조회를 위한 새로운 HTTP 요청
 * 2. WAS 는 쓰레드 풀에서 쓰레드를 하나 조회
 * 3. 쓰레드 thread-A 가 할당 (다른 쓰레드가 할당될 수 있음)
 * 4. thread-A 는 쓰레드 로컬에서 데이터를 조회
 * 5. 쓰레드 로컬은 thread-A 전용 보관소에 있는 사용자A 값을 반환
 * 6. 결과적으로 사용자A 값이 반환
 * 7. 사용자B 는 사용자A 의 정보를 조회
 *
 * *** 사용자A 의 요청이 끝날 때 쓰레드 로컬의 값을 ThreadLocal.remove() 를 통해서 꼭 제거 ***
 *
 */
@Slf4j
public class ThreadLocalLogTrace implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    //private TraceId traceIdHolder;  //traceId 동기화, 동시성 이슈 발생
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
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

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if(traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if(traceId.isFirstLevel()) {
            traceIdHolder.remove();   //destroy
        } else {
            traceIdHolder.set(traceId.createPreviousId());
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
