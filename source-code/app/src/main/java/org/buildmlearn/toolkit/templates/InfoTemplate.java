package org.buildmlearn.toolkit.templates;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;

import org.buildmlearn.toolkit.R;
import org.buildmlearn.toolkit.activity.TemplateEditorInterface;
import org.buildmlearn.toolkit.infotemplate.fragment.SplashFragment;
import org.buildmlearn.toolkit.model.Template;
import org.buildmlearn.toolkit.model.TemplateInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @brief Info template code implementing methods of TemplateInterface
 * <p/>
 * Created by abhishek on 16/06/15 at 9:59 PM.
 */
public class InfoTemplate implements TemplateInterface {

    transient private InfoAdapter adapter;
    private ArrayList<InfoModel> infoData;
    private int templateId;

    public InfoTemplate() {
        infoData = new ArrayList<>();
    }

    public static boolean validated(Context context, EditText word, EditText meaning) {
        if (word == null || meaning == null) {
            return false;
        }

        String wordText = word.getText().toString().trim();
        String meaningText = meaning.getText().toString().trim();

        if ("".equals(wordText)) {
            word.setError(context.getString(R.string.enter_word));
            return false;
        } else if ("".equals(meaningText)) {
            meaning.setError(context.getString(R.string.enter_description));
            return false;
        }
        return true;

    }

    @Override
    public Object newTemplateEditorAdapter(Context context, final TemplateEditorInterface templateEditorInterface) {
        adapter = new InfoAdapter(context, infoData) {
            @Override
            public boolean onItemClick(int position, View view) {
                return templateEditorInterface.onItemLongClick(position, view);
            }

            @Override
            protected String getAuthorName() {
                return templateEditorInterface.getAuthorName();
            }

            @Override
            protected void setAuthorName(String authorName) {
                templateEditorInterface.setAuthorName(authorName);
            }

            @Override
            protected void setTitle(String title) {
                templateEditorInterface.setProjectTitle(title);
            }

            @Override
            protected void restoreToolbarColorSchema() {
                templateEditorInterface.restoreColorSchema();
            }

            @Override
            protected String getTitle() {
                return templateEditorInterface.getProjectTitle();
            }
        };
        setEmptyView((Activity) context);
        return adapter;
    }

    @Override
    public BaseAdapter newMetaEditorAdapter(Context context) {
        return null;
    }

    @Override
    public Object currentTemplateEditorAdapter() {
        return adapter;
    }

    @Override
    public BaseAdapter currentMetaEditorAdapter() {
        return null;
    }

    @Override
    public BaseAdapter loadProjectMetaEditor(Context context, Document doc) {
        return null;
    }

    @Override
    public Object loadProjectTemplateEditor(Context context, ArrayList<Element> data, final TemplateEditorInterface templateEditorInterface) {
        infoData = new ArrayList<>();
        for (Element item : data) {
            String infoObject = item.getElementsByTagName("item_title").item(0).getTextContent();
            String infoDescription = item.getElementsByTagName("item_description").item(0).getTextContent();
            infoData.add(new InfoModel(infoObject, infoDescription));
        }
        adapter = new InfoAdapter(context, infoData) {
            @Override
            public boolean onItemClick(int position, View view) {
                return templateEditorInterface.onItemLongClick(position, view);
            }

            @Override
            protected String getAuthorName() {
                return templateEditorInterface.getAuthorName();
            }

            @Override
            protected void setAuthorName(String authorName) {
                templateEditorInterface.setAuthorName(authorName);
            }

            @Override
            protected void setTitle(String title) {
                templateEditorInterface.setProjectTitle(title);
            }

            @Override
            protected void restoreToolbarColorSchema() {
                templateEditorInterface.restoreColorSchema();
            }

            @Override
            protected String getTitle() {
                return templateEditorInterface.getProjectTitle();
            }
        };
        setEmptyView((Activity) context);
        return adapter;
    }

    @Override
    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    @Override
    public String getTitle() {
        return "Info Template";
    }

