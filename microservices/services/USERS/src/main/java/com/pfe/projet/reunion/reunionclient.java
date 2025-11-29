package com.pfe.projet.reunion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(name = "reunions-service",
        url = "http://localhost:8010/api/v1/Reunions")
public interface reunionclient  {
    @GetMapping("/{reunion-id}")
    Optional<ReunionResponse> findReunionById(@PathVariable("reunion-id") String reunionId);
    @PostMapping("/api/v1/Interventions/{interventionId}/assign-user/{userId}")
    void assignUserToIntervention(@PathVariable("interventionId") Long interventionId, @PathVariable("userId") Long userId);


}
