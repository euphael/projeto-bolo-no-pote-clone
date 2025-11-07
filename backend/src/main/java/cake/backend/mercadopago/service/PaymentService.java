package cake.backend.mercadopago.service;

import cake.backend.mercadopago.model.RequestCardPaymentDto;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public void payWithCard(RequestCardPaymentDto dto) {
        PaymentClient client = new PaymentClient();
        IdentificationRequest identificationRequest = IdentificationRequest.builder()
                .type(dto.payer().identification().type())
                .number(dto.payer().identification().number())
                .build();
        PaymentPayerRequest paymentPayerRequest = PaymentPayerRequest.builder()
                .email(dto.payer().email())
                .firstName(dto.payer().firstName())
                .identification(identificationRequest)
                .build();
        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder().
                transactionAmount(dto.transaction_amount())
                .token(dto.token())
                .installments(dto.installments())
                .paymentMethodId(dto.paymentMethodId())
                .payer(paymentPayerRequest)
                .build();
        try {
            MPRequestOptions request = MPRequestOptions.createDefault();
            request.setAccessToken("APP_USR-1052167594032235-052917-6903d4523caed2a87f9c4837ddef45ec-1832239249");
            Payment payment = client.create(paymentCreateRequest, request);
            System.out.println(payment);
        } catch (MPApiException e) {
            throw new RuntimeException("MercadoPago Error. Status: " +
                    e.getApiResponse().getStatusCode() +
                    ", Content: " +
                    e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
