package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.UserDictionary;
import de.salychevms.deutschtrainer.Models.UserStatistic;
import org.aspectj.weaver.ast.Literal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, Long> {
    Optional<UserStatistic> findByWord(UserDictionary userDictionary);

    List<UserStatistic> findAllByNewWordIsTrue();

    @Query("SELECT us FROM UserStatistic us ORDER BY us.failsAll DESC")
    List<UserStatistic> findAllOrderByFailsAllDesc();

    @Query("SELECT us FROM UserStatistic us ORDER BY us.iterationsAll ASC")
    List<UserStatistic> findAllOrderByIterationsAllAsc();

}
