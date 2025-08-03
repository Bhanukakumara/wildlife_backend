package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.UserCreateDto;
import com.example.wildlife_backend.dto.UserGetDto;
import com.example.wildlife_backend.util.UserStatus;
import com.example.wildlife_backend.util.UserType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // Core CRUD Operations
    UserGetDto createUser(UserCreateDto userCreateDto);
    Optional<UserGetDto> getUserById(Long userId);
    Optional<UserGetDto> getUserByEmail(String email);
    List<UserGetDto> getAllUsers();
    boolean updateUser(Long userId, UserCreateDto userDetails);
    boolean deleteUser(Long userId);

    // Authentication and Authorization
//    User authenticateUser(String email, String password);
//    void updateUserPassword(Long userId, String newPassword);
//    void resetPassword(String email, String token, String newPassword);
//    boolean verifyPasswordResetToken(String email, String token);

    // User Profile Management
//    User updateUserProfile(Long userId, User userDetails);
//    User updateUserStatus(Long userId, UserStatus status);
//    User updateUserType(Long userId, UserType userType);

    // Relationship Management
//    void addAddressToUser(Long userId, Address address);
//    void removeAddressFromUser(Long userId, Long addressId);
//    List<Address> getUserAddresses(Long userId);
//
//    List<Photo> getUserPhotos(Long userId);
//    void addPhotoToUser(Long userId, Photo photo);
//
//    List<Order> getUserOrders(Long userId);
//    void addOrderToUser(Long userId, Order order);
//
//    List<PaymentMethod> getUserPaymentMethods(Long userId);
//    void addPaymentMethodToUser(Long userId, PaymentMethod paymentMethod);
//    void removePaymentMethodFromUser(Long userId, Long paymentMethodId);
//
//    List<CartItem> getUserCartItems(Long userId);
//    void addCartItemToUser(Long userId, CartItem cartItem);
//    void removeCartItemFromUser(Long userId, Long cartItemId);
//
//    List<PhotoReview> getUserReviews(Long userId);
//    void addReviewToUser(Long userId, PhotoReview review);

    // Search and Filter Operations
    List<UserGetDto> findUsersByStatus(UserStatus status);
    List<UserGetDto> findUsersByType(UserType userType);
    List<UserGetDto> searchUsersByName(String name, String lastName);
    List<UserGetDto> findUsersByDateOfBirthRange(LocalDate startDate, LocalDate endDate);

    // Validation and Utility Methods
    boolean isEmailUnique(String email);
    boolean isPhoneUnique(String phone);
}
