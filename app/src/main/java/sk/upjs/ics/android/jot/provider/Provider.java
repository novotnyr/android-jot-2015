package sk.upjs.ics.android.jot.provider;

import android.provider.BaseColumns;

public interface Provider {
    public interface Note extends BaseColumns {
        public static final String TABLE_NAME = "note";

        public static final String DESCRIPTION = "description";

        public static final String TIMESTAMP = "timestamp";
    }
}
