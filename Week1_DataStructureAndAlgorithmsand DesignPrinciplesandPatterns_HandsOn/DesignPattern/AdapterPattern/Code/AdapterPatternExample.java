interface PaymentProcessor {
    void processPayment();
}

class StripeGateway {
    void stripePay() {
        System.out.println("Payment processed via Stripe");
    }
}

class PayPalGateway {
    void payWithPayPal() {
        System.out.println("Payment processed via PayPal");
    }
}

class StripeAdapter implements PaymentProcessor {
    private StripeGateway stripe;

    StripeAdapter(StripeGateway stripe) {
        this.stripe = stripe;
    }

    public void processPayment() {
        stripe.stripePay();
    }
}

class PayPalAdapter implements PaymentProcessor {
    private PayPalGateway paypal;

    PayPalAdapter(PayPalGateway paypal) {
        this.paypal = paypal;
    }

    public void processPayment() {
        paypal.payWithPayPal();
    }
}

public class AdapterPatternExample {
    public static void main(String[] args) {
        PaymentProcessor p1 = new StripeAdapter(new StripeGateway());
        PaymentProcessor p2 = new PayPalAdapter(new PayPalGateway());
        p1.processPayment();
        p2.processPayment();
    }
}
