package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

public class PayRequestDto {
	private int loanId;
	private double inputAmount;

	public PayRequestDto() {
	}

	public PayRequestDto(int aLoanId, double aInputAmount) {
		this.loanId = aLoanId;
		this.inputAmount = aInputAmount;
	}

	public int getLoanId() {
		return this.loanId;
	}

	public double getInputAmount() {
		return this.inputAmount;
	}
}
