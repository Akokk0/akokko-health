package com.akokko.service;

import com.akokko.pojo.Member;

public interface MemberService {
    Member findByTelephone(String telephone);
    void add(Member member);
}
