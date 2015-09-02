package com.srinsoft.slenium.tests.web.repo;

import java.util.List;

import com.srinsoft.slenium.tests.web.domain.Member;

public interface MemberDao
{
    public Member findById(Long id);

    public Member findByEmail(String email);

    public List<Member> findAllOrderedByName();

    public void register(Member member);
}
