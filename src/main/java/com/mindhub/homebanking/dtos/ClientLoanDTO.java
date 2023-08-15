package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.ClientLoan;
public class ClientLoanDTO {
    //Properties or attributes
    private Long id;
    private Long loanId;
    private String name;
    private double amount;
    private int payments;
    public ClientLoanDTO(){
    }
    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }
    // Getters
    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }
}
