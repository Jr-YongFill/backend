package com.yongfill.server.domain.posts.service;


import com.yongfill.server.domain.posts.repository.ViewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final ViewJpaRepository viewJpaRepository;

    // 매일 자정 12:00에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void run() {
        viewJpaRepository.deleteAll();
    }
}