package gr.nightwall.deliveryapp.database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gr.nightwall.deliveryapp.database.interfaces.OnGetDataListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.models.management.BusinessSettings;
import gr.nightwall.deliveryapp.models.management.User;
import gr.nightwall.deliveryapp.utils.Consts;

public class FirebaseDB {

    public enum Reference{
        BUSINESS_SETTINGS,
        USERS
    }

    // region DATABASE REFERENCES

    private static DatabaseReference getReference(Reference reference){
        switch (reference){
            case BUSINESS_SETTINGS:
                return getBusinessSettingsDatabase();
            case USERS:
                return getUsersDatabase();
        }

        return null;
    }

    private static DatabaseReference getBusinessSettingsDatabase(){
        return FirebaseDatabase.getInstance().getReference(Consts.DB_BUSINESS_SETTINGS);
    }

    private static DatabaseReference getUsersDatabase(){
        return FirebaseDatabase.getInstance().getReference(Consts.DB_USERS);
    }

    //endregion

    //region ACTIONS

    public static <T> void saveData(Reference db , T item, OnSaveDataListener onSaveDataListener){
        DatabaseReference reference = getReference(db);

        if(reference == null){
            onSaveDataListener.onFail();
            return;
        }

        reference.setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                onSaveDataListener.onSuccess();
            } else{
                onSaveDataListener.onFail();
            }
        });
    }

    public static <T> void getData(Reference db, Class<T> itemClass, OnGetDataListener onGetDataListener){
        DatabaseReference reference = getReference(db);

        if(reference == null){
            onGetDataListener.onFail();
            return;
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                T item = dataSnapshot.getValue(itemClass);

                if (item == null) {
                    onGetDataListener.onFail();
                }

                onGetDataListener.onSuccess(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onGetDataListener.onFail();
            }
        });
    }

    //endregion

}
