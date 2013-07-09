package ru.hh.spellcorrector.morpher;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.hh.spellcorrector.CorrektorService;
import ru.hh.spellcorrector.dict.StreamDictionary;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.testng.Assert.assertEquals;
import static ru.hh.spellcorrector.morpher.Morphers.cutDoubleSteps;

public class CorrektorTest {

  private static final String TEST_DICT =
      "дизъюнкция|2.3\n" +
      "рыбная|14.24\n" +
      "ловля|7.2\n" +
      "двигатель|68.4\n" +
      "внутреннего|172.16\n" +
      "сгорания|4.48\n" +
      "охота|4.48\n" +
      "подъезд|8.32\n" +
      "менеджер|11.8\n" +
      "эхолот|5.32\n" +
      "юбилей|1.13\n" +
      "sales|12.4\n" +
      "marketing|18.2\n" +
      "test|10000.0\n";

  @BeforeClass
  public static void initDictionary() throws IOException {
    StreamDictionary.load(new ByteArrayInputStream(TEST_DICT.getBytes("utf-8")));
  }

  private CorrektorService corrector;

  @BeforeMethod
  public void intiCorrector() {
    corrector = new CorrektorService(cutDoubleSteps(), StreamDictionary.getInstance());
  }

  @Test
  public void testIdentity() {
    assertEquals(corrector.correct("дизъюнкция".toLowerCase()).toCorrection(), "дизъюнкция");
  }

  @Test
  public void testDelete() {
    assertCorrection("Ъдизъюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("дизъЪюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("дизъюнкцияЪ".toLowerCase(), "дизъюнкция");
    assertCorrection("дизъЯюнкцияУ".toLowerCase(), "дизъюнкция");
  }

  @Test
  public void testTranspose() {
    assertCorrection("ИДзъюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("дизъюнкцИЯ".toLowerCase(), "дизъюнкция");
    assertCorrection("дизЮЪнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("ИДзъюКНция".toLowerCase(), "дизъюнкция");
  }

  @Test
  public void testReplace() {
    assertCorrection("Иизъюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("Цизъюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("дизЦюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("дизъюнкциЦ".toLowerCase(), "дизъюнкция");
  }

  @Test
  public void testInsert() {
    assertCorrection("Ддизъюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("Цдизъюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("дизъЦюнкция".toLowerCase(), "дизъюнкция");
    assertCorrection("дизъюнкцияЦ".toLowerCase(), "дизъюнкция");
  }

  @Test
  public void testSplit() {
    assertCorrection("рыбнаяловля", "рыбная ловля");
    assertCorrection("двигательвнутреннегосгорания", "двигатель внутреннего сгорания");
    assertCorrection("salesменеджер", "sales менеджер");
    assertCorrection("salesmarketing", "sales marketing");
    assertCorrection("test,sales,markting", "test,sales,marketing");
    assertCorrection("testt,;;sales,markting", "test,;;sales,marketing");
  }

  @Test
  public void testCharset() {
    assertCorrection("рыбнаяkjdkz", "рыбная ловля");
    assertCorrection("lbp].yrwbz", "дизъюнкция");
    assertCorrection("j[jnf", "охота");
    assertCorrection("gjl]tpl", "подъезд");
    assertCorrection("vtytl;th", "менеджер");
    assertCorrection("'[jkjn", "эхолот");
    assertCorrection(".,bktq", "юбилей");
  }

  public void assertCorrection(String source, String correction) {
    assertEquals(corrector.correct(source).toCorrection(), correction);
  }

}

