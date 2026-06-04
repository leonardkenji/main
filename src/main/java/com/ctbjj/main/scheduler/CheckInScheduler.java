package com.ctbjj.main.scheduler;

import com.ctbjj.main.service.CheckInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckInScheduler {

    private final CheckInService checkInService;

    @Scheduled(fixedDelay = 60_000)
    public void autoCheckout() {
        log.debug("Executando auto-checkout de check-ins expirados");
        checkInService.autoCheckout();
    }
}
