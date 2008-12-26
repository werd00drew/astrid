package com.timsu.astrid.data.tag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.timsu.astrid.data.AbstractController;
import com.timsu.astrid.data.AbstractModel;
import com.timsu.astrid.data.task.TaskIdentifier;


/** A single tag on a task
 *
 * @author timsu
 *
 */
public class TagToTaskMapping extends AbstractModel {

    /** Version number of this model */
    static final int                   VERSION             = 2;

    // field names

    static final String                TASK                = "task";
    static final String                TAG                 = "tag";

    /** Default values container */
    private static final ContentValues defaultValues       = new ContentValues();

    @Override
    public ContentValues getDefaultValues() {
        return defaultValues;
    }

    static String[] FIELD_LIST = new String[] {
        AbstractController.KEY_ROWID,
        TASK,
        TAG,
    };

    // --- database helper

    /** Database Helper manages creating new tables and updating old ones */
    static class TagToTaskMappingDatabaseHelper extends SQLiteOpenHelper {
        String tableName;

        TagToTaskMappingDatabaseHelper(Context context, String databaseName, String tableName) {
            super(context, databaseName, null, VERSION);
            this.tableName = tableName;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = new StringBuilder().
            append("CREATE TABLE ").append(tableName).append(" (").
                append(AbstractController.KEY_ROWID).append(" integer primary key autoincrement, ").
                append(TASK).append(" integer not null,").
                append(TAG).append(" integer not null,").
                append("unique (").append(TASK).append(",").append(TAG).append(")").
            append(");").toString();
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(getClass().getSimpleName(), "Upgrading database from version " +
                    oldVersion + " to " + newVersion + ".");

            switch(oldVersion) {
            default:
                // we don't know how to handle it... do the unfortunate thing
                Log.e(getClass().getSimpleName(), "Unsupported migration, table dropped!");
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
                onCreate(db);
            }
        }
    }


    // --- constructor pass-through

    TagToTaskMapping(TaskIdentifier task, TagIdentifier tag) {
        super();
        setTask(task);
        setTag(tag);
    }

    TagToTaskMapping(Cursor cursor) {
        super(cursor);
    }

    // --- getters and setters: expose them as you see fit

    public boolean isNew() {
        return getCursor() == null;
    }

    public TaskIdentifier getTask() {
        return new TaskIdentifier(retrieveInteger(TASK));
    }

    public TagIdentifier getTag() {
        return new TagIdentifier(retrieveInteger(TAG));
    }

    private void setTask(TaskIdentifier task) {
        setValues.put(TASK, task.getId());
    }

    private void setTag(TagIdentifier tag) {
        setValues.put(TAG, tag.getId());
    }
}