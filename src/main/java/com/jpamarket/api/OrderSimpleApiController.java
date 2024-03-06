package com.jpamarket.api;

import com.jpamarket.domain.Address;
import com.jpamarket.domain.Order;
import com.jpamarket.domain.OrderStatus;
import com.jpamarket.repository.OrderRepository;
import com.jpamarket.repository.OrderSearch;
import com.jpamarket.repository.SimpleOrderQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
     * 혹여나 LAZY -> EAGER 로 바꾸려 절대 하지말 것.
     * @return
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return all;
    }

    /**
     * DTO로 변환하여 api 스펙에 맞게 리턴.
     *
     * 하지만 쿼리가 너무 많이 나간다. order, member, delivery 까지 3개 건드려야함. (각각 id별로 조회해서 이 경우에는 총 5번나감)
     * n+1 의 문제..
     * order -> 1개 쿼리로 2개 row 반환
     * member -> 각 2개 쿼리
     * delivery -> 각 2개 쿼리
     *
     * ---> 'fetch join'으로 튜닝하면 된다
     * @return
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        return orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * fetch join 활용
     * 쿼리 1개 나간다.
     * 단점은 모든 컬럼을 select 해오는 단점이 있음.
     * @return
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * dto를 따로 만들었지만 dto맞게 select 해오기 때문에 재사용성이 떨어진다.
     * 이런 trade-off 를 가지고 있다. 근데 과연 v3, v4가 차이가 많이 날까?
     *
     * @return
     */
    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> orderV4() {
        return orderRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getOrderStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
