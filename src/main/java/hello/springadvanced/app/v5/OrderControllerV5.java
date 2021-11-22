package hello.springadvanced.app.v5;

import hello.springadvanced.trace.callback.TraceCallback;
import hello.springadvanced.trace.callback.TraceTemplate;
import hello.springadvanced.trace.logtrace.LogTrace;
import hello.springadvanced.trace.template.AbstractTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 한계
 * 지금까지 방식의 한계는 아무리 최적화를 해도 결국 로그 추적기를 적용하기 위해서 원본 코드의 수정이 필요
 * 클래스가 수백개이면 수백개를 더 힘들게 수정하는가 조금 덜 힘들게 수정하는가의 차이가 있을 뿐, 본질적으로 코드를 다 수정해야 하는 것은 마찬가지
 * 지금부터 원본 코드를 손대지 않고 로그 추적기를 적용할 수 있는 방법 -> 프록시에 대한 이해가 필요
 */
@RestController
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);  //딱 한번 생성됨
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {

        return template.execute("OrderController.request()", new TraceCallback<>() {
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        });
    }
}
