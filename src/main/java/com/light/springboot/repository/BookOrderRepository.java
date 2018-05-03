package com.light.springboot.repository;



import com.light.springboot.entity.Book;
import com.light.springboot.entity.BookOrder;
import com.light.springboot.entity.OrderPrimaryKey;
import com.light.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface BookOrderRepository extends JpaRepository<BookOrder, OrderPrimaryKey> {

    Optional<BookOrder> findByOrderPK(OrderPrimaryKey orderPK);

    @Query("select max(bookOrder.orderPK.oid) from BookOrder bookOrder")
    Long findMaxOrderId();

    @Query("select bookOrder from BookOrder bookOrder where bookOrder.orderPK.uid = :userId")
    List<BookOrder> findByUserId(@Param("userId") Long userId);

    @Query("select bookOrder from BookOrder bookOrder where bookOrder.orderPK.oid = :orderId")
    List<BookOrder> findByOrderId(@Param("orderId") Long orderId);

    @Query(value="select distinct oid from bookorder where uid = :userId", nativeQuery = true)
    List<Long> findOrdersByUserId(@Param("userId") Long userId);

    @Query(value="select count(*) from bookorder", nativeQuery = true)
    Long getSize();

}
