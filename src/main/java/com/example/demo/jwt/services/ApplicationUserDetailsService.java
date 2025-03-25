package com.example.demo.jwt.services;

import com.example.demo.User.ProfileOfUser;
import com.example.demo.User.ProfileOfUserRepository;
import com.example.demo.jwt.models.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    ProfileOfUserRepository  profileOfUserRepository;
    public ApplicationUserDetailsService(ProfileOfUserRepository profileOfUserRepository) {
        this.profileOfUserRepository = profileOfUserRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new UserPrincipal(profileOfUserRepository.getUserByEmail(email));
    }

    public static Boolean verifyPasswordHash(
            String password,
            byte[] storedHash,
            byte[] storedSalt
    ) throws NoSuchAlgorithmException {

        byte[] computedHash = hashPassword(password, storedSalt);
        return MessageDigest.isEqual(computedHash, storedHash);
    }

    public ProfileOfUser authenticate(String email, String password) throws NoSuchAlgorithmException {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new BadCredentialsException("Unauthorized: Email or password is empty");
        }

        // Retrieve user from database
        var userEntity = profileOfUserRepository.getUserByEmail(email);
        if (userEntity == null) {
            throw new BadCredentialsException("Unauthorized: User not found");
        }

        // Verify password
        boolean verified = verifyPasswordHash(
                password,
                userEntity.getStoredHash(),
                userEntity.getStoredSalt()
        );

        if (!verified) {
            throw new BadCredentialsException("Unauthorized: Incorrect password");
        }

        return userEntity;
    }


    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        return md.digest(password.getBytes());
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 16-byte salt
        random.nextBytes(salt);
        return salt;
    }
}
