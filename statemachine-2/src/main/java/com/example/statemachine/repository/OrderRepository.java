package com.example.statemachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.statemachine.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}