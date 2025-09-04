package io.github.hansel.vetclinic.api.unit.entity;

import io.github.hansel.vetclinic.api.dto.request.AddressRequest;
import io.github.hansel.vetclinic.api.dto.request.CustomerRequest;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    void shouldCreateCustomerWithDTO() {
        CustomerRequest dto = new CustomerRequest(
                "Ana Silva",
                "11911111111",
                "ana.silva@example.com",
                new AddressRequest("Rua das Flores", "100", "Apto 101", "Centro", "12345678")
        );
        Customer customer = new Customer(dto);

        assertEquals("Ana Silva", customer.getName());
        assertEquals("11911111111", customer.getPhone());
        assertEquals("ana.silva@example.com", customer.getEmail());
        assertTrue(customer.isActive());
        assertNotNull(customer.getAddress());
    }

    @Test
    void shouldUpdateCustomerPartially() {
        CustomerRequest dto = new CustomerRequest("Ana Silva", "11911111111", "ana.silva@example.com",
                new AddressRequest("Rua das Flores", "100", "Centro", "12345678", "Apto 101"));
        Customer customer = new Customer(dto);

        CustomerRequest updateDto = new CustomerRequest(
                null,
                "11922222222",
                null,
                new AddressRequest("Rua do Limoeiro", "200", "Jardim", "02020000", "Casa 2")
        );
        customer.update(updateDto);

        assertEquals("Ana Silva", customer.getName()); // unchanged
        assertEquals("11922222222", customer.getPhone()); // updated
        assertEquals("ana.silva@example.com", customer.getEmail()); // unchanged
        assertEquals("Rua do Limoeiro", customer.getAddress().getStreet()); // updated
    }

    @Test
    void shouldDeactivateAndActivateCustomer() {
        CustomerRequest dto = new CustomerRequest("Ana Silva", "11911111111", "ana.silva@example.com",
                new AddressRequest("Rua das Flores", "100", "Centro", "12345678", "Apto 101"));
        Customer customer = new Customer(dto);

        customer.deactivate();
        assertFalse(customer.isActive());

        customer.activate();
        assertTrue(customer.isActive());
    }

    @Test
    void shouldThrowWhenDeactivatingInactiveCustomer() {
        CustomerRequest dto = new CustomerRequest("Ana Silva", "11911111111", "ana.silva@example.com",
                new AddressRequest("Rua das Flores", "100", "Centro", "12345678", "Apto 101"));
        Customer customer = new Customer(dto);
        customer.deactivate();

        BadRequestException exception = assertThrows(BadRequestException.class, customer::deactivate);
        assertEquals("Customer is already inactive", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenActivatingActiveCustomer() {
        CustomerRequest dto = new CustomerRequest("Ana Silva", "11911111111", "ana.silva@example.com",
                new AddressRequest("Rua das Flores", "100", "Centro", "12345678", "Apto 101"));
        Customer customer = new Customer(dto);

        BadRequestException exception = assertThrows(BadRequestException.class, customer::activate);
        assertEquals("Customer is already active", exception.getFieldErrors().get(0).message());
    }
}
