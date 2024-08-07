package tech.cyberboy.order_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import tech.cyberboy.order_service.dto.InventoryResponse;
import tech.cyberboy.order_service.dto.OrderLineItemDto;
import tech.cyberboy.order_service.dto.OrderRequest;
import tech.cyberboy.order_service.model.Order;
import tech.cyberboy.order_service.model.OrderLineItem;
import tech.cyberboy.order_service.repository.OrderRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream().map(this::mapFromDto).toList();
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = orderLineItems.stream().map(OrderLineItem::getSkuCode).toList();

        // Call Inventory Service and place order if the product is in stock
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean isAllProductInStock= Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (isAllProductInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again");
        }

    }

    private OrderLineItem mapFromDto(OrderLineItemDto orderLineItemDto) {
        return OrderLineItem.builder()
                .skuCode(orderLineItemDto.getSkuCode())
                .price(orderLineItemDto.getPrice())
                .quantity(orderLineItemDto.getQuantity())
                .build();
    }
}
