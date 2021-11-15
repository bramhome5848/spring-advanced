package hello.springadvanced.trace.threadlocal;

import hello.springadvanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> fieldService.logic("userA");
        Runnable userB = () -> fieldService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();    //A실행
        //sleep(2000); //동시성 문제 발생 X
        sleep(100); //동시성 문제 발생O
        threadB.start();    //B실행
        sleep(3000); //메인 쓰레드 종료 대기 -> B가 종료되기 전 main 쓰레드가 종료되는 것을 방지
        log.info("main exit");

        /**
         * 1. Thread-A 는 userA 를 nameStore 에 저장
         * 2. Thread-B 는 userB 를 nameStore 에 저장
         * 3. Thread-A 는 userB 를 nameStore 에서 조회
         * 4. Thread-B 는 userB 를 nameStore 에서 조회
         * 여러 쓰레드가 동시에 같은 인스턴스의 필드 값을 변경하면서 발생하는 문제 -> 동시성 문제
         * 동시성 문제는 여러 쓰레드가 같은 인스턴스의 필드에 접근해야 하기 때문에 트래픽이 적은 상황에서는 확률상 잘 나타나지 않고
         * 트래픽이 점점 많아질 수 록 자주 발생
         * 특히 스프링 빈 처럼 싱글톤 객체의 필드를 변경하며 사용할 때 이러한 동시성 문제가 발생
         *
         * 참고
         * 동시성 문제는 지역 변수에서는 발생하지 않음 -> 지역 변수는 쓰레드마다 각각 다른 메모리 영역에 할당
         * 동시성 문제가 발생하는 곳은 같은 인스턴스의 필드(주로 싱글톤에서 자주 발생), 또는 static 같은 공용 필드
         * 동시성 문제는 값을 읽기만 하면 발생하지 않음. 어디선가 값을 변경하기 때문에 발생
         */
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
