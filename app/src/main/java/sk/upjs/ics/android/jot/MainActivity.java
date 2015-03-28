package sk.upjs.ics.android.jot;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import sk.upjs.ics.android.jot.provider.Provider;
import sk.upjs.ics.android.jot.provider.NoteContentProvider;
import sk.upjs.ics.android.util.OnEnterPressedEditorActionListener;

import static sk.upjs.ics.android.util.Defaults.NO_COOKIE;
import static sk.upjs.ics.android.util.Defaults.NO_CURSOR;
import static sk.upjs.ics.android.util.Defaults.NO_FLAGS;
import static sk.upjs.ics.android.util.Defaults.NO_SELECTION;
import static sk.upjs.ics.android.util.Defaults.NO_SELECTION_ARGS;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final int NOTES_LOADER_ID = 0;
    private static final int INSERT_NOTE_TOKEN = 0;
    private static final int DELETE_NOTE_TOKEN = 0;

    private GridView notesGridView;
    private SimpleCursorAdapter adapter;
    private EditText newNoteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(NOTES_LOADER_ID, Bundle.EMPTY, this);

        notesGridView = (GridView) findViewById(R.id.notesGridView);
        notesGridView.setAdapter(initializeAdapter());
        notesGridView.setOnItemClickListener(this);

        newNoteEditText = (EditText) findViewById(R.id.newNoteTextView);
        newNoteEditText.setOnEditorActionListener(new OnEnterPressedEditorActionListener() {
            @Override
            protected void onEnterPressed(TextView textView) {
                onNoteAdded();
            }
        });
    }

    private void onNoteAdded() {
        String noteDescription = newNoteEditText.getText().toString();
        ContentValues values = new ContentValues();
        values.put(Provider.Note.DESCRIPTION, noteDescription);

        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(MainActivity.this, "Note was saved", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        Uri uri = NoteContentProvider.CONTENT_URI;

        insertHandler.startInsert(INSERT_NOTE_TOKEN, NO_COOKIE, uri, values);
    }

    private ListAdapter initializeAdapter() {
        String[] from = {Provider.Note.DESCRIPTION };
        int[] to = {R.id.notesGridViewItem};
        this.adapter = new SimpleCursorAdapter(this, R.layout.note, NO_CURSOR, from, to, NO_FLAGS);
        return this.adapter;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(NoteContentProvider.CONTENT_URI);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.adapter.swapCursor(NO_CURSOR);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        Cursor selectedNoteCursor = (Cursor) parent.getItemAtPosition(position);
        int descriptionColumnIndex = selectedNoteCursor.getColumnIndex(Provider.Note.DESCRIPTION);
        String noteDescription = selectedNoteCursor.getString(descriptionColumnIndex);

        new AlertDialog.Builder(this)
                .setMessage(noteDescription)
                .setTitle("Note")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(id);
                    }
                })
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing, dismiss the dialog
                    }
                })
                .show();

    }

    private void deleteNote(long id) {
        AsyncQueryHandler deleteHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                Toast.makeText(MainActivity.this, "Note was deleted", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        Uri selectedNoteUri = ContentUris.withAppendedId(NoteContentProvider.CONTENT_URI, id);
        deleteHandler.startDelete(DELETE_NOTE_TOKEN, NO_COOKIE, selectedNoteUri,
                NO_SELECTION, NO_SELECTION_ARGS);
    }
}
