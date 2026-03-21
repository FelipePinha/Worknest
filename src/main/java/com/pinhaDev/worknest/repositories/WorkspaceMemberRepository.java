package com.pinhaDev.worknest.repositories;

import com.pinhaDev.worknest.domain.models.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, UUID> {

}
