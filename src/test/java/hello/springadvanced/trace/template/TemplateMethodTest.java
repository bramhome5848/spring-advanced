package hello.springadvanced.trace.template;

import hello.springadvanced.trace.template.code.AbstractTemplate;
import hello.springadvanced.trace.template.code.SubClassLogic1;
import hello.springadvanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

    /**
     * logic1() 과 logic2() 는 시간을 측정하는 부분과 비즈니스 로직을 실행하는 부분이 함께 존재
     * 변하는 부분: 비즈니스 로직
     * 변하지 않는 부분: 시간 측정
     */
    @Test
    void templateMethodV0() {
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
     * 템플릿 메서드 패턴 적용
     * 템플릿 메서드 패턴은 다형성을 사용해서 변하는 부분과 변하지 않는 부분을 분리하는 방법
     */
    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();
        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }

    /**
     * 템플릿 메서드 패턴,익명 내부 클래스 사용
     * 템플릿 메서드 패턴은 SubClassLogic1 , SubClassLogic2 처럼 클래스를 계속 만들어야 하는 단점
     * 익명 내부 클래스를 사용하면 객체 인스턴스를 생성하면서 동시에 생성할 클래스를 상속 받은 자식 클래스를 정의
     * 이 클래스는 SubClassLogic1 처럼 직접 지정하는 이름이 없고 클래스 내부에 선언되는 클래스여서 익명 내부 클래스
     */
    @Test
    void templateMethodV2() {
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직1 실행");
            }
        };

        //자바가 임의로 만들어주는 익명 내부 클래스 이름은 TemplateMethodTest$1
        log.info("클래스 이름1={}", template1.getClass());
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직2 실행");
            }
        };

        //자바가 임의로 만들어주는 익명 내부 클래스 이름은 TemplateMethodTest$2
        log.info("클래스 이름2={}", template2.getClass());
        template2.execute();
    }
}
