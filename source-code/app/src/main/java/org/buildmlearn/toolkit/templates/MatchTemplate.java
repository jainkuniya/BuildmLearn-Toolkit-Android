package org.buildmlearn.toolkit.templates;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.buildmlearn.toolkit.R;
import org.buildmlearn.toolkit.activity.TemplateEditorInterface;
import org.buildmlearn.toolkit.matchtemplate.fragment.SplashFragment;
import org.buildmlearn.toolkit.model.Template;
import org.buildmlearn.toolkit.model.TemplateInterface;
import org.buildmlearn.toolkit.views.TextViewPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @brief Match template code implementing methods of TemplateInterface
 * <p/>
 * Created by Anupam (opticod) on 16/7/16.
 */
public class MatchTemplate implements TemplateInterface {

    private final ArrayList<MatchMetaModel> metaData;
    transient private MatchAdapter adapter;
    transient private MatchMetaAdapter metaAdapter;
    private ArrayList<MatchModel> matchData;
    private int templateId;

    public MatchTemplate() {
        matchData = new ArrayList<>();
        metaData = new ArrayList<>();
    }

    private static boolean validated(Context context, EditText title, EditText firstListTitle, EditText secondListTitle) {
        if (title == null || firstListTitle == null || secondListTitle == null) {
            return false;
        }

        String titleText = title.getText().toString().trim();
        String firstListTitleText = firstListTitle.getText().toString().trim();
        String secondListTitleText = secondListTitle.getText().toString().trim();

        if ("".equals(titleText)) {
            title.hasFocus();
            title.setError(context.getString(R.string.match_main_title));
            return false;
        } else if ("".equals(firstListTitleText)) {
            firstListTitle.hasFocus();
            firstListTitle.setError(context.getString(R.string.match_first_list_title));
            return false;
        } else if ("".equals(secondListTitleText)) {
            secondListTitle.hasFocus();
            secondListTitle.setError(context.getString(R.string.match_second_list_title));
            return false;
        } else if (firstListTitleText.equalsIgnoreCase(secondListTitleText)){
            Toast.makeText(context, "Title of two lists cannot be same.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private static boolean validated(Context context, EditText firstListTitle, EditText secondListTitle) {
        if (firstListTitle == null || secondListTitle == null) {
            return false;
        }

        String firstListTitleText = firstListTitle.getText().toString().trim();
        String secondListTitleText = secondListTitle.getText().toString().trim();

        if (firstListTitleText.equals("")) {
            firstListTitle.hasFocus();
            firstListTitle.setError(context.getString(R.string.match_first_list_title));
            return false;
        } else if (secondListTitleText.equals("")) {
            secondListTitle.hasFocus();
            secondListTitle.setError(context.getString(R.string.match_second_list_title));
            return false;
        } else if (secondListTitleText.equals(firstListTitleText)){
            Toast.makeText(context, "Two options cannot be same.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public Object newTemplateEditorAdapter(Context context, final TemplateEditorInterface templateEditorInterface) {
        adapter = new MatchAdapter(context, matchData) {
            @Override
            public boolean onLongItemClick(int position, View view) {
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
            protected void populateMetaList(ListView listView) {
                templateEditorInterface.populateMetaList(listView);
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
        return adapter;
    }

    public BaseAdapter newMetaEditorAdapter(Context context) {
        metaAdapter = new MatchMetaAdapter(context, metaData);
        setEmptyView((Activity) context);
        return metaAdapter;
    }

    @Override
    public Object currentTemplateEditorAdapter() {
        return adapter;
    }

    public BaseAdapter currentMetaEditorAdapter() {
        return metaAdapter;
    }

    @Override
    public Object loadProjectTemplateEditor(Context context, ArrayList<Element> data, final TemplateEditorInterface templateEditorInterface) {
        matchData = new ArrayList<>();
        for (Element item : data) {
            String first_list_item = item.getElementsByTagName("first_list_item").item(0).getTextContent();
            String second_list_item = item.getElementsByTagName("second_list_item").item(0).getTextContent();

            matchData.add(new MatchModel(first_list_item, second_list_item));

        }
        adapter = new MatchAdapter(context, matchData) {
            @Override
            public boolean onLongItemClick(int position, View view) {
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
            protected void populateMetaList(ListView listView) {
                templateEditorInterface.populateMetaList(listView);
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
        return adapter;
    }

    public BaseAdapter loadProjectMetaEditor(Context context, Document doc) {

        String title = doc.getElementsByTagName(MatchMetaModel.TITLE_TAG).item(0).getTextContent();
        String firstListTitle = doc.getElementsByTagName(MatchMetaModel.FIRST_TITLE_TAG).item(0).getTextContent();
        String secondListTitle = doc.getElementsByTagName(MatchMetaModel.SECOND_TITLE_TAG).item(0).getTextContent();
        metaData.add(new MatchMetaModel(title, firstListTitle, secondListTitle));
        metaAdapter = new MatchMetaAdapter(context, metaData);
        setEmptyView((Activity) context);

        return metaAdapter;

    }

    @Override
    public String getTitle() {
        return "Match Template";
    }

    @Override
    public void addItem(final Activity activity) {
        View dialogView = View.inflate(activity,R.layout.match_dialog_add_edit, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.match_dialog_add_title)
                .setView(dialogView,
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_left),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_top),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_right),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_bottom))
                .setPositiveButton(R.string.quiz_add, null)
                .setNegativeButton(R.string.quiz_cancel, null)
                .create();
        dialog.show();

        final EditText first_list_item = (EditText) dialogView.findViewById(R.id.first_list_item);
        final EditText second_list_item = (EditText) dialogView.findViewById(R.id.second_list_item);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validated(activity, first_list_item, second_list_item)) {
                    String first_list_itemText = first_list_item.getText().toString().trim();
                    String second_list_itemText = second_list_item.getText().toString().trim();

                    if(matchData.size() > 0) {
                        for (int i = 0; i < matchData.size(); i++) {
                            if (first_list_itemText.equals(matchData.get(i).getMatchA()) || first_list_itemText.equals(matchData.get(i).getMatchB())) {
                                first_list_item.requestFocus();
                                first_list_item.setError("Option already inserted in list");
                                return;
                            }
                            if (second_list_itemText.equals(matchData.get(i).getMatchA()) || second_list_itemText.equals(matchData.get(i).getMatchB())) {
                                second_list_item.requestFocus();
                                second_list_item.setError("Option already inserted in list");
                                return;
                            }
                        }
                    }

                    MatchModel temp = new MatchModel(first_list_itemText, second_list_itemText);
                    matchData.add(temp);
                    adapter.notifyDataSetChanged();
                    setEmptyView(activity);
                    dialog.dismiss();
                }

            }
        });
    }

    @Override
    public void addMetaData(final Activity activity) {
        View dialogView = View.inflate(activity,R.layout.match_meta_dialog_add_edit_data, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.comprehension_add_meta_title)
                .setView(dialogView,
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_left),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_top),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_right),
                        activity.getResources().getDimensionPixelSize(R.dimen.spacing_bottom))
                .setPositiveButton(R.string.info_template_add, null)
                .setNegativeButton(R.string.info_template_cancel, null)
                .create();
        dialog.show();

