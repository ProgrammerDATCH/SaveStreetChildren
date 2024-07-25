package com.savestreetchildren.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeActivity extends AppCompatActivity {
    ImageView imgNtakomisiyo;
    ImageView errorImg;
    Button errorReflesh;
    TextView errorTxt;
    WebView webView;

    SwipeRefreshLayout mySwipeRefreshLayout;

    private static final int MY_REQUEST_CODE = 107;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeContainer);
        webView = findViewById(R.id.webView);
        imgNtakomisiyo = findViewById(R.id.imgNtakomisiyo);
        errorImg = findViewById(R.id.errorImg);
        errorReflesh = findViewById(R.id.errorRefleshBtn);
        errorTxt = findViewById(R.id.errorTxt);
        errorReflesh.setVisibility(View.GONE);
        errorTxt.setVisibility(View.GONE);
        errorImg.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("https://impressa78.000webhostapp.com/index");

        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);



        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webView.reload();
                    }
                }
        );

        Animation zoomInOut = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInOut.setDuration(1000);
        zoomInOut.setRepeatCount(Animation.INFINITE);
        zoomInOut.setRepeatMode(Animation.REVERSE);
        imgNtakomisiyo.startAnimation(zoomInOut);

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                imgNtakomisiyo.startAnimation(zoomInOut);
                imgNtakomisiyo.setVisibility(View.VISIBLE);

                errorImg.setVisibility(View.GONE);
                errorReflesh.setVisibility(View.GONE);
                errorTxt.setVisibility(View.GONE);
            }




            @Override
            public boolean  shouldOverrideUrlLoading(WebView view, String url) {

                if (url != null && url.startsWith("whatsapp://")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://www.youtube.com")) {
                    Intent ytIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(ytIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://play.google.com/")) {
                    Intent psIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(psIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://m.facebook.com")) {
                    Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(fbIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://twitter")) {
                    Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(twitterIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://www.instagram.com")) {
                    Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(twitterIntent);
                    return true;

                }
                else {
                    return false;
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                imgNtakomisiyo.clearAnimation();
                imgNtakomisiyo.setVisibility(View.GONE);
                mySwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isConnected())
                {
                    Toast.makeText(HomeActivity.this, "Network Disconnected!", Toast.LENGTH_LONG).show();
                    errorTxt.setText("Network Disconnected!, Connect again & Reflesh");
                
                

                errorImg.setVisibility(View.VISIBLE);
                errorReflesh.setVisibility(View.VISIBLE);
                errorTxt.setVisibility(View.VISIBLE);
                errorReflesh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentLink = view.getUrl();
                        webView.loadUrl(currentLink);
                    }
                });
                
                }
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                HomeActivity.this.filePathCallback = filePathCallback;
                showFileChooser();
                return true;
            }
        });


    }


    private static final int FILE_CHOOSER_REQUEST_CODE = 1;
    private ValueCallback<Uri[]> filePathCallback;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        Intent chooser = Intent.createChooser(intent, "Choose File");
        startActivityForResult(chooser, FILE_CHOOSER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            filePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
        } else {
            filePathCallback.onReceiveValue(null);
        }
        filePathCallback = null;
        super.onActivityResult(requestCode, resultCode, data);
    }




    //set back button functionality
    @Override
    public void onBackPressed() { //if user presses the back button do this
        if (webView.isFocused() && webView.canGoBack()) { //check if in webview and the user can go back
            webView.goBack(); //go back in webview
        } else { //do this if the webview cannot go back any further
            new AlertDialog.Builder(this) //alert the person knowing they are about to close
                    .setTitle("EXIT")
                    .setMessage("Are you sure. You want to close this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

}


