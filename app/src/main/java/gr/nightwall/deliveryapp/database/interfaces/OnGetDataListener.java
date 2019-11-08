package gr.nightwall.deliveryapp.database.interfaces;

public interface OnGetDataListener {

    <T> void onSuccess(T item);

    void onFail();

}
