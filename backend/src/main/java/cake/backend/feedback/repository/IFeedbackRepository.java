package cake.backend.feedback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cake.backend.auth.entity.User;
import cake.backend.feedback.model.Feedback;

public interface IFeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByUserId(Long userId);
}
