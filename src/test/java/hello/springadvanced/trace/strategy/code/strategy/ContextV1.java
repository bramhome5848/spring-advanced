package hello.springadvanced.trace.strategy.code.strategy;

import hello.springadvanced.trace.strategy.code.strategy.Strategy;
import lombok.extern.slf4j.Slf4j;

/**
 * 필드에 전략을 보관하는 방식
 * ContextV1 은 변하지 않는 로직을 가지고 있는 템플릿 역할을 하는 코드
 * 전략 패턴에서는 -> 컨텍스트(문맥)이라 함
 * 컨텍스트(문맥)는 크게 변하지 않지만, 그 문맥 속에서 strategy 를 통해 일부 전략이 변경됨
 *
 * Context 는 내부에 Strategy strategy 필드를 가지고 있음
 * 해당 필드에 변하는 부분인 Strategy 의 구현체를 주입
 * 전략 패턴의 핵심은 Context 는 Strategy 인터페이스에만 의존
 * -> Strategy 의 구현체를 변경하거나 새로 만들어도 Context 코드에는 영향을 주지 않음
 * ** 스프링에서 의존관계 주입에서 사용하는 방식이 바로 전략 패턴 **
 */
@Slf4j
public class ContextV1 {

    private final Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call(); //위임
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
