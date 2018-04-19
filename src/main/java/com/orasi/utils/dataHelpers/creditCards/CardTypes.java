package com.orasi.utils.dataHelpers.creditCards;

import com.orasi.AutomationException;

public enum CardTypes {

    DINERS_CLUB("dc", "dinersclub", "diners_club", "diners"), JCB("jcb"), AMEX("amex", "ae", "americanexpress", "american_express"), DISCOVER("disc", "dc", "discover"), MC("mc", "mastercard", "master_card"), VISA("visa"), VISAEXPIRED("visaexpired", "visaexp");

    private final String[] cardType;

    CardTypes(String... type) {
        cardType = type;
    }

    public String[] getCardType() {
        return cardType;
    }

    public static CardTypes fromString(String type) {
        for (CardTypes cardType : values()) {
            if (cardType.toString().equalsIgnoreCase(type.replace("_", "").replace(" ", ""))) {
                return cardType;
            }
        }

        for (CardTypes card : CardTypes.values()) {
            for (String cardType : card.getCardType()) {
                if (cardType.equalsIgnoreCase(type.replace("_", "").replace(" ", ""))) {
                    return card;
                }
            }
        }

        throw new AutomationException("No card type defined found for requested value [ " + type + " ]");
    }
}
