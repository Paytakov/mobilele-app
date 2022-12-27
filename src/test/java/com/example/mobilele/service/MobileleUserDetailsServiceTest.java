package com.example.mobilele.service;

import com.example.mobilele.model.entity.UserEntity;
import com.example.mobilele.model.entity.UserRoleEntity;
import com.example.mobilele.model.enums.UserRoleEnum;
import com.example.mobilele.model.user.MobileleUserDetails;
import com.example.mobilele.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MobileleUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepo;

    private MobileleUserDetailsService toTest;

    @BeforeEach
    void setUp() {
        toTest = new MobileleUserDetailsService(
                mockUserRepo
        );
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername("non-existant@example.com")
        );
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        UserEntity testUser = new UserEntity()
                .setEmail("test@example.com")
                .setFirstName("Gosho")
                .setLastName("Goshev")
                .setPassword("topsecret")
                .setActive(true)
                .setImageUrl("http://image.com/image")
                .setUserRoles(
                        List.of(
                                new UserRoleEntity().setUserRole(UserRoleEnum.ADMIN),
                                new UserRoleEntity().setUserRole(UserRoleEnum.USER)
                        )
                );

        when(mockUserRepo.findByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));

        MobileleUserDetails userDetails = (MobileleUserDetails)
                toTest.loadUserByUsername(testUser.getEmail());

        Assertions.assertEquals(testUser.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(testUser.getFirstName() + " " + testUser.getLastName(),
                userDetails.getFullName());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());

        var authorities = userDetails.getAuthorities();

        Assertions.assertEquals(2, authorities.size());

        var authoritiesIter= authorities.iterator();

        Assertions.assertEquals("ROLE_" + UserRoleEnum.ADMIN,
                authoritiesIter.next().getAuthority());
        Assertions.assertEquals("ROLE_" + UserRoleEnum.USER,
                authoritiesIter.next().getAuthority());

    }
}
