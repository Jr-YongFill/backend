    package com.yongfill.server.domain.member.repository;

    import com.yongfill.server.domain.member.entity.Member;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface MemberJpaRepository extends JpaRepository<Member, Long> {

        Optional<Member> findMemberById(Long id);
        Optional<Member> findMemberByEmail(String Email);
        Optional<Member> findMemberByNickname(String NickName);
    }
