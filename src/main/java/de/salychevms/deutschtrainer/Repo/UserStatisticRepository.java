package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.UserStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, Long> {
}
