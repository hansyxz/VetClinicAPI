package io.github.hansel.vetclinic.api.unit.entity;

import io.github.hansel.vetclinic.api.dto.customer.AddressRequest;
import io.github.hansel.vetclinic.api.dto.customer.CustomerRequest;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CustomerTest {

    private CustomerRequest request;
    private Customer customer;

    @BeforeEach
    void setUp() {
        request = new CustomerRequest("Ana Silva", "11911111111", "ana.silva@example.com", mock(AddressRequest.class));
        customer = new Customer(request);
    }

    @Test
    void shouldCreateCustomerWithDTO() {
        assertEquals("Ana Silva", customer.getName());
        assertEquals("11911111111", customer.getPhone());
        assertEquals("ana.silva@example.com", customer.getEmail());
        assertTrue(customer.isActive());
        assertNotNull(customer.getAddress());
    }

    @Test
    void shouldUpdateCustomerPartially() {
        CustomerRequest updateRequest = new CustomerRequest(null, "11900000000", "ana@example.com",
                new AddressRequest("Rua ABC", "10", "Jardim", "10101010", "Casa 10"));
        customer.update(updateRequest);

        assertEquals("Ana Silva", customer.getName()); // unchanged
        assertEquals("11900000000", customer.getPhone()); // updated
        assertEquals("ana@example.com", customer.getEmail()); // updated
        assertEquals("Rua ABC", customer.getAddress().getStreet()); // updated
    }

    @Test
    void shouldDeactivateAndActivateCustomer() {
        customer.deactivate();
        assertFalse(customer.isActive());

        customer.activate();
        assertTrue(customer.isActive());
    }

    @Test
    void shouldThrowWhenDeactivatingInactiveCustomer() {
        customer.deactivate();

        BadRequestException exception = assertThrows(BadRequestException.class, customer::deactivate);
        assertEquals("Customer is already inactive", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenActivatingActiveCustomer() {
        BadRequestException exception = assertThrows(BadRequestException.class, customer::activate);
        assertEquals("Customer is already active", exception.getFieldErrors().get(0).message());
    }
}
