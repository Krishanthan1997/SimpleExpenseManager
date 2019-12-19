package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;



public class PersistentMemoryAccountDAO extends SQLiteOpenHelper implements AccountDAO {
    
    public static final String database_name = "170313D.db";
    public static final String col_1 = "accountno";
    public static final String col_2 = "bankname";
    public static final String col_3 = "holdername";
    public static final String col_4 = "balance";
    
    public PersistentMemoryAccountDAO(Context context) {
        super(context, database_name , null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table account"+"(accountno TEXT(50) PRIMARY KEY,bankname TEXT(50),holdername TEXT(50),balance Double)"
        );

        db.execSQL(
                "create table transaction" +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT,accountno TEXT(50), type TEXT(20), date BLOB , amount Double,FOREIGN KEY (accountno) REFERENCES +"account"+(accountno))"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS account");
        onCreate(db);
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> accountNumber = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account"+, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            accountNumber.add(res.getString(res.getColumnIndex(col_1)));
            res.moveToNext();
        }
        return accountNumber;
    }



    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> accountlist = new ArrayList<Account>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String accountNo = res.getString(res.getColumnIndex(col_1));
            String bankName = res.getString(res.getColumnIndex(col_2));
            String accountHolderName = res.getString(res.getColumnIndex(col_3);
            Double balance = res.getDouble(res.getColumnIndex(col_4));

            accountlist.add(new Account(accountNo,bankName,accountHolderName,balance));
            res.moveToNext();
        }
        return accountlist;
    }



    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account where accountno="+accountNo+"", null );

        String accountno = res.getString(res.getColumnIndex(col_1));
        String bankName = res.getString(res.getColumnIndex(col_2));
        String accountHolderName = res.getString(res.getColumnIndex(col_3));
        Double balance = res.getDouble(res.getColumnIndex(col_4));

        return  new Account(accountno,bankName,accountHolderName,balance);
    }




    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountno",account.getAccountNo());
        contentValues.put("bankname",account.getBankName());
        contentValues.put("holdername",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        long result = db.insert("account",null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }




    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("account","accountno = "+accountNo,null) > 0;
    }




    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(accountNo ==null){
            throw new InvalidAccountException("Invalid Account Number");

        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM account WHERE accountno = ?",new String[]{accountNo});
        Account account = null;
        String accountNo = res.getString(res.getColumnIndex(col_1));
        String bankName = res.getString(res.getColumnIndex(col_2));
        String accountHolderName = res.getString(res.getColumnIndex(col_3));
        double balance = res.getDouble(res.getColumnIndex(col_4));
        account = new Account(accountNo,bankName,accountHolderName,balance);
        double balance = account.getBalance();
        if(expenseType == ExpenseType.INCOME){
            account.setBalance(balance+amount);
        }else if (expenseType == ExpenseType.EXPENSE){
            account.setBalance(balance-amount);

        }
        if(account.getBalance()<0 ){
            throw new InvalidAccountException("Insufficient credit");
        }

        else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("accountno",account.getAccountNo());
            contentValues.put("bankname",account.getBankName());
            contentValues.put("holdername",account.getAccountHolderName());
            contentValues.put("balance",account.getBalance());
            long result = db.update("account",contentValues,"accountno = ?",new String[]{account.getAccountNo()});
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }

    }
}
