package gr.nightwall.deliveryapp.database.interfaces;

public interface OnGetItemListener {

    <T> void onSuccess(T item);

    void onFail();

}
