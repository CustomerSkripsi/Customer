package mobi.garden.bottomnavigationtest.Model;

public class Rating {
    public String mFeedbackOption;
    public int mFeedbackOptionID;
    public boolean isChecked;

    public Rating(String mFeedbackOption, int mFeedbackOptionID) {
        this.mFeedbackOption = mFeedbackOption;
        this.mFeedbackOptionID = mFeedbackOptionID;
        isChecked = false;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFeedbackOption() {
        return mFeedbackOption;
    }
}
