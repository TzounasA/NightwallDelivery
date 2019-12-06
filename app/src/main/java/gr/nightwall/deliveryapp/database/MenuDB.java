package gr.nightwall.deliveryapp.database;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.database.interfaces.OnGetItemListener;
import gr.nightwall.deliveryapp.database.interfaces.OnGetListListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.models.shop.*;

import static gr.nightwall.deliveryapp.database.FirebaseDB.Reference.CATEGORIES;
import static gr.nightwall.deliveryapp.database.FirebaseDB.Reference.ITEMS;
import static gr.nightwall.deliveryapp.database.FirebaseDB.Reference.ITEM_TEMPLATES;

public class MenuDB {

    //region ITEM TEMPLATES

    public static void saveItemTemplate(ItemTemplate itemTemplate, OnSaveDataListener onSaveDataListener){
        FirebaseDB.saveItemWithId(ITEM_TEMPLATES, itemTemplate, itemTemplate.getId(), onSaveDataListener);
    }

    public static void getAllItemTemplates(OnGetListListener onGetListListener){
        FirebaseDB.getList(ITEM_TEMPLATES, ItemTemplate.class, onGetListListener);
    }

    public static void getItemTemplateByID(String id, OnGetItemListener onGetItemListener){
        FirebaseDB.getItemById(ITEM_TEMPLATES, id, ItemTemplate.class, onGetItemListener);
    }

    //endregion

    //region ITEMS

    public static void saveItem(Item item, OnSaveDataListener onSaveDataListener){
        FirebaseDB.saveItemWithId(ITEMS, item, item.getId(), onSaveDataListener);
    }

    public static void getAllItems(OnGetListListener onGetListListener){
        FirebaseDB.getList(ITEMS, Item.class, "priorityNumber", onGetListListener);
    }

    public static void getItemsOfCategory(String categoryId, OnGetListListener onGetListListener){
        FirebaseDB.getList(ITEMS, Item.class, "categoryId", categoryId, onGetListListener);
    }

    public static Item getItemByID(String id){
        return null;
    }

    public static void deleteItem(Item item, OnSaveDataListener onSaveDataListener){
        FirebaseDB.deleteValueOfId(ITEMS, item.getId(), onSaveDataListener);
    }

    //endregion

    //region CATEGORIES

    public static void saveCategory(Category category, OnSaveDataListener onSaveDataListener){
        FirebaseDB.saveItemWithId(CATEGORIES, category, category.getId(), onSaveDataListener);
    }

    public static void getAllCategories(OnGetListListener onGetListListener){
        FirebaseDB.getList(CATEGORIES, Category.class, "priorityNumber", onGetListListener);
    }

    public static Category getCategoryByID(String id){
        return null;
    }

    public static void deleteCategory(Category category, OnSaveDataListener onSaveDataListener){
        FirebaseDB.deleteValueOfId(CATEGORIES, category.getId(), onSaveDataListener);
    }

    //endregion

}
