package asvirido.student.com.ft_hangouts;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;

public class ContactManager {

    private ContentResolver contentResolver;

    ContactManager(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /*  Get raw contact id by contact given name and family name.
    *   Return raw contact id.
    **/
    public long getRawContactIdByName(String givenName, ContentResolver contentResolver) {
        String          whereClause;
        Uri rawContactUri;
        Cursor cursor;
        long            rawContactId;
        int             queryResultCount;

        String queryColumnArr[] = { ContactsContract.RawContacts._ID };
        whereClause = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " = '" + givenName + "'";
        rawContactUri = ContactsContract.RawContacts.CONTENT_URI;
        cursor = contentResolver.query(rawContactUri, queryColumnArr, whereClause, null, null);
        rawContactId = -1;
        if (cursor != null) {
            queryResultCount = cursor.getCount();
            if (queryResultCount > 0) {
                cursor.moveToFirst();
                rawContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
            }
        }
        return (rawContactId);
    }

    public boolean checkPermissionWrite(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("onAddContact", "don't have permissions");
            return (false);
        }
        return (true);
    }
    public void deleteContact(String name) {
        long rawContactId = getRawContactIdByName(name, this.contentResolver);
        Uri dataContentUri = ContactsContract.Data.CONTENT_URI;
        StringBuffer dataWhereClauseBuf = new StringBuffer();
        dataWhereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        dataWhereClauseBuf.append(" = ");
        dataWhereClauseBuf.append(rawContactId);
        contentResolver.delete(dataContentUri, dataWhereClauseBuf.toString(), null);
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;
        StringBuffer rawContactWhereClause = new StringBuffer();
        rawContactWhereClause.append(ContactsContract.RawContacts._ID);
        rawContactWhereClause.append(" = ");
        rawContactWhereClause.append(rawContactId);
        contentResolver.delete(rawContactUri, rawContactWhereClause.toString(), null);
        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        StringBuffer contactWhereClause = new StringBuffer();
        contactWhereClause.append(ContactsContract.Contacts._ID);
        contactWhereClause.append(" = ");
        contactWhereClause.append(rawContactId);
        contentResolver.delete(contactUri, contactWhereClause.toString(), null);
    }

    public boolean addContact(String name, String phoneNumber) {
        if ((phoneNumber.matches("[0-9_+]+")) == false) {
            return false;
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            this.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            Log.d("add", e.getMessage());
            return (false);
        } catch (OperationApplicationException e) {
            Log.d("add", e.getMessage());
            return (false);
        }
        return (true);
    }
}
