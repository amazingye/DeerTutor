package com.ye.deertutor.Activities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Parent;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ParentModifyActivity extends Activity {

    public EditText parentUsernameEdit;
    public Button parentHeadiconEdit;
    public EditText childSexEdit;
    public EditText childGradeEdit;
    public EditText addressEdit;

    public String parentUsername;
    public File headIcon;
    public BmobFile parentHeadIcon;
    public String childSex;
    public String childGrade;
    public String address;

    /*public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;*/

    public Uri imageUri;

    public Button parentModifySaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_modify);

        parentHeadiconEdit = (Button) findViewById(R.id.parentheadiconedit);
        parentHeadiconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseHeadicon();
            }
        });

        parentUsernameEdit = (EditText) findViewById(R.id.parentusernameedit);
        childSexEdit = (EditText) findViewById(R.id.childsexeedit);
        childGradeEdit = (EditText) findViewById(R.id.childgradeedit);
        addressEdit = (EditText) findViewById(R.id.addressedit);

        parentModifySaveButton = (Button) findViewById(R.id.parentmodifysave);
        parentModifySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveModify();
            }
        });
    }

    public void saveModify() {
        parentUsername = parentUsernameEdit.getText().toString();
        childSex = childSexEdit.getText().toString();
        childGrade = childGradeEdit.getText().toString();
        address = addressEdit.getText().toString();

        DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);
        currentUser.setUsername(parentUsername);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "用户名更改成功");
                }
            }
        });
        BmobQuery<Parent> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", currentUser);
        query.findObjects(new FindListener<Parent>() {
            @Override
            public void done(List<Parent> list, BmobException e) {
                if (e == null) {
                    for (Parent parent2 : list) {
                        updateParent(parent2);
                    }
                } else {
                    Log.i("bmob", e.getMessage() + "" + e.getErrorCode());
                }
            }
        });
    }

    public void updateParent(Parent parent) {
        parent.setSex(childSex);
        parent.setGrade(childGrade);
        parent.setAddress(address);
        parent.update(parent.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "更新成功");
                    Toast.makeText(ParentModifyActivity.this,
                            "修改资料成功", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    public void chooseHeadicon() {
        headIcon = new File(Environment.getExternalStorageDirectory(),
                "headicon.jpg");
        try {
            if (headIcon.exists()) {
                headIcon.delete();
            }
            headIcon.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(headIcon);

        /*Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image*//*");
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent,SELECT_PIC_KITKAT);
        } else {
            startActivityForResult(intent,IMAGE_REQUEST_CODE);
        }*/

        /*Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image*//*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,TAKE_PHOTO);*/

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,2);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            Uri uri = data.getData();
            String path = getPath(this,uri);
            Log.i("tag",path);
            parentHeadIcon = new BmobFile(new File(path));
            parentHeadIcon.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.i("bmob","上传文件成功:"+parentHeadIcon.getFileUrl());
                    }else{
                        Log.i("bmob","上传文件失败：" + e.getMessage());
                    }
                }

                @Override
                public void onProgress(Integer value) {
                    Log.i("progress",value.toString());
                }
            });
        }

    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}

