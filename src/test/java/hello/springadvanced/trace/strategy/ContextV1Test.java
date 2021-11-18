package hello.springadvanced.trace.strategy;

import hello.springadvanced.trace.strategy.code.strategy.ContextV1;
import hello.springadvanced.trace.strategy.code.strategy.Strategy;
import hello.springadvanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.springadvanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    /**
     * 전략 패턴 적용
     * 의존관계 주입을 통해 ContextV1 에 Strategy 의 구현체인 strategyLogic1 를 주입하
     * Context 안에 원하는 Strategy 를 주입 -> 원하는 모양으로 조립을 완료하고 난 다음에 context1.execute() 를 호출해서 실행
     */
    @Test
    void strategyV1() {
        Strategy strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1); //Context 에 원하는 Strategy 구현체를 주입
        context1.execute(); //실행 -> context 로직 시작 -> 주입받은 strategy 의 call 실행

        Strategy strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();
    }

    /**
     * 전략 패턴 익명 내부 클래스1
     */
    @Test
    void strategyV2() {
        Strategy strategyLogic1 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("strategyLogic1={}", strategyLogic1.getClass());
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        Strategy strategyLogic2 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        };
        log.info("strategyLogic2={}", strategyLogic2.getClass());
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();
    }

    /**
     * 전략 패턴 익명 내부 클래스2
     */
    @Test
    void strategyV3() {
        ContextV1 context1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context1.execute();

        ContextV1 context2 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
        context2.execute();
    }

    /**
     * 전략 패턴, 람다
     */
    @Test
    void strategyV4() {
        ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직1 실행"));
        context1.execute();

        ContextV1 context2 = new ContextV1(() -> log.info("비즈니스 로직2 실행"));
        context2.execute();
    }

    /**
     * 정리
     * 변하지 않는 부분을 Context 에 두고 변하는 부분을 Strategy 를 구현
     * 그리고 Context 의 내부 필드에 Strategy 를 주입해서 사용
     *
     * 선 조립, 후 실행
     * ** Context 의 내부 필드에 Strategy 를 두고 사용하는 부분 **
     * Context 와 Strategy 를 실행 전에 원하는 모양으로 조립, Context 를 실행하는 선 조립, 후 실행 방식에서 매우 유용
     * Context 와 Strategy 를 한번 조립하고 나면 이후로는 Context 를 실행하기만 하면 됨
     *
     * 스프링으로 애플리케이션을 개발할 때 애플리케이션 로딩 시점에 의존관계 주입을 통해
     * 필요한 의존관계를 모두 맺어두고 난 다음에 실제 요청을 처리하는 것 과 같은 원리
     *
     * 단점은 Context 와 Strategy 를 조립한 이후에는 전략을 변경하기가 번거롭다는 점
     * 물론 Context 에 setter 를 제공해서 Strategy 를 넘겨 받아 변경하면 되지만
     * Context 를 싱글톤으로 사용할 때는 동시성 이슈 등 고려할 점이 많음
     * 전략을 실시간으로 변경해야 하면 차라리 Context 를 하나더 생성하고 그곳에 다른 Strategy 를 주입하는 것이 더 나은 선택일 수 있음
     */
}
