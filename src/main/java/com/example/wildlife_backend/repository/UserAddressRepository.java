package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress,Long> {
}
