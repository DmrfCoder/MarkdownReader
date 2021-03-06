package cn.dmrfcoder.markdownreader.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import cn.dmrfcoder.markdownreader.MarkdownView;
import cn.dmrfcoder.markdownreader.css.InternalStyleSheet;
import cn.dmrfcoder.markdownreader.css.styles.Github;

public class ReaderActivity extends Activity {

    private MarkdownView mMarkdownView;
    private InternalStyleSheet mStyle = new Github();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        //Cria o bean.
        MyBean myBean = new MyBean();
        myBean.setHello("Olá");
        myBean.setDiasDaSemana(MyBean.DiasDaSemana.DOMINGO);

        mMarkdownView = findViewById(R.id.mark_view);
        mMarkdownView.addStyleSheet(mStyle);
        //http://stackoverflow.com/questions/6370690/media-queries-how-to-target-desktop-tablet-and-mobile
        mStyle.addMedia("screen and (min-width: 320px)");
        mStyle.addRule("h1", "color: green");
        mStyle.endMedia();
        mStyle.addMedia("screen and (min-width: 481px)");
        mStyle.addRule("h1", "color: red");
        mStyle.endMedia();
        mStyle.addMedia("screen and (min-width: 641px)");
        mStyle.addRule("h1", "color: blue");
        mStyle.endMedia();
        mStyle.addMedia("screen and (min-width: 961px)");
        mStyle.addRule("h1", "color: yellow");
        mStyle.endMedia();
        mStyle.addMedia("screen and (min-width: 1025px)");
        mStyle.addRule("h1", "color: gray");
        mStyle.endMedia();
        mStyle.addMedia("screen and (min-width: 1281px)");
        mStyle.addRule("h1", "color: orange");
        mStyle.endMedia();
        mMarkdownView.setBean(myBean);


        Intent intent = getIntent();
        String path =  intent.getStringExtra("path");
        if (path != null) {

            File file=new File(path);
            mMarkdownView.loadMarkdownFromFile(file);
           // mMarkdownView.loadMarkdownFromUrl(path);
            //String path = uri.getPath();
            //mMarkdownView.loadMarkdownFromUrl(uri);

        }

        Uri uri = (Uri) intent.getData();
        if (uri != null) {
            mMarkdownView.loadMarkdownFromUrl(uri.toString());
        }




        //mMarkdownView.loadMarkdownFromAsset("What.md");
    }

    public static class MyBean {

        public enum DiasDaSemana {
            DOMINGO,
            SEGUNDA,
            TERCA,
            QUARTA,
            QUINTA,
            SEXTA,
            SABADO
        }

        private String hello;
        private DiasDaSemana diasDaSemana;

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }

        public DiasDaSemana getDiasDaSemana() {
            return diasDaSemana;
        }

        public void setDiasDaSemana(DiasDaSemana diasDaSemana) {
            this.diasDaSemana = diasDaSemana;
        }
    }
}
