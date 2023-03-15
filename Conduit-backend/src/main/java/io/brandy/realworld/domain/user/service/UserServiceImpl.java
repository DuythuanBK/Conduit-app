package io.brandy.realworld.domain.user.service;

import io.brandy.realworld.domain.user.dto.UserDto;
import io.brandy.realworld.domain.user.entity.UserEntity;
import io.brandy.realworld.domain.user.repository.UserRepository;
import io.brandy.realworld.expection.AppException;
import io.brandy.realworld.expection.Error;
import io.brandy.realworld.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private UserDto convertEntityToDto(UserEntity userEntity) {
        return new UserDto(userEntity.getEmail(),
                            jwtUtils.encode(userEntity.getName()),
                            userEntity.getName(),
                            userEntity.getBio(),
                            userEntity.getImage());

    }

    @Override
    public UserDto registration(UserDto.Registration registration) {
        userRepository.findByNameOrEmail(registration.getName(), registration.getEmail())
                .ifPresent(entity -> { throw new AppException(Error.DUPLICATED_USER); });
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registration.getName());
        userEntity.setPassword(passwordEncoder.encode(registration.getPassword()));
        userEntity.setEmail(registration.getEmail());
        userRepository.save(userEntity);

        return convertEntityToDto(userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto login(UserDto.Login login) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(login.getEmail());

        if(!userEntity.isPresent()) {
            throw new AppException(Error.USER_NOT_FOUND);
        }

        if(!passwordEncoder.matches(login.getPassword(), userEntity.get().getPassword())) {
            throw new AppException(Error.LOGIN_INFO_INVALID);
        }

        return convertEntityToDto(userEntity.get());
    }

    @Override
    public UserDto currentUser(UserDto.Auth authUser) {
        if(authUser == null) {
            throw new AppException(Error.USER_NOT_FOUND);
        }

        UserEntity userEntity = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));

        return convertEntityToDto(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto.Auth authUser, UserDto.Update update) {
        UserEntity userEntity = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));

        if(update.getName() != null) {
            userRepository.findByName(update.getName())
                    .filter(found -> !found.getId().equals(userEntity.getId()))
                    .ifPresent(found -> { throw new AppException(Error.DUPLICATED_USER); });

            userEntity.setName(update.getName());
        }

        if(update.getEmail() != null) {
            userRepository.findByEmail(update.getEmail())
                    .filter(found -> !found.getId().equals(userEntity.getId()))
                    .ifPresent(found -> { throw new AppException(Error.DUPLICATED_USER); } );

            userEntity.setEmail(update.getEmail());
        }

        if(update.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(update.getPassword()));
        }

        if(update.getImage() != null) {
            userEntity.setImage(update.getImage());
        }

        if(update.getBio() != null) {
            userEntity.setBio(update.getBio());
        }

        userRepository.save(userEntity);
        return convertEntityToDto(userEntity);
    }


}
