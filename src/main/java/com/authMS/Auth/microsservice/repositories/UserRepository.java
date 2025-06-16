package com.authMS.Auth.microsservice.repositories;

import com.authMS.Auth.microsservice.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {

}
