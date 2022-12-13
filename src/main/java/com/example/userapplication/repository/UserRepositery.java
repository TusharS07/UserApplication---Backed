package com.example.userapplication.repository;
import com.example.userapplication.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositery extends JpaRepository<UserModel, Integer> {

    public UserModel findByEmail(String email);
    public UserModel findByPassword(String password);
    UserModel findByEmailAndPassword(String email, String password);
}