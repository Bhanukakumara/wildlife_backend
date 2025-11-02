package com.example.wildlife_backend.dto.wishList;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WishlistCreateDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Photo ID is required")
    private Long photoId;
}
