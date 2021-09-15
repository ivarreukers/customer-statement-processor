package nl.rabobank.ivarreukers.customerstatementprocessor.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
  @NotNull
  private long reference;
  @NotBlank
  @Pattern(regexp = "[a-zA-Z0-9 ]{3,}")
  private String accountNumber;

  // we don't want these properties in the output so mark them WRITE_ONLY for jackson
  @NotNull
  @JsonProperty(access = Access.WRITE_ONLY)
  private BigDecimal startBalance;

  @NotNull
  @JsonProperty(access = Access.WRITE_ONLY)
  private BigDecimal mutation;

  @JsonProperty(access = Access.WRITE_ONLY)
  private String description;

  @NotNull
  @JsonProperty(access = Access.WRITE_ONLY)
  private BigDecimal endBalance;
}
