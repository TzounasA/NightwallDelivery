package gr.nightwall.deliveryapp.views;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.interfaces.OnActionListener;
import gr.nightwall.deliveryapp.interfaces.OnInputPositiveClickListener;
import gr.nightwall.deliveryapp.utils.Utils;

public class CustomDialog {

    public enum DialogType {
        TEXT, INPUT, CUSTOM
    }

    private static CustomDialog openDialog = null;
    private static boolean isOpen = false;

    private ViewGroup parent;

    // === VIEWS ===
    private RelativeLayout dialogView;

    private View titleDivider;
    private ViewGroup dialogTitleLayout;
    private CircleImageView dialogIcon;
    private TextView dialogTitle;

    private TextView tvDialogText;

    private ViewGroup dialogButtonsLayout;
    private View buttonsDivider;
    private MaterialButton btnPositive, btnNegative;

    //Input
    private TextInputLayout dialogInputLayout;
    private TextInputEditText etDialogInput;

    // Custom View
    private FrameLayout customViewFrame;

    // === FIELDS ===
    private DialogType dialogType;

    private String title, text;
    private String btnPositiveText, btnNegativeText;
    private int positiveTextColorResource, positiveRippleColorResource, dialogIconColorResource;

    private Drawable iconDrawable;

    // Input
    private String hint, prefill;

    // Custom View
    private View customView;

    // === VARS ===
    private boolean closeOnPositive = true;
    private boolean closeOnNegative = true;
    private boolean positiveEnabled = true;
    private boolean negativeEnabled = true;
    private boolean cancelable = true;
    private boolean firstOpen = true;

    // === LISTENERS ===
    private OnActionListener onPositiveClickListener;
    private OnActionListener onNegativeClickListener;
    private OnInputPositiveClickListener onInputPositiveClickListener;
    private OnActionListener onCloseListener;


    /* ============================================ *
     *                CONSTRUCTORS                  *
     * ============================================ */

    public CustomDialog(Context context, DialogType type, ViewGroup parent) {
        this.dialogType = type;
        this.parent = parent;

        inflateDialog(context);
        init(context);
    }

    private void inflateDialog(Context context) {
        int viewResource = 0;
        switch (dialogType) {
            case TEXT:
                viewResource = R.layout.tz_dialog_text;
                break;
            case INPUT:
                viewResource = R.layout.tz_dialog_input;
                break;
            case CUSTOM:
                viewResource = R.layout.tz_dialog_custom;
                break;
        }

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        dialogView = (RelativeLayout) inflater.inflate(viewResource, parent, false);
    }

    private void init(Context context) {
        initDialog(context);
        initTitle();
        initButtons(context);

        if (dialogType == DialogType.TEXT || dialogType == DialogType.INPUT)
            initText();

        if (dialogType == DialogType.INPUT)
            initInput();
        else if (dialogType == DialogType.CUSTOM)
            initCustomView();
    }

    private void initDialog(final Context context) {
        CardView cardDialog = dialogView.findViewById(R.id.cardDialog);

        // Can't click through
        cardDialog.setClickable(true);
        cardDialog.setFocusable(true);
    }

    private void initTitle() {
        dialogTitleLayout = dialogView.findViewById(R.id.dialogTitleLayout);
        dialogTitleLayout.setVisibility(View.GONE);

        titleDivider = dialogView.findViewById(R.id.titleDivider);
        titleDivider.setVisibility(View.GONE);

        dialogIcon = dialogView.findViewById(R.id.dialogIcon);
        dialogIcon.setVisibility(View.GONE);

        dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        dialogTitle.setVisibility(View.GONE);
    }

    private void initText() {
        tvDialogText = dialogView.findViewById(R.id.tvDialogText);
        tvDialogText.setVisibility(View.GONE);
    }

    private void initButtons(final Context context) {
        dialogButtonsLayout = dialogView.findViewById(R.id.dialogButtonsLayout);
        buttonsDivider = dialogView.findViewById(R.id.buttonsDivider);
        buttonsDivider.setVisibility(View.GONE);

        // Button Positive
        btnPositive = dialogView.findViewById(R.id.btnPositive);
        if (btnPositive != null)
            btnPositive.setOnClickListener(view -> closeDialog(context));

        // Button Negative
        btnNegative = dialogView.findViewById(R.id.btnNegative);
        if (btnNegative != null)
            btnNegative.setOnClickListener(view -> closeDialog(context));
    }

