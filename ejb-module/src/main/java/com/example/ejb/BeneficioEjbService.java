package com.example.ejb;

import com.example.exception.BeneficioException;
import com.example.model.Beneficio;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int MAXIMO_TRES_TENTATIVAS_RETRY_AUTOMATICO = 3;

    // Construtor adicional para testes
    // Construtor adicional para testes
    public BeneficioEjbService() {
        super();
    }

    public BeneficioEjbService(EntityManager em) {
        super();
        this.entityManager = em;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Beneficio create(Beneficio beneficio) {
        entityManager.persist(beneficio);
        return beneficio;
    }

    public Beneficio findById(Long id) {
        return entityManager.find(Beneficio.class, id);
    }

    public List<Beneficio> findAll() {
        TypedQuery<Beneficio> query = entityManager.createQuery("SELECT b FROM Beneficio b", Beneficio.class);
        return query.getResultList();
    }

    public Beneficio update(Beneficio beneficio) {
        return entityManager.merge(beneficio);
    }

    public void delete(Long id) {
        Beneficio b = entityManager.find(Beneficio.class, id);
        if (b == null) {
            throw new BeneficioException(
                    BeneficioException.Tipo.NOT_FOUND,
                    "Benefício não encontrado para exclusão.");
        }
        entityManager.remove(b);
    }

    // public void transfer(Long fromId, Long toId, BigDecimal amount) {
    // Beneficio from = em.find(Beneficio.class, fromId);
    // Beneficio to = em.find(Beneficio.class, toId);

    // // BUG: sem validações, sem locking, pode gerar saldo negativo e lost update
    // from.setValor(from.getValor().subtract(amount));
    // to.setValor(to.getValor().add(amount));

    // em.merge(from);
    // em.merge(to);
    // }
    // TRANSFERÊNCIA (já existente, mas com melhorias)
    // Usando Lock optimista e retry definido (melhoria, transformar em parametro de
    // sistema)
    // Validacoes: .existencia de origem e destino
    // .Saldo na origem
    // .transferencia maior que zero

    // Metodo coordenador: controla o loop de retry
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        int attempt = 0;
        boolean success = false;

        while (!success) {
            attempt++;
            try {
                // chama o método isolado em nova transação
                doTransfer(fromId, toId, amount);
                success = true;
            } catch (OptimisticLockException e) {
                if (attempt >= MAXIMO_TRES_TENTATIVAS_RETRY_AUTOMATICO) {
                    throw new RuntimeException("Falha de concorrência após várias tentativas.", e);
                }
                System.out.println("Conflito detectado, retry #" + attempt);
                // rollback automático já ocorreu, próxima iteração abre nova transação
            }
        }
    }

    // Metodo isolado: cada chamada roda em uma nova transação
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void doTransfer(Long fromId, Long toId, BigDecimal amount) {
        Beneficio from = entityManager.find(Beneficio.class, fromId);
        Beneficio to = entityManager.find(Beneficio.class, toId);

        if (from == null || to == null) {
            throw new BeneficioException(
                    BeneficioException.Tipo.NOT_FOUND,
                    "Benefício origem ou destino não encontrado.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BeneficioException(
                    BeneficioException.Tipo.INVALID_AMOUNT,
                    "Valor da transferência deve ser positivo.");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new BeneficioException(
                    BeneficioException.Tipo.INSUFFICIENT_BALANCE,
                    "Saldo insuficiente no benefício origem.");
        }

        // Atualiza valores
        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        entityManager.merge(from);
        entityManager.merge(to);
    }

}
