package com.finance_dashboard.users;

record User(
    Long userId, 
    String username, 
    String email,
    String hashPassword,
    String phoneNumber
) {
    
}
