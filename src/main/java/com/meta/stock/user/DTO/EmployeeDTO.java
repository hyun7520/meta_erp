package com.meta.stock.user.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    // Request용 DTO (생성/수정 시 사용)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String name;           // 이름
        private String email;          // 이메일
        private String password;       // 비밀번호
        private long departmentId;     // 부서 ID
        private long roleId;           // 역할 ID
    }

    // Response용 DTO (조회 시 사용)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private long employeeId;       // 직원 ID
        private String name;           // 이름
        private String email;          // 이메일
        private String hireDate;       // 입사일
        private long departmentId;     // 부서 ID
        private String departmentName; // 부서명
        private long roleId;           // 역할 ID
        private String roleName;       // 역할명
    }
}