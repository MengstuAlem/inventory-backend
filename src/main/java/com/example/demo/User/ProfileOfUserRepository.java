package com.example.demo.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileOfUserRepository extends JpaRepository<ProfileOfUser, Long> {

    ProfileOfUser getUserByEmail(String email);
}
