package cake.backend.order.controller;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

import cake.backend.order.model.ObtainOrderDto;
import cake.backend.ws.WebSocketHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cake.backend.order.entity.Order;
import cake.backend.order.model.CreateOrderDto;
import cake.backend.order.model.UpdateOrderDto;
import cake.backend.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private final OrderService orderService;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private Gson gson;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ObtainOrderDto>> getAll() {
        return new ResponseEntity<>(orderService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Order>> getAll(@RequestParam Long userId) {
        return new ResponseEntity<>(orderService.getAll(userId), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Order> get(@RequestParam Long id) {
        Order order = orderService.get(id);
        return order == null ? 
            new ResponseEntity<>(HttpStatus.NOT_FOUND) : 
            new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDto dto) {
        Order order = orderService.create(dto);
        return order == null ? 
            new ResponseEntity<>(HttpStatus.BAD_REQUEST) : 
            new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Order> update(@RequestParam Long id, @RequestBody UpdateOrderDto dto) {
        try {
            Order order = orderService.update(id, dto);
            if(order == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            webSocketHandler.broadcast(gson.toJson(order));
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IOException E) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<Order> cancel(@RequestParam Long id) {
        Order order = orderService.cancel(id);
        return order == null ? 
            new ResponseEntity<>(HttpStatus.BAD_REQUEST) : 
            new ResponseEntity<>(order, HttpStatus.OK);
    }
}
