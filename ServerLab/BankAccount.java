public class BankAccount
{
 private double accountBalance;

 public BankAccount() { this.accountBalance = 0.00; }





public  void makeDeposit(double amount)
 {
  double holdAccountBalance;

  holdAccountBalance = accountBalance;
  if( amount <= 0.00 ) { throw new IllegalArgumentException("Tranasction amount must be greater than zero: " + amount); }

  try { Thread.sleep( (int) (Math.random()*100) ); } catch(Exception e) {}

  this.accountBalance = holdAccountBalance + amount;
  System.out.println("Depost " + amount + ". Account balance is now " + this.accountBalance);
 }


}
