package gr.nightwall.deliveryapp.database.interfaces;

import java.util.ArrayList;

public interface OnGetListListener {

    <T> void onSuccess(ArrayList<T> list);

    void onFail();

}
