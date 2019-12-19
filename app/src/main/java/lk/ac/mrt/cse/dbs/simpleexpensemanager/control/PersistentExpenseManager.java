package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;

    public PersistentExpenseManager(Context context) {
        this.context=context;
        try{
            setup();
        }catch (ExpenseManagerException e){
            e.printStackTrace();
        }
    }

    public void setup() throws ExpenseManagerException {

        TransactionDAO persistentTransactionDAO = new PersistentMemoryTransactionDAO(context);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentMemoryAccountDAO(context);
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        // Account dummyAcct1 = new Account("12345Azzzz", "Yoda Bank", "Anakin Skywalker", 10000.0);
        // Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        // getAccountsDAO().addAccount(dummyAcct1);
        // getAccountsDAO().addAccount(dummyAcct2);

    }



}