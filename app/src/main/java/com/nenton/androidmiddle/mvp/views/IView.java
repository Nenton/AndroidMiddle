package com.nenton.androidmiddle.mvp.views;


public interface IView {
   void showMessage(String message);
   void showError(Exception e);

   void showLoad();
   void hideLoad();
}
