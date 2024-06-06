package com.alibou.newsmicroservices.controller;

import com.alibou.newsmicroservices.dto.UserCreateDto;
import com.alibou.newsmicroservices.dto.UserDto;
import com.alibou.newsmicroservices.service.UserService;
import com.alibou.newsmicroservices.validation.CreateAction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(Pageable pageable){
        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable @Positive Integer id){
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable @Email String username){
        return new ResponseEntity<>(userService.findByUserName(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody @Validated({Default.class, CreateAction.class}) UserCreateDto userCreateDto){
        return new ResponseEntity<>(userService.save(userCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> update(@PathVariable @Positive Integer id,
                                          @RequestBody @Validated UserCreateDto userCreateDto){
        return new ResponseEntity<>(userService.update(id, userCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Positive Integer id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
