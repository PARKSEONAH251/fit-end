package com.example.demo.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.RawFood.RawFood;
import com.example.demo.Jwt.JwtUtil;
import com.example.demo.Repo.RepoRawFood;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestApi {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RepoRawFood RepoRawFood;

    @GetMapping("/api/data")
    public ResponseEntity<Map<String, Object>> getData(@RequestParam("jwt") String jwt,
            @RequestParam("foodnm") String foodNm) {
        // JWT 검증 로직

        String username = jwtUtil.extractUsername(jwt);
        if (username != null && jwtUtil.validateToken(jwt, username)) {
            // JSON 데이터 생성
            List<RawFood> rawFoods = RepoRawFood.findByFoodNmContaining(foodNm);

            // JSON 데이터 생성
            Map<String, Object> data = new HashMap<>();
            data.put("message", "JWT가 성공적으로 검증되었습니다.");
            data.put("jwt", jwt);
            data.put("username", username);
            data.put("rawFoods", rawFoods); // 데이터베이스에서 가져온 RawFood 데이터 추가

            return ResponseEntity.ok(data);
        } else {
            // JWT 검증 실패 시
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("error", "JWT 검증에 실패했습니다.");
            return ResponseEntity.status(401).body(errorData);
        }
    }
}