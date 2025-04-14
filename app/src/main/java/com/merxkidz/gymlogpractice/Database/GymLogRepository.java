package com.merxkidz.gymlogpractice.Database;

import android.app.Application;
import android.util.Log;

import com.merxkidz.gymlogpractice.Database.entities.GymLog;
import com.merxkidz.gymlogpractice.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GymLogRepository {
    private GymLogDAO gymLogDAO;
    private ArrayList<GymLog> allLogs;

    public GymLogRepository(Application application){
        GymLogDatabase db = GymLogDatabase.getDatabase(application);
        this.gymLogDAO = db.gymLogDAO();
        this.allLogs = this.gymLogDAO.getAllRecords();
    }

    public ArrayList<GymLog> getAllLogs() {
        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<GymLog>>() {
                    @Override
                    public ArrayList<GymLog> call() throws Exception {
                        return gymLogDAO.getAllRecords();
                    }
                }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            Log.i(MainActivity.TAG,"Problem when getting all GymLogs in the repository");
        }
        return null;
    }

    public void insertGymLog(GymLog gymLog){
        GymLogDatabase.databaseWriteExecutor.execute(() ->{
            gymLogDAO.insert(gymLog);
        });
    }

}
