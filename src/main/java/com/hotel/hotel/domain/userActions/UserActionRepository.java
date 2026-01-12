package com.hotel.hotel.domain.userActions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserActionRepository extends JpaRepository<UserAction, Long>, JpaSpecificationExecutor<UserAction>{
    
}