    @Override
    public void addItem(final Activity activity) {


        View dialogView = View.inflate(activity,R.layout.info_dialog_add_edit_data, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.info_add_new_title)
                .setView(dialogView,
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_left),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_top),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_right),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_bottom))
                .setPositiveButton(R.string.info_template_add, null)
                .setNegativeButton(R.string.info_template_cancel, null)
                .create();
        dialog.show();

        final EditText word = (EditText) dialogView.findViewById(R.id.info_word);
        final EditText meaning = (EditText) dialogView.findViewById(R.id.info_meaning);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validated(activity, word, meaning)) {
                    String wordText = word.getText().toString().trim();
                    String meaningText = meaning.getText().toString().trim();

                    InfoModel temp = new InfoModel(wordText, meaningText);
                    infoData.add(temp);
                    adapter.notifyDataSetChanged();
                    setEmptyView(activity);
                    dialog.dismiss();
                }

            }
        });
    }

    @Override
    public void addMetaData(Activity activity) {
        // This is intentionally empty
    }

    @Override
    public void editItem(final Activity activity, int position) {
        View dialogView = View.inflate(activity,R.layout.info_dialog_add_edit_data, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.info_edit_title)
                .setView(dialogView,
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_left),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_top),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_right),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_bottom))
                .setPositiveButton(R.string.info_template_ok, null)
                .setNegativeButton(R.string.info_template_cancel, null)
                .create();
        dialog.show();

        final InfoModel data = infoData.get(position);

        final EditText word = (EditText) dialogView.findViewById(R.id.info_word);
        final EditText meaning = (EditText) dialogView.findViewById(R.id.info_meaning);
        word.setText(data.getInfoObject().trim());
        meaning.setText(data.getInfoDescription().trim());

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validated(activity, word, meaning)) {
                    String wordText = word.getText().toString().trim();
                    String meaningText = meaning.getText().toString().trim();

                    data.setWord(wordText);
                    data.setInfoDescription(meaningText);

                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });
    }

    @Override
    public Object deleteItem(Activity activity, int position) {
        InfoModel infoModel = infoData.get(position);
        infoData.remove(position);
        setEmptyView(activity);
        adapter.notifyDataSetChanged();
        setEmptyView(activity);
        return infoModel;
    }

    @Override
    public void restoreItem(Activity activity, int position, Object object) {
        if (object instanceof InfoModel) {
            InfoModel infoModel = (InfoModel) object;
            if (infoModel != null) {
                infoData.add(position, infoModel);
                adapter.notifyDataSetChanged();
                setEmptyView(activity);
            }
        }
    }

    @Override
    public ArrayList<Element> getItems(Document doc) {
        ArrayList<Element> itemElements = new ArrayList<>();


        for (InfoModel data : infoData) {

            itemElements.add(data.getXml(doc));
        }

        return itemElements;
    }

    @Override
    public android.support.v4.app.Fragment getSimulatorFragment(String filePathWithName) {
        return SplashFragment.newInstance(filePathWithName);
    }

    @Override
    public String getAssetsFileName(Context context) {
        Template[] templates = Template.values();
        return context.getString(templates[templateId].getAssetsName());
    }

    @Override
    public String getAssetsFilePath() {
        return "assets/";
    }

    @Override
    public String getApkFilePath() {
        return "BasicmLearningApp.apk";
    }

    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent intent) {
        // This is intentionally empty
    }

    @Override
    public boolean moveDown(Activity activity, int selectedPosition) {
        try {
            //Check already at last
            if (selectedPosition == infoData.size() - 1)
                return false;
            Collections.swap(infoData, selectedPosition, selectedPosition + 1);
            adapter.notifyDataSetChanged();
            return true;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean moveUp(Activity activity, int selectedPosition) {
        try {
            //Check already at top
            if (selectedPosition == 0)
                return false;
            Collections.swap(infoData, selectedPosition, selectedPosition - 1);
            adapter.notifyDataSetChanged();
            return true;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @brief Toggles the visibility of empty text if Array has zero elements
     */
    private void setEmptyView(Activity activity) {
        if (infoData.size() < 1) {
            activity.findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.empty).setVisibility(View.GONE);
        }
    }
}