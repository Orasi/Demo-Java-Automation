package com.orasi.utils.dataHelpers.personFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.orasi.utils.Randomness;
import com.orasi.utils.dataHelpers.creditCards.CreditCard;
import com.orasi.utils.dataHelpers.creditCards.CreditCards;
import com.orasi.utils.dataHelpers.personFactory.seeds.FemaleFirstNames;
import com.orasi.utils.dataHelpers.personFactory.seeds.LastNames;
import com.orasi.utils.dataHelpers.personFactory.seeds.MaleFirstNames;
import com.orasi.utils.date.DateTimeConversion;

public class Person {
    Random random = new Random();
    private ArrayList<Address> addresses = new ArrayList<Address>();
    private ArrayList<Phone> phones = new ArrayList<Phone>();
    private ArrayList<Email> emails = new ArrayList<Email>();
    private ArrayList<CreditCard> creditcards = new ArrayList<CreditCard>();
    private boolean isPrimary = false;
    private String title = "";
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String suffix = "";
    private String nickname = "";
    private String birthDate = "";
    private String age = "";
    private String ssn = "";
    private String username = "";
    private String password = "";
    private boolean deceased = false;

    /**
     * Upon instantiation, generate name guest data, along with
     * address, phone, and email data
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     */
    public Person() {
        populateSeededData();
        if (Integer.parseInt(getAge()) <= 18) {
            setAge("45");
            setBirthDate("1970-01-14");
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.substring(0, 1).toUpperCase()
                + firstName.substring(1).toLowerCase();
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.substring(0, 1).toUpperCase()
                + lastName.substring(1).toLowerCase();
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAge() {
        if (Integer.parseInt(age) < 0)
            return "0";
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean getDeceased() {
        return deceased;
    }

    public void setDeceased(boolean deceased) {
        this.deceased = deceased;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isChild() {
        return (Integer.parseInt(getAge()) <= 18);
    }

    /**
     * Return all addresses associated to the Guest
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     * @return all addresses as an ArrayList
     */
    public ArrayList<Address> getAllAddresses() {
        return addresses;
    }

    /**
     * Associate a new Address to the guest using random data. Will be
     * marked not a primary
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     */
    public void addAddress() {
        addresses.add(new Address());
    }

    /**
     * Associate a new Address to the guest using preset data
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     */
    public void addAddress(Address address) {
        addresses.add(address);
    }

    /**
     * Return all phone numbers associated to the Guest
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     * @return all phones as an ArrayList
     */
    public ArrayList<Phone> getAllPhones() {
        return phones;
    }

    /**
     * Associate a new Phone to the guest using random data. Will be
     * marked not a primary
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     */
    public void addPhone() {
        phones.add(new Phone());
    }

    /**
     * Associate a new Phone to the guest using preset data
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     */
    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    /**
     * Return all email addresses associated to the Guest
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     * @return all email addresses as an ArrayList
     */
    public ArrayList<Email> getAllEmails() {
        return emails;
    }

    /**
     * Associate a new Email to the guest using random data. Will be
     * marked not a primary
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     */
    public void addEmail() {
        emails.add(new Email());
    }

    /**
     * Associate a new Email to the guest using preset data
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     */
    public void addEmail(Email email) {
        emails.add(email);
    }
    
    /**
     * Return all credit cards associated to the Guest
     * 
     * @author John Martin
     * @version 01/11/2018 John Martin - original
     * @return all credit cards as an ArrayList
     */
    public ArrayList<CreditCard> getAllCreditCards() {
    	return creditcards;
    }
    
    /**
     * Associate a new Credit Card to the guest using preset Visa type
     * 
     * @author John Martin
     * @version 01/11/2018 John Martin - original
     */
    @SuppressWarnings("static-access")
	public void addCreditCard( ) {
    	CreditCards cards = new CreditCards(this);
    	creditcards.add(cards.VISA());
    }
    
    /**
     * Associate a new Credit Card to the guest using preset data
     * 
     * @author John Martin
     * @version 01/11/2018 John Martin - original
     */
    public void addCreditCard(CreditCard creditcard) {
    	creditcards.add(creditcard);
    }

    
    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    /**
     * Return the address marked as Primary
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     * @return the Guest's primary Address
     */
    public Address primaryAddress() {
        Address primaryAddress = null;

        for (Address address : addresses) {
            if (address.isPrimary()) {
                primaryAddress = address;
            }
        }

        return primaryAddress;
    }

    /**
     * Return the phone number marked as Primary
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     * @return the Guest's primary Phone Number
     */
    public Phone primaryPhone() {
        Phone primaryPhone = null;

        for (Phone phone : phones) {
            if (phone.isPrimary()) {
                primaryPhone = phone;
            }
        }

        return primaryPhone;
    }

    /**
     * Return the Email Address marked as Primary
     *
     * @author Justin Phlegar
     * @version 11/28/2015 Justin Phlegar - original
     * @return the Guest's primary Email Address
     */
    public Email primaryEmail() {
        Email primaryEmail = null;

        for (Email email : emails) {
            if (email.isPrimary()) {
                primaryEmail = email;
            }
        }

        return primaryEmail;
    }
    
    /**
     * Return the Credit Card marked as Primary
     * 
     * @author John Martin
     * @version 01/11/2018 John Martin - original
     * @return the Guest's primary Credit Card
     */
    public CreditCard primaryCreditCard() {
    	CreditCard primaryCreditCard = null;
    	
    	for (CreditCard card : creditcards) {
    		if (card.isPrimary()) {
    			primaryCreditCard = card;
    		}
    	}
    	
    	return primaryCreditCard;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = "<br/>";

        result.append(" Name: " + title + " " + firstName + " " + lastName
                + NEW_LINE);
        result.append(" Age: " + age + NEW_LINE);
        result.append(" Primary Street Address: "
                + primaryAddress().getAddress1() + NEW_LINE);
        result.append(" Priamry Local Info: " + primaryAddress().getCity()
                + ", " + primaryAddress().getStateAbbv() + " "
                + primaryAddress().getZipCode() + NEW_LINE);
        result.append(" Primary Phone: " + primaryPhone().getNumber()
                + NEW_LINE);
        if (emails.size() > 0) {
            result.append(" Primary Email: " + primaryEmail().getEmail() + NEW_LINE);
        }

        return result.toString();
    }

    protected void populateSeededData() {

        boolean isMale = (Math.random() < .5);

        if (isMale) {
            this.title = "Mr.";
            setFirstName(MaleFirstNames.getFirstName());
        } else {
            this.title = "Mrs.";
            setFirstName(FemaleFirstNames.getFirstName());
        }

        this.middleName = "Automation";
        setLastName(LastNames.getLastName());

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.US);
        Date date = Randomness.randomDate();
        String dateOfBirth = format.format(date);

        Calendar dob = Calendar.getInstance();
        dob.setTime(date);
        Calendar today = Calendar.getInstance();
        int convertedAge = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR)) {
            convertedAge--;
        }

        if (convertedAge < 0) {
            convertedAge = 0;
        }
        this.age = String.valueOf(convertedAge);
        this.birthDate = DateTimeConversion.convert(dateOfBirth,
                "yyyy-MM-dd'T'hh:mm:ss'Z'", "yyyy-MM-dd");
        this.username = getFirstName() + "." + getLastName();
        this.password = "Blah123!";
        this.ssn = Randomness.randomNumber(3) + "-"
                + Randomness.randomNumber(2) + "-" + Randomness.randomNumber(4);
        addAddress(new Address());
        addPhone(new Phone());
        addEmail(new Email());
        addCreditCard();
        addresses.get(0).setPrimary(true);
        phones.get(0).setPrimary(true);
        emails.get(0).setPrimary(true);
        emails.get(0).setEmail(
                getFirstName() + "." + getLastName() + "@AutomatedTesting.com");
        creditcards.get(0).setPrimary(true);
    }

}
