package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.DeRuPairsService;
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
        Long ru1Id=4L;
        Long ru2Id=5L;
        Russian russian1 = new Russian("ru1");
        Russian russian2 = new Russian("ru2");
        russian1.setId(ru1Id);
        russian2.setId(ru2Id);
        DeRuPairs pair1 = new DeRuPairs(deutsch, russian1);
        DeRuPairs pair2 = new DeRuPairs(deutsch, russian2);
        String expected = "Вы добавили перевод слова: "+deutsch.getDeWord()+
                "\nНа русском это будет:\n\t"+russian1.getRuWord()+"\n\t"+russian2.getRuWord();

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
        String isItRu="рофывждфлдв";

        boolean yes= deRuPairsController.isItRussian(isItRu);

        assertTrue(yes);
    }

    @Test
    void testIsItRussianNo() {
        String isItRu="üapsifanß";

        boolean no= deRuPairsController.isItRussian(isItRu);

        assertFalse(no);
    }

    @Test
    void testIsItGermanYes() {
        String isItDe="üapsifanß";

        boolean yes= deRuPairsController.isItGerman(isItDe);

        assertTrue(yes);
    }

    @Test
    void testIsItGermanNo() {
        String isItDe="рофывждфлдв";

        boolean no= deRuPairsController.isItGerman(isItDe);

        assertFalse(no);
    }

    @Test
    void TestGetRUWordsWhichUserLooksFor() {
        Long telegramId=123L;
        String identifier="RU";
        String looksFor="дом";
        String dom="дом";
        String domashniy="домашний";
        String upravdom="управдом";

        Deutsch de=new Deutsch("was???");
        de.setId(999L);
        Russian ru1=new Russian(dom);
        Russian ru2=new Russian(domashniy);
        Russian ru3=new Russian(upravdom);
        ru1.setId(555L);
        ru2.setId(666L);
        ru3.setId(777L);
        DeRuPairs pair1=new DeRuPairs(de, ru1);
        DeRuPairs pair2=new DeRuPairs(de, ru3);
        Long p1Id=789L;
        Long p2Id=890L;
        pair1.setId(p1Id);
        pair2.setId(p2Id);
        List<UserDictionary> dictionaryList=new ArrayList<>();
        UserDictionary ud1=new UserDictionary(new UserLanguage(),pair1,new Date());
        UserDictionary ud2=new UserDictionary(new UserLanguage(), pair2, new Date());
        dictionaryList.add(ud1);
        dictionaryList.add(ud2);
        List<Russian> found=new ArrayList<>();
        List<String> foundedWords=new ArrayList<>();
        found.add(ru1);
        found.add(ru2);
        found.add(ru3);
        foundedWords.add(dom+ " <<еще варианты>>");
        foundedWords.add(domashniy);
        foundedWords.add(upravdom+ " <<еще варианты>>");

        when(userDictionaryController.getAllByTelegramId(telegramId)).thenReturn(dictionaryList);
        when(deRuPairsService.findPairById(p1Id)).thenReturn(Optional.of(pair1));
        when(deRuPairsService.findPairById(p2Id)).thenReturn(Optional.of(pair2));
        when(russianController.findAllRussianWordsWhichContain(looksFor)).thenReturn(found);
        List<String> result=deRuPairsController.getWordsWhichUserLooksFor(telegramId, looksFor, identifier);

        assertEquals(3, result.size());
        assertEquals(foundedWords, result);
        assertTrue(result.contains(dom+ " <<еще варианты>>"));
        assertTrue(result.contains(domashniy));
        assertTrue(result.contains(upravdom+ " <<еще варианты>>"));
    }

    @Test
    void TestGetDEWordsWhichUserLooksFor() {
        Long telegramId=123L;

        String identifier="DE";
        String looksFor="unter";
        String verb="unternehmen";
        String nomen="das Unternehmen";
        String unterstutzung="die Unterstützung";

        List<Deutsch> found=new ArrayList<>();
        List<String> foundedWords=new ArrayList<>();
        found.add(new Deutsch(verb));
        found.add(new Deutsch(nomen));
        found.add(new Deutsch(unterstutzung));
        foundedWords.add(verb);
        foundedWords.add(nomen);
        foundedWords.add(unterstutzung);

        when(deutschController.findAllDeutschWordsWhichContain(looksFor)).thenReturn(found);
        List<String> result=deRuPairsController.getWordsWhichUserLooksFor(telegramId, looksFor, identifier);

        assertNotNull(result);
        assertEquals(foundedWords, result);
        assertTrue(result.contains(verb));
        assertTrue(result.contains(nomen));
        assertTrue(result.contains(unterstutzung));
    }

    @Test
    void testGetDETranslations() {
        Long telegramId=123L;
        String chosenWord="дом";
        String identifier="RU";
        String haus="das Haus";
        String gebaude="das Gebaude";

        Russian chosenRu= new Russian(chosenWord);
        chosenRu.setId(111L);

        Deutsch de1=new Deutsch(haus);
        Deutsch de2=new Deutsch(gebaude);
        de1.setId(555L);
        de2.setId(666L);

        List<DeRuPairs> pairs=new ArrayList<>();
        pairs.add(new DeRuPairs(de1, chosenRu));
        pairs.add(new DeRuPairs(de2, chosenRu));

        List<String> translationList=new ArrayList<>();
        translationList.add(haus);
        translationList.add(gebaude);

        when(russianController.findByWord(chosenWord)).thenReturn(Optional.of(chosenRu));
        when(deRuPairsService.findAllByRussianId(chosenRu.getId())).thenReturn(pairs);
        when(deutschController.findById(555L)).thenReturn(de1);
        when(deutschController.findById(666L)).thenReturn(de2);
        List<String> result=deRuPairsController.getTranslations(telegramId, chosenWord, identifier);

        assertNotNull(result);
        assertEquals(translationList, result);
        assertTrue(result.contains(haus));
        assertTrue(result.contains(gebaude));
    }

    @Test
    void testGetRUTranslations() {
        Long telegramId=234L;
        String chosenWord="das Haus";
        String identifier="DE";
        String dom="дом";
        String zdanie="здание";

        Deutsch chosenDe= new Deutsch(chosenWord);
        chosenDe.setId(222L);

        Russian ru1=new Russian(dom);
        Russian ru2=new Russian(zdanie);
        ru1.setId(888L);
        ru2.setId(999L);

        List<DeRuPairs> pairs=new ArrayList<>();
        pairs.add(new DeRuPairs(chosenDe, ru1));
        pairs.add(new DeRuPairs(chosenDe, ru2));

        List<String> translationList=new ArrayList<>();
        translationList.add(dom);
        translationList.add(zdanie);

        when(deutschController.findByWord(chosenWord)).thenReturn(Optional.of(chosenDe));
        when(deRuPairsService.findAllByDeutschId(chosenDe.getId())).thenReturn(pairs);
        when(russianController.findById(888L)).thenReturn(ru1);
        when(russianController.findById(999L)).thenReturn(ru2);
        List<String> result=deRuPairsController.getTranslations(telegramId, chosenWord, identifier);

        assertNotNull(result);
        assertEquals(translationList, result);
        assertTrue(result.contains(dom));
        assertTrue(result.contains(zdanie));
    }

    @Test
    void testGetPairByGermanIdAndRussianId() {
        Long ruId=1111L;
        Long deId=3333L;
        DeRuPairs pair=new DeRuPairs(new Deutsch("deutsch"), new Russian("russian"));

        when(deRuPairsService.findByGermanIdAndRussianId(deId,ruId)).thenReturn(Optional.of(pair));
        Optional<DeRuPairs> result=deRuPairsController.getPairByGermanIdAndRussianId(deId, ruId);

        result.ifPresent(value->assertEquals(pair, value));
    }

    @Test
    void getDeRuById() {
        Long id=123456L;
        DeRuPairs pair=new DeRuPairs(new Deutsch("de"), new Russian("ru"));

        when(deRuPairsService.findPairById(id)).thenReturn(Optional.of(pair));
        Optional<DeRuPairs> result=deRuPairsController.getDeRuById(id);

        result.ifPresent(value-> assertEquals(pair, value));
    }
}