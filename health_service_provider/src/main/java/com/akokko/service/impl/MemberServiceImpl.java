package com.akokko.service.impl;

import com.akokko.dao.MemberDao;
import com.akokko.pojo.Member;
import com.akokko.service.MemberService;
import com.akokko.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Integer> findMemberCountByMonth(List<String> date) {
        //创建人数集合
        List<Integer> memberCount = new ArrayList<>();
        //初始化数据
        if (date != null) {
            for (String s : date) {
                s = s + ".31";
                //调用dao查询数据库
                Integer count = memberDao.findMemberCountBeforeDate(s);
                //将数据添加到数组里
                memberCount.add(count);
            }
        }
        return memberCount;
    }
}
