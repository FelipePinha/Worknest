package com.pinhaDev.worknest.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record AddContributorRequest(

        @NotEmpty
        String contributorEmail
) {
}
