package com.equipay.equipay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class EquiPayDatabase extends SQLiteOpenHelper {

    ExpenseTable ExpenseTable = new ExpenseTable();
    OccasionTable occasionTable = new OccasionTable();
    // Occasion Table Variables
    String occasionId = occasionTable.getOccasionID();
    String occasionName = occasionTable.getOccasionName();
    String dateOfOccasion = occasionTable.getDateOfOccasion();
    String members = occasionTable.getMembers();
    String occasionTableTableName = occasionTable.getTableName();

    // Expense Table Variables
    String ExpenseTableTableName = ExpenseTable.getTableName();
    String ExpenseEvent = ExpenseTable.getExpenseEvent();
    String ExpenseId = ExpenseTable.getExpenseId();
    String payee = ExpenseTable.getPayee();
    String amount = ExpenseTable.getAmount();
    String behalfOf = ExpenseTable.getBehalfOf();
    String dateOfExpense = ExpenseTable.getDateOfExpense();
    String occasionIdFK = ExpenseTable.getOccasionIdFK();

    public EquiPayDatabase(Context context){
        super(context,new OccasionTable().getTableName(),null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createExpenseTable = createExpenseTableQuery();
        String createOccasionTable = createOccasionTableQuery();
        db.execSQL(createOccasionTable);
        db.execSQL(createExpenseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS "+new OccasionTable().getTableName());
        db.execSQL("DROP IF TABLE EXISTS "+new ExpenseTable().getTableName());
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

    public String createExpenseTableQuery(){

        String createExpenseTableQuery =
                "CREATE TABLE "+ExpenseTableTableName +"("+
                        ExpenseId+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                        ExpenseEvent+" TEXT NOT NULL," +
                        occasionIdFK+" INTEGER NOT NULL, "+
                        payee+" TEXT NOT NULL," +
                        amount+" REAL NOT NULL," +
                        behalfOf+" TEXT NOT NULL," +
                        dateOfExpense+" TEXT NOT NULL," +
                        "FOREIGN KEY ("+occasionIdFK+") REFERENCES "+occasionTableTableName+"("+occasionId+"))";
        return createExpenseTableQuery;
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
    public boolean createNewExpense(Expense Expense){
        ArrayList<String> membersList = Expense.getOnBehalfOf();
        String memberString = "";
        for (String memberName:membersList){
            memberString+=memberName;
            memberString+=",";
        }
        memberString = memberString.substring(0,memberString.length()-1);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ExpenseEvent,Expense.getEvent());
        contentValues.put(occasionId, Expense.getOccasionId());
        contentValues.put(payee, Expense.getPayee());
        contentValues.put(amount, Expense.getAmount());
        contentValues.put(behalfOf, memberString);
        contentValues.put(dateOfExpense,Expense.getDateOfExpense());

        long result = db.insert(ExpenseTableTableName,null,contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor getExpensesByOccasionId(String occasionIdValue){
        Integer occId = Integer.parseInt(occasionIdValue);
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ExpenseTableTableName +" where OccasionId = "+occId;
        System.out.println("QUERY: "+query);
        Cursor data = db.rawQuery(query,null);
        if(data.getCount()==0){
            System.out.println("NO RECORDS FOUND");
        }
        return data;
    }
    public void deleteExpense(Expense Expense){
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(ExpenseTableTableName, "ExpenseId=?",new String[]{Expense.getExpenseId()});
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
