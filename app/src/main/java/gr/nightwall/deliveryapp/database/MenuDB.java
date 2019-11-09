package gr.nightwall.deliveryapp.database;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.database.interfaces.OnGetItemListener;
import gr.nightwall.deliveryapp.database.interfaces.OnGetListListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.models.shop.*;

import static gr.nightwall.deliveryapp.database.FirebaseDB.Reference.ITEM_TEMPLATES;

public class MenuDB {

    //region ITEM TEMPLATES

    public static void saveItemTemplate(ItemTemplate itemTemplate, OnSaveDataListener onSaveDataListener){
        FirebaseDB.saveItemWithId(ITEM_TEMPLATES, itemTemplate, itemTemplate.getId(), onSaveDataListener);
    }

    public static void getAllItemTemplates(OnGetListListener onGetListListener){
        FirebaseDB.getList(ITEM_TEMPLATES, ItemTemplate.class, onGetListListener);
    }

    public static void getItemTemplateByID(String id){

    }

    //endregion

    //region ITEMS

    public static void addItem(Item item){

    }

    public static void saveItem(Item item){

    }

    public static ArrayList<Item> getAllItems(){
        return null;
    }

    public static Item getItemByID(String id){
        return null;
    }

    //endregion

    //region CATEGORIES

    public static void addCategory(Category category){

    }

    public static void saveCategory(Category category){

    }

    public static ArrayList<Category> getAllCategories(){
        return null;
    }

    public static Category getCategoryByID(String id){
        return null;
    }

    //endregion

}
