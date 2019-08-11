package com.example.statemachine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.statemachine.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("Select o from Order o where o.state = :state")
	public List<Order> findByState(@Param("state") String state);
}
