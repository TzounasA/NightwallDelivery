package gr.nightwall.deliveryapp.models.shop;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.utils.Utils;

public class Category {

    private String id, name, priorityNumber;
    private String description, imageURL;
    private List<String> itemsIds;
    private boolean visible = true;

    //private Type categoryType;

    
    /* = = = = = = = = = = = = = = = *
     *          CONSTRUCTORS         *
     * = = = = = = = = = = = = = = = */

    public Category() {
        id = Utils.generateID();

        name = "";
        description = "";

        itemsIds = new ArrayList<>();
        //categoryType = new CategoryType(CategoryType.Type.FOOD);
    }

    public Category(String priority){
        this();
        priorityNumber = priority;
    }

    /* = = = = = = = = = = = = = = = *
     *            SETTERS            *
     * = = = = = = = = = = = = = = = */

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPriorityNumber(String priority) {
        this.priorityNumber = priority;
    }

//    public void setCategoryType(CategoryType categoryType) {
//        this.categoryType = categoryType;
//    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    /* = = = = = = = = = = = = = = = *
     *              EDITS            *
     * = = = = = = = = = = = = = = = */

    public void addItem(String itemId){
        checkIfNull();
        itemsIds.add(itemId);
    }

    public void removeItemAt(int index){
        checkIfNull();
        itemsIds.remove(index);
    }

    public void removeItem(String itemId){
        checkIfNull();
        itemsIds.remove(itemId);
    }


    /* = = = = = = = = = = = = = = = *
     *            GETTERS            *
     * = = = = = = = = = = = = = = = */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public List<String> getItemsIds() {
        checkIfNull();
        return itemsIds;
    }

    public String getPriorityNumber() {
        return priorityNumber;
    }

//    public CategoryType getCategoryType() {
//        return categoryType;
//    }

    public boolean isVisible() {
        return visible;
    }


    /* = = = = = = = = = = = = = = = *
     *         GET MORE INFO         *
     * = = = = = = = = = = = = = = = */

    @Exclude
    public int getItemsCount(){
        checkIfNull();
        return itemsIds.size();
    }

//    @Exclude
//    public String getCategoryTypeName(){
//        return categoryType == null? "" : categoryType.getName();
//    }
//
//    @Exclude
//    public int getCategoryTypeIconRes(){
//        return categoryType == null? R.drawable.ic_category_24 : categoryType.getIconRes();
//    }
//
//    @Exclude
//    public int getCategoryTypeID(){
//        return categoryType == null? -1 : categoryType.getID();
//    }




    /* = = = = = = = = = = = = = = = *
     *        PRIVATE METHODS        *
     * = = = = = = = = = = = = = = = */

    private void checkIfNull(){
        if (itemsIds == null)
            itemsIds = new ArrayList<>();
    }


    /* = = = = = = = = = = = = = = = *
     *            USELESS            *
     * = = = = = = = = = = = = = = = */

    public void setId(String id) {
        this.id = id;
    }

    public void setItemsIds(List<String> itemsIds) {
        this.itemsIds = itemsIds;
    }
}
