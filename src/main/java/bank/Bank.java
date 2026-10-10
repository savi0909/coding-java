/*
Problem Statement
Exhibit A
You have been tasked with writing a program for a popular bank that will automate all its incoming transactions (transfer, deposit, and withdraw). The bank has n accounts numbered from 1 to n. The initial balance of each account is stored in a 0-indexed integer array balance, with the (i + 1)th account having an initial balance of balance[i].

Execute all the valid transactions. A transaction is valid if:
  1. The given account number(s) are between 1 and n, and
  2. The amount of money withdrawn or transferred from is less than or equal to the balance of the account.

Implement the Bank class:
  • Bank(long[] balance) — Initializes the object with the 0-indexed integer array balance.
  • boolean transfer(int account1, int account2, long money) — Transfers money dollars from account1 to account2. Returns true on success, false otherwise.
  • boolean deposit(int account, long money) — Deposits money dollars into the given account. Returns true on success, false otherwise.
  • boolean withdraw(int account, long money) — Withdraws money dollars from the given account. Returns true on success, false otherwise.

Example 1:
Input
["Bank", "withdraw", "transfer", "deposit", "transfer", "withdraw"]
[[[10, 100, 20, 50, 30]], [3, 10], [5, 1, 20], [5, 20], [3, 4, 15], [10, 50]]
Output
[null, true, true, true, false, false]

Explanation:
  Bank bank = new Bank([10, 100, 20, 50, 30]);
  bank.withdraw(3, 10);    // true — account 3 has $20, withdraw $10 → $10
  bank.transfer(5, 1, 20); // true — account 5: $30→$10, account 1: $10→$30
  bank.deposit(5, 20);     // true — account 5: $10→$30
  bank.transfer(3, 4, 15); // false — account 3 only has $10
  bank.withdraw(10, 50);   // false — account 10 does not exist

Constraints:
  • n == balance.length
  • 1 <= n, account, account1, account2 <= 10^5
  • 0 <= balance[i], money <= 10^12
  • At most 10^4 calls will be made to each function transfer, deposit, withdraw.
 */
package bank;


public class Bank {

    Long[] balance;

    public Bank(long[] balance) {
        this.balance = new Long[balance.length];
        for (int i = 0; i < balance.length; i++) {
            this.balance[i] = balance[i];
        }
    }

    //test bank
    public static void main(String[] args) {
        long [] balance = {10, 100, 20, 50, 30};
        Bank bank = new Bank(balance);
        System.out.println(bank.withdraw(3, 10));    // true — account 3 has $20, withdraw $10 → $10
        System.out.println(bank.transfer(5, 1, 20)); // true — account 5: $30→$10, account 1: $10→$30
        System.out.println(bank.deposit(5, 20));     // true — account 5: $10→$30
        System.out.println(bank.transfer(3, 4, 15)); // false — account 3 only has $10
        System.out.println(bank.withdraw(10, 50));
    }

    public boolean transfer(int account1, int account2, long money) {
        if (account1 < 1 || account1 > balance.length || account2 < 1 || account2 > balance.length) {
            return false;
        }
        if (account1 == account2) return false;
        if (balance[account1 - 1] < money) return false;

        Long firstLock = balance[account1-1];
        Long secondLock = balance[account2-1];
        if(account1 < account2){
            firstLock = balance[account1-1];
            secondLock = balance[account2-1];
        }
        else { // reverse order of locks
            firstLock = balance[account2-1];
            secondLock = balance[account1-1];
        }

        synchronized (firstLock) {
            synchronized (secondLock) {
                balance[account1 - 1] -= money;
                balance[account2 - 1] += money;
            }

        }
        return true;
    }

    public boolean deposit(int account, long money) {
        if (account < 1 || account > balance.length) {
           return false;
        }
        synchronized (balance[account - 1]) {
            balance[account - 1] += money;
        }
        return true;
    }

    public boolean withdraw(int account, long money) {
        if (account < 1 || account > balance.length) {
            return false;
        }
        if (balance[account - 1] < money) return false;
        synchronized (balance[account - 1]) {
            balance[account - 1] -= money;
        }
        return true;
    }


}
