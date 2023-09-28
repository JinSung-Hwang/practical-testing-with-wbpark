package sample.cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query(
        "select o "
      + "from Order o "
      + "where o.registeredDateTime >= :startDate "
      + "and o.registeredDateTime < :endDate "
      + "and o.status = :orderStatus"
  )
  List<Order> findOrdersBy(LocalDateTime startDate, LocalDateTime endDate, OrderStatus orderStatus);



}
