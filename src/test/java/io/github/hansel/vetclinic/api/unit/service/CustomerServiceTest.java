package io.github.hansel.vetclinic.api.unit.service;

import io.github.hansel.vetclinic.api.dto.request.AddressRequest;
import io.github.hansel.vetclinic.api.dto.request.CustomerRequest;
import io.github.hansel.vetclinic.api.dto.response.CustomerDetailResponse;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.exception.BusinessValidationException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.CustomerRepository;
import io.github.hansel.vetclinic.api.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerService service;

    private CustomerRequest request;
    private Customer customer;

    @BeforeEach
    void setUp() {
        request = new CustomerRequest("Ana Silva", "11911111111", "ana.silva@example.com", mock(AddressRequest.class));
        customer = spy(new Customer(request));
    }

    @Test
    void shouldCreateCustomer() {
        when(repository.save(any(Customer.class))).thenReturn(customer);

        CustomerDetailResponse response = service.create(request);

        assertEquals("Ana Silva", response.name());
        verify(repository).save(any(Customer.class));
    }

    @Test
    void shouldThrowWhenCreatingWithDuplicatePhone() {
        when(repository.existsByPhone(request.phone())).thenReturn(true);

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> service.create(request));

        assertEquals("Phone already exists", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenCreatingWithDuplicateEmail() {
        when(repository.existsByEmail(request.email())).thenReturn(true);

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> service.create(request));

        assertEquals("Email already exists", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldFindCustomerById() {
        when(repository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(customer));

        CustomerDetailResponse response = service.findByIdAndActiveTrue(1L);

        assertEquals("Ana Silva", response.name());
    }

    @Test
    void shouldThrowWhenCustomerNotFoundById() {
        when(repository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findByIdAndActiveTrue(1L));

        assertEquals("Customer not found with id 1", exception.getMessage());
    }

    @Test
    void shouldUpdateCustomer() {
        when(repository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(customer));

        CustomerDetailResponse response = service.update(request, 1L);

        assertNotNull(response);
        verify(repository).findByIdAndActiveTrue(1L);
        verify(customer).update(any(CustomerRequest.class));
    }

    @Test
    void shouldDeactivateCustomer() {
        when(repository.findById(1L)).thenReturn(Optional.of(customer));

        service.deactivate(1L);

        verify(customer).deactivate();
    }

    @Test
    void shouldActivateCustomer() {
        when(repository.findById(1L)).thenReturn(Optional.of(customer));

        service.deactivate(1L);
        service.activate(1L);

        verify(customer).activate();
    }
}
