package de.salychevms.deutschtrainer.TrainerDataBase.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserDictionary;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserStatistic;
import de.salychevms.deutschtrainer.TrainerDataBase.Repo.UserStatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserStatisticService {
    private final UserStatisticRepository userStatisticRepository;

    public UserStatisticService(UserStatisticRepository userStatisticRepository) {
        this.userStatisticRepository = userStatisticRepository;
    }

    @Transactional
    public Optional<UserStatistic> getUserStatisticById(Long id) {
        return userStatisticRepository.findById(id);
    }

    @Transactional
    public Optional<UserStatistic> getUserStatisticByUserDictionary(UserDictionary userDictionary) {
        return userStatisticRepository.findByWord(userDictionary);
    }

    @Transactional
    public List<UserStatistic> getUserStatisticSortByFailsAllDesc() {
        return userStatisticRepository.findAllOrderByFailsAllDesc();
    }

    @Transactional
    public List<UserStatistic> getUserStatisticSortByIterationAllAsc() {
        return userStatisticRepository.findAllOrderByIterationsAllAsc();
    }

    @Transactional
    public List<UserStatistic> getUserStatisticNewWordIsTrue() {
        return userStatisticRepository.findAllByNewWordIsTrue();
    }

    @Transactional
    public void saveNewWordInStatistic(UserDictionary userDictionary) {
        userStatisticRepository.save(new UserStatistic(userDictionary));
    }

    @Transactional
    public void updateStatusNewWordByUserDictionary(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent() && statistic.get().isNewWord() && statistic.get().getIterationsAll() >= 5) {
            UserStatistic updatableStatistic = statistic.get();
            updatableStatistic.setNewWord(false);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateAllIteration(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            Long count = updatableStatistic.getIterationsAll();
            if (count != null) {
                count++;
                updatableStatistic.setIterationsAll(count);
            } else {
                updatableStatistic.setIterationsAll(1L);
            }
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateMonthIteration(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            Long count = updatableStatistic.getIterationsPerMonth();
            if (count != null) {
                count++;
                updatableStatistic.setIterationsPerMonth(count);
            } else {
                updatableStatistic.setIterationsPerMonth(1L);
            }
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void deleteMonthIteration(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            updatableStatistic.setIterationsPerMonth(null);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateWeekIteration(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            Long count = updatableStatistic.getIterationsPerWeek();
            if (count != null) {
                count++;
                updatableStatistic.setIterationsPerWeek(count);
            } else {
                updatableStatistic.setIterationsPerWeek(1L);
            }
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void deleteWeekIteration(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            updatableStatistic.setIterationsPerWeek(null);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateDayIteration(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            Long count = updatableStatistic.getIterationsPerDay();
            if (count != null) {
                count++;
                updatableStatistic.setIterationsPerDay(count);
            } else {
                updatableStatistic.setIterationsPerDay(1L);
            }
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void deleteDayIteration(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            updatableStatistic.setIterationsPerDay(null);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateAllFails(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            Long count = updatableStatistic.getFailsAll();
            if (count != null) {
                count++;
                updatableStatistic.setFailsAll(count);
            } else {
                updatableStatistic.setFailsAll(1L);
            }
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateMonthFails(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            Long count = updatableStatistic.getFailsPerMonth();
            if (count != null) {
                count++;
                updatableStatistic.setFailsPerMonth(count);
            } else {
                updatableStatistic.setFailsPerMonth(1L);
            }
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void deleteMonthFails(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            updatableStatistic.setFailsPerMonth(null);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateWeekFails(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            Long count = updatableStatistic.getFailsPerWeek();
            if (count != null) {
                count++;
                updatableStatistic.setFailsPerWeek(count);
            } else {
                updatableStatistic.setFailsPerWeek(1L);
            }
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void deleteWeekFails(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            updatableStatistic.setFailsPerWeek(null);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateDayFails(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            Long count = updatableStatistic.getFailsPerDay();
            if (count != null) {
                count++;
                updatableStatistic.setFailsPerDay(count);
            } else {
                updatableStatistic.setFailsPerDay(1L);
            }
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void deleteDayFails(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            updatableStatistic.setFailsPerDay(null);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void updateLastTraining(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            updatableStatistic.setLastTraining(new Date());
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public List<UserStatistic> findAll() {
        List<UserStatistic> allStatistic = userStatisticRepository.findAll();
        if (!allStatistic.isEmpty()) {
            return allStatistic;
        } else return null;
    }

    @Transactional
    public void setFailStatusTrue(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            if (!updatableStatistic.isFailStatus()) {
                updatableStatistic.setFailStatus(true);
            }
            updatableStatistic.setFailTraining(5);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void setFailStatusFalse(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        if (statistic.isPresent()) {
            UserStatistic updatableStatistic = statistic.get();
            if (updatableStatistic.isFailStatus()) {
                updatableStatistic.setFailStatus(false);
            }
            updatableStatistic.setFailTraining(null);
            userStatisticRepository.save(updatableStatistic);
        }
    }

    @Transactional
    public void decreaseFailTraining(UserDictionary userDictionary) {
        Optional<UserStatistic> statistic = userStatisticRepository.findByWord(userDictionary);
        statistic.ifPresent(updatableStatistic -> {
            Integer failTraining = updatableStatistic.getFailTraining();
            if (failTraining != null) {
                int newFailTraining = failTraining - 1;
                updatableStatistic.setFailTraining(newFailTraining <= 0 ? null : newFailTraining);
                if (newFailTraining <= 0) {
                    updatableStatistic.setFailStatus(false);
                }
                userStatisticRepository.save(updatableStatistic);
            }
        });
    }

    @Transactional
    public int getCountPairsWithNewWordForUserAndLanguage(Long telegramId, String languageIdentifier){
        return Math.toIntExact(userStatisticRepository.countPairsWithNewWordForUserAndLanguage(telegramId, languageIdentifier));
    }

    @Transactional
    public Date findLastTrainingForUserAndLanguage(Long telegramId, String languageIdentifier){
        return userStatisticRepository.findLastTrainingForUserAndLanguage(telegramId, languageIdentifier);
    }

    @Transactional
    public List<UserStatistic> findWordsWithMaxFailsAllForUserAndLanguageIdentifier(Long telegramId, String languageIdentifier){
        return userStatisticRepository.findWordsWithMaxFailsAllForUserAndLanguageIdentifier(telegramId, languageIdentifier);
    }

    @Transactional
    public int countWordsWithFailStatusForUserAndLanguage(Long telegramId, String languageIdentifier){
        return userStatisticRepository.countWordsWithFailStatusForUserAndLanguage(telegramId, languageIdentifier);
    }
}
