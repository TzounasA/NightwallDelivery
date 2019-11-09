package gr.nightwall.deliveryapp.models.shop;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.utils.Utils;

public class Category {

    private String id, name, priority;
    private String description, imageURL;
    private List<String> itemsIds;
    private boolean visible = true;

    private CategoryType categoryType;

    /* = = = = = = = = = = = = = = = *
     *          CONSTRUCTORS         *
     * = = = = = = = = = = = = = = = */

    public Category() {}

    public Category(String name, String priority) {
        this.name = name;
        this.priority = priority;

        id = name.trim().replace(" ", "_").replace("/", "-");
        id = id + "_" + System.currentTimeMillis()/1000;
        id = Utils.fixDatabasePath(id);

        description = "";
        imageURL = "";
    }

    public Category(String name, String priority, CategoryType categoryType) {
        this(name, priority);

        this.categoryType = categoryType;
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

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

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

    public String getPriority() {
        return priority;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

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

    @Exclude
    public String getCategoryTypeName(){
        return categoryType == null? "" : categoryType.getName();
    }

    @Exclude
    public int getCategoryTypeIconRes(){
        return categoryType == null? R.drawable.ic_category_24 : categoryType.getIconRes();
    }

    @Exclude
    public int getCategoryTypeID(){
        return categoryType == null? -1 : categoryType.getID();
    }




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
