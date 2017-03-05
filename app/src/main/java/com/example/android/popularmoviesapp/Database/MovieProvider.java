package com.example.android.popularmoviesapp.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by samuel on 3/5/2017.
 */

public class MovieProvider extends ContentProvider {
    static final int MOVIE = 100;
    static final int MOVIE_WITH_REVIEW_AND_TRAILER = 101;
    static final int TRAILER = 200;
    static final int TRAILER_WITH_MOVIEID = 201;
    static final int REVIEW = 300;
    static final int REVIEW_WITH_MOVIEID = 301;
    public static final UriMatcher sUriMatcher = buildUriMatcher() ;
    MovieDbHelper mOpenHelper ;

    private static final SQLiteQueryBuilder sMovieWithReviewAndTrailer;
    static {
        sMovieWithReviewAndTrailer = new SQLiteQueryBuilder();
        sMovieWithReviewAndTrailer.setTables(MovieContract.MovieEntry.TABLE_NAME+
                " INNER JOIN "+ MovieContract.ReviewEntry.TABLE_NAME +" ON "+ MovieContract.MovieEntry.TABLE_NAME+
                "."+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+" = "+ MovieContract.ReviewEntry.TABLE_NAME+"."+
                MovieContract.ReviewEntry.COLUMN_MOVIE_ID+" INNER JOIN "+ MovieContract.TrailerEntry.TABLE_NAME+
                " ON "+ MovieContract.MovieEntry.TABLE_NAME+"."+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+" = "+
                MovieContract.TrailerEntry.TABLE_NAME+"."+ MovieContract.TrailerEntry.COLUMN_MOVIE_ID);
    }

