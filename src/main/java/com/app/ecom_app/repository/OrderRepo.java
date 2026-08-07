package com.app.ecom_app.repository;

import com.app.ecom_app.dto.OrderResponse;
import com.app.ecom_app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {


    List<Order> findByUser_Id(String userId);

    Optional<Order> findByIdAndUser_Id(String orderId, String userId);

}
