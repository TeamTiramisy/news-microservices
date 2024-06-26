package com.alibou.newsmicroservices.service;

import com.alibou.newsmicroservices.dto.UserCreateDto;
import com.alibou.newsmicroservices.dto.UserDto;
import com.alibou.newsmicroservices.exception.ResourceNotFoundException;
import com.alibou.newsmicroservices.mapper.UserMapper;
import com.alibou.newsmicroservices.repository.UserRepository;
import com.alibou.newsmicroservices.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> findAll(Pageable pageable){
        return userRepository.findAll(pageable).stream()
                .map(userMapper::mapToDto)
                .collect(toList());
    }

    public UserDto findById(Integer id){
        return userRepository.findById(id)
                .map(userMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));
    }

    public UserDto findByUserName(String username){
        return userRepository.findByUsername(username)
                .map(userMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_USERNAME, username, Constant.ERROR_CODE));
    }

    @Transactional
    public UserDto save(UserCreateDto userCreateDto){
        return Optional.of(userCreateDto)
                .map(userMapper::mapToEntity)
                .map(userRepository::save)
                .map(userMapper::mapToDto)
                .orElseThrow();
    }

    @Transactional
    public UserDto update(Integer id, UserCreateDto userCreateDto){
        return userRepository.findById(id)
                .map(user -> userMapper.mapToUpdate(user, userCreateDto))
                .map(userRepository::saveAndFlush)
                .map(userMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));
    }

    @Transactional
    public boolean delete(Integer id){
        return userRepository.findById(id)
                .map(comment -> {
                    userRepository.delete(comment);
                    return true;
                })
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));
    }
}
