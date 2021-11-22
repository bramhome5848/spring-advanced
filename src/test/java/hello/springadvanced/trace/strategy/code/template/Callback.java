package hello.springadvanced.trace.strategy.code.template;

/**
 * 콜백 정의
 * > 프로그래밍에서 콜백(callback) 또는 콜애프터 함수(call-after function)는
 * 다른 코드의 인수로서 넘겨주는 실행 가능한 코드.
 * 콜백을 넘겨받는 코드는 이 콜백을 필요에 따라 즉시 실행할 수도 있고, 아니면 나중에 실행할 수도 있음
 *
 * 템플릿 콜백 패턴
 * 스프링에서는 ContextV2 와 같은 방식의 전략 패턴을 템플릿 콜백 패턴이
 * 전략 패턴에서 Context 가 템플릿 역할을 하고, Strategy 부분이 콜백으로 넘어온다 생각하면 됨
 * 템플릿 콜백 패턴은 GOF 패턴은 아니고, 스프링 내부에서 이런 방식을 자주 사용하기 때문에, 스프링 안에서만 이렇게 명칭
 * 전략 패턴에서 템플릿과 콜백 부분이 강조된 패턴 -> 템플릿 콜백 패턴
 * 스프링에서는 JdbcTemplate , RestTemplate , TransactionTemplate , RedisTemplate 처럼 다양한 템플릿 콜백 패턴이 사용
 * 스프링에서 이름에 XxxTemplate 가 있다면 템플릿 콜백 패턴으로 만들어져 있다 생각하면 됨
 */
public interface Callback {
    void call();
}
