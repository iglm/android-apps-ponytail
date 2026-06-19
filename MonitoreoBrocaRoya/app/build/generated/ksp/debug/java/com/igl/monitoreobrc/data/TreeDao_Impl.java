package com.igl.monitoreobrc.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TreeDao_Impl implements TreeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TreeRecord> __insertionAdapterOfTreeRecord;

  private final EntityDeletionOrUpdateAdapter<TreeRecord> __deletionAdapterOfTreeRecord;

  public TreeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTreeRecord = new EntityInsertionAdapter<TreeRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trees` (`id`,`plot`,`treeNumber`,`disease`,`severity`,`date`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TreeRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPlot());
        statement.bindLong(3, entity.getTreeNumber());
        statement.bindString(4, entity.getDisease());
        statement.bindString(5, entity.getSeverity());
        statement.bindLong(6, entity.getDate());
        statement.bindString(7, entity.getNotes());
      }
    };
    this.__deletionAdapterOfTreeRecord = new EntityDeletionOrUpdateAdapter<TreeRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `trees` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TreeRecord entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public void insert(final TreeRecord record) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTreeRecord.insert(record);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final TreeRecord record) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTreeRecord.handle(record);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flow<List<TreeRecord>> getAll() {
    final String _sql = "SELECT * FROM trees ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trees"}, new Callable<List<TreeRecord>>() {
      @Override
      @NonNull
      public List<TreeRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlot = CursorUtil.getColumnIndexOrThrow(_cursor, "plot");
          final int _cursorIndexOfTreeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "treeNumber");
          final int _cursorIndexOfDisease = CursorUtil.getColumnIndexOrThrow(_cursor, "disease");
          final int _cursorIndexOfSeverity = CursorUtil.getColumnIndexOrThrow(_cursor, "severity");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<TreeRecord> _result = new ArrayList<TreeRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TreeRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPlot;
            _tmpPlot = _cursor.getString(_cursorIndexOfPlot);
            final int _tmpTreeNumber;
            _tmpTreeNumber = _cursor.getInt(_cursorIndexOfTreeNumber);
            final String _tmpDisease;
            _tmpDisease = _cursor.getString(_cursorIndexOfDisease);
            final String _tmpSeverity;
            _tmpSeverity = _cursor.getString(_cursorIndexOfSeverity);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new TreeRecord(_tmpId,_tmpPlot,_tmpTreeNumber,_tmpDisease,_tmpSeverity,_tmpDate,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TreeRecord>> getDiseased() {
    final String _sql = "SELECT * FROM trees WHERE disease != 'ninguna' ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trees"}, new Callable<List<TreeRecord>>() {
      @Override
      @NonNull
      public List<TreeRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlot = CursorUtil.getColumnIndexOrThrow(_cursor, "plot");
          final int _cursorIndexOfTreeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "treeNumber");
          final int _cursorIndexOfDisease = CursorUtil.getColumnIndexOrThrow(_cursor, "disease");
          final int _cursorIndexOfSeverity = CursorUtil.getColumnIndexOrThrow(_cursor, "severity");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<TreeRecord> _result = new ArrayList<TreeRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TreeRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPlot;
            _tmpPlot = _cursor.getString(_cursorIndexOfPlot);
            final int _tmpTreeNumber;
            _tmpTreeNumber = _cursor.getInt(_cursorIndexOfTreeNumber);
            final String _tmpDisease;
            _tmpDisease = _cursor.getString(_cursorIndexOfDisease);
            final String _tmpSeverity;
            _tmpSeverity = _cursor.getString(_cursorIndexOfSeverity);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new TreeRecord(_tmpId,_tmpPlot,_tmpTreeNumber,_tmpDisease,_tmpSeverity,_tmpDate,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> countByDisease(final String disease) {
    final String _sql = "SELECT COUNT(*) FROM trees WHERE disease = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, disease);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trees"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
