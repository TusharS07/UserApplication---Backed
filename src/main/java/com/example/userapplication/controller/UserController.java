package com.example.userapplication.controller;

import com.example.userapplication.Response;
import com.example.userapplication.dto.LoginDTO;
import com.example.userapplication.dto.RegisterDTO;
import com.example.userapplication.dto.UpdateDTO;
import com.example.userapplication.model.UserModel;
import com.example.userapplication.repository.UserRepositery;
import com.example.userapplication.service.IuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepositery userRepositery;

    @Autowired
    IuserService iuserService;



    @GetMapping("/hello")
    public String printHello(){
        return "hello World";
    }

    @PostMapping("/print")
    public String display(){
        return "Welcome to User Application";
    }

    @PostMapping("/addUser")
    public ResponseEntity<Response> addUser(@RequestBody RegisterDTO registerDTO){
        iuserService.registerUser(registerDTO);
        Response response = new Response(registerDTO, "UserData Added Successfull");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getData")
    public ResponseEntity<Response> getUserData(@RequestHeader String token) {
        UserModel getUserData = iuserService.getUserData(token);
        Response response = new Response(getUserData, "User Data");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteUserData(@RequestParam int id) {
        iuserService.deleteUser(id);
        Response response = new Response("Deleted for id: " + id, "Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUserData(@PathVariable int id, @RequestBody RegisterDTO registerDTO) {
        UserModel update = iuserService.updateUserData(id, registerDTO);
        Response response = new Response(update, "User Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginPage(@RequestBody LoginDTO loginDTO) {
         String token = iuserService.login(loginDTO);
        Response response = new Response(token, "Login Succesfull");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //----------------------------- validate JWT token & Update UserData --------------------------------

    @PutMapping("/UpdateData/UsingToken")
    public ResponseEntity<Response> updateData(UpdateDTO updateDTO, @RequestHeader String token)  {
        UserModel update = iuserService.update(updateDTO, token);
        Response response = new Response(update, "User Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/DeleteUser/UsingToken")
    public ResponseEntity<Response> deleteUser(@RequestHeader String token) {
        iuserService.delete(token);
        Response response = new Response("Deleted for User: ", "Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/LogOut")
    public ResponseEntity<Response> logOutUser(@RequestHeader String token) {
        iuserService.logOut(token);
        Response response = new Response("User Logout", "SuccessFully Logout");
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/Forgot_Password")
    public ResponseEntity<Response> forgotPassword(@RequestParam String password, @RequestHeader String token) {
        iuserService.forgotPassword(password, token);
        Response response = new Response("Please Login again with new password", "Password changed Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> getAllUser(@RequestParam int id){
        List<UserModel> userModelList = iuserService.getAllUsers(id);
        Response response = new Response(userModelList, "All users Data" );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
