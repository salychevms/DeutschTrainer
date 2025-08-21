package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.DeRuPairsController;
import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.DeutschController;
import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.RussianController;
import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.UserDictionaryController;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.DeRuPairsService;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeRuPairsControllerTest {
    @Mock
    private DeRuPairsService deRuPairsService;
    @Mock
    private RussianController russianController;
    @Mock
    private DeutschController deutschController;
    @Mock
    private UserDictionaryController userDictionaryController;
    @InjectMocks
    private DeRuPairsController deRuPairsController;

    @Test
    void testCreatePairs() {
        Deutsch deutsch = new Deutsch("word");
        Russian ru1 = new Russian("ru1");
        Russian ru2 = new Russian("ru2");
        List<Russian> russians = new ArrayList<>();
        russians.add(ru1);
        russians.add(ru2);
        Long pair1Id = 123L;
        Long pair2Id = 222L;
        List<Long> pairs = new ArrayList<>();
        pairs.add(pair1Id);
        pairs.add(pair2Id);

        when(deRuPairsService.createNewPairs(deutsch, ru1)).thenReturn(pair1Id);
        when(deRuPairsService.createNewPairs(deutsch, ru2)).thenReturn(pair2Id);

        List<Long> result = deRuPairsController.createPairs(deutsch, russians);

        verify(deRuPairsService, times(1)).createNewPairs(deutsch, ru1);
        verify(deRuPairsService, times(1)).createNewPairs(deutsch, ru2);

        assertEquals(2, result.size());
        assertEquals(pairs, result);
    }

    @Test
    void testGetAllWordPairsByPairId() {
        Long germanId = 1L;
        List<Long> pairsId = new ArrayList<>();
        pairsId.add(2L);
        pairsId.add(3L);
        Deutsch deutsch = new Deutsch("deutsch");
        deutsch.setId(germanId);
        Long ru1Id = 4L;
        Long ru2Id = 5L;
        Russian russian1 = new Russian("ru1");
        Russian russian2 = new Russian("ru2");
        russian1.setId(ru1Id);
        russian2.setId(ru2Id);
        DeRuPairs pair1 = new DeRuPairs(deutsch, russian1);
        DeRuPairs pair2 = new DeRuPairs(deutsch, russian2);
        String expected = "Вы добавили перевод слова: " + deutsch.getDeWord() +
                "\nНа русском это будет:\n\t" + russian1.getRuWord() + "\n\t" + russian2.getRuWord();

        when(deutschController.findById(germanId)).thenReturn(deutsch);
        when(deRuPairsService.findById(2L)).thenReturn(Optional.of(pair1));
        when(deRuPairsService.findById(3L)).thenReturn(Optional.of(pair2));
        when(russianController.findById(4L)).thenReturn(russian1);
        when(russianController.findById(5L)).thenReturn(russian2);

        String result = deRuPairsController.getAllWordPairsByPairId(germanId, pairsId);

        verify(deutschController, times(1)).findById(germanId);
        verify(deRuPairsService, times(1)).findById(2L);
        verify(deRuPairsService, times(1)).findById(3L);
        verify(russianController, times(1)).findById(4L);
        verify(russianController, times(1)).findById(5L);

        assertEquals(expected, result);
    }

    @Test
    void testIsItRussianYes() {
        String isItRu = "рофывждфлдв";

        boolean yes = deRuPairsController.isItRussian(isItRu);

        assertTrue(yes);
    }

    @Test
    void testIsItRussianNo() {
        String isItRu = "üapsifanß";

        boolean no = deRuPairsController.isItRussian(isItRu);

        assertFalse(no);
    }

    @Test
    void testIsItGermanYes() {
        String isItDe = "üapsifanß";

        boolean yes = deRuPairsController.isItGerman(isItDe);

        assertTrue(yes);
    }

    @Test
    void testIsItGermanNo() {
        String isItDe = "рофывждфлдв";

        boolean no = deRuPairsController.isItGerman(isItDe);

        assertFalse(no);
    }

    @Test
    void TestGetRUWordsWhichUserLooksFor() {
        Long telegramId = 123L;
        String identifier = "RU";
        String looksFor = "дом";
        String dom = "дом";
        String domashniy = "домашний";
        String upravdom = "управдом";

        Deutsch de = new Deutsch("was???");
        de.setId(999L);
        Russian ru1 = new Russian(dom);
        Russian ru2 = new Russian(domashniy);
        Russian ru3 = new Russian(upravdom);
        ru1.setId(555L);
        ru2.setId(666L);
        ru3.setId(777L);
        DeRuPairs pair1 = new DeRuPairs(de, ru1);
        DeRuPairs pair2 = new DeRuPairs(de, ru3);
        Long p1Id = 789L;
        Long p2Id = 890L;
        pair1.setId(p1Id);
        pair2.setId(p2Id);
        List<UserDictionary> dictionaryList = new ArrayList<>();
        UserDictionary ud1 = new UserDictionary(new UserLanguage(), pair1, new Date());
        UserDictionary ud2 = new UserDictionary(new UserLanguage(), pair2, new Date());
        dictionaryList.add(ud1);
        dictionaryList.add(ud2);
        List<Russian> found = new ArrayList<>();
        List<String> foundedWords = new ArrayList<>();
        found.add(ru1);
        found.add(ru2);
        found.add(ru3);
        foundedWords.add(dom + " <<еще варианты>>");
        foundedWords.add(domashniy);
        foundedWords.add(upravdom + " <<еще варианты>>");

        when(userDictionaryController.getAllByTelegramId(telegramId)).thenReturn(dictionaryList);
        when(deRuPairsService.findPairById(p1Id)).thenReturn(Optional.of(pair1));
        when(deRuPairsService.findPairById(p2Id)).thenReturn(Optional.of(pair2));
        when(russianController.findSmartMatches(looksFor)).thenReturn(found);
        List<String> result = deRuPairsController.getWordsWhichUserLooksFor(telegramId, looksFor, identifier);

        assertEquals(3, result.size());
        assertEquals(foundedWords, result);
        assertTrue(result.contains(dom + " <<еще варианты>>"));
        assertTrue(result.contains(domashniy));
        assertTrue(result.contains(upravdom + " <<еще варианты>>"));
    }

    @Test
    void TestGetDEWordsWhichUserLooksFor() {
        Long telegramId = 123L;

        String identifier = "DE";
        String looksFor = "unter";
        String verb = "unternehmen";
        String nomen = "das Unternehmen";
        String unterstutzung = "die Unterstützung";

        List<Deutsch> found = new ArrayList<>();
        List<String> foundedWords = new ArrayList<>();
        found.add(new Deutsch(verb));
        found.add(new Deutsch(nomen));
        found.add(new Deutsch(unterstutzung));
        foundedWords.add(verb);
        foundedWords.add(nomen);
        foundedWords.add(unterstutzung);

        when(deutschController.findSmartMatches(looksFor)).thenReturn(found);
        List<String> result = deRuPairsController.getWordsWhichUserLooksFor(telegramId, looksFor, identifier);

        assertNotNull(result);
        assertEquals(foundedWords, result);
        assertTrue(result.contains(verb));
        assertTrue(result.contains(nomen));
        assertTrue(result.contains(unterstutzung));
    }

    @Test
    void testGetPairByGermanIdAndRussianId() {
        Long ruId = 1111L;
        Long deId = 3333L;
        DeRuPairs pair = new DeRuPairs(new Deutsch("deutsch"), new Russian("russian"));

        when(deRuPairsService.findByGermanIdAndRussianId(deId, ruId)).thenReturn(Optional.of(pair));
        Optional<DeRuPairs> result = deRuPairsController.getPairByGermanIdAndRussianId(deId, ruId);

        result.ifPresent(value -> assertEquals(pair, value));
    }

    @Test
    void getDeRuById() {
        Long id = 123456L;
        DeRuPairs pair = new DeRuPairs(new Deutsch("de"), new Russian("ru"));

        when(deRuPairsService.findPairById(id)).thenReturn(Optional.of(pair));
        Optional<DeRuPairs> result = deRuPairsController.getDeRuById(id);

        result.ifPresent(value -> assertEquals(pair, value));
    }

    @Test
    void testGetDETranslations() {
        String fromLanguage = "RU";
        Long telegramId = 123L;

        String chosenWord = "111";
        Long chosenWordId = 111L;
        String ruWord="дом";
        Russian chosenRu = new Russian();
        chosenRu.setRuWord(ruWord);
        chosenRu.setId(chosenWordId);

        String haus = "das Haus";
        String gebaude = "das Gebaude";
        Deutsch de1 = new Deutsch();
        Deutsch de2 = new Deutsch();
        de1.setId(555L);
        de1.setDeWord(haus);
        de2.setId(666L);
        de2.setDeWord(gebaude);

        Map<Long, String> expected = new HashMap<>();
        expected.put(de1.getId(), haus+ " <<уже добавлено>>");
        expected.put(de2.getId(), gebaude+ " <<уже добавлено>>");

        DeRuPairs pair1 = new DeRuPairs();
        DeRuPairs pair2 = new DeRuPairs();
        pair1.setId(777L);
        pair1.setDeutsch(de1);
        pair1.setRussian(chosenRu);
        pair2.setId(888L);
        pair2.setDeutsch(de2);
        pair2.setRussian(chosenRu);
        List<DeRuPairs> pairs = new ArrayList<>();
        pairs.add(pair1);
        pairs.add(pair2);

        UserDictionary userDictionary1 = new UserDictionary();
        userDictionary1.setPair(pair1);
        userDictionary1.setId(505L);
        UserDictionary userDictionary2 = new UserDictionary();
        userDictionary2.setPair(pair2);
        userDictionary2.setId(606L);
        List<UserDictionary> userDictionaryList = new ArrayList<>();
        userDictionaryList.add(userDictionary1);
        userDictionaryList.add(userDictionary2);

        when(userDictionaryController.getAllByTelegramId(telegramId)).thenReturn(userDictionaryList);
        when(deRuPairsService.findPairById(pair1.getId())).thenReturn(Optional.of(pair1));
        when(deRuPairsService.findPairById(pair2.getId())).thenReturn(Optional.of(pair2));
        when(deRuPairsService.findAllByRussianId(chosenWordId)).thenReturn(pairs);
        when(deutschController.findById(de1.getId())).thenReturn(de1);
        when(deutschController.findById(de2.getId())).thenReturn(de2);

        Map<Long, String> result = deRuPairsController.getTranslations(telegramId, chosenWord, fromLanguage);

        assertFalse(result.isEmpty());
        assertEquals(expected, result);
    }

    @Test
    void testGetRUTranslations() {
        String fromLanguage="DE";
        Long telegramId=234L;

        String chosenWord="222";
        Long chosenWordId=222L;
        String deWord="das Haus";
        Deutsch chosenDe = new Deutsch();
        chosenDe.setDeWord(deWord);
        chosenDe.setId(chosenWordId);

        String dom="дом";
        String zdanie="здание";
        Russian ru1=new Russian(dom);
        Russian ru2=new Russian(zdanie);
        ru1.setId(888L);
        ru1.setRuWord(dom);
        ru2.setId(999L);
        ru2.setRuWord(zdanie);

        Map<Long, String> expected = new HashMap<>();
        expected.put(ru1.getId(), dom+ " <<уже добавлено>>");
        expected.put(ru2.getId(), zdanie+ " <<уже добавлено>>");

        List<DeRuPairs> pairs=new ArrayList<>();
        DeRuPairs pair1 = new DeRuPairs();
        pair1.setId(707L);
        pair1.setDeutsch(chosenDe);
        pair1.setRussian(ru1);
        DeRuPairs pair2 = new DeRuPairs();
        pair2.setDeutsch(chosenDe);
        pair2.setRussian(ru2);
        pair2.setId(808L);
        pairs.add(pair1);
        pairs.add(pair2);

        UserDictionary userDictionary1 = new UserDictionary();
        userDictionary1.setPair(pair1);
        userDictionary1.setId(505L);
        UserDictionary userDictionary2 = new UserDictionary();
        userDictionary2.setPair(pair2);
        userDictionary2.setId(606L);
        List<UserDictionary> userDictionaryList = new ArrayList<>();
        userDictionaryList.add(userDictionary1);
        userDictionaryList.add(userDictionary2);

        when(userDictionaryController.getAllByTelegramId(telegramId)).thenReturn(userDictionaryList);
        when(deRuPairsService.findPairById(pair1.getId())).thenReturn(Optional.of(pair1));
        when(deRuPairsService.findPairById(pair2.getId())).thenReturn(Optional.of(pair2));
        when(deRuPairsService.findAllByDeutschId(chosenWordId)).thenReturn(pairs);
        when(russianController.findById(ru1.getId())).thenReturn(ru1);
        when(russianController.findById(ru2.getId())).thenReturn(ru2);

        Map<Long, String> result = deRuPairsController.getTranslations(telegramId, chosenWord, fromLanguage);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void getAll() {
        DeRuPairs pair1 = new DeRuPairs();
        DeRuPairs pair2 = new DeRuPairs();
        DeRuPairs pair3 = new DeRuPairs();

        List<DeRuPairs> pairs = new ArrayList<>();
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);

        when(deRuPairsService.getAll()).thenReturn(pairs);
        List<DeRuPairs> result = deRuPairsController.getAll();

        assertFalse(result.isEmpty());
        assertEquals(pairs, result);
    }

    @Test
    void getWordMapWhichUserLooksFor() {
        /*Long telegramId=234L;
        String userRUWord="ru";
        String userDEWord="de";
        String postfix=" <<уже добавлено>>";
        Deutsch deutsch1=new Deutsch("de1");
        Deutsch deutsch2=new Deutsch("de2");
        deutsch1.setId(123L);
        deutsch2.setId(234L);
        List<Deutsch> deutschList = new ArrayList<>();
        deutschList.add(deutsch1);
        deutschList.add(deutsch2);
        Russian russian1=new Russian("ru1");
        Russian russian2=new Russian("ru2");
        russian1.setId(345L);
        russian2.setId(456L);
        List<Russian> russianList = new ArrayList<>();
        russianList.add(russian1);
        russianList.add(russian2);
        DeRuPairs pair1 = new DeRuPairs(deutsch1,russian1);
        DeRuPairs pair2 = new DeRuPairs(deutsch2,russian2);
        UserDictionary userDictionary1 = new UserDictionary();
        UserDictionary userDictionary2 = new UserDictionary();
        userDictionary1.setPair(pair1);
        userDictionary2.setPair(pair2);
        List<UserDictionary> userDictionaryList = new ArrayList<>();
        userDictionaryList.add(userDictionary1);
        userDictionaryList.add(userDictionary2);
        Map<Long, String> expectedRu = new HashMap<>();
        Map<Long, String> expectedDe = new HashMap<>();
        StringBuilder b1=new StringBuilder(deutsch1.getDeWord());
        StringBuilder b2=new StringBuilder(deutsch2.getDeWord());
        StringBuilder b3=new StringBuilder(russian1.getRuWord());
        StringBuilder b4=new StringBuilder(russian2.getRuWord());
        b1.append(postfix);
        b2.append(postfix);
        b3.append(postfix);
        b4.append(postfix);
        expectedDe.put(deutsch1.getId(), b1.toString());
        expectedDe.put(deutsch2.getId(), b2.toString());
        expectedRu.put(russian1.getId(), b3.toString());
        expectedRu.put(russian2.getId(), b4.toString());

        when(userDictionaryController.getAllByTelegramId(telegramId)).thenReturn(userDictionaryList);
        when(deRuPairsService.findPairById(pair1.getId())).thenReturn(Optional.of(pair1));
        when(deRuPairsService.findPairById(pair2.getId())).thenReturn(Optional.of(pair2));
        when(deutschController.findAllDeutschWordsWhichContain(userRUWord)).thenReturn(deutschList);
        when(russianController.findAllRussianWordsWhichContain(userDEWord)).thenReturn(russianList);
        System.out.println(expectedDe);
        System.out.println(expectedRu);

        Map<Long, String> resultDe=deRuPairsController.getWordMapWhichUserLooksFor(telegramId, userDEWord, "DE");
        System.out.println(resultDe);
        Map<Long, String> resultRu=deRuPairsController.getWordMapWhichUserLooksFor(telegramId, userRUWord, "RU");
        System.out.println(resultRu);

        assertEquals(expectedDe, resultDe);
        assertEquals(expectedRu, resultRu);*/
        Long telegramId = 234L;
        String userRUWord = "ru";
        String userDEWord = "de";
        String postfix = " <<еще варианты>>";

        Deutsch deutsch1 = new Deutsch("de1");
        Deutsch deutsch2 = new Deutsch("de2");
        deutsch1.setId(123L);
        deutsch2.setId(234L);
        List<Deutsch> deutschList = List.of(deutsch1, deutsch2);

        Russian russian1 = new Russian("ru1");
        Russian russian2 = new Russian("ru2");
        russian1.setId(345L);
        russian2.setId(456L);
        List<Russian> russianList = List.of(russian1, russian2);

        DeRuPairs pair1 = new DeRuPairs(deutsch1, russian1);
        DeRuPairs pair2 = new DeRuPairs(deutsch2, russian2);
        UserDictionary userDictionary1 = new UserDictionary();
        UserDictionary userDictionary2 = new UserDictionary();
        userDictionary1.setPair(pair1);
        userDictionary2.setPair(pair2);
        List<UserDictionary> userDictionaryList = List.of(userDictionary1, userDictionary2);

        Map<Long, String> expectedRu = Map.of(
                russian1.getId(), russian1.getRuWord(),
                russian2.getId(), russian2.getRuWord() + postfix
        );

        Map<Long, String> expectedDe = Map.of(
                deutsch1.getId(), deutsch1.getDeWord(),
                deutsch2.getId(), deutsch2.getDeWord() + postfix
        );

        when(userDictionaryController.getAllByTelegramId(telegramId)).thenReturn(userDictionaryList);
        when(deRuPairsService.findPairById(pair1.getId())).thenReturn(Optional.of(pair1));
        when(deRuPairsService.findPairById(pair2.getId())).thenReturn(Optional.of(pair2));
        when(deutschController.findSmartMatches(userDEWord)).thenReturn(deutschList);
        when(russianController.findSmartMatches(userRUWord)).thenReturn(russianList);

        Map<Long, String> resultDe = deRuPairsController.getWordMapWhichUserLooksFor(telegramId, userDEWord, "DE");
        Map<Long, String> resultRu = deRuPairsController.getWordMapWhichUserLooksFor(telegramId, userRUWord, "RU");

        assertEquals(expectedDe, resultDe);
        assertEquals(expectedRu, resultRu);
    }
}