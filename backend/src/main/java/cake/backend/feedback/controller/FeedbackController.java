package cake.backend.feedback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cake.backend.feedback.model.Feedback;
import cake.backend.feedback.model.dto.CreateFeedbackDto;
import cake.backend.feedback.service.FeedbackService;

@RestController
@RequestMapping("feedback")
public class FeedbackController {
    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/getByOrderId")
    public ResponseEntity<List<Feedback>> getByOrder(@RequestParam Long orderId) {
        return new ResponseEntity<>(feedbackService.getOrderFeedbacks(orderId), HttpStatus.OK);
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<List<Feedback>> getByUserId(@RequestParam Long userId) {
        return new ResponseEntity<>(feedbackService.getUserFeedbacks(userId), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Feedback> create(@RequestBody CreateFeedbackDto dto) {
        Feedback feedback = feedbackService.create(dto);
        return feedback == null ? 
            new ResponseEntity<>(HttpStatus.BAD_REQUEST) : 
            new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }
}