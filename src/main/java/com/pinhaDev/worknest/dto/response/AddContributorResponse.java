package com.pinhaDev.worknest.dto.response;

import com.pinhaDev.worknest.domain.enums.UserRole;

import java.util.UUID;

public record AddContributorResponse(
        UUID id,

        String email,

        UserRole role,

        UUID workspaceId
) {

}
