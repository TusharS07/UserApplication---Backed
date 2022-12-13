package com.example.userapplication.service;

import com.example.userapplication.dto.LoginDTO;
import com.example.userapplication.dto.RegisterDTO;
import com.example.userapplication.dto.UpdateDTO;
import com.example.userapplication.model.UserModel;

import java.util.List;


public interface IuserService {

    String registerUser(RegisterDTO registerDTO);
    UserModel getUserData(String token);
    UserModel updateUserData(int id, RegisterDTO registerDTO);
    UserModel update(UpdateDTO updateDTO, String token);
    String forgotPassword(String password, String token);
    String deleteUser(int id);
    String delete(String token);

    String login(LoginDTO loginDTO);

    String logOut(String token);
    String userLogin(LoginDTO loginDTO);

    List<UserModel> getAllUsers(int id);
}
