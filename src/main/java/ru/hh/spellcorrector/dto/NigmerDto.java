package ru.hh.spellcorrector.dto;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "nigmer")
public class NigmerDto {

  @XmlElement
  public List<WordDto> word = Lists.newArrayList();

  public String toCorrection() {
    StringBuilder builder = new StringBuilder();
    for (WordDto w: word) {
      if (w.text != null) {
        builder.append(w.text);
      }
      if (w.correction != null) {
        builder.append(w.correction.variant);
      }
    }
    return builder.toString();
  }

}
