
package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class PersistentMemoryTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    public static final String DATABASE_NAME = "170313D.db";
    public static final String col_1 = "id";
    public static final String col_2 = "accountno";
    public static final String col_3= "type";
    public static final String col_4 ="date" ;
    public static final String col_5 = "amount";

    public PersistentMemoryTransactionDAO(Context context) {
        super(context, database_name , null,1);
        transactions = new LinkedList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS transaction");
        onCreate(db);
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        Transaction trans = new Transaction(date,accountNo,expenseType,amount);

        DateFormat format = new SimpleDateFormat("m-d-yyyy", Locale.ENGLISH);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountno",trans.getAccountNo());
        contentValues.put("type",trans.getExpenseType().toString());
        contentValues.put("date",format.format(trans.getDate()));
        contentValues.put("amount",trans.getAmount());

        long res = db.insert("transaction",null,contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }


    }




    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM transaction",null);

        ArrayList<Transaction> transactionList=new ArrayList<>();
        DateFormat format = new SimpleDateFormat("m-d-yyyy", Locale.ENGLISH);
        if(res.getCount()==0){
            return transactionList;
        }else{

            while(res.moveToNext()){
                String accountNo = res.getString(res.getColumnIndex(col_2));
                Date date = new Date();
                ExpenseType expenseType = ExpenseType.valueOf(res.getString(res.getColumnIndex(col_3)));
                try {
                    date =  format.parse(res.getString(res.getColumnIndex(col_4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//

                double amount = res.getDouble(res.getColumnIndex(col_5));
                transactionList.add(new Transaction(date,accountNo,expenseType,amount));
            }
            return transactionList;
        }

    }



    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM transaction LIMIT "+limit,null);

        ArrayList<Transaction> transactionList=new ArrayList<>();
        DateFormat format = new SimpleDateFormat("m-d-yyyy", Locale.ENGLISH);
        if(res.getCount()==0){
            return transactionList;
        }else{

            while(res.moveToNext()){
                String accountNo = res.getString(res.getColumnIndex(col_2));
                Date date = new Date();
                ExpenseType expenseType = ExpenseType.valueOf(res.getString(res.getColumnIndex(col_3)));
                try {
                    date =  format.parse(res.getString(res.getColumnIndex(col_4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//

                double amount = res.getDouble(res.getColumnIndex(col_5));
                transactionList.add(new Transaction(date,accountNo,expenseType,amount));
            }
            return transactionList;
        }
    }

}
