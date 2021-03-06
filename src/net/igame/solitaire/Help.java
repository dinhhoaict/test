/*
  Copyright 2015 Curtis Gedak

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package net.igame.solitaire;

import net.igame.solitaire.R;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Help {

  public Help(final SolitaireCG solitaire, final DrawMaster drawMaster) {

    solitaire.setContentView(R.layout.help);
    WebView webView = (WebView) solitaire.findViewById(R.id.help_web_view);
    webView.setFocusable(true);
    webView.setFocusableInTouchMode(true);

    webView.setWebViewClient(new WebViewClient(){
      @Override
      public boolean shouldOverrideUrlLoading(WebView wv, String url) {
        wv.loadUrl(url);
        return true;
      }
    });

    // Load help contents
    // Alternatively:
    //   webView.loadUrl("file:///android_res/raw/help_contents.txt");
    String helpText = "<html><body>"
      + "<h1>" + String.format(solitaire.getString(R.string.help_window_title), SolitaireCG.VERSION_NAME) + "</h1>"
      + Utils.readRawTextFile(solitaire, R.raw.help_contents).replace("\n"," ")
        // Append README file
      + "<hr>"
      + "<h2 id='readme'>"
      + solitaire.getString(R.string.readme_header)
      + "</h2></a>"
      + "<pre style='font-size:smaller;'>"
      + Utils.readRawTextFile(solitaire, R.raw.readme).replace("\n","<br>")
      + "</pre>"
        // Append COPYING file
      + "<hr>"
      + "<h2 id='copying'>"
      + solitaire.getString(R.string.copying_header)
      + "</h2></a>"
      + "<pre style='font-size:smaller;'>"
      //Work around android API loadData issue 4401 problem with % character
      + Utils.readRawTextFile(solitaire, R.raw.copying).replace("\n","<br>").replace("%","&#37;")
      + "</pre>"
      + "</body></html>";
    webView.loadData(helpText, "text/html; charset=utf-8", "utf-8");

    webView.setOnKeyListener(new WebView.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        WebView wv = (WebView) v;
        switch (keyCode) {
          case KeyEvent.KEYCODE_BACK:
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
              if (wv.canGoBack()) {
                wv.goBack();
              } else {
                solitaire.CancelOptions();
              }
              return true;
            }
        }
        return false;
      }
    });

    webView.requestFocus();
  }
}