        final EditText title = (EditText) dialogView.findViewById(R.id.meta_title);
        final EditText firstListTitle = (EditText) dialogView.findViewById(R.id.meta_first_list_title);
        final EditText secondListTitle = (EditText) dialogView.findViewById(R.id.meta_second_list_title);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validated(activity, title, firstListTitle, secondListTitle)) {

                    String titleText = title.getText().toString().trim();
                    String firstListTitleText = firstListTitle.getText().toString().trim();
                    String secondListTitleText = secondListTitle.getText().toString().trim();
                    MatchMetaModel temp = new MatchMetaModel(titleText, firstListTitleText, secondListTitleText);
                    metaData.add(temp);
                    setEmptyView(activity);
                    metaAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void editItem(final Activity activity, final int position) {
        if (position == -2) {
            View dialogView = View.inflate(activity,R.layout.match_meta_dialog_add_edit_data, null);
            final AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.comprehension_edit_meta_title)
                    .setView(dialogView,
                            activity.getResources().getDimensionPixelSize(R.dimen.spacing_left),
                            activity.getResources().getDimensionPixelSize(R.dimen.spacing_top),
                            activity.getResources().getDimensionPixelSize(R.dimen.spacing_right),
                            activity.getResources().getDimensionPixelSize(R.dimen.spacing_bottom))
                    .setPositiveButton(R.string.info_template_ok, null)
                    .setNegativeButton(R.string.info_template_cancel, null)
                    .create();
            dialog.show();

            final MatchMetaModel data = metaData.get(0);

            final EditText title = (EditText) dialogView.findViewById(R.id.meta_title);
            final EditText firstListTitle = (EditText) dialogView.findViewById(R.id.meta_first_list_title);
            final EditText secondListTitle = (EditText) dialogView.findViewById(R.id.meta_second_list_title);

            title.setText(data.getTitle().trim());
            firstListTitle.setText(data.getFirstListTitle().trim());
            secondListTitle.setText(data.getSecondListTitle().trim());

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (validated(activity, title, firstListTitle, secondListTitle)) {

                        String titleText = title.getText().toString().trim();
                        String firstListTitleText = firstListTitle.getText().toString().trim();
                        String secondListTitleText = secondListTitle.getText().toString().trim();

                        data.setTitle(titleText);
                        data.setFirstListTitle(firstListTitleText);
                        data.setSecond_list_title(secondListTitleText);
                        metaAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });

        } else {

            final MatchModel data = matchData.get(position);

            View dialogView = View.inflate(activity,R.layout.match_dialog_add_edit, null);
            final AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.match_dialog_edit_title)
                    .setView(dialogView,
                            activity.getResources().getDimensionPixelSize(R.dimen.spacing_left),
                            activity.getResources().getDimensionPixelSize(R.dimen.spacing_top),
                            activity.getResources().getDimensionPixelSize(R.dimen.spacing_right),
                            activity.getResources().getDimensionPixelSize(R.dimen.spacing_bottom))
                    .setPositiveButton(R.string.info_template_ok, null)
                    .setNegativeButton(R.string.info_template_cancel, null)
                    .create();
            dialog.show();

            final EditText first_list_item = (EditText) dialogView.findViewById(R.id.first_list_item);
            final EditText second_list_item = (EditText) dialogView.findViewById(R.id.second_list_item);

            first_list_item.setText(data.getMatchA().trim());
            second_list_item.setText(data.getMatchB().trim());

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (validated(activity, first_list_item, second_list_item)) {

                        String first_list_itemText = first_list_item.getText().toString().trim();
                        String second_list_itemText = second_list_item.getText().toString().trim();

                        if (matchData.size() > 0){
                            for(int i=0;i<matchData.size();i++){
                                if(i == position)
                                    continue;
                                if (first_list_itemText.equals(matchData.get(i).getMatchA()) || first_list_itemText.equals(matchData.get(i).getMatchB())) {
                                    first_list_item.requestFocus();
                                    first_list_item.setError("Option already inserted in the list");
                                    return;
                                }
                                if (second_list_itemText.equals(matchData.get(i).getMatchA()) || second_list_itemText.equals(matchData.get(i).getMatchB())) {
                                    second_list_item.requestFocus();
                                    second_list_item.setError("Option already inserted in the list");
                                    return;
                                }
                            }
                        }

                        data.setMatchA(first_list_itemText);
                        data.setMatchB(second_list_itemText);

                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public Object deleteItem(Activity activity, int position) {
        MatchMetaModel matchMetaModel = null;
        MatchModel matchModel = null;
        if (position == -2) {
            matchMetaModel = metaData.get(0);
            metaData.remove(0);
            metaAdapter.notifyDataSetChanged();
        } else {
            matchModel = matchData.get(position);
            matchData.remove(position);
        }
        adapter.notifyDataSetChanged();
        setEmptyView(activity);
        if (matchMetaModel == null) {
            return matchModel;
        } else {
            return matchMetaModel;
        }
    }

    @Override
    public void restoreItem(Activity activity, int position, Object object) {
        if (position == -2) {
            if (object instanceof MatchMetaModel) {
                MatchMetaModel matchMetaModel = (MatchMetaModel) object;
                if (matchMetaModel != null) {
                    metaData.add(matchMetaModel);
                    metaAdapter.notifyDataSetChanged();
                }
            }
        } else {
            if (object instanceof MatchModel) {
                MatchModel matchModel = (MatchModel) object;
                if (matchModel != null) {
                    matchData.add(position, matchModel);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        setEmptyView(activity);
    }

    @Override
    public ArrayList<Element> getItems(Document doc) {
        ArrayList<Element> itemElements = new ArrayList<>();

        for (MatchMetaModel data : metaData) {
            itemElements.add(data.getXml(doc));
        }

        for (MatchModel data : matchData) {

            itemElements.add(data.getXml(doc));
        }

        return itemElements;
    }

    @Override
    public android.support.v4.app.Fragment getSimulatorFragment(String filePathWithName) {
        return SplashFragment.newInstance(filePathWithName);
    }

    @Override
    public void setTemplateId(int templateId) {
        this.templateId = templateId;
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
        return "MatchApp.apk";
    }

    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent intent) {
        // This is intentionally empty
    }

    @Override
    public boolean moveDown(Activity activity, int selectedPosition) {
        //Check already at last
        if (selectedPosition == matchData.size() - 1)
            return false;
        Collections.swap(matchData, selectedPosition, selectedPosition + 1);
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean moveUp(Activity activity, int selectedPosition) {
        //Check already at top
        if (selectedPosition == 0)
            return false;
        Collections.swap(matchData, selectedPosition, selectedPosition - 1);
        adapter.notifyDataSetChanged();
        return true;
    }

    /**
     * @brief Toggles the visibility of empty text if Array has zero elements
     */
    private void setEmptyView(Activity activity) {
        try {
            if (matchData.size() < 1 && metaData.size() < 1) {
                ((TextViewPlus) activity.findViewById(R.id.empty_view_text)).setText(R.string.meta_add_help);
                activity.findViewById(R.id.empty).setVisibility(View.VISIBLE);
            } else if (matchData.size() < 1) {
                ((TextViewPlus) activity.findViewById(R.id.empty_view_text)).setText(R.string.add_item_help);
                activity.findViewById(R.id.empty).setVisibility(View.VISIBLE);
            } else if (metaData.size() < 1) {
                ((TextViewPlus) activity.findViewById(R.id.empty_view_text)).setText(R.string.meta_add_help);
                activity.findViewById(R.id.empty).setVisibility(View.VISIBLE);
            } else {
                activity.findViewById(R.id.empty).setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
