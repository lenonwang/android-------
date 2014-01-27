package com.example.gallerytest;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class GalleryTestActivity extends Activity {
	
    final int SELECT_PICTURE 	 = 100; // request code for browsing images
    final int CAMERA_PIC_REQUEST = 200; // request code for taking a new picture
    private Uri browseImageUri;			// image uri for existing images
    private ImageView theImageView;		// imageview defined
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        theImageView = (ImageView) findViewById(R.id.thePicture); // imageview element
        
    }

    public void browseImages(View view) { // onClick method for browsing images
    	Intent browse = new Intent();
    	browse.setType("image/*");
    	browse.setAction(Intent.ACTION_GET_CONTENT);
    	startActivityForResult(Intent.createChooser(browse, "Ñ¡ÔñÍ·Ïñ"), SELECT_PICTURE);    	
    }
    		    
	public void getCamera(View view) { // onClick method for taking a new picture
		Intent getPicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		
		startActivityForResult(getPicture, CAMERA_PIC_REQUEST);
	}
	    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // handling the activity results
    	Log.d("resultCode", String.valueOf(resultCode));
    	if (resultCode != RESULT_OK) return;
		
    	
    	Bitmap selectedImage 	= null;
    	String path				= "";

        	if (requestCode == SELECT_PICTURE) { // actions for select from file (100)
        		Log.d("resultCode", "picture");
        		browseImageUri = data.getData();
        		path = getRealPath(browseImageUri); // if using the gallery, you need to get the actual path
        		
        		if (path == null) // if the path is not supplied, get it now
        			path = browseImageUri.getPath();
        		
        		if (path != null) // getting the image
        			selectedImage = BitmapFactory.decodeFile(path);

        	} else if(requestCode == CAMERA_PIC_REQUEST){ // actions for new image (200)
        		Log.d("resultCode", "cameara");
    		    selectedImage = (Bitmap) data.getExtras().get("data");  
        	}
        	Log.d("resultCode", "final");	
        theImageView.setImageBitmap(selectedImage); // display our image
    }
        
    public String getRealPath(Uri imageUri) { // since the gallery doesn't give us the real path, let's take it
    	String [] imageProj = {MediaStore.Images.Media.DATA}; // the column to return
    	Cursor cursor		= managedQuery(imageUri, imageProj, null, null, null); // query the uri and projection
    	
    	if (cursor == null) return null;
    	
    	int columnIndex 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); // get the column index
    	
    	cursor.moveToFirst(); // move to the first column
    	
    	return cursor.getString(columnIndex); // return 
    }
}