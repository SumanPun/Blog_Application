package com.example.blog.services;

import com.example.blog.config.AppConstants;
import com.example.blog.entites.Role;
import com.example.blog.entites.User;
import com.example.blog.payloads.UserDto;
import com.example.blog.repositories.RoleRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.exceptions.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    public UserDto createUser(UserDto userDto) {

        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepository.save(user);
        return this.userToDto(savedUser);
    }

    public UserDto updateUser(UserDto userDto, Integer user_id){
        User user = this.userRepository.findById(user_id)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",user_id));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        User updatedUser = this.userRepository.save(user);
        UserDto userDto1 = this.userToDto(updatedUser);
        return userDto1;
    }

    public UserDto getById(Integer user_Id) {
        User user = this.userRepository.findById(user_Id)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",user_Id));
        return this.userToDto(user);
    }

    public List<UserDto> getAllUser() {
       List<User> users = this.userRepository.findAll();
       List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
       return userDtos;
    }

    public void deleteUser(Integer user_id) {
        User user = this.userRepository.findById(user_id)
                .orElseThrow(()->new ResourceNotFoundException("user","Id",user_id));
        this.userRepository.delete(user);
    }

    public UserDto registerNewUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        //encode the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        //roles
       Role role = this.roleRepository.findById(AppConstants.NORMAL_USER).get();
       user.getRoles().add(role);
       User newUser = this.userRepository.save(user);
       return this.userToDto(newUser);
    }

    private User dtoToUser(UserDto userDto) {

        User user = this.modelMapper.map(userDto,User.class);

        //User user = new User();
//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setPassword(userDto.getPassword());
//        user.setAbout(userDto.getAbout());
        return user;
    }

    private UserDto userToDto(User user) {

        UserDto userDto = this.modelMapper.map(user,UserDto.class);


//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setName(user.getName());
//        userDto.setEmail(user.getEmail());
//        userDto.setPassword(user.getPassword());
//        userDto.setAbout(user.getAbout());
        return userDto;
    }


}
