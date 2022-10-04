package it.develhope.controllerprotection.salary.entities;

public class CreateSalaryDTO {

    private Long amount;

    public CreateSalaryDTO() {
    }

    public CreateSalaryDTO(Long amount) {
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
