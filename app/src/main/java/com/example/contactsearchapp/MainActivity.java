package com.example.contactsearchapp;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_READ_CONTACTS = 79;
    RecyclerView recyclercontacts;
    AppCompatEditText edtsearch;
    ArrayList mobileArray;
    ArrayList<ContactsModel> contactsModelArrayList = new ArrayList<>();

    ContactsAdapter contactsAdapter;
    int pagedata = 50;
    int limit = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtsearch = findViewById(R.id.edtsearch);
        Contacts.initialize(this);
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edtsearch.setCursorVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contactsModelArrayList = new ArrayList<>();
                edtsearch.setCursorVisible(true);
                String text = s.toString();
                pagedata = 50;
                limit = 0;
                if (text.length() <= 0) {
                    getAllContacts();

                } else if (text.length() >= 3) {
                    getAllSearchedContacts(text);
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            mobileArray = getAllContacts();
        } else {
            requestPermission();
        }

    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mobileArray = getAllContacts();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private ArrayList getAllSearchedContacts(String text) {
        ArrayList<String> nameList = new ArrayList<>();
        contactsModelArrayList = new ArrayList<>();

        recyclercontacts = findViewById(R.id.recyclercontacts);
        recyclercontacts.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));
        Query mainQuery = Contacts.getQuery();
        Query q1 = Contacts.getQuery();
        q1.whereContains(Contact.Field.DisplayName, text);
        Query q2 = Contacts.getQuery();
        q2.whereContains(Contact.Field.PhoneNormalizedNumber, text);
        Query q3 = Contacts.getQuery();
        q3.whereContains(Contact.Field.Email, text);
        Query q4 = Contacts.getQuery();
        q4.whereContains(Contact.Field.Note, text);
        Query q5 = Contacts.getQuery();
        q5.whereContains(Contact.Field.CompanyName, text);
        Query q6 = Contacts.getQuery();
        q6.whereContains(Contact.Field.Address, text);
        List<Query> qs = new ArrayList<>();
        qs.add(q1);
        qs.add(q2);
        qs.add(q3);
        qs.add(q4);
        qs.add(q5);
        qs.add(q6);

        mainQuery.or(qs);
        List<Contact> contacts = mainQuery.find();
        for (int i = 0; i < contacts.size(); i++) {
            try {
                ContactsModel contactsModel = new ContactsModel();
                Contact contact = contacts.get(i);
                contactsModel.setName(contact.getDisplayName());
                if (contact.getPhoneNumbers().size() > 0) {
                    contactsModel.setNumber(contact.getPhoneNumbers().get(0).getNumber());
                }
                if (contact.getEmails().size() > 0) {
                    contactsModel.setEmail(contact.getEmails().get(0).getAddress());
                }
                contactsModel.setOrganization(contact.getCompanyName());
                contactsModel.setImage(contact.getPhotoUri());
                contactsModel.setId(contact.getId().toString());
                contactsModel.setOther(contact.getNote());
                contactsModelArrayList.add(contactsModel);
            } catch (Exception e) {

                e.printStackTrace();
                continue;
            }

        }


        //******************* Raw Code to search contacts*****************
//        ContentResolver cr = getContentResolver();
//        String whereString = "display_name LIKE ? ";
//        String[] whereParams = new String[]{"%" + text + "%"};
//
//        Log.d("safiyas search", " ASC" + " LIMIT " + limit + ", " + pagedata);
//        Cursor cur = cr.query(ContactsContract.Data.CONTENT_URI,
//                null,
//                whereString,
//                whereParams,
//                " ASC" + " LIMIT " + limit + ", " + pagedata);
//        Log.d("safiyas searchcur", cur.toString());
//        if ((cur != null ? cur.getCount() : 0) > 0) {
//            while (cur != null && cur.moveToNext()) {
//                id = cur.getString(
//                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
//
//                nameList.add(name);
//                ContactsModel contactsModel = new ContactsModel();
//                contactsModel.setName(name);
//                contactsModel.setId(id);
//                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0 && tempid!=id) {
//                    Cursor pCur = cr.query(
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//
//                            new String[]{id}, null);
//                    while (pCur.moveToNext()) {
//                        tempid=id;
//                        mobilenumber = pCur.getString(pCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        email = pCur.getString(pCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Email.DATA));
//                        organization = pCur.getString(pCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Organization.DATA));
//                        image = pCur.getString(pCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//                        other = pCur.getString(pCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Website.DATA));
//                        contactsModel.setNumber(mobilenumber);
//                        contactsModel.setEmail(email);
//                        contactsModel.setOrganization(organization);
//                        contactsModel.setImage(image);
//                        contactsModel.setOther(other);
//                    }
//                    pCur.close();
//                }
//                contactsModelArrayList.add(contactsModel);
//                HashSet<ContactsModel> hashSet = new HashSet<ContactsModel>();
//                hashSet.addAll(contactsModelArrayList);
//                contactsModelArrayList.clear();
//                contactsModelArrayList.addAll(hashSet);
//                Log.d("safiyas search", contactsModelArrayList.toString());
//            }
//        }
//        if (cur != null) {
//            cur.close();
//        }
//
        contactsAdapter = new ContactsAdapter(this, contactsModelArrayList) {
            @Override
            public void load() {
            }
        };
        recyclercontacts.setAdapter(contactsAdapter);

        return nameList;
    }

    private ArrayList getAllContacts() {
        String id, name, email, organization, image, other, mobilenumber;
        ArrayList<String> nameList = new ArrayList<>();
        recyclercontacts = findViewById(R.id.recyclercontacts);
        recyclercontacts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Log.d("safiyas", " ASC" + " LIMIT " + limit + ", " + pagedata);
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER,
                        ContactsContract.Contacts._ID}, null, null,
                ContactsContract.Data.DISPLAY_NAME + " ASC" + " LIMIT " + limit + ", " + pagedata);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                nameList.add(name);
                ContactsModel contactsModel = new ContactsModel();
                contactsModel.setName(name);
                contactsModel.setId(id);
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        mobilenumber = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        email = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.DATA));
                        organization = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Organization.DATA));
                        image = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        other = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Website.DATA));
                        contactsModel.setNumber(mobilenumber);
                        contactsModel.setEmail(email);
                        contactsModel.setOrganization(organization);
                        contactsModel.setImage(image);
                        contactsModel.setOther(other);
                    }
                    pCur.close();
                }
                contactsModelArrayList.add(contactsModel);
            }

        }
        if (cur != null) {
            cur.close();
        }
        recyclercontacts.scrollToPosition(pagedata);
        contactsAdapter = new ContactsAdapter(this, contactsModelArrayList) {
            @Override
            public void load() {
                limit = pagedata;
                pagedata += 50;

                getAllContacts();
            }
        };
        recyclercontacts.setAdapter(contactsAdapter);

        return nameList;
    }
}