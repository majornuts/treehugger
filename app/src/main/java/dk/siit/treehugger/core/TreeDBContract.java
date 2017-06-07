package dk.siit.treehugger.core;

import android.provider.BaseColumns;

public class TreeDBContract {

    public static class TreeEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";

        public static final String COLUMN_NAME_DANSK_NAVN = "dansk_navn";
        public static final String COLUMN_NAME_TRAE_ART = "traeart";
//        public static final String COLUMN_NAME_COORDINATE_LAT = "coordinate_lat";
//        public static final String COLUMN_NAME_COORDINATE_LON = "coordinate_lon";



        public static final String COLUMN_NAME_COORDINATE_LAT = "coordinate_lat";
        public static final String COLUMN_NAME_COORDINATE_LON = "coordinate_lon";






        // Not used
        public static final String COLUMN_NAME_EJER = "ejer";
        public static final String COLUMN_NAME_SAERLIGT_TRAE = "saerligt_trae";
        public static final String COLUMN_NAME_TRAE_HISTORIE = "trae_historie";
        public static final String COLUMN_NAME_PLANTEAAR = "planteaar";
        public static final String COLUMN_NAME_FREDET_BESKYTTET_TRAE = "fredet_beskyttet_trae";
    }
}
