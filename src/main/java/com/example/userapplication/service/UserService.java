package com.example.userapplication.service;

import com.example.userapplication.dto.LoginDTO;
import com.example.userapplication.dto.RegisterDTO;
import com.example.userapplication.dto.UpdateDTO;
import com.example.userapplication.exception.UserException;
import com.example.userapplication.model.UserModel;
import com.example.userapplication.repository.UserRepositery;
import com.example.userapplication.utility.EmailService;
import com.example.userapplication.utility.JwtUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IuserService {

    @Autowired
    UserRepositery userRepositery;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EmailService emailService;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public String registerUser(RegisterDTO registerDTO) {
        if (userRepositery.findByEmail(registerDTO.getEmail()) == null) {
            /*logger.error("In Error");
            logger.warn("In Warn");
            logger.info("In Info");
            logger.debug("In debug"); //this use max time as devlper
            logger.trace("In Trace");
             */
            UserModel userModel = modelMapper.map(registerDTO, UserModel.class);
            userRepositery.save(userModel);
            logger.debug("In debug");
            emailService.sendMail(registerDTO.getEmail(), "User Registered Successful");
            return "User Added Successfully";
        }
        throw new UserException("User Already Exist");
    }

    @Override
    public UserModel getUserData(String token) {
        LoginDTO loginDTO = jwtUtils.decodeToken(token);
        UserModel user = userRepositery.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (userRepositery.findByEmail(user.getEmail()).isLogin()) {
            return userRepositery.findById(user.getId()).get();
        }
        throw new UserException("Id is invalid");
    }

    @Override
    public String deleteUser(int id) {
        if (userRepositery.findById(id).isPresent()) {
            userRepositery.deleteById(id);
            return "data Deleted";
        }
        throw new UserException("User not Found");
    }



    //use this one only
    @Override
    public String login(LoginDTO loginDTO) {
        if (userRepositery.findByEmail(loginDTO.getEmail()) != null) {
            if (userRepositery.findByEmail(loginDTO.getEmail()).getPassword().equals(loginDTO.getPassword())) {
                String token =  jwtUtils.generateToken(loginDTO);
                UserModel userModel = userRepositery.findByEmail(loginDTO.getEmail());
                userModel.setLogin(true);
                userModel.setId(userModel.getId());
                userRepositery.save(userModel);
                return token;
            }
            throw new UserException("please check Your Password");
        }
        throw new UserException("Check Your Email-ID");
    }



    @Override
    public String userLogin(LoginDTO loginDTO) {
        if (userRepositery.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()) != null) {
            String token =  jwtUtils.generateToken(loginDTO);
            UserModel userModel = userRepositery.findByEmail(loginDTO.getEmail());
            userModel.setLogin(true);
            userModel.setId(userModel.getId());
            userRepositery.save(userModel);
            return token;
        }
        throw new UserException("Check Your Email-id and Password");
    }

    @Override
    public List<UserModel> getAllUsers(int id) {
        if (userRepositery.findById(id).get().getRole().equals("Admin")) {
            return userRepositery.findAll();
        }
        throw new UserException("Not Accessable to You");
    }

    @Override
    public UserModel updateUserData(int id, RegisterDTO registerDTO) {
        if (userRepositery.findById(id).isPresent()) {
            UserModel model = userRepositery.findById(id).get();
            UserModel update = modelMapper.map(registerDTO, UserModel.class);
            update.setId(id);
            if (userRepositery.findByEmail(registerDTO.getEmail()).equals(null)) {
                System.out.println(update.toString());
                if (update.getFirstName() == null){
                    update.setFirstName(model.getFirstName());
                }
                if (update.getLastName() == null){
                    update.setLastName(model.getLastName());
                }
                if (update.getAddress() == null){
                    update.setAddress(model.getAddress());
                }
                if (update.getEmail() == null){
                    update.setEmail(model.getEmail());
                }
                if (update.getPassword() == null){
                    update.setPassword(model.getPassword());
                }
                if (update.getRole() == null) {
                    update.setRole(model.getRole());
                }
                return userRepositery.save(update);
            }
            throw new UserException("This email id is already present in database...please try with anthor email id");
        }
        throw new UserException("Id is invalid");
    }

//**------**----**------**-------**----**-------** validate JWT token **--------------------------------
    @Override
    public UserModel update(UpdateDTO updateDTO, String token) {
        LoginDTO loginDTO = jwtUtils.decodeToken(token);
        UserModel user = userRepositery.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (user != null) {
            UserModel update = modelMapper.map(updateDTO, UserModel.class);
            update.setId(user.getId());
            update.setLogin(true);
                System.out.println(update);
                if (update.getFirstName() == null) {
                    update.setFirstName(user.getFirstName());
                }
                if (update.getLastName() == null) {
                    update.setLastName(user.getLastName());
                }
                if (update.getAddress() == null) {
                    update.setAddress(user.getAddress());
                }
                if (update.getEmail() == null) {
                    update.setEmail(user.getEmail());
                }
                if (update.getPassword() == null) {
                    update.setPassword(user.getPassword());
                }
                if (update.getRole() == null) {
                    update.setRole(user.getRole());
                }
                return userRepositery.save(update);
        }
        throw new UserException("Email And Password is not Matched");
    }

    @Override
    public String forgotPassword(String password, String token) {
        LoginDTO loginDTO = jwtUtils.decodeToken(token);
        UserModel user = userRepositery.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (userRepositery.findByEmail(user.getEmail()).isLogin()) {
            user.setPassword(password);
            user.setLogin(false);
            user.setId(user.getId());
            userRepositery.save(user);
            return "Password changed Successful";
        }
        throw new UserException("please Login with Proper email and password");

    }


    @Override
    public String delete(String token) {
        LoginDTO loginDTO = jwtUtils.decodeToken(token);
        UserModel user = userRepositery.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (userRepositery.findByEmail(user.getEmail()).isLogin()) {
            userRepositery.deleteById(user.getId());
            return "data Deleted";
        }
        throw new UserException("User not Found");
    }

    @Override
    public String logOut(String token) {
        LoginDTO loginDTO = jwtUtils.decodeToken(token);
        UserModel userLogout = userRepositery.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (userRepositery.findByEmail(userLogout.getEmail()).isLogin()) {
            userLogout.setLogin(false);
            //userLogout.setId(userLogout.getId());
            userRepositery.save(userLogout);
            return "User Logout Successfully";
        }
        throw new UserException("User Not Found");

    }

}
