package cake.backend.feedback.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cake.backend.auth.entity.User;
import cake.backend.auth.repository.IUserRepository;
import cake.backend.feedback.model.Feedback;
import cake.backend.feedback.model.dto.CreateFeedbackDto;
import cake.backend.feedback.repository.IFeedbackRepository;
import cake.backend.order.entity.Order;
import cake.backend.order.repository.IOrderRepository;
import jakarta.transaction.Transactional;

@Service
public class FeedbackService {
    private IFeedbackRepository feedbackRepository;
    private IUserRepository userRepository;
    private IOrderRepository orderRepository;

    @Autowired
    public FeedbackService(
        IFeedbackRepository feedbackRepository, 
        IUserRepository userRepository, 
        IOrderRepository orderRepository
    ) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public List<Feedback> getUserFeedbacks(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();
        return feedbackRepository.findByUserId(userId);
    }

    public List<Feedback> getOrderFeedbacks(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) return new ArrayList<>();
        return order.getFeedbacks();
    }

    @Transactional
    public Feedback create(CreateFeedbackDto dto) {
        Order order = orderRepository.findById(dto.orderId()).orElse(null);
        if(order == null) return null;
        User user = userRepository.findById(dto.userId()).orElse(null);
        if(user == null) return null;
        if(dto.grade() < 0 || dto.grade() > 10) return null;
        Feedback feedback = Feedback.builder()
            .grade(dto.grade())
            .comment(dto.comment())
            .userId(user.getId()).build();
        feedbackRepository.save(feedback);
        List<Feedback> feedbacks = order.getFeedbacks();
        feedbacks.add(feedback);
        order.setFeedbacks(feedbacks);
        orderRepository.save(order);
        return feedback;
    }
}
