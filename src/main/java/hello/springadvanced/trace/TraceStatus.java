package hello.springadvanced.trace;

/**
 * TraceStatus -> 로그를 시작할 때의 상태 정보
 */
public class TraceStatus {

    private TraceId traceId;    // 내부에 트랜잭션 ID, level
    private Long startTimeMs;   // 로그 시작시간 -> 로그 종료시 시작 시간을 기준으로 전체 수행 시간을 구함
    private String message;     // 시작시 사용한 메시지, 이후 로그 종료시에도 이 메시지를 사용해서 출력

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }

    public TraceId getTraceId() {
        return traceId;
    }
}
