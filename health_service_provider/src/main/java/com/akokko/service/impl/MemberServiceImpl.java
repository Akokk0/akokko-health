package com.akokko.service.impl;

import com.akokko.dao.MemberDao;
import com.akokko.pojo.Member;
import com.akokko.service.MemberService;
import com.akokko.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            String md5_password = MD5Utils.md5(password);
            member.setPassword(md5_password);
        }
        memberDao.add(member);
    }
}
