package gr.nightwall.deliveryapp.database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.database.interfaces.OnGetItemListener;
import gr.nightwall.deliveryapp.database.interfaces.OnGetListListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.utils.Consts;

public class FirebaseDB {

    // region DATABASE REFERENCES

    public enum Reference{
        BUSINESS_SETTINGS,
        USERS,

        ITEM_TEMPLATES,
        CATEGORIES,
        ITEMS
    }

    private static DatabaseReference getReference(Reference reference){
        switch (reference){
            case BUSINESS_SETTINGS:
                return FirebaseDatabase.getInstance().getReference(Consts.DB_BUSINESS_SETTINGS);
            case USERS:
                return FirebaseDatabase.getInstance().getReference(Consts.DB_USERS);
            case ITEM_TEMPLATES:
                return FirebaseDatabase.getInstance().getReference(Consts.DB_ITEM_TEMPLATES);
            case CATEGORIES:
                return FirebaseDatabase.getInstance().getReference(Consts.DB_CATEGORIES);
            case ITEMS:
                return FirebaseDatabase.getInstance().getReference(Consts.DB_ITEMS);
        }

        return null;
    }

    //endregion

    //region ACTIONS

    public static <T> void saveItem(Reference db , T item, OnSaveDataListener onSaveDataListener){
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

    public static <T> void saveItemWithId(Reference db , T item, String itemId, OnSaveDataListener onSaveDataListener){
        DatabaseReference reference = getReference(db);

        if(reference == null){
            onSaveDataListener.onFail();
            return;
        }

        reference.child(itemId).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                onSaveDataListener.onSuccess();
            } else{
                onSaveDataListener.onFail();
            }
        });
    }

    public static <T> void getItem(Reference db, Class<T> itemClass, OnGetItemListener onGetItemListener){
        DatabaseReference reference = getReference(db);

        if(reference == null){
            onGetItemListener.onFail();
            return;
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                T item = dataSnapshot.getValue(itemClass);

                if (item == null) {
                    onGetItemListener.onFail();
                    return;
                }

                onGetItemListener.onSuccess(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onGetItemListener.onFail();
            }
        });
    }

    public static <T> void getList(Reference db, Class<T> itemClass, OnGetListListener onGetListListener){
        DatabaseReference reference = getReference(db);

        if(reference == null){
            onGetListListener.onFail();
            return;
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<T> list = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    T item = snapshot.getValue(itemClass);

                    if (item == null) {
                        onGetListListener.onFail();
                        return;
                    }

                    list.add(item);
                }

                onGetListListener.onSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onGetListListener.onFail();
            }
        });
    }

    public static <T> void getList(Reference db, Class<T> itemClass, String orderBy, OnGetListListener onGetListListener){
        DatabaseReference reference = getReference(db);

        if(reference == null){
            onGetListListener.onFail();
            return;
        }

        reference
                .orderByChild(orderBy)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<T> list = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            T item = snapshot.getValue(itemClass);

                            if (item == null) {
                                onGetListListener.onFail();
                                return;
                            }

                            list.add(item);
                        }

                        onGetListListener.onSuccess(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onGetListListener.onFail();
                    }
                });
    }

    public static <T> void getList(Reference db, Class<T> itemClass, String orderByChild, String equalTo, OnGetListListener onGetListListener){
        DatabaseReference reference = getReference(db);

        if(reference == null){
            onGetListListener.onFail();
            return;
        }

        reference
                .orderByChild(orderByChild)
                .equalTo(equalTo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<T> list = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            T item = snapshot.getValue(itemClass);

                            if (item == null) {
                                onGetListListener.onFail();
                                return;
                            }

                            list.add(item);
                        }

                        onGetListListener.onSuccess(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onGetListListener.onFail();
                    }
                });
    }

    public static <T> void deleteValueOfId(Reference db, String id, OnSaveDataListener onSaveDataListener ){
        DatabaseReference reference = getReference(db);

        if(reference == null){
            onSaveDataListener.onFail();
            return;
        }

        reference.child(id).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                onSaveDataListener.onSuccess();
            } else{
                onSaveDataListener.onFail();
            }
        });
    }

    //endregion

}
