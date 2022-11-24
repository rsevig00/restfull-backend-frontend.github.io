package com.microservices.microservice.rest;

import com.google.gson.Gson;
import com.microservices.microservice.model.entitys.User;
import com.microservices.microservice.model.entitys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {

    // standard constructors
    @Autowired
    private final UserRepository userRepository = null;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Gson gson = new Gson();

    @GetMapping("/users")
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        //check if username exists
        if (!userRepository.findByUsername(user.getName()).isEmpty()) {
            return ResponseEntity.ok(gson.toJson("Username already exists"));
        } else if(!userRepository.findByEmail(user.getEmail()).isEmpty()){
            return ResponseEntity.ok(gson.toJson("Email already exists"));
        } else {
            User userFinal = new User(user.getName(),user.getEmail(),user.getPassword());
            User userLogged = new User(user.getName(),user.getEmail(),user.getPassword());
            userFinal.setPassword(passwordEncoder.encode(userFinal.getPassword()));
            userRepository.save(userFinal);

            //Actualizar login
            final String uri = "http://backend-login:8082/auth/users";
            RestTemplate restTemplate = new RestTemplate();
            System.out.println("User logged: " + userLogged.getName() + " " + userLogged.getEmail() + " " + userLogged.getPassword());
            User result = restTemplate.postForObject(uri, userLogged, User.class);
            return ResponseEntity.ok(gson.toJson(0));
        }
    }

    @DeleteMapping("/users/{id}")
    @ResponseBody
    //Solo funciona si el parametro tiene el mismo nombre que la variable
    public void removeUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        final String uri = "http://backend-login:8082/auth/users";
        RestTemplate restTemplate = new RestTemplate();
        //delete user from login
        restTemplate.delete(uri + "/" + id);
    }

    @PutMapping("/users")
    public ResponseEntity<String> modifyUser(@RequestBody User updatedUser) {
        if (!userRepository.findByUsername(updatedUser.getName()).isEmpty() && !user.getName().equals(updatedUser.getName())) {
            return ResponseEntity.ok(gson.toJson("Username already exists"));
        } else if(!userRepository.findByEmail(updatedUser.getEmail()).isEmpty() && !user.getEmail().equals(updatedUser.getEmail())){
            return ResponseEntity.ok(gson.toJson("Email already exists"));
        } else {
            User user = userRepository.findById(updatedUser.getId()).orElse(null);
            // This should throw NullPointerException if no user is found with the ID
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            //Save in login
            final String uri = "http://backend-login:8082/auth/users";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(uri, updatedUser, User.class);
            return ResponseEntity.ok(gson.toJson(0));
        }
    }
}