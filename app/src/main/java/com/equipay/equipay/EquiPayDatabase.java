package com.equipay.equipay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class EquiPayDatabase extends SQLiteOpenHelper {

    TransactionTable transactionTable = new TransactionTable();
    OccasionTable occasionTable = new OccasionTable();
    // Occasion Table Variables
    String occasionId = occasionTable.getOccasionID();
    String occasionName = occasionTable.getOccasionName();
    String dateOfOccasion = occasionTable.getDateOfOccasion();
    String members = occasionTable.getMembers();
    String occasionTableTableName = occasionTable.getTableName();

    // Transaction Table Variables
    String transactionTableTableName = transactionTable.getTableName();
    String transactionEvent = transactionTable.getTransactionEvent();
    String transactionId = transactionTable.getTransactionId();
    String payee = transactionTable.getPayee();
    String amount = transactionTable.getAmount();
    String behalfOf = transactionTable.getBehalfOf();
    String dateOfTransaction = transactionTable.getDateOfTransaction();
    String occasionIdFK = transactionTable.getOccasionIdFK();

    public EquiPayDatabase(Context context){
        super(context,new OccasionTable().getTableName(),null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTransactionTable = createTransactionTableQuery();
        String createOccasionTable = createOccasionTableQuery();
        db.execSQL(createOccasionTable);
        db.execSQL(createTransactionTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS "+new OccasionTable().getTableName());
        db.execSQL("DROP IF TABLE EXISTS "+new TransactionTable().getTableName());
        onCreate(db);
    }

    public boolean createOccasion(Occasion occasion){
        ArrayList<String> membersList = occasion.getMembers();
        String memberString = "";
        for (String memberName:membersList){
            memberString+=memberName;
            memberString+=",";
        }
        memberString = memberString.substring(0,memberString.length()-1);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(occasionName, occasion.getOccasionName());
        contentValues.put(members, memberString);
        contentValues.put(dateOfOccasion,occasion.getCreatedOnDate());
        long result = db.insert(occasionTableTableName,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public String createTransactionTableQuery(){

        String createTransactionTableQuery =
                "CREATE TABLE "+transactionTableTableName +"("+
                        transactionId+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                        transactionEvent+" TEXT NOT NULL," +
                        occasionIdFK+" INTEGER NOT NULL, "+
                        payee+" TEXT NOT NULL," +
                        amount+" REAL NOT NULL," +
                        behalfOf+" TEXT NOT NULL," +
                        dateOfTransaction+" TEXT NOT NULL," +
                        "FOREIGN KEY ("+occasionIdFK+") REFERENCES "+occasionTableTableName+"("+occasionId+"))";
        return createTransactionTableQuery;
    }

    public String createOccasionTableQuery(){

        String createOccasionTableQuery =
                "CREATE TABLE "+occasionTableTableName +"("+
                        occasionId+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                        occasionName+" TEXT NOT NULL," +
                        members+" TEXT NOT NULL," +
                        dateOfOccasion+" TEXT NOT NULL)";
        return createOccasionTableQuery;
    }
    public Cursor getOccasionsList(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+occasionTableTableName;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
    public void deleteOccasion(Occasion occasion){
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(occasionTableTableName, "OccasionId=?",new String[]{occasion.getOccasionId()});
        db.close();
    }
    public boolean createNewTransaction(Transaction transaction){
        ArrayList<String> membersList = transaction.getOnBehalfOf();
        String memberString = "";
        for (String memberName:membersList){
            memberString+=memberName;
            memberString+=",";
        }
        memberString = memberString.substring(0,memberString.length()-1);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(transactionEvent,transaction.getEvent());
        contentValues.put(occasionId, transaction.getOccasionId());
        contentValues.put(payee, transaction.getPayee());
        contentValues.put(amount, transaction.getAmount());
        contentValues.put(behalfOf, memberString);
        contentValues.put(dateOfTransaction,transaction.getDateOfTransaction());

        long result = db.insert(transactionTableTableName,null,contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor getTransactionsByOccasionId(String occasionIdValue){
        Integer occId = Integer.parseInt(occasionIdValue);
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+transactionTableTableName +" where OccasionId = "+occId;
        System.out.println("QUERY: "+query);
        Cursor data = db.rawQuery(query,null);
        if(data.getCount()==0){
            System.out.println("NO RECORDS FOUND");
        }
        return data;
    }
    public void deleteTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(transactionTableTableName, "TransactionId=?",new String[]{transaction.getTransactionId()});
        db.close();
    }

    public void updateMembersForOccasion(Occasion occasion){
        System.out.println("Occasion: "+occasion);
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> membersList = occasion.getMembers();
        String memberString = "";
        for (String memberName:membersList){
            memberString+=memberName;
            memberString+=",";
        }
        memberString = memberString.substring(0,memberString.length()-1);
        String strSQL = "UPDATE "+occasionTableTableName+" SET "+members+" = '"+memberString+"' WHERE "+occasionId+" = "+occasion.getOccasionId();
        db.execSQL(strSQL);
    }
    public Cursor getOccasionDetails(String occasionIdValue){
        Integer occId = Integer.parseInt(occasionIdValue);
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+occasionTableTableName +" where OccasionId = "+occId;
        System.out.println("QUERY: "+query);
        Cursor data = db.rawQuery(query,null);
        if(data.getCount()==0){
            System.out.println("NO RECORDS FOUND");
        }
        return data;
    }
}
