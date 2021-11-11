package hello.springadvanced.app.v2;

import hello.springadvanced.trace.TraceStatus;
import hello.springadvanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    private final HelloTraceV2 trace;

    @GetMapping("/v2/request")
    public String request(String itemId) {

        TraceStatus status = null;

        //trace.exception() 예외처리를 위한 try-catch 까지 필요..
        //try-catch 블록 모두에서 사용할 수 있도록 status 선언 필요..
        //throw e -> 로그 때문에 예외가 사라지지 않도록 처리까지 필요..
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(status.getTraceId(), itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;    //예외를 꼭 다시 던져줘야 함 -> 던지지 않으면 예외를 먹어버림
        }
    }
}
