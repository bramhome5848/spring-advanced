package hello.springadvanced.trace;

import java.util.UUID;

/**
 * *** 로그 추적기 요구사항 ***
 *
 * 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력
 * 애플리케이션의 흐름을 변경하면 안됨
 * -- 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨 메서드 호출에 걸린 시간
 * 정상 흐름과 예외 흐름 구분
 * -- 예외 발생시 예외 정보가 남아야 함
 * 메서드 호출의 깊이 표현
 * HTTP 요청을 구분
 * -- HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
 * -- 트랜잭션 ID (DB 트랜잭션 X), 여기서는 하나의 HTTP 요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라 함
 *
 * TraceId 클래스 예
 * [796bccd9] OrderController.request() //트랜잭션 ID:796bccd9, level:0
 * [796bccd9] |-->OrderService.orderItem() //트랜잭션 I ㄴD:796bccd9, level:1
 * [796bccd9] | |-->OrderRepository.save()//트랜잭션 ID:796bccd9, level:2
 */
public class TraceId {

    private String id;  // 트랜잭션 id
    private int level;  // 깊이

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    public TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