    private void initInput(){
        dialogInputLayout = dialogView.findViewById(R.id.dialogInputLayout);
        etDialogInput = dialogView.findViewById(R.id.etDialogInput);

        etDialogInput.setFocusable(true);
        etDialogInput.requestFocus();
    }

    private void initCustomView() {
        customViewFrame = dialogView.findViewById(R.id.customViewFrame);
        titleDivider.setVisibility(View.GONE);
        buttonsDivider.setVisibility(View.GONE);
    }


    /* ============================================ *
     *                  ACTIONS                     *
     * ============================================ */

    public void openDialog() {
        parent.setVisibility(View.VISIBLE);
        dialogView.setVisibility(View.VISIBLE);

        final CardView cardDialog = dialogView.findViewById(R.id.cardDialog);
        View shadowBack = dialogView.findViewById(R.id.shadowBack);
        cardDialog.setVisibility(View.VISIBLE);

        if (cardDialog.getHeight() == 0) return;

        // Animation
        YoYo.with(Techniques.FadeInUp)
                .duration(150)
                .onStart(animator -> {
                    dialogView.setVisibility(View.VISIBLE);
                    cardDialog.setVisibility(View.VISIBLE);
                    parent.setVisibility(View.VISIBLE);

                    openDialog = CustomDialog.this;
                    isOpen = true;
                })
                .playOn(cardDialog);

        YoYo.with(Techniques.FadeIn)
                .duration(200)
                .interpolate(new AccelerateInterpolator())
                .playOn(shadowBack);
    }

    public void closeDialog(Context context) {
        CardView cardDialog = dialogView.findViewById(R.id.cardDialog);
        View shadowBack = dialogView.findViewById(R.id.shadowBack);

        // Animation
        YoYo.with(Techniques.FadeOutDown)
                .duration(100)
                .onEnd(animator -> {
                    dialogView.setVisibility(View.GONE);
                    parent.setVisibility(View.GONE);
                    parent.removeAllViews();

                    openDialog = null;
                    isOpen = false;

                    if (onCloseListener != null)
                        onCloseListener.onActionClick(CustomDialog.this);
                })
                .playOn(cardDialog);

        YoYo.with(Techniques.FadeOut)
                .duration(100)
                .interpolate(new DecelerateInterpolator())
                .playOn(shadowBack);

        Utils.closeSoftKeyboard(context);
    }

    public void show(){
        View.OnLayoutChangeListener onLayoutChangeListener = (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (firstOpen){
                openDialog();
                firstOpen = false;
            }
        };
        dialogView.addOnLayoutChangeListener(onLayoutChangeListener);

        parent.addView(dialogView);

        openDialog();
    }


    /* ============================================ *
     *                  SETTERS                     *
     * ============================================ */

    public CustomDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomDialog setTitle(Context context, @StringRes int res) {
        return setTitle(res == 0 ? null : context.getString(res));
    }

    public CustomDialog setBtnPositiveText(String btnPositiveText) {
        this.btnPositiveText = btnPositiveText;

        return this;
    }

    public CustomDialog removePositiveDialog() {
        return this;
    }

    public CustomDialog setBtnNegativeText(String btnNegativeText) {
        this.btnNegativeText = btnNegativeText;

        return this;
    }

    public CustomDialog setPositiveTextColorResource(int positiveTextColorResource) {
        this.positiveTextColorResource = positiveTextColorResource;
        return this;
    }

    public CustomDialog setPositiveRippleColorResource(int positiveRippleColorResource) {
        this.positiveRippleColorResource = positiveRippleColorResource;
        return this;
    }

    public CustomDialog removeNegativeDialog() {
        return this;
    }

