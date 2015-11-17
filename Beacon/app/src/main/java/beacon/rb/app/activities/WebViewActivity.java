package beacon.rb.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import beacon.rb.app.R;


/**
 * Created by jesus.martinez on 13/07/2015.
 */
public class WebViewActivity extends Activity {


    private WebView webView;
    private ProgressBar progressBar;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        webView = (WebView) findViewById(R.id.webView);

        loadWeb();
    }

    private void loadWeb() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.setInitialScale(0);
        webView.setWebChromeClient(new MyWebChromeViewClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }


    private class MyWebChromeViewClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {

            progressBar.setProgress(progress);
            if (progress >= 100) {
                progressBar.setVisibility(View.GONE);
            }else{
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
