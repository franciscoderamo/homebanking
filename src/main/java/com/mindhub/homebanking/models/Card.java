package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
public class Card {
    //Properties or attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private short cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private boolean isActive;
    private boolean isExpired;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    // Constructors
    public Card() {
    }
    public Card(String cardHolder, CardType type, CardColor color, String number, short cvv, LocalDate fromDate, LocalDate thruDate) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.isActive = true; //By default, the transaction is considered active
        this.isExpired = false;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public short getCvv() {
        return cvv;
    }

    public void setCvv(short cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }
    public boolean getIsActive() {
        return isActive;
    }
    public void setIsActivate() {
        this.isActive = false;
    }
    public boolean getIsExpired() {
//        LocalDate currentDate = LocalDate.now();
//        return thruDate.isBefore(currentDate);
        return isExpired;
    }
    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
