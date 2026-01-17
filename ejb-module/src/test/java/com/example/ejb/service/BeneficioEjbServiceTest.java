package com.example.ejb.service;

import com.example.ejb.BeneficioEjbService;
import com.example.ejb.exception.BeneficioException;
import com.example.ejb.model.Beneficio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeneficioEjbServiceTest {

    private EntityManager em;
    private BeneficioEjbService service;


    @BeforeEach
    void setup() {
        em = Mockito.mock(EntityManager.class);
        service = new BeneficioEjbService();
        service.setEntityManager(em); // ✅ injeta o mock
    }
    private BeneficioEjbService createServiceWithMocks(Beneficio from, Beneficio to, EntityManager em) throws Exception {
        when(em.find(Beneficio.class, 1L)).thenReturn(from);
        when(em.find(Beneficio.class, 2L)).thenReturn(to);
        when(em.merge(any(Beneficio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BeneficioEjbService realService = new BeneficioEjbService();
        Field field = BeneficioEjbService.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(realService, em);
        return realService;
    }

    // CREATE
    @Test
    void testCreateBeneficio() {
        Beneficio beneficio = new Beneficio(null, "Vale Alimentação", new BigDecimal("500.00"));
        service.create(beneficio);
        verify(em, times(1)).persist(beneficio);
    }

    // READ - findById
    @Test
    void testFindById() {
        Beneficio beneficio = new Beneficio(1L, "Vale Transporte", new BigDecimal("300.00"));
        when(em.find(Beneficio.class, 1L)).thenReturn(beneficio);

        Beneficio result = service.findById(1L);

        assertNotNull(result);
        assertEquals("Vale Transporte", result.getNome());
    }

    // READ - findAll
    @Test
    void testFindAll() {
        Beneficio b1 = new Beneficio(1L, "Vale Alimentação", new BigDecimal("500.00"));
        Beneficio b2 = new Beneficio(2L, "Vale Transporte", new BigDecimal("300.00"));

        TypedQuery<Beneficio> query = Mockito.mock(TypedQuery.class);
        when(em.createQuery("SELECT b FROM Beneficio b", Beneficio.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(b1, b2));

        List<Beneficio> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("Vale Alimentação", result.get(0).getNome());
    }

    // UPDATE
    @Test
    void testUpdateBeneficio() {
        Beneficio beneficio = new Beneficio(1L, "Vale Alimentação", new BigDecimal("500.00"));
        when(em.merge(beneficio)).thenReturn(beneficio);

        Beneficio updated = service.update(beneficio);

        assertNotNull(updated);
        verify(em, times(1)).merge(beneficio);
    }

    // DELETE
    @Test
    void testDeleteBeneficio() {
        Beneficio beneficio = new Beneficio(1L, "Vale Transporte", new BigDecimal("300.00"));
        when(em.find(Beneficio.class, 1L)).thenReturn(beneficio);

        service.delete(1L);

        verify(em, times(1)).remove(beneficio);
    }

    // DELETE - not found
    @Test
    void testDeleteBeneficioNotFound() {
        when(em.find(Beneficio.class, 99L)).thenReturn(null);

        assertThrows(BeneficioException.class, () -> service.delete(99L));
    }
    // TRANSFER
    @Test
    void testTransferSuccess() {
        Beneficio from = new Beneficio(1L, "Origem", new BigDecimal("1000.00"));
        Beneficio to = new Beneficio(2L, "Destino", new BigDecimal("500.00"));

        when(em.find(Beneficio.class, 1L)).thenReturn(from);
        when(em.find(Beneficio.class, 2L)).thenReturn(to);

        service.transfer(1L, 2L, new BigDecimal("200.00"));

        assertEquals(new BigDecimal("800.00"), from.getValor());
        assertEquals(new BigDecimal("700.00"), to.getValor());

        verify(em, times(1)).merge(from);
        verify(em, times(1)).merge(to);
    }

    @Test
    void testTransferBeneficioNotFound() {
        when(em.find(Beneficio.class, 1L)).thenReturn(new Beneficio(1L, "Origem", BigDecimal.TEN));
        when(em.find(Beneficio.class, 99L)).thenReturn(null);

        BeneficioException exception = assertThrows(BeneficioException.class,
                () -> service.transfer(1L, 99L, BigDecimal.ONE));
        assertEquals(BeneficioException.Tipo.NOT_FOUND, exception.getTipo());
    }

    @Test
    void testTransferInvalidAmount() {
        Beneficio from = new Beneficio(1L, "Origem", new BigDecimal("100.00"));
        Beneficio to = new Beneficio(2L, "Destino", new BigDecimal("50.00"));

        when(em.find(Beneficio.class, 1L)).thenReturn(from);
        when(em.find(Beneficio.class, 2L)).thenReturn(to);

        BeneficioException exception = assertThrows(BeneficioException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("-10.00")));
        assertEquals(BeneficioException.Tipo.INVALID_AMOUNT, exception.getTipo());
    }

    @Test
    void testTransferInsufficientBalance() {
        Beneficio from = new Beneficio(1L, "Origem", new BigDecimal("50.00"));
        Beneficio to = new Beneficio(2L, "Destino", new BigDecimal("50.00"));

        when(em.find(Beneficio.class, 1L)).thenReturn(from);
        when(em.find(Beneficio.class, 2L)).thenReturn(to);

        BeneficioException exception = assertThrows(BeneficioException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("100.00")));
        assertEquals(BeneficioException.Tipo.INSUFFICIENT_BALANCE, exception.getTipo());
    }

    @Test
    void testTransferOptimisticLockExhaustedRetries() {
        Beneficio from = new Beneficio(1L, "Origem", new BigDecimal("1000.00"));
        Beneficio to = new Beneficio(2L, "Destino", new BigDecimal("500.00"));

        when(em.find(Beneficio.class, 1L)).thenReturn(from);
        when(em.find(Beneficio.class, 2L)).thenReturn(to);

        when(em.merge(any())).thenThrow(new OptimisticLockException("Conflito persistente"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("200.00")));

        assertTrue(exception.getMessage().contains("Falha de concorrência"));
        verify(em, times(3)).merge(any());
    }

    @Test
    void testSingleThreadRetry() {
        BeneficioEjbService realService = new BeneficioEjbService(/* injete EntityManager mockado */);
        BeneficioEjbService spyService = Mockito.spy(realService);

        Long fromId = 1L;
        Long toId   = 2L;
        BigDecimal amount = new BigDecimal("100");

        // Simula falha na primeira chamada e sucesso na segunda
        doThrow(new OptimisticLockException("Conflito simulado"))
                .doNothing()
                .when(spyService).doTransfer(fromId, toId, amount);

        spyService.transfer(fromId, toId, amount);

        // Verifica que houve 2 chamadas: falha + retry
        verify(spyService, times(2)).doTransfer(fromId, toId, amount);
    }
    @Test
    void testConcurrentTransfersSemSaldo() throws InterruptedException {
        BeneficioEjbService realService = new BeneficioEjbService(/* injete EntityManager mockado */);
        BeneficioEjbService spyService = Mockito.spy(realService);

        Long fromId = 1L;
        Long toId   = 2L;
        BigDecimal amount = new BigDecimal("100");

        // Configura o spy para simular falha na primeira chamada e sucesso depois
        doThrow(new OptimisticLockException("Conflito simulado"))
                .doNothing()
                .when(spyService).doTransfer(fromId, toId, amount);

        int threads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    spyService.transfer(fromId, toId, amount);
                } catch (Exception e) {
                    System.out.println("Thread falhou: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // Verifica que houve pelo menos 2 chamadas (uma falha + retries)
        verify(spyService, atLeast(2)).doTransfer(fromId, toId, amount);

    }
    @Test
    void testMultiThreadConcurrentTransfers() throws Exception {
        EntityManager em = Mockito.mock(EntityManager.class);

        Beneficio from = new Beneficio();
        from.setId(1L);
        from.setValor(new BigDecimal("1000"));

        Beneficio to = new Beneficio();
        to.setId(2L);
        to.setValor(new BigDecimal("1000"));

        BeneficioEjbService realService = createServiceWithMocks(from, to, em);
        BeneficioEjbService spyService = Mockito.spy(realService);

        Long fromId = 1L;
        Long toId   = 2L;
        BigDecimal amount = new BigDecimal("100");

        // Cada thread: primeira chamada falha, segunda aplica transferência
        doThrow(new OptimisticLockException("Conflito simulado"))
                .doAnswer(invocation -> {
                    from.setValor(from.getValor().subtract(amount));
                    to.setValor(to.getValor().add(amount));
                    return null;
                })
                .when(spyService).doTransfer(fromId, toId, amount);

        int threads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    spyService.transfer(fromId, toId, amount);
                } catch (Exception e) {
                    System.out.println("Thread falhou: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // ✅ Verificação dos saldos finais (duas transferências aplicadas)
        assertEquals(new BigDecimal("800"), from.getValor(), "Saldo origem deve ser 800");
        assertEquals(new BigDecimal("1200"), to.getValor(), "Saldo destino deve ser 1200");

        verify(spyService, atLeast(3)).doTransfer(fromId, toId, amount);
    }







}