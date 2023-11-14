package com.app.stockproject.service;

import com.app.stockproject.bean.Role;
import com.app.stockproject.bean.User;
import com.app.stockproject.dao.UserDao;
import com.app.stockproject.dto.UserDto;
import com.app.stockproject.interfaces.IService;
import com.app.stockproject.utils.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService  {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Transactional
    @Cacheable(cacheNames = "sd::api_users", key = "'user_'+#id")
    public UserDto getById(Long id) {
        try {
            Optional<User> optionalUser = userDao.findById(id);
            if (optionalUser.isPresent() && optionalUser.get().isActivo()) {
                return entityToDto(optionalUser.get());
            }
            return null; // User not found or not active
        } catch (Exception e) {
            logger.error("Error getting user by ID", e);
            throw e;
        }
    }

    @Transactional
    public List<UserDto> getAll(int page) {
        try {
            Pageable pageable = PageRequest.of(page, Setting.PAGE_SIZE);
            List<User> activeUsers = userDao.findAllByActivoIsTrue(pageable);
            List<UserDto> userDtoList = convertToDtoList(activeUsers);

            // Cache each user manually in Redis
            for (UserDto userDto : userDtoList) {
                String cacheName = "sd::api_users";
                String key = "user_" + userDto.getId();
                Cache cache = cacheManager.getCache(cacheName);

                // Check if the user is already in the cache
                Cache.ValueWrapper valueWrapper = cache.get(key);

                if (valueWrapper == null) {
                    // If not in the cache, cache it
                    cache.put(key, userDto);
                }
            }

            return userDtoList;
        } catch (Exception e) {
            logger.error("Error getting all users", e);
            throw e;
        }
    }


    @Transactional
    @CachePut(cacheNames = "sd::api_users", key = "'user_'+#id")
    public UserDto update(Long id, UserDto userDto) {
        try {
            Optional<User> optionalUser = userDao.findById(id);
            if (optionalUser.isPresent()) {
                User user = dtoToEntity(userDto);
                user.setId(id);
                userDao.save(user);
                return entityToDto(user);
            }
            return null; // User not found
        } catch (Exception e) {
            logger.error("Error updating user", e);
            throw e;
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "sd::api_users", key = "'user_'+#id")
    public boolean delete(Long id) {
        try {
            Optional<User> optionalUser = userDao.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setActivo(false); // Logical deletion
                userDao.save(user);
                return true;
            }
            return false; // User not found
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            throw e;
        }
    }

    private UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole().name());
        userDto.setActivo(user.isActivo());
        return userDto;
    }

    private User dtoToEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setActivo(userDto.isActivo());
        return user;
    }

    private List<UserDto> convertToDtoList(List<User> users) {
        return users.stream().map(this::entityToDto).toList();
    }

}
