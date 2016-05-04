package com.litao.android.lib;

/**
 * Created by 李涛 on 16/4/21.
 */
public class Configuration {

    /**
     * ablums dialog full screen
     */
    public static final int DIALOG_FULL = -1;

    /**
     * ablums dialog half screen
     */
    public static final int DIALOG_HALF = -2;

    /**
     * ablums dialog mode grid view
     */
    public static final int DIALOG_GRID = -1;

    /**
     * ablums dialog mode list view
     */
    public static final int DIALOG_LIST = -2;

    /**
     * Flag to indicate that this view has a camera button
     */
    public boolean hasCamera;

    /**
     * Flag to indicate that this photo view has a layer mask
     */
    public boolean hasShade;

    /**
     * Flag to indicate that this photo view clickable
     */
    public boolean hasPreview;

    /**
     * GridView spacing between rows and columns
     */
    public int spaceSize;

    /**
     * Maximum width of photos
     */
    public int photoMaxWidth;

    /**
     * Checkbox background color
     */
    public int checkBoxColor;

    /**
     * ablums dialog height
     */
    public int dialogHeight;

    /**
     * ablums dialog title
     */
    public String ablumsTitle;

    /**
     * maximum photos selection limit
     */
    public int maximum;

    /**
     * ablums dialog mode
     *
     * DIALOG_FULL or DIALOG_HALF
     */
    public int dialogMode;

    /**
     * Toast of maximum photos selection limit
     */
    public String tip;

    private Configuration(final Builder builder) {
        this.hasCamera = builder.hasCamera;
        this.spaceSize = builder.spaceSize;
        this.photoMaxWidth = builder.photoMaxWidth;
        this.checkBoxColor = builder.checkBoxColor;
        this.dialogHeight = builder.dialogHeight;
        this.maximum = builder.maximum;
        this.tip = builder.tip;
        this.ablumsTitle = builder.ablumsTitle;
        this.hasShade = builder.hasShade;
        this.dialogMode = builder.dialogMode;
        this.hasPreview = builder.hasPreview;
    }

    public static class Builder {

        private boolean hasCamera;

        private boolean hasShade;

        private boolean hasPreview;

        private int spaceSize;

        private int photoMaxWidth;

        private int checkBoxColor;

        private int dialogHeight;

        private int dialogMode;

        public String ablumsTitle;

        private int maximum;

        private String tip;

        public Builder() {
            hasCamera = true;
            hasShade = true;
            hasPreview = true;
            spaceSize = 4;
            photoMaxWidth = 120;
            checkBoxColor = 0xFF3F51B5;
            dialogHeight = DIALOG_HALF;
            dialogMode = DIALOG_GRID;
            maximum = 9;
            tip = null;
            ablumsTitle = null;
        }

        public Builder(final Configuration cfg) {
            hasCamera = cfg.hasCamera;
            hasShade = cfg.hasShade;
            hasPreview = cfg.hasPreview;
            spaceSize = cfg.spaceSize;
            photoMaxWidth = cfg.photoMaxWidth;
            checkBoxColor = cfg.checkBoxColor;
            dialogHeight = cfg.dialogHeight;
            dialogMode = cfg.dialogMode;
            maximum = cfg.maximum;
            tip = cfg.tip;
            ablumsTitle = cfg.ablumsTitle;

        }

        public Builder hasCamera(boolean hasCamera) {
            this.hasCamera = hasCamera;
            return this;
        }

        public Builder hasShade(boolean hasShade){
            this.hasShade = hasShade;
            return this;
        }

        public Builder hasPreview(boolean hasPreview){
            this.hasPreview = hasPreview;
            return this;
        }

        public Builder setSpaceSize(int spaceSize) {
            this.spaceSize = spaceSize;
            return this;
        }

        public Builder setPhotoMaxWidth(int photoMaxWidth) {
            this.photoMaxWidth = photoMaxWidth;
            return this;
        }

        public Builder setCheckBoxColor(int checkBoxColor) {
            this.checkBoxColor = checkBoxColor;
            return this;
        }


        public Builder setDialogHeight(int dialogHeight) {
            this.dialogHeight = dialogHeight;
            return this;
        }

        public Builder setDialogMode(int dialogMode){
            this.dialogMode = dialogMode;
            return this;
        }

        public Builder setAblumsTitle(String ablumsTitle) {
            this.ablumsTitle = ablumsTitle;
            return this;
        }

        public Builder setMaximum(int maximum) {
            this.maximum = maximum;
            return this;
        }

        public Builder setTip(String maximumMsg) {
            this.tip = tip;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
