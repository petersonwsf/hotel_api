package com.hotel.hotel.modules.userActions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hotel.hotel.modules.userActions.model.UserAction;

public interface UserActionRepository extends JpaRepository<UserAction, Long>, JpaSpecificationExecutor<UserAction>{}