    private static final SQLiteQueryBuilder sMovieList;
    static {
        sMovieList = new SQLiteQueryBuilder();
        sMovieList.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }
    private static final SQLiteQueryBuilder sMovieTrailer ;
    static {
        sMovieTrailer = new SQLiteQueryBuilder();
        sMovieTrailer.setTables(MovieContract.TrailerEntry.TABLE_NAME);
    }
    private static final SQLiteQueryBuilder sMovieReview;
    static {
        sMovieReview = new SQLiteQueryBuilder();
        sMovieReview.setTables(MovieContract.ReviewEntry.TABLE_NAME);
    }
    private static final String sMovieWithIdSelection = MovieContract.MovieEntry.TABLE_NAME+
            "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
    private static final String sTrailerWithIdSelection = MovieContract.TrailerEntry.TABLE_NAME+
            "."+ MovieContract.TrailerEntry.COLUMN_MOVIE_ID+ " = ? ";
    private static final String sReviewWithIdSelection = MovieContract.ReviewEntry.TABLE_NAME+
            "."+ MovieContract.ReviewEntry.COLUMN_MOVIE_ID+ " = ? ";
    private Cursor getMovieWithTrailers(Uri uri , String[] projection , String sortOrder){
        String id = uri.getPathSegments().get(1);
        String selectionArgs[];
        String selection;
        selection = sTrailerWithIdSelection;
        selectionArgs = new String[]{id};
        return sMovieTrailer.query(mOpenHelper.getReadableDatabase() ,
                projection,
                selection,
                selectionArgs,
                null ,
                null,
                null);
    }
    private Cursor getMovieWithReviews (Uri uri , String[] projection, String sortOrder){
        String id = uri.getPathSegments().get(1);
        String selectionArgs[];
        String selection;
        selection = sReviewWithIdSelection;
        selectionArgs = new String[]{id};
        return sMovieReview.query(mOpenHelper.getReadableDatabase() ,
                projection,
                selection,
                selectionArgs,
                null ,
                null,
                null);
    }
    private Cursor getMovieWithReviewsAndTrailers(Uri uri , String[] projection , String sortOrder){
        String id = uri.getPathSegments().get(1);
        String selectionArgs[];
        String selection;
        selection = sMovieWithIdSelection;
        selectionArgs = new String[]{id};
        return sMovieWithReviewAndTrailer.query(mOpenHelper.getReadableDatabase() ,
                projection,
                selection,
                selectionArgs,
                null ,
                null,
                null);
    }
    private Cursor getBasicMovieList(Uri uri , String[] projection , String sortOrder){
        // String id = uri.getPathSegments().get(1);
        String selectionArgs[];
        String selection;
        selection =null;
        selectionArgs = null;
        return sMovieList.query(mOpenHelper.getReadableDatabase() ,
                projection,
                selection,
                selectionArgs,
                null ,
                null,
                null);
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
            {
                retCursor = getBasicMovieList(uri, projection, sortOrder);
                break;
            }
            // "weather/*"
            case MOVIE_WITH_REVIEW_AND_TRAILER: {
                retCursor = getMovieWithReviewsAndTrailers(uri, projection, sortOrder);
                break;
            }
            case TRAILER_WITH_MOVIEID: {
                retCursor = getMovieWithTrailers(uri , projection , sortOrder);
                break;
            }
            case REVIEW_WITH_MOVIEID: {

                retCursor = getMovieWithReviews(uri , projection , sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case MOVIE :
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case REVIEW :
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case MOVIE_WITH_REVIEW_AND_TRAILER:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            default:throw new UnsupportedOperationException(uri.toString());
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db =mOpenHelper.getWritableDatabase();
        Log.e("DB IS OPEn" , String.valueOf(db.isOpen()));
        Uri retUri = null;
        final int match =sUriMatcher.match(uri);
        try{
            switch (match) {
                case MOVIE: {
                    long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                    if (_id > 0)
                        retUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, _id);
                    else
                        throw new SQLiteConstraintException("Movie Already Exist");
                    break;
                }
                case TRAILER: {
                    long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, contentValues);
                    if (_id > 0)
                        retUri = ContentUris.withAppendedId(MovieContract.TrailerEntry.CONTENT_URI, _id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }
                case REVIEW: {
                    long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, contentValues);
                    if (_id > 0)
                        retUri = ContentUris.withAppendedId(MovieContract.ReviewEntry.CONTENT_URI, _id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
            getContext().getContentResolver().notifyChange(uri, null);
            db.close();}
        catch(SQLiteConstraintException e){
            Toast.makeText(getContext() , "Movie Already On Your Favorites List" , Toast.LENGTH_LONG);

        }
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] strings) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deletedRows ;
        switch (match) {
            case MOVIE: {
                deletedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, strings);
                break;
            }
            case TRAILER: {
                deletedRows = db.delete(MovieContract.TrailerEntry.TABLE_NAME, selection, strings);
                break;
            }
            case REVIEW :{
                deletedRows = db.delete(MovieContract.ReviewEntry.TABLE_NAME , selection , strings);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri , null);
        return  deletedRows;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
    public int bulkInsert(Uri uri , ContentValues[] contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:{
                db.beginTransaction();
                int returnRows = 0;
                for(ContentValues iterateVals : contentValues){
                    long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, iterateVals);
                    if(_id != -1){
                        returnRows++;
                    }
                }
                db.setTransactionSuccessful();
                getContext().getContentResolver().notifyChange(uri, null);
                db.endTransaction();
                return returnRows;}
            case TRAILER:{
                db.beginTransaction();
                int returnRows = 0;
                for(ContentValues iterateVals : contentValues){
                    long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, iterateVals);
                    if(_id != -1){
                        returnRows++;
                    }
                }
                db.setTransactionSuccessful();
                getContext().getContentResolver().notifyChange(uri, null);
                db.endTransaction();
                return returnRows;

            }
            case REVIEW:{
                db.beginTransaction();
                int returnRows = 0;
                for(ContentValues iterateVals : contentValues){
                    long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, iterateVals);
                    if(_id != -1){
                        returnRows++;
                    }
                }
                db.setTransactionSuccessful();
                getContext().getContentResolver().notifyChange(uri, null);
                db.endTransaction();
                return returnRows;

            }
            default: return super.bulkInsert(uri, contentValues);

        }
    }
    public static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY , MovieContract.PATH_MOVIE , MOVIE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY , MovieContract.PATH_MOVIE+"/*" ,MOVIE_WITH_REVIEW_AND_TRAILER );
        matcher.addURI(MovieContract.CONTENT_AUTHORITY , MovieContract.PATH_TRAILER , TRAILER);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY , MovieContract.PATH_REVIEW , REVIEW);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY , MovieContract.PATH_REVIEW+"/*" , REVIEW_WITH_MOVIEID);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY , MovieContract.PATH_TRAILER+"/*" , TRAILER_WITH_MOVIEID);

        return matcher;

    }
}

