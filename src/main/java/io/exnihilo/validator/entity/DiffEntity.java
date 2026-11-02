package io.exnihilo.validator.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request and response entity for key-based structural diffs: {@code inputMessage} holds the base
 * document, {@code compareMessage} the document to compare against, and the response lists the
 * detected key differences.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiffEntity {

  private boolean valid;
  private boolean identical;
  private String inputMessage;
  private String compareMessage;
  private String validationMessage;
  private List<String> differences;
}
