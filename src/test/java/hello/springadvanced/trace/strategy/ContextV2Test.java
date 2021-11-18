package hello.springadvanced.trace.strategy;

import hello.springadvanced.trace.strategy.code.strategy.ContextV2;
import hello.springadvanced.trace.strategy.code.strategy.Strategy;
import hello.springadvanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.springadvanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1() {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    /**
     * 전략 패턴 익명 내부 클래스
     */
    @Test
    void strategyV2() {
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행"); }
        });
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행"); }
        });
    }

    /**
     *전략 패턴 익명 내부 클래스2, 람다
     */
    @Test
    void strategyV3() {
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직1 실행"));
        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }

    /**
     * 실행할 때 마다 전략을 유연하게 변경 가능
     * 단점 -> 실행할 때 마다 전략을 계속 지정해야 함
     * 원하는 것은 애플리케이션 의존 관계를 설정하는 것 처럼 선 조립, 후 실행이 아니고
     * 단순히 코드를 실행할 때 변하지 않는 템플릿이 있고, 그 템플릿 안에서 원하는 부분만 다른 코드를 실행하고 싶을 뿐
     * -> 실행 시점에 유연하게 실행 코드 조각을 전달하는 ContextV2 가 더 적합
     */
}
