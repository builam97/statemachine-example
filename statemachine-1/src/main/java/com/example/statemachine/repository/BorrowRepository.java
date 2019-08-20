package com.example.statemachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.statemachine.entities.Borrow;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long>{

}
