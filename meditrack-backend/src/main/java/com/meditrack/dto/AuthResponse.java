package com.meditrack.dto;

public class AuthResponse {
    private String token;
    private String role;
    private String name;
    private Long userId;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, String role, String name, Long userId, String message) {
        this.token = token;
        this.role = role;
        this.name = name;
        this.userId = userId;
        this.message = message;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

    public static class AuthResponseBuilder {
        private AuthResponse r = new AuthResponse();
        public AuthResponseBuilder token(String t) { r.token = t; return this; }
        public AuthResponseBuilder role(String rl) { r.role = rl; return this; }
        public AuthResponseBuilder name(String n) { r.name = n; return this; }
        public AuthResponseBuilder userId(Long u) { r.userId = u; return this; }
        public AuthResponseBuilder message(String m) { r.message = m; return this; }
        public AuthResponse build() { return r; }
    }
}