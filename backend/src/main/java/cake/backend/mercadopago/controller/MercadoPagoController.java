package cake.backend.mercadopago.controller;

import cake.backend.mercadopago.model.RequestCardPaymentDto;
import cake.backend.mercadopago.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mp")
public class MercadoPagoController {
    @Autowired
    private PaymentService service;

    @PostMapping("/card")
    public ResponseEntity<String > create(@RequestBody RequestCardPaymentDto request){
        try {
            service.payWithCard(request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
