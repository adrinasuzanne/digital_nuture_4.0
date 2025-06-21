interface CustomerRepository {
    String findCustomerById(int id);
}

class CustomerRepositoryImpl implements CustomerRepository {
    public String findCustomerById(int id) {
        return "Customer#" + id;
    }
}

class CustomerService {
    private CustomerRepository repo;

    CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    void findCustomer(int id) {
        System.out.println(repo.findCustomerById(id));
    }
}

public class DependencyInjectionExample {
    public static void main(String[] args) {
        CustomerRepository repo = new CustomerRepositoryImpl();
        CustomerService service = new CustomerService(repo);
        service.findCustomer(123);
    }
}
