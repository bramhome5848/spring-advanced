package hello.springadvanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 쓰레드 로컬은 해당 쓰레드만 접근할 수 있는 특별한 저장소
 * 물건 보관 창구 -> 여러 사람이 같은 물건 보관 창구를 사용하더라도 창구 직원은 사용자를 인식해서 사용자별로 확실하게 물건을 구분
 * UserA, UserA 모두 창구 직원을 통해서 물건을 보관, 직원이 사용자에 따라 보관한 물건을 구분해주는 것
 */
@Slf4j
public class ThreadLocalService {

    /**
     * String type -> ThreadLocal<String> 으로 변경
     * Java 는 언어차원에서 쓰레드 로컬을 지원하기 위한 java.lang.ThreadLocal 클래스를 제공
     */
    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    /**
     * ThreadLocal.set(xxx) : 값 저장
     * ThreadLocal.get() : 값 조회
     * ThreadLocal.remove() : 값 제거
     * 주의
     * 해당 쓰레드가 쓰레드 로컬을 모두 사용하고 나면 remove()를 호출해서 저장된 값을 제거해야 함.
     */
    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}", name, nameStore.get());
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore={}",nameStore.get());
        return nameStore.get();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
