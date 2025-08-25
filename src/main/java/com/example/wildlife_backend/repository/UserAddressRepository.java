package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserAddressRepository extends JpaRepository<UserAddress,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserAddress ua WHERE ua.address.id = ?1")
    void deleteByAddressId(Long addressId);

    UserAddress findByAddressId(Long addressId);
}
