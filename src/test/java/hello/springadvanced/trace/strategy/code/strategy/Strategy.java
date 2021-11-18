package hello.springadvanced.trace.strategy.code.strategy;

/**
 * 전략 패턴은 변하지 않는 부분을 Context 라는 곳에 두고,
 * 변하는 부분을 Strategy 라는 인터페이스를 만들고 해당 인터페이스를 구현하도록 해서 문제를 해결
 * 상속이 아니라 위임으로 문제를 해결
 * 전략 패턴에서 Context 는 변하지 않는 템플릿 역할을 하고, Strategy 는 변하는 알고리즘 역할
 */
public interface Strategy {
    void call();
}
