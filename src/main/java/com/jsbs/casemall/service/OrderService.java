package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.OrderStatus;
import com.jsbs.casemall.dto.CartDto;
import com.jsbs.casemall.dto.CartItemDto;
import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.dto.OrderItemDto;
import com.jsbs.casemall.entity.*;
import com.jsbs.casemall.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final ProductModelRepository productModelRepository;

    // 주문 목록 가져오기
    @Transactional(readOnly = true)
    public OrderDto getOrder(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        OrderDto orderDto = getExistingOrderDto(user);
        if (orderDto != null) {
            return orderDto;
        } else {
            return OrderDto.builder().build(); // 빈 주문 정보 반환
        }
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderByOrderId(String orderId) {
        // 최정 주문 완료한것 만 보여주기
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(()-> new IllegalArgumentException("찾는 주문이 없습니다"));


        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
        OrderDto dto =  OrderDto.builder()
                .orderNo(order.getId())
                .totalPrice(order.getOrderItems().stream().mapToInt(OrderDetail::getTotalPrice).sum())
                .items(orderItemDtos)
                .userName(order.getUsers().getName())
                .orderId(order.getOrderId())
                .email(order.getUsers().getEmail())
                .phone(order.getUsers().getPhone())
                .pCode(order.getUsers().getPCode())
                .loadAddress(order.getUsers().getLoadAddr())
                .lotAddress(order.getUsers().getLotAddr())
                .detailAddress(order.getUsers().getDetailAddr())
                .orderTime(order.getOrderDate().toLocalDate())
                .payInfo(order.getPaymentMethod())
                .build();
        dto.tranceOther(dto.getPhone(),dto.getEmail());

        return dto;
    }

    // 결재 성공 내역
    @Transactional(readOnly = true)
    public List<OrderDto> history(String userId){
        Users users = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("회원을 찾을 수 없습니다"));
        List<Order> findStatuesOrders = orderRepository.findByUsersAndOrderStatus(users,OrderStatus.ORDER);
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : findStatuesOrders) {
            List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());

            OrderDto dto = OrderDto.builder()
                    .orderNo(order.getId())
                    .totalPrice(order.getOrderItems().stream().mapToInt(OrderDetail::getTotalPrice).sum())
                    .items(orderItemDtos)
                    .userName(order.getUsers().getName())
                    .orderId(order.getOrderId())
                    .email(order.getUsers().getEmail())
                    .phone(order.getUsers().getPhone())
                    .pCode(order.getUsers().getPCode())
                    .loadAddress(order.getUsers().getLoadAddr())
                    .lotAddress(order.getUsers().getLotAddr())
                    .detailAddress(order.getUsers().getDetailAddr())
                    .orderTime(order.getOrderDate().toLocalDate())
                    .build();
            dto.tranceOther(dto.getPhone(), dto.getEmail());

            orderDtos.add(dto);
        }
        log.info("사이즈 확인 : {} " , orderDtos.size());
        return orderDtos;
    }


    // 기존에 있는 오더 있는지 확인
    @Transactional(readOnly = true)
    public OrderDto getExistingOrderDto(Users user) {
        List<Order> existingOrders = findExistingOrders(user);

        if (!existingOrders.isEmpty()) {
            Order existingOrder = existingOrders.get(0);
            List<OrderItemDto> orderItemDtos = existingOrder.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());
            OrderDto dto =  OrderDto.builder()
                    .orderNo(existingOrder.getId())
                    .totalPrice(existingOrder.getOrderItems().stream().mapToInt(OrderDetail::getTotalPrice).sum())
                    .items(orderItemDtos)
                    .userName(user.getName())
                    .orderId(existingOrder.getOrderId())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .pCode(user.getPCode())
                    .loadAddress(user.getLoadAddr())
                    .lotAddress(user.getLotAddr())
                    .detailAddress(user.getDetailAddr())
                    .build();
            dto.tranceOther(dto.getPhone(),dto.getEmail());
            return dto;
        }
        return null; // 기존 주문이 없으면 null 반환
    }

    // 생성된 주문들 찾기
    private List<Order> findExistingOrders(Users user) {
        return orderRepository.findByUsersAndOrderStatus(user, OrderStatus.STAY);
    }

    // 주문 생성
    public OrderDto createOrder(String userId, List<Long> itemIds) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다"));

        List<OrderDetail> orderDetails = new ArrayList<>();
        int totalAmount = 0;

        // 기존 주문이 있는지 확인하고, 있는 경우 기존 주문에 항목 추가
        List<Order> existingOrders = findExistingOrders(user);
        Order order;
        if (!existingOrders.isEmpty()) {
            order = existingOrders.get(0);
        } else {
            order = Order.createOrder(user, new ArrayList<>());
            orderRepository.save(order);
        }

        for (Long cartItemId : itemIds) {
            CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new IllegalArgumentException("찾는 아이템이 없습니다"));
            Product product = cartItem.getProduct();
            ProductModel productModel = cartItem.getProductModel();

            // 기존 주문 항목 중 동일한 제품 및 모델이 있는지 확인
            boolean exists = order.getOrderItems().stream()
                    .anyMatch(item -> item.getProduct().getId().equals(product.getId()) &&
                            item.getProductModel().getId().equals(productModel.getId()));

            if (!exists) {
                // 기존 주문 항목이 없는 경우 새로 추가
                OrderDetail orderDetail = OrderDetail.createOrderDetails(product, productModel, cartItem.getCount());
                order.addOrderItem(orderDetail); // OrderDetail 객체를 Order 객체에 추가
                totalAmount += orderDetail.getTotalPrice();
            } else {
                // 기존 주문 항목에 있고 수량의 차이가 있다면
                OrderDetail existingOrderDetail = order.getOrderItems().stream()
                        .filter(item -> item.getProduct().getId().equals(product.getId()) &&
                                item.getProductModel().getId().equals(productModel.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("주문 항목을 찾을 수 없습니다."));

                int newCount = cartItem.getCount();
                int oldCount = existingOrderDetail.getCount();
                int difference = newCount - oldCount;

                if (difference > 0) {
                    existingOrderDetail.getProductModel().removeStock(difference);
                } else {
                    existingOrderDetail.getProductModel().addStock(Math.abs(difference));
                }

                existingOrderDetail.setCount(newCount);
                totalAmount += existingOrderDetail.getTotalPrice();
            }
        }

        orderRepository.save(order);

        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
        OrderDto dto = OrderDto.builder()
                .orderNo(order.getId())
                .orderId(order.getOrderId())
                .totalPrice(totalAmount)
                .items(orderItemDtos)
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .pCode(user.getPCode())
                .loadAddress(user.getLoadAddr())
                .lotAddress(user.getLotAddr())
                .detailAddress(user.getDetailAddr())
                .build();
        dto.tranceOther(dto.getPhone(),dto.getEmail());

        return dto;
    }

    // 주문 아이템 삭제
    public void removeOrderDetail(Long orderId, Long orderDetailId, String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        // 해당 주문을 찾기
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!order.getUsers().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 주문에 대한 권한이 없습니다.");
        }

        // 해당 주문 항목을 찾기
        OrderDetail orderDetail = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderDetailId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("주문 항목을 찾을 수 없습니다."));

        // 재고 롤백
        orderDetail.getProductModel().addStock(orderDetail.getCount());

        // 주문 항목 삭제
        order.getOrderItems().remove(orderDetail);

        // 주문 항목이 없으면 주문 삭제
        if (order.getOrderItems().isEmpty()) {
            orderRepository.delete(order);
        } else {
            orderRepository.save(order);
        }
    }

    // 수량 업데이트
    public void updateOrderItemQuantity(Long orderDetailId, int newCount) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new IllegalArgumentException("주문 항목을 찾을 수 없습니다."));

        int oldCount = orderDetail.getCount();
        int difference = newCount - oldCount;

        if (difference > 0) {
            orderDetail.getProductModel().removeStock(difference);
        } else {
            orderDetail.getProductModel().addStock(Math.abs(difference));
        }
        // 장바구니 항목도 업데이트
        CartItem cartItem = cartItemRepository.findByProductAndProductModel(orderDetail.getProduct(), orderDetail.getProductModel())
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다."));
        cartItem.updateCount(newCount);
        cartItemRepository.save(cartItem);

        orderDetail.setCount(newCount);
        orderDetailRepository.save(orderDetail);
    }


    // 유효성 검사
    @Transactional(readOnly = true)
    public boolean validatePayment(String orderId, int amount) {
        try {
            // 주문을 조회합니다.
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

            // 주문 상세 항목의 가격을 합산합니다.
            int price = 0;
            for (OrderDetail orderDetail : order.getOrderItems()) {
                price += orderDetail.getTotalPrice();
            }

            // 결제 금액 확인
            if (price == amount) {
                return true;
            } else {
                throw new IllegalArgumentException("결제 금액이 올바르지 않습니다.");
            }
        } catch (IllegalArgumentException e) {
            log.error("validatePayment 에서 발생: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("Unexpected error during payment validation: {}", e.getMessage());
            return false;
        }
    }


    //
    public void updateOrderWithPaymentInfo(String orderId, String paymentMethod, String payInfo) {
        try {
            Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new EntityNotFoundException("주문정보를 찾을수 없습니다"));
            order.updatePaymentInfo(paymentMethod, payInfo);
            orderRepository.save(order);
//            cartService.clearCart(orderId); // 결제가 다 되면 장바구니 비우기  > 주문한것만 지워야 하겠지

        } catch (EntityNotFoundException e) {
            log.error("updateOrderWithPaymentInfo 에서 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during updating order with payment info: {}", e.getMessage());
            throw new RuntimeException("Updating order with payment info failed", e);
        }
    }

    public void failOrder(String orderId) {
        try {
            // 실패시 해당 주문아이디로 주문을 찾고 상태를 캔슬로 변경 > 재고 다시 원상복구
            Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new EntityNotFoundException("주문정보를 찾을수 없습니다"));
            order.setOrderStatus(OrderStatus.CANCEL);

            for (OrderDetail orderDetail : order.getOrderItems()) {

                orderDetail.getProductModel().addStock(orderDetail.getOrderPrice());
            }

            orderRepository.save(order);
        } catch (EntityNotFoundException e) {
            log.error("failOrder 에서 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during order cancellation: {}", e.getMessage());
            throw new RuntimeException("Order cancellation failed", e);
        }
    }

    public OrderDto createOrderByNow(CartDto cartDto, long prId, String userid) {
        Users user = userRepository.findById(userid).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다"));

        int totalAmount = (int)cartDto.getTotalPrice();

        // 기존 주문이 있는지 확인하고, 있는 경우 기존 주문에 항목 추가
        List<Order> existingOrders = findExistingOrders(user);
        Order order;
        if (!existingOrders.isEmpty()) {
            order = existingOrders.get(0);
        } else {
            order = Order.createOrder(user, new ArrayList<>());
            orderRepository.save(order);
        }

        for (CartItemDto cartItemDto : cartDto.getItems()) {
            long modelId = cartItemDto.getModelId();
            ProductModel productModel = productModelRepository.findById(modelId)
                    .orElseThrow(() -> new IllegalArgumentException("찾는 모델이 없습니다"));

            Product product = productRepository.findById(prId)
                    .orElseThrow(() -> new IllegalArgumentException("찾는 제품이 없습니다"));

            // 기존 주문 항목 중 동일한 제품 및 모델이 있는지 확인
            boolean exists = order.getOrderItems().stream()
                    .anyMatch(item -> item.getProduct().getId().equals(product.getId()) &&
                            item.getProductModel().getId().equals(productModel.getId()));

            if (!exists) {
                // 기존 주문 항목이 없는 경우 새로 추가
                OrderDetail orderDetail = OrderDetail.createOrderDetails(product, productModel, cartItemDto.getCount());
                order.addOrderItem(orderDetail); // OrderDetail 객체를 Order 객체에 추가
            } else {
                // 기존 주문 항목에 있고 수량의 차이가 있다면
                OrderDetail existingOrderDetail = order.getOrderItems().stream()
                        .filter(item -> item.getProduct().getId().equals(product.getId()) &&
                                item.getProductModel().getId().equals(productModel.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("주문 항목을 찾을 수 없습니다."));

                int newCount = cartItemDto.getCount();
                int oldCount = existingOrderDetail.getCount();
                int difference = newCount - oldCount;

                if (difference > 0) {
                    existingOrderDetail.getProductModel().removeStock(difference);
                } else {
                    existingOrderDetail.getProductModel().addStock(Math.abs(difference));
                }

                existingOrderDetail.setCount(newCount);
            }
        }

        orderRepository.save(order);

        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
        OrderDto dto = OrderDto.builder()
                .orderNo(order.getId())
                .orderId(order.getOrderId())
                .totalPrice(totalAmount)
                .items(orderItemDtos)
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .pCode(user.getPCode())
                .loadAddress(user.getLoadAddr())
                .lotAddress(user.getLotAddr())
                .detailAddress(user.getDetailAddr())
                .build();
        dto.tranceOther(dto.getPhone(), dto.getEmail());

        return dto;
    }

}
