package dp.factroy2;

public class LoanFactory {

	public Loan getLoan(String loanType) {
		if (loanType == null) {
			return null;
		}
		if (loanType.equalsIgnoreCase("PERSONAL")) {
			return new PersonalLoan();

		} else if (loanType.equalsIgnoreCase("HOME")) {
			return new HomeLoan();

		} else if (loanType.equalsIgnoreCase("CAR")) {
			return new CarLoan();
		}

		return null;
	}

}
