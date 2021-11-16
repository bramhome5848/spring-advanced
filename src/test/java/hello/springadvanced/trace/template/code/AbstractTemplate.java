package hello.springadvanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 템플릿 -> 기준이 되는 거대한 틀
 * 템플릿이라는 틀에 변하지 않는 부분을 몰아두고 일부 변하는 부분을 별도로 호출해서 해결
 * 변하지 않는 부분인 시간 측정 로직을 몰아둔 템플릿에 몰아둠
 * 템플릿 안에서 변하는 부분은 call() 메서드를 호출해서 처리
 * 템플릿 메서드 패턴은 부모 클래스에 변하지 않는 템플릿 코드를 작성
 * 변하는 부분은 자식 클래스에 두고 상속과 오버라이딩을 사용해서 처리
 */
@Slf4j
public abstract class AbstractTemplate {

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        call(); //상속
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    protected abstract void call();
}
