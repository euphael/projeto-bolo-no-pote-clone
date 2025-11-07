package cake.backend.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cake.backend.feedback.model.Feedback;
import cake.backend.feedback.repository.IFeedbackRepository;
import cake.backend.feedback.service.FeedbackService;
import cake.backend.order.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cake.backend.auth.entity.User;
import cake.backend.auth.repository.IUserRepository;
import cake.backend.order.entity.Order;
import cake.backend.order.repository.IOrderRepository;
import cake.backend.product.entity.Product;
import cake.backend.product.repository.IProductRepository;

@Service
public class OrderService {
    private IOrderRepository orderRepository;
    private IUserRepository userRepository;
    private IProductRepository productRepository;
    private IFeedbackRepository feedbackRepository;

    @Autowired
    public OrderService(
            IOrderRepository orderRepository,
            IUserRepository userRepository,
            IProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<ObtainOrderDto> getAll() {
        List<Order> orders = orderRepository.findAll();
        List<ObtainOrderDto> dtos = new ArrayList<>();
        Feedback feedback = null;
        for(Order order : orders) {
            User user = userRepository.findById(order.getUserId()).orElse(null);
            if(user == null) continue;

            if (!order.getFeedbacks().isEmpty()){
                feedback = order.getFeedbacks().get(0);
          ;  }

            ObtainOrderDto dto = getObtainOrderDto(order, feedback, user);
            dtos.add(dto);
        }
        return dtos;
    }

    private static @NotNull ObtainOrderDto getObtainOrderDto(Order order, Feedback feedback, User user) {
        ObtainOrderDto dto;
        if(feedback != null) {
            dto = new ObtainOrderDto(
                    order.getId(), user.getName(), order.getUserId(), order.getProduct().getName(), order.getQuantity(), order.getStatus(), feedback.getGrade(), feedback.getComment()
            );
        } else {
            dto = new ObtainOrderDto(
                    order.getId(), user.getName(), order.getUserId(), order.getProduct().getName(), order.getQuantity(), order.getStatus(), 0, "SEM AVALIAÇÃO"
            );
        }
        return dto;
    }

    public List<Order> getAll(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return new ArrayList<>();
        return orderRepository.findByUserId(user.getId());
    }

    public Order get(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order create(CreateOrderDto dto) {
        User user = userRepository.findById(dto.userId()).orElse(null);
        if (user == null)
            return null;
        Product product = productRepository.findById(dto.product().productId()).orElse(null);
        return orderRepository.save(
                Order.builder()
                .userId(user.getId())
                .status(EnumOrderStatus.ENVIADO.toString())
                .product(product).quantity(dto.product().quantity())
                .startDate(new Date().toString())
                .endDate(dto.endDate())
                .comment("Pedido realizado com sucesso!")
                .build());
    }

    public Order update(Long id, UpdateOrderDto dto) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return null;
        User user = userRepository.findById(dto.userId()).orElse(null);
        if (user == null)
            return null;
        Product product = productRepository.findById(dto.product().productId()).orElse(null);
        if(product != null) {
            order.setProduct(product);
            order.setQuantity(dto.product().quantity());
        }
        order.setEndDate(dto.endDate());
        order.setStatus(dto.status());
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null)
            orderRepository.delete(order);
    }

    public Order cancel(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return null;
        order.setStatus(EnumOrderStatus.CANCELADO.toString());
        order.setComment("Pedido cancelado");
        return orderRepository.save(order);
    }
}
