package one.digitalinnovation.beerstock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BeerType {

    LAGER("lager"),
    MALZBIER("Malzbier"),
    WITBIER("Witbier"),
    WEIS("Weis"),
    ALE("Ale"),
    IPA("Ipa"),
    STOUT("Stout");

    private final String description;
}
