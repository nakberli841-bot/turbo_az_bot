package com.nurlan.turboazbot.turbo_az_bot.repo;

import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepo extends JpaRepository<User,Integer> {

    public User findByEmail(@Param("email") String email);

}
