package com.jpamarket.api;

import com.jpamarket.domain.Order;
import com.jpamarket.repository.OrderRepository;
import com.jpamarket.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  xToOne 의 상황 ( ManyToOne, OneToOne )
 *  Order
 *  Order -> Member
 *  Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * v1
     * Entity 그대로 반환 (당연하지만 실무에서 절대 사용 x)
     * 무한루프.. (Order -> Member -> Order -> Member....)  -> 해결하려면 양방향 걸리면 @JsonIgnore 사용하면 되긴 함
     *
     * Order.member 는 LAZY 로딩으로 되어 있는데, DB의 접근 전까지 프록시 객체를 가짜로 넣어놓음 -> hibernate bytebuddy 관련 error 발생
     * hibernate 5 모듈이 필요함. -> json 아무것도 하지말라고 할 수 있긴 함
     *
     * @return
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());
        return all;
    }
}
