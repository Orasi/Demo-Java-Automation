package com.orasi.utils.dataHelpers.creditCards;

import com.orasi.AutomationException;
import com.orasi.utils.dataHelpers.personFactory.Address;
import com.orasi.utils.dataHelpers.personFactory.Person;

/**
 * Container class to store credit cards and easily retrieve them
 * The credit cards in this class are considered test cards for PayPal
 * as listed below, but cardnumber info still not plaintext to
 * encourage data masking and safety
 *
 * @author justin.phlegar@orasi.com
 * @see https://www.paypalobjects.com/en_US/vhelp/paypalmanager_help/credit_card_numbers.htm
 *
 */
public class CreditCards {
    private static ThreadLocal<Person> person = new ThreadLocal<>();
    private static ThreadLocal<Address> address = new ThreadLocal<>();

    private static Person getPerson() {
        if (person.get() == null) {
            person.set(new Person());
            address.set(person.get().primaryAddress());
        }

        return person.get();
    }

    /**
     * Allow tester to reuse a pre-defined Person and associate that persons address info to the card
     * Will check for a "Billing" type of address first. If Billing exists, associate that address
     * to card. If Billing does NOT exist, then use the persons primary address
     *
     * This is done for cases where there may be business rules that billing address info entered must
     * match a persons records on file. By using the Person factory, that make this easier to do.
     *
     * @param person
     */
    public CreditCards(Person person) {
        CreditCards.person.set(person);
        boolean billingFound = false;
        for (Address personAddress : person.getAllAddresses()) {
            if ("Billing".equalsIgnoreCase(personAddress.getType())) {
                address.set(personAddress);
                billingFound = true;
            }
        }

        if (!billingFound) {
            address.set(person.getAllAddresses().get(0));
        }
    }

    /*
     * Expected storage pattern is as follows
     * return new CreditCard(
     * cardType,
     * nameOnCard,
     * encodedCardNumber,
     * encodedSecurityCode,
     * expirationMonth,
     * expirationYear,
     * billingAddress,
     * billingAddress2,
     * billingCity,
     * billingState,
     * billingCountry,
     * billingZipCode
     * );
     */

    public final static CreditCard AMEX() {
        return new CreditCard(
                "AMEX",
                getPerson().getFullName(),
                "Mzc4MjgyMjQ2MzEwMDA1",
                "MzI2NQ==",
                "12",
                "20",
                address.get().getAddress1(),
                address.get().getAddress2(),
                address.get().getCity(),
                address.get().getStateAbbv(),
                address.get().getCountryAbbv(),
                address.get().getZipCode());
    }

    public final static CreditCard DINERSCLUB() {
        return new CreditCard(
                "DINERSCLUB",
                getPerson().getFullName(),
                "MzA1NjkzMDkwMjU5MDQ=",
                "NzE4",
                "12",
                "20",
                address.get().getAddress1(),
                address.get().getAddress2(),
                address.get().getCity(),
                address.get().getStateAbbv(),
                address.get().getCountryAbbv(),
                address.get().getZipCode());
    }

    public final static CreditCard DISCOVER() {
        return new CreditCard(
                "DISCOVER",
                getPerson().getFullName(),
                "NjAxMTExMTExMTExMTExNw==",
                "NzE4",
                "12",
                "20",
                address.get().getAddress1(),
                address.get().getAddress2(),
                address.get().getCity(),
                address.get().getStateAbbv(),
                address.get().getCountryAbbv(),
                address.get().getZipCode());
    }

    public final static CreditCard JCB() {
        return new CreditCard(
                "JCB",
                getPerson().getFullName(),
                "MzUzMDExMTMzMzMwMDAwMA==",
                "MDM2",
                "12",
                "20",
                address.get().getAddress1(),
                address.get().getAddress2(),
                address.get().getCity(),
                address.get().getStateAbbv(),
                address.get().getCountryAbbv(),
                address.get().getZipCode());
    }

    public final static CreditCard MASTERCARD() {
        return new CreditCard(
                "MASTERCARD",
                getPerson().getFullName(),
                "NTEwNTEwNTEwNTEwNTEwMA==",
                "MDM2",
                "12",
                "20",
                address.get().getAddress1(),
                address.get().getAddress2(),
                address.get().getCity(),
                address.get().getStateAbbv(),
                address.get().getCountryAbbv(),
                address.get().getZipCode());
    }

    public final static CreditCard VISA() {
        return new CreditCard(
                "VISA",
                getPerson().getFullName(),
                "NDAxMjg4ODg4ODg4MTg4MQ==",
                "OTgw",
                "12",
                "20",
                address.get().getAddress1(),
                address.get().getAddress2(),
                address.get().getCity(),
                address.get().getStateAbbv(),
                address.get().getCountryAbbv(),
                address.get().getZipCode());
    }

    public final static CreditCard VISA_EXPIRED() {
        return new CreditCard(
                "VISA_EXPIRED",
                getPerson().getFullName(),
                "NDAxMjg4ODg4ODg4MTg4MQ==",
                "OTgw",
                "12",
                "12",
                address.get().getAddress1(),
                address.get().getAddress2(),
                address.get().getCity(),
                address.get().getStateAbbv(),
                address.get().getCountryAbbv(),
                address.get().getZipCode());
    }

    /**
     * This method allows you to enter the type of card you want by string
     *
     * @param type
     * @return
     */
    public static CreditCard getCreditCardByType(String type) {
    	return getCreditCardByType(CardTypes.fromString(type));
    }
    
    /**
     * This method allows you to enter the type of card you want by enum
     * 
     * @param type
     * @return
     */
    public static CreditCard getCreditCardByType(CardTypes type) {
    	switch (type) {
    		case AMEX: return AMEX();
    		case DINERS_CLUB: return DINERSCLUB();
    		case DISCOVER: return DISCOVER();
    		case JCB: return JCB();
    		case MC: return MASTERCARD();
    		case VISA: return VISA();
    		case VISAEXPIRED: return VISA_EXPIRED();
    	}
    	
    	// Should not be at this point unless card was not found above
        throw new AutomationException("Credit Card type of [ " + type + " ] was not valid or availible");
    }
}
