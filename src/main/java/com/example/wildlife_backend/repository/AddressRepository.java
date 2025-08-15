package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