    public CustomDialog setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        return this;
    }

    public CustomDialog setIconResource(Context context, int res) {
        Drawable iconDrawable = ResourcesCompat.getDrawable(context.getResources(), res, null);
        setIconDrawable(iconDrawable);

        return this;
    }

    public CustomDialog setDialogIconColorResource(int dialogIconColorResource) {
        this.dialogIconColorResource = dialogIconColorResource;
        return this;
    }

    // Listeners
    public CustomDialog setOnPositiveClickListener(OnActionListener onPositiveClickListener) {
        this.onPositiveClickListener = onPositiveClickListener;
        return this;
    }

    public CustomDialog setOnNegativeClickListener(OnActionListener onNegativeClickListener) {
        this.onNegativeClickListener = onNegativeClickListener;
        return this;
    }

    public CustomDialog setOnCloseListener(OnActionListener onCloseListener) {
        this.onCloseListener = onCloseListener;
        return this;
    }

    // Variables
    public CustomDialog setCloseOnPositive(boolean closeOnPositive) {
        this.closeOnPositive = closeOnPositive;
        return this;
    }

    public CustomDialog setCloseOnNegative(boolean closeOnNegative) {
        this.closeOnNegative = closeOnNegative;
        return this;
    }

    public CustomDialog setPositiveEnabled(boolean positiveEnabled) {
        this.positiveEnabled = positiveEnabled;
        return this;
    }

    public CustomDialog setNegativeEnabled(boolean negativeEnabled) {
        this.negativeEnabled = negativeEnabled;
        return this;
    }

    public CustomDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }


    // = = = = TEXT = = = =

    public CustomDialog setText(String text) {
        this.text = text;

        return this;
    }

    public CustomDialog setText(Context context, @StringRes int res) {
        return setText(res == 0 ? null : context.getString(res));
    }


    // = = = = INPUT = = = =

    public CustomDialog setInput(@Nullable String hint, @Nullable String prefill){
        this.hint = hint;
        this.prefill = prefill;
        return this;
    }

    public CustomDialog setInput(Context context, @Nullable String hint, @StringRes int prefill) {
        return setInput(
                hint,
                prefill == 0 ? null : context.getString(prefill));
    }

    public CustomDialog setInput(Context context, @StringRes int hint, @Nullable String prefill) {
        return setInput(
                hint == 0 ? null : context.getString(hint),
                prefill);
    }

    public CustomDialog setInput(Context context, @StringRes int hint, @StringRes int prefill) {
        return setInput(
                hint == 0 ? null : context.getString(hint),
                prefill == 0 ? null : context.getString(prefill));
    }

    public CustomDialog setOnInputPositiveClickListener(OnInputPositiveClickListener onInputPositiveClickListener) {
        this.onInputPositiveClickListener = onInputPositiveClickListener;
        return this;
    }

    public CustomDialog setInputType(int type){
        etDialogInput.setInputType(type);
        return this;
    }


    // = = = = CUSTOM VIEW = = = =

    public CustomDialog setCustomView(@NonNull View view) {
        if (view.getParent() != null && view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        this.customView = view;
        return this;
    }

    public CustomDialog setCustomView(Context context, @LayoutRes int layoutRes) {
        LayoutInflater li = LayoutInflater.from(context);
        return setCustomView(li.inflate(layoutRes, null));
    }


    /* ============================================ *
     *                  GETTERS                     *
     * ============================================ */

    public DialogType getDialogType() {
        return dialogType;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public int getDialogIconColorResource() {
        return dialogIconColorResource;
    }

    public String getBtnPositiveText() {
        return btnPositiveText;
    }

    public int getPositiveTextColorResource() {
        return positiveTextColorResource;
    }

    public int getPositiveRippleColorResource() {
        return positiveRippleColorResource;
    }

    public String getBtnNegativeText() {
        return btnNegativeText;
    }

    public RelativeLayout getDialogView() {
        return dialogView;
    }

    public static boolean isOpen() {
        return isOpen;
    }

    public static CustomDialog getOpenDialog() {
        return openDialog;
    }

    // Listeners
    public OnActionListener getOnPositiveClickListener() {
        return onPositiveClickListener;
    }

    public OnActionListener getOnNegativeClickListener() {
        return onNegativeClickListener;
    }

    public OnActionListener getOnCloseListener() {
        return onCloseListener;
    }

    // Variables
    public boolean mustCloseOnPositive() {
        return closeOnPositive;
    }

    public boolean mustCloseOnNegative() {
        return closeOnNegative;
    }

    public boolean isPositiveEnabled() {
        return positiveEnabled;
    }

    public boolean isNegativeEnabled() {
        return negativeEnabled;
    }

    public boolean isCancelable() {
        return cancelable;
    }


    // = = = = TEXT = = = =

    public String getText() {
        return text;
    }


    // = = = = INPUT = = = =

    public String getHint() {
        return hint;
    }

    public String getPrefill() {
        return prefill;
    }

    public OnInputPositiveClickListener getOnInputPositiveClickListener() {
        return onInputPositiveClickListener;
    }


    // = = = = CUSTOM VIEW = = = =

    public View getCustomView() {
        return customView;
    }


    /* ============================================ *
     *                    BUILD                     *
     * ============================================ */

    public CustomDialog build(Context context) {
        buildDialog(context);
        buildTitle();
        buildIcon(context);
        buildText();
        buildInput();
        buildCustomView();
        buildButtons();
        buildButtonsListeners(context);

        return this;
    }

    private void buildDialog(final Context context) {
        if (cancelable){
            View shadowBack = dialogView.findViewById(R.id.shadowBack);
            shadowBack.setOnClickListener(view -> closeDialog(context));
        }
    }

    private void buildTitle() {
        if (title != null && !title.isEmpty()){
            dialogTitleLayout.setVisibility(View.VISIBLE);
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
    }

    private void buildIcon(Context context) {
        if (iconDrawable == null)
            return;

        dialogTitleLayout.setVisibility(View.VISIBLE);
        dialogIcon.setVisibility(View.VISIBLE);
        dialogIcon.setImageDrawable(iconDrawable);

        if (dialogIconColorResource == 0)
            return;

        dialogIcon.setColorFilter(ContextCompat.getColor(context, dialogIconColorResource),
                    android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void buildText() {
        if (text != null && !text.isEmpty()){
            tvDialogText.setVisibility(View.VISIBLE);
            tvDialogText.setText(text);
        }
    }

    private void buildInput() {
        if (dialogType!= DialogType.INPUT)
            return;

        if (hint != null)
            dialogInputLayout.setHint(hint);

        if (prefill != null)
            etDialogInput.setText(prefill);
    }

    private void buildCustomView() {
        if (dialogType!= DialogType.CUSTOM)
            return;

        if (customView == null)
            return;

        customViewFrame.addView(customView);
    }

    private void buildButtons() {
        buildPositiveButton();
        buildNegativeButton();
    }

    private void buildPositiveButton() {
        if (!positiveEnabled) {
            btnPositive.setVisibility(View.GONE);
            return;
        }

        String positive = getBtnPositiveText();
        if (btnPositiveText != null && !positive.isEmpty())
            btnPositive.setText(positive);

        if (positiveTextColorResource != 0)
            btnPositive.setTextColor(positiveTextColorResource);

        if (positiveRippleColorResource != 0)
            btnPositive.setRippleColorResource(positiveRippleColorResource);
    }

    private void buildNegativeButton() {
        if (!negativeEnabled) {
            btnNegative.setVisibility(View.GONE);
            return;
        }

        String negative = getBtnNegativeText();
        if (negative != null && !negative.isEmpty())
            btnNegative.setText(negative);
    }

    private void buildButtonsListeners(final Context context) {
        // Positive
        final OnActionListener onPositiveClickListener = getOnPositiveClickListener();

        if (onPositiveClickListener != null){
            btnPositive.setOnClickListener(v -> {
                onPositiveClickListener.onActionClick(CustomDialog.this);

                if (mustCloseOnPositive())
                    closeDialog(context);
            });
        }

        // Negative
        final OnActionListener onNegativeClickListener = getOnNegativeClickListener();

        if (onNegativeClickListener != null){
            btnNegative.setOnClickListener(v -> {
                onNegativeClickListener.onActionClick(CustomDialog.this);

                if (mustCloseOnNegative())
                    closeDialog(context);
            });
        } else {
            Log.i("TAGY", "buildButtonsListeners: onNegativeClickListener == null");
        }

        // Input Positive
        final OnInputPositiveClickListener onInputPositiveClickListener = getOnInputPositiveClickListener();

        if (onInputPositiveClickListener != null){
            btnPositive.setOnClickListener(v -> {
                Editable input = etDialogInput.getText();
                String inputString = input == null ? null : input.toString();
                onInputPositiveClickListener.onPositiveClick(CustomDialog.this, inputString);

                if (mustCloseOnPositive())
                    closeDialog(context);
            });
        }

    }

}
