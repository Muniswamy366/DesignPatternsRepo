package dp.factroy2;

public class FactoryPatternDemo2 {

	public static void main(String[] args) {
		
		LoanFactory loanFactory = new LoanFactory();

		Loan personalLoan = loanFactory.getLoan("PERSONAL");
		personalLoan.interest();

		Loan homeLoan = loanFactory.getLoan("HOME");
		homeLoan.interest();
		
		Loan carLoan = loanFactory.getLoan("CAR");
		carLoan.interest();
		
	}
}
